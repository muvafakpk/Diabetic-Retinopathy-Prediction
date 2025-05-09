
# 🩺 Diabetic Retinopathy Prediction using Machine Learning

This project aims to automate the detection of Diabetic Retinopathy (DR) from retinal fundus images using a deep learning model integrated with a Django web application and a mobile Android app for remote access.

## 📌 Project Overview

Diabetic Retinopathy is a complication of diabetes that affects the eyes and can lead to vision loss if not diagnosed early. Our solution uses a pre-trained DenseNet-169 model fine-tuned to classify images into five DR severity levels:

- No DR (0)
- Mild (1)
- Moderate (2)
- Severe (3)
- Proliferative DR (4)

The system supports doctors, administrators, and patients through a role-based web and mobile interface.

---

## 💻 Technologies Used

### 🔍 Machine Learning
- TensorFlow & Keras
- DenseNet-169
- CLAHE (Contrast Limited Adaptive Histogram Equalization) preprocessing

### 🧠 Model
- Input: 224x224 retinal fundus images
- Output: 5-class DR severity prediction

### 🌐 Web Application
- Django (Python backend)
- MySQL (database)
- Bootstrap (frontend)

### 📱 Mobile Application
- Java (Android)
- Volley library for API requests

---

## ⚙️ Features

- ✅ Image upload and prediction (DR level)
- ✅ Admin and doctor dashboard
- ✅ Patient login to view results
- ✅ Prediction history and record management

---

## 🏗️ Project Structure

---

## 🚀 Getting Started

### Backend Setup
```bash
cd Diabetic-Retinopathy-Prediction/
pip install -r requirements.txt
python manage.py makemigrations
python manage.py migrate
python manage.py runserver


