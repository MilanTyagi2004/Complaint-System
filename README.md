# Complaint Management System

A comprehensive **Complaint Management System** built with Spring Boot 3.5.5, MongoDB, and JWT authentication. This system allows users to submit complaints, staff to manage them, and admins to oversee the entire process.

## 🚀 Features

### Core Functionality
- **User Registration & Authentication** - JWT-based secure authentication
- **Complaint Submission** - Users can submit complaints with location and department details
- **Staff Management** - Staff can view, update, and manage complaint statuses
- **Admin Dashboard** - Administrative functions for user and staff management
- **OTP Verification** - Phone number verification using Twilio SMS
- **Email Notifications** - Automated email notifications for complaint updates
- **Priority Management** - Four priority levels: LOW, MEDIUM, HIGH, CRITICAL
- **Status Tracking** - Real-time complaint status updates (PENDING, IN_PROGRESS, RESOLVED, REJECTED)

### Technical Features
- **RESTful API** - Well-documented REST endpoints with Swagger/OpenAPI
- **Role-Based Access Control** - Three user roles: USER, STAFF, ADMIN
- **Data Validation** - Comprehensive input validation using Bean Validation
- **Exception Handling** - Global exception handling with proper error responses
- **Health Monitoring** - Spring Boot Actuator for application health checks
- **Comprehensive Testing** - 80%+ code coverage with unit, integration, and end-to-end tests

## 🛠️ Tech Stack

- **Backend**: Spring Boot 3.5.5
- **Database**: MongoDB
- **Security**: Spring Security + JWT
- **Documentation**: Swagger/OpenAPI 3
- **Testing**: JUnit 5, Mockito, TestContainers
- **Code Coverage**: JaCoCo
- **Build Tool**: Maven
- **Java Version**: 21

## 📋 Prerequisites

- Java 21 or higher
- Maven 3.6+
- MongoDB (running on localhost:27017)
- Gmail account for email notifications
- Twilio account for SMS OTP verification

## 🚀 Quick Start

### 1. Clone the Repository
```bash
git clone <repository-url>
cd SIH
```

### 2. Set Up Environment Variables
Run the setup script to configure Twilio credentials:
```bash
setup-env.bat
```

Or manually set these environment variables:
```bash
# Twilio Configuration
TWILIO_ACCOUNT_SID=your_account_sid
TWILIO_AUTH_TOKEN=your_auth_token
TWILIO_TRIAL_NUMBER=your_trial_number

# Email Configuration
MAIL_USERNAME=your_gmail@gmail.com
MAIL_PASSWORD=your_app_password

# JWT Secret
JWT_SECRET=your_jwt_secret_key
```

### 3. Start MongoDB
Ensure MongoDB is running on `localhost:27017`

### 4. Run the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 📚 API Documentation

Once the application is running, access the interactive API documentation:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## 🔗 API Endpoints

### Public Endpoints
- `POST /public/signup` - User registration
- `POST /public/login` - User login
- `POST /otp/send` - Send OTP for phone verification
- `POST /otp/verify` - Verify OTP

### User Endpoints (Requires Authentication)
- `POST /user/newComplaint` - Submit a new complaint
- `GET /user/getAll` - Get all user's complaints
- `GET /user/id/{complaintId}` - Get specific complaint
- `DELETE /user/id/{complaintId}` - Delete complaint

### Staff Endpoints (Requires STAFF Role)
- `GET /staff/complaints` - Get all complaints
- `PUT /staff/complaints/{complaintId}/status` - Update complaint status
- `GET /staff/complaints/{complaintId}` - Get specific complaint

### Admin Endpoints (Requires ADMIN Role)
- `GET /admin/users` - Get all users
- `PUT /admin/users/{userId}/role` - Update user role
- `GET /admin/staff` - Get all staff members

## 🏗️ Project Structure

