# 💻 Console Based Banking Application (Java Project)

This is a **Console-Based Banking Application** built using **Basic Java**, organized using the **Modular Approach** with the help of **4 Packages**:

---

## 📦 Project Packages

### 1️⃣ `entity`

🔸 Contains **model classes** (also known as entities).
🔹 Examples in this project:
    👤 `User` class
    📄 `Transaction` class

---

### 2️⃣ `main`

🔸 Stores the **core logic** and **entry point** of the application.
🔹 Example:
    🚀 `Main` class

---

### 3️⃣ `repository`

🔸 Acts as the **data layer** for storing and managing information.
🔹 Uses **Collections** like `List`, `Map`, `Set` to store data.
🔹 Example:
    📚 `UserRepository` class

---

### 4️⃣ `services`

🔸 Handles the **business logic** of the application.
🔹 Responsible for operations like filtering, sorting, and processing data.
🔹 Interfaces and classes here are used by the `Main` class.

---

## 🧾 Data Storage

* Data is **stored in-memory** using Java Collections such as:
  🔹 `List`
  🔹 `Map`
  🔹 `Set`

---

## 👥 Application Users

### 👤 **User Functionalities:**

* 🔐 Login to account
* 💰 Check account balance
* 💸 Transfer funds
* 🧾 View transaction history
* 📝 Raise chequebook request

---

### 👮‍♂️ **Admin Functionalities:**

* 🆕 Create new user account
* 🔍 Check any user's account balance
* 📑 View any user's transaction history
* ✅ Approve chequebook requests raised by users

---
