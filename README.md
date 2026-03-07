# Ocean View Resort Reservation System 🌊

A professional Java-based web application designed to transition resort operations from manual paper-based logs to a secure, automated digital platform. This project demonstrates robust **System Design**, **CI/CD integration**, and a commitment to **Ethical Software Development**.

## 🚀 Key Features

- **Executive Dashboard:** Real-time visualization of room occupancy and resort status.
- **Reservation Management:** Complete CRUD (Create, Read, Update, Delete) operations for reservation inventory.
- **Confirmation Notification:** Send confirmation message for guests
- **Calculate Bill:** After add new reservation calculate the total bill.
- **View Real Time Room Avaliability:** Staff can view real time room availability through their dashboards.
- **View Daily Summary:** Admin can view daily summary every day. 
- **Room Management:** Complete CRUD (Create, Read, Update, Delete) operations for physical room inventory.
-  **Profile View:** Staff can view their own profiles 
-  **Staff Management:** Complete CRUD (Create, Read, Update, Delete) operations for staff inventory.
- **Role-Based Access Control (RBAC):** Distinct interfaces and permissions for "System Administrators" and "Receptionists".
- **Automated Testing:** 14+ JUnit tests covering the data-tier logic (DAOs).
- **CI/CD Pipeline:** Automated build and test execution via GitHub Actions.

## 🛠️ Technology Stack

- **Backend:** Java 17, Jakarta EE (Servlets).
- **Frontend:** HTML5, CSS3 (Luxury UI), JavaScript (Fetch API).
- **Database:** MySQL 8.0.
- **Build Tool:** Maven.
- **Version Control:** Git & GitHub.

## ⚖️ Ethical & Professional Considerations

As part of this project's development, the following professional issues were addressed:
1. **GDPR Compliance:** Protecting guest data through secure session management and role-based data access.
2. **Responsibility of Developers:** Implementing a CI/CD "Safety Net" to ensure system stability before deployment.
3. **Duty to Public Interest:** Prioritizing data integrity and system reliability over rapid feature deployment.

## ⚙️ Setup & Installation

### Local Development (WAMP)
1. Clone the repository: `git clone <repo-url>`
2. Import `ocean_view_db.sql` into your local MySQL/WAMP instance.
3. Update `DBUtil.java` with your local database credentials.
4. Open the project in **IntelliJ IDEA** and run via **Tomcat 10.1**.

### GitHub Actions (CI/CD)
The project includes a `.github/workflows/maven.yml` file. Every push triggers:
- Environment setup (JDK 17).
- MySQL service container launch.
- Database schema initialization.
- `mvn package` (running all JUnit tests).

## 🔐 Testing Credentials

For evaluation purposes, use the following pre-configured accounts:

| Role | Username | Password |
| :--- | :--- | :--- |
| **Admin** | `admin` | `admin123` |
| **Receptionist** | `recep_user` | `recep1` |

## UI 
**Login**

<img width="938" height="457" alt="image" src="https://github.com/user-attachments/assets/43f30cb1-fea0-4491-8cfc-62d9f5a57eb8" />

**Admin Dashboard**

<img width="775" height="387" alt="image" src="https://github.com/user-attachments/assets/7c67d48c-c4f0-4660-bfda-233af8a43e21" />

**Receptionist Dashboard**

<img width="770" height="385" alt="image" src="https://github.com/user-attachments/assets/f5d9af61-2b62-4e24-8d9f-5f221d35091b" />

**Add new Reservation**

<img width="830" height="423" alt="image" src="https://github.com/user-attachments/assets/530d77e0-e887-473d-be84-941f4d2c7b03" />

**View Reservation**

<img width="930" height="467" alt="image" src="https://github.com/user-attachments/assets/019673fd-d4ae-4860-86e0-4d742b329b74" />

**Daily Summary**

<img width="893" height="448" alt="image" src="https://github.com/user-attachments/assets/6693c434-8fb0-4b9a-a19d-0d00a3850b3c" />

**Staff Management**

<img width="720" height="312" alt="image" src="https://github.com/user-attachments/assets/4ff1c792-374f-4e02-9943-4f564583fb76" />

**Operational Guide**

<img width="823" height="789" alt="image" src="https://github.com/user-attachments/assets/bfc36224-d9fd-4136-99d4-30ca1af2f544" />


## Main Wireframes

**Admin / Receptionist Dashboard**

<img width="589" height="529" alt="image" src="https://github.com/user-attachments/assets/6468bf57-ab06-4d7b-9571-11aa12d0feed" />

**Guest Registration**

<img width="365" height="463" alt="image" src="https://github.com/user-attachments/assets/8f9769f4-5ce8-49e9-8182-4ee3dd18e0f7" />

**View Reservations**

<img width="693" height="604" alt="image" src="https://github.com/user-attachments/assets/ffcc16ff-f99e-4a64-b718-37f686b41c85" />

**Edit Reservation**

<img width="548" height="668" alt="image" src="https://github.com/user-attachments/assets/6101802d-c745-4d68-8b5b-7a2fad3d2fcc" />

**Daily Summary**

<img width="498" height="381" alt="image" src="https://github.com/user-attachments/assets/5cc31788-3178-4fb0-b938-b3f7797a92e1" />

**Rate Management**

<img width="548" height="493" alt="image" src="https://github.com/user-attachments/assets/8b4ae2c1-e938-49ff-b0c3-761f925d86f3" />

**Staff Management**

<img width="634" height="485" alt="image" src="https://github.com/user-attachments/assets/3a43a3f1-707f-4608-854a-6f59a082b22a" />


## 📁 Project Structure

- `src/main/java`: Backend logic (Servlets, DAOs, Utilities).
- `src/main/webapp`: UI components (HTML, CSS, Images).
- `src/test/java`: JUnit test suite.
- `pom.xml`: Dependency management.

## 📬 **Author**

Name: P. D. Dinithi Dedunu

Degree: BSc(Hons) Software Engineering

Email: dedunudinithi@gmail.com

## ⚡ **Installation**
1. Clone the repository:  
```bash
git clone https://github.com/Dedunu2002/OceanViewResortReservationSystem
