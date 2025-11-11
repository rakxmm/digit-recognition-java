# 🧠 Digit Recognition in Java

A simple Java application that recognizes handwritten digits using a neural network trained on the **MNIST dataset**.

This project demonstrates how to build a feed-forward neural network from scratch in Java and use it to classify handwritten digits.  
It includes a minimal **graphical interface** where the user can draw a digit and get a prediction in real time.

---

## 🚀 Features

- Implementation of a **fully-connected neural network** (no external ML frameworks)
- **Training and testing** on MNIST dataset
- Simple **Swing-based GUI** for drawing and recognizing digits
- Model **serialization and loading** (no need to retrain every run)
- Lightweight and educational – easy to understand and extend

---

## 🧩 Technologies Used

| Area | Technology |
|------|-------------|
| Programming language | Java 17+ |
| GUI | Java Swing |
| Machine learning | Custom neural network implementation |
| Dataset | MNIST (28x28 grayscale images) |
| Build tool | Gradle / Maven (optional) |

---

## 🏗️ How It Works

1. The MNIST dataset is loaded and preprocessed.  
2. A small feed-forward neural network (e.g., [784, 128, 10]) is created.  
3. The model is trained using backpropagation and saved to a file.  
4. The user interface allows drawing a digit and sends the pixel data to the model.  
5. The neural network predicts the most probable digit (0–9).

---

## ▶️ How to Run

1. **Clone the repository**

   ```bash
   git clone https://github.com/rakxmm/digit-recognition-java.git
   cd digit-recognition-java
