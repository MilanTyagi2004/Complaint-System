Complaint  Management System

A Spring Boot & MongoDB application for managing citizen complaints. Citizens can submit and track complaints, while staff and admins can manage, assign, and resolve issues efficiently.

🚀 Features

User Onboarding & Auth: JWT authentication + phone OTP via Twilio

Complaint Lifecycle: Submit complaints with title, location, department, priority, and status

Automatic Assignment: Complaints auto-assigned to staff based on department & location

Notifications: Email alerts when complaints are created or updated

Roles & Permissions: Separate flows for End-users, Staff, and Admins

APIs & Docs: REST APIs with Swagger/OpenAPI; includes health checks

🛠 Tech Stack

Backend: Spring Boot, Spring Security

Database: MongoDB (Spring Data with auditing)

Authentication & Notifications: JWT, Twilio OTP, SMTP Email

API Documentation: Swagger/OpenAPI

⚡ Getting Started
Prerequisites

Java 24
Maven

MongoDB running locally or remotely

# Clone the repo
git clone https://github.com/MilanTyagi2004/Complaint-System

# Build and run
mvn clean install
mvn spring-boot:run


Configuration

Update application.properties or application.yml with:

MongoDB connection details

JWT secret

Twilio credentials

SMTP email credentials

📚 API Documentation

Open Swagger UI to explore endpoints:

http://localhost:8080/swagger-ui.html

👤 Usage

End-User: Register, login, submit complaints, track status

Staff: View assigned complaints, update status

Admin: Manage users, view all complaints, assign complaints manually

🌟 Impact

Enables citizens to submit grievances and track progress while allowing staff and admins to manage complaints efficiently.

🤝 Contributing

Fork the repo

Create a branch: git checkout -b feature/your-feature

Commit changes: git commit -m "Add feature"

Push: git push origin feature/your-feature

Open a Pull Request

SMTP email credentials

Twilio account for OTP verification
