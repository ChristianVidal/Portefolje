package JunitTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import dat109_oblig1.Brett;
import dat109_oblig1.Controller;
import dat109_oblig1.Spiller;
import dat109_oblig1.Terning;

class ControllerTest {
	
	@Test
	void startTest() {
		Controller controller = new Controller();
		Brett brett = new Brett();
		Terning terning = new Terning();
		List<Spiller> spillere = new ArrayList<>(Arrays.asList(new Spiller("navn1"), new Spiller("navn2")));
		Spiller spiller1 = spillere.get(0);
		Spiller spiller2 = spillere.get(1);
		
		controller.start(brett, terning,spillere);
		assertTrue(spiller1.getHarVunnet() || spiller2.getHarVunnet());
		
	}

}
