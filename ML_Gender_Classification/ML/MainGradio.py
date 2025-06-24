import gradio as gr
import cv2
import numpy as np
from tensorflow.keras.models import load_model

# Load the trained model
model = load_model("gender_classifier_model.keras")
IMG_SIZE = 224
CLASS_NAMES = ['Male', 'Female']

def predict_gender(image):
    # Convert RGB (Gradio) to BGR (used during training)
    image = cv2.cvtColor(image, cv2.COLOR_RGB2BGR)

    # Resize, normalize, batchify
    image = cv2.resize(image, (IMG_SIZE, IMG_SIZE))
    image = image / 255.0
    image = np.expand_dims(image, axis=0)

    # Predict
    prediction = float(model.predict(image, verbose=0).squeeze())
    label = "Female" if prediction > 0.5 else "Male"
    confidence = prediction if prediction > 0.5 else 1 - prediction

    return f"{label} ({confidence:.2%} confidence)"

# Define interface
interface = gr.Interface(
    fn=predict_gender,
    inputs=gr.Image(type="numpy", label="Upload a face image"),
    outputs=gr.Textbox(label="Predicted Gender"),
    title="Gender Classification",
    description="Upload a face image. The model will predict the gender with confidence."
)

interface.launch()