```
src/
├── main/java/com/SIH/SIH/
│   ├── config/          # Configuration classes
│   ├── controller/      # REST controllers
│   ├── dto/            # Data Transfer Objects
│   ├── entity/         # MongoDB entities
│   ├── exception/      # Custom exceptions
│   ├── filter/         # Security filters
│   ├── repostitory/    # MongoDB repositories
│   ├── services/       # Business logic
│   └── util/           # Utility classes
└── test/               # Comprehensive test suite
    ├── controller/     # Controller tests
    ├── services/       # Service tests
    ├── integration/    # Integration tests
    └── builder/        # Test data builders
```

## 🧪 Testing

The project includes comprehensive testing with **80%+ code coverage**:

### Run All Tests
```bash
mvn test
```

### Run Specific Test Suites
```bash
# Unit tests only
mvn test -Dtest=*Test

# Integration tests only
mvn test -Dtest=*IntegrationTest

# Controller tests only
mvn test -Dtest=*ControllerTest
```

### Generate Coverage Report
```bash
mvn test jacoco:report
```

Coverage report will be available at: `target/site/jacoco/index.html`

## 📊 Database Schema

### User Collection
```json
{
  "id": "string",
  "firstName": "string",
  "lastName": "string", 
  "email": "string",
  "password": "string (encrypted)",
  "city": "string",
  "phoneNumber": "string",
  "department": "string (for staff)",
  "designation": "string (for staff)",
  "active": "boolean",
  "verified": "boolean",
  "role": "USER|STAFF|ADMIN",
  "createdAt": "LocalDateTime",
  "updatedAt": "LocalDateTime"
}
```

### Complaint Collection
```json
{
  "id": "string",
  "title": "string",
  "pincode": "integer",
  "city": "string",
  "state": "string",
  "description": "string",
  "department": "string",
  "priority": "LOW|MEDIUM|HIGH|CRITICAL",
  "status": "PENDING|IN_PROGRESS|RESOLVED|REJECTED",
  "userId": "string",
  "staffId": "string",
  "createdAt": "LocalDateTime",
  "updatedAt": "LocalDateTime"
}
```

## 🔐 Security Features

- **JWT Authentication** - Secure token-based authentication
- **Password Encryption** - BCrypt password hashing
- **Role-Based Authorization** - Different access levels for USER, STAFF, ADMIN
- **Input Validation** - Comprehensive validation using Bean Validation
- **CORS Configuration** - Cross-origin resource sharing setup
- **Security Headers** - Security headers for protection against common attacks

## 📧 Email & SMS Integration

### Email Service
- **SMTP Configuration** - Gmail SMTP integration
- **Automated Notifications** - Email notifications for complaint updates
- **Template Support** - Customizable email templates

### SMS Service (Twilio)
- **OTP Verification** - Phone number verification via SMS
- **Trial Account Support** - Works with Twilio trial accounts
- **International Support** - Global SMS delivery

## 🚀 Deployment

### Using Maven
```bash
mvn clean package
java -jar target/SIH-0.0.1-SNAPSHOT.jar
```

### Using Docker (if Dockerfile is available)
```bash
docker build -t sih-app .
docker run -p 8080:8080 sih-app
```

## 🔧 Configuration

### Application Properties
Key configuration options in `application.yml`:

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017
      database: Complaint_system
  
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD}

twilio:
  accountSid: ${TWILIO_ACCOUNT_SID}
  authToken: ${TWILIO_AUTH_TOKEN}
  trialNumber: ${TWILIO_TRIAL_NUMBER}

management:
  endpoints:
    web:
      exposure:
        include: health
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Check the [TESTING_GUIDE.md](TESTING_GUIDE.md) for testing information
- Review the API documentation at `/swagger-ui.html`

## 🎯 Future Enhancements

- [ ] File upload support for complaint attachments
- [ ] Real-time notifications using WebSockets
- [ ] Advanced reporting and analytics dashboard
- [ ] Mobile app integration
- [ ] Multi-language support
- [ ] Advanced search and filtering capabilities

---

**Built with ❤️ using Spring Boot and MongoDB**
