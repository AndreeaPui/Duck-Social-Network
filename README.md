# 🦆 Social Duck Network

[![Java](https://img.shields.io/badge/Language-Java-orange.svg)](https://www.oracle.com/java/)
[![JavaFX](https://img.shields.io/badge/GUI-JavaFX-blue.svg)](https://openjfx.io/)
[![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-336791.svg)](https://www.postgresql.org/)

**Social Duck Network** is a comprehensive desktop social media application. This project marks my first major milestone in integrating **relational databases** with a graphical interface, while strictly adhering to **SOLID principles** and the **MVC (Model-View-Controller)** design pattern.

---

## 📖 Project Overview

Social Duck is designed to simulate a real-world social environment where users can connect, communicate, and stay updated through notifications.

### 🌟 Key Features
* **Account Management:** Secure sign-up and login system.
* **Social Connections:** Search for new "PenPals," send friend requests, and manage your friend list.
* **Persistent Messaging:** A full-featured chat system where messages are saved in the database, including a **Reply** functionality.
* **Real-Time Notifications:** Instant alerts for pending friend requests upon login.
* **Data Pagination:** Optimized browsing of user lists to ensure high performance.

---

## 🏗️ Architectural Excellence

This application was built with a focus on **Clean Code** and scalable architecture:

* **MVC Pattern:** Total separation of concerns. The **Model** handles data logic, **FXML** defines the **View**, and **Controllers** manage the user interaction.
* **SOLID Principles:**
    * **Single Responsibility:** Dedicated *Repository* classes for DB operations and *Service* classes for business logic.
    * **Dependency Inversion:** High-level modules do not depend on low-level modules; both depend on abstractions.
* **Design Patterns:**
    * **Repository Pattern:** Abstracts the data layer.
    * **Observer Pattern:** Used JavaFX `ObservableList` to ensure the UI updates automatically when data changes.

---

## 📸 Visual Showcase

### 1. Onboarding & Security
| Welcome Screen | Secure Login |

<img width="981" height="609" alt="Screenshot 2026-01-03 133250" src="https://github.com/user-attachments/assets/82869ee7-05d7-4aaf-984c-2c3003454386" />

<img width="432" height="719" alt="Screenshot 2026-01-03 133302" src="https://github.com/user-attachments/assets/2e7a7df8-d414-45a8-846b-14bab745f663" />

### 2. Social & Networking
| Discover PenPals | Friend Requests |

<img width="443" height="834" alt="Screenshot 2026-01-03 133328" src="https://github.com/user-attachments/assets/f76834b1-3ebc-4939-ab99-8cab8fe8e859" />
<img width="442" height="829" alt="Screenshot 2026-01-03 133338" src="https://github.com/user-attachments/assets/945e5f36-5bf1-4e05-9705-0aa34834fced" />

### 3. Communication & Alerts
| Home Dashboard | Persistent Chat |

<img width="453" height="505" alt="Screenshot 2026-01-03 133319" src="https://github.com/user-attachments/assets/9df68881-3db4-477c-b3a2-496c833090d9" />
<img width="450" height="839" alt="Screenshot 2026-01-03 133417" src="https://github.com/user-attachments/assets/86996514-78ee-41ee-93bb-28a39b32c7df" />

## 🛠️ Tech Stack
* **Language:** Java 17+
* **GUI Framework:** JavaFX (FXML & CSS)
* **Database:** PostgreSQL / MySQL (Relational)
* **Persistence:** JDBC (Java Database Connectivity)
* **Build Tool:** Maven

---

## 🚀 Getting Started

### Prerequisites
* JDK 17 or higher
* Maven installed
* PostgreSQL instance running

### Installation
1. **Clone the repository:**
   ```bash
   git clone [https://github.com/yourusername/SocialDuckNetwork.git](https://github.com/yourusername/SocialDuckNetwork.git)

2. **Configure the Database: Update the database.properties file with your credentials:**

** Properties
db.url=jdbc:postgresql://localhost:5432/social_duck

db.user=your_username

db.password=your_password

3. **Run the application:**
   ```bash
   mvn javafx:run
