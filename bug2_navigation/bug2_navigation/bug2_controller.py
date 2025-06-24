import rclpy
from rclpy.node import Node
from sensor_msgs.msg import LaserScan
from geometry_msgs.msg import Twist
from std_srvs.srv import SetBool
from bug2_interfaces.srv import GoToPoint
from geometry_msgs.msg import Point
from nav_msgs.msg import Odometry
import math
import numpy as np

class Bug2Controller(Node):
    def __init__(self):
        super().__init__('Bug2Controller')

        self.go_to_point_client = self.create_client(GoToPoint, 'go_to_point_service')
        self.wall_follower_client = self.create_client(SetBool, 'wall_follower_service')

        while not self.go_to_point_client.wait_for_service(timeout_sec=1.0):
            pass
        while not self.wall_follower_client.wait_for_service(timeout_sec=1.0):
            pass
        
        self.state = 'go_to_point'
        self.target_position = Point()
        self.position_precision = 0.1
        self.left_line = False
        self.distance_when_left_line = None
        self.rejoin_threshold = 0.5

        self.laser_subscriber = self.create_subscription(LaserScan, '/scan', self.laser_callback, 10)
        self.odometry_subscriber = self.create_subscription(Odometry, '/odom', self.odom_callback, 10)

        self.timer = self.create_timer(0.1, self.timer_callback)

        self.set_target_position()
        self.current_position = Point()
        self.line_m, self.line_b = self.calculate_line()
        self.can_switch_to_wall_following = True

        self.laser_data = None

        self.send_target_position()

    def laser_callback(self, msg):
        self.laser_data = msg

    def odom_callback(self, msg):
        self.current_position.x = msg.pose.pose.position.x
        self.current_position.y = msg.pose.pose.position.y
    
    def get_current_position(self):
        return self.current_position

    def set_target_position(self):
        self.target_position.x = 2.0
        self.target_position.y = 8.0
        self.target_position.z = 0.0
    
    def send_target_position(self):
        request = GoToPoint.Request()
        request.target_position = self.target_position
        request.move_switch = True

        future = self.go_to_point_client.call_async(request)
        future.add_done_callback(self.handle_response)
    
    def handle_response(self, future):
        try:
            response = future.result()
            if response.success:
                self.get_logger().info('Successfully sent target position to GoToPoint service.')
            else:
                self.get_logger().info('Failed to send target position to GoToPoint service.')
        except Exception as e:
            self.get_logger().error(f'Service call failed: {e}')
    
    def get_target_position(self):
        request = GoToPoint.Request()
        request.move_switch = True

        future = self.go_to_point_client.call_async(request)

        rclpy.spin_until_future_complete(self, future)
        if future.result() is not None:
            self.target_position = future.result().target_position

    def is_obstacle_detected(self):
        if self.laser_data is None:
            return False

        obstacle_threshold = 0.8
        front_distances = self.laser_data.ranges[350:] + self.laser_data.ranges[:10]
        max_range = 5.0

        if self.can_switch_to_wall_following:
            for distance in front_distances:
                if distance < obstacle_threshold:
                    return True
        else:
            for distance in front_distances:
                if distance < obstacle_threshold:
                    self.can_switch_to_wall_following = True
                return False
        return False
    
    def calculate_line(self):
        """Calculate the slope (m) and intercept (b) for the line equation y = mx + b."""
        initial_position = self.get_current_position()
        x1, y1 = initial_position.x, initial_position.y
        x2, y2 = self.target_position.x, self.target_position.y

        # Handle vertical line (infinite slope)
        if x2 == x1:
            return None, None  # Special case for vertical line (infinite slope)

        # Calculate slope (m) and intercept (b)
        m = (y2 - y1) / (x2 - x1)
        b = y1 - m * x1

        return m, b

    def distance_to_goal(self, current_position):
        """Calculate distance between current position and the goal."""
        return math.sqrt(pow(self.target_position.x - current_position.x, 2) +
                         pow(self.target_position.y - current_position.y, 2))
    
    def is_closer_to_goal(self, current_position):
        """Determine if the robot is closer to the goal than when it left the line."""
        current_distance = self.distance_to_goal(current_position)
        return current_distance < self.distance_when_left_line - self.rejoin_threshold

    def is_on_line(self, current_position):
        """Check if the robot is back on the line between the start and the goal."""
        x, y = current_position.x, current_position.y

        # Check if the robot is on a vertical line
        if self.line_m is None:
            return abs(x - self.get_current_position().x) < self.position_precision

        # Check if the current position (x, y) satisfies the line equation y = mx + b within precision
        line_y = self.line_m * x + self.line_b
        return abs(y - line_y) < self.position_precision

    def start_wall_follower(self):
        go_to_point_request = GoToPoint.Request()
        go_to_point_request.move_switch = False  # Disable go_to_point
        self.go_to_point_client.call_async(go_to_point_request)

        request = SetBool.Request()
        request.data = True
        future = self.wall_follower_client.call_async(request)
        future.add_done_callback(self.wall_follower_response_callback)

    def stop_wall_follower(self):
        request = SetBool.Request()
        request.data = False
        future = self.wall_follower_client.call_async(request)
        future.add_done_callback(self.wall_follower_response_callback)

        go_to_point_request = GoToPoint.Request()
        go_to_point_request.target_position = self.target_position
        go_to_point_request.move_switch = True
        self.go_to_point_client.call_async(go_to_point_request)
    
    def timer_callback(self):
        if self.state == 'go_to_point':
            if self.is_obstacle_detected() and self.can_switch_to_wall_following:
                self.get_logger().info("Obstacle detected in 'go_to_point' state.")
                if not self.left_line:
                    self.distance_when_left_line = self.distance_to_goal(self.get_current_position())
                    self.left_line = True
                    self.get_logger().info(f"Left line at distance: {self.distance_when_left_line}")

                self.start_wall_follower()
                self.state = 'wall_following'
                self.can_switch_to_wall_following = False
                self.get_logger().info("Switching to 'wall_following' state.")

        elif self.state == 'wall_following':
            if self.is_on_line(self.get_current_position()) and self.is_closer_to_goal(self.get_current_position()):
                self.get_logger().info("Back on line and closer to goal in 'wall_following' state.")    
                self.stop_wall_follower()
                self.state = 'go_to_point'
                self.get_logger().info("Switching to 'go_to_point' state.")

    def wall_follower_response_callback(self, future):
        try:
            response = future.result()
            if response.success:
                self.get_logger().info(response.message)
            else:
                self.get_logger().error("Failed to change wall follower state.")
        except Exception as e:
            self.get_logger().error(f"Service call failed: {e}")

def main(args=None):
    rclpy.init(args=args)

    bug2_controller = Bug2Controller()

    rclpy.spin(bug2_controller)

    bug2_controller.destroy_node()
    rclpy.shutdown()

if __name__ == '__main__':
    main()