# Originators Todo List API

<img src="https://upload.wikimedia.org/wikipedia/commons/4/44/Spring_Framework_Logo_2018.svg" alt="Spring Boot Logo" width="200">

## Overview
The **Originators Todo List API** is a Spring Boot-based backend service for managing a to-do list application. It provides endpoints for creating, updating, retrieving, and deleting tasks, supporting multiple users and authentication mechanisms.

## Features
- User authentication and authorization
- Task creation, updating, and deletion
- Task categorization and prioritization
- Deadline and reminder management
- RESTful API with JSON responses
- Database integration using MongoDB

## Tech Stack
- **Backend:** Java, Spring Boot
- **Database:** MongoDB
- **Authentication:** JWT
- **Build Tool:** Maven
- **Deployment:** Docker

## Setup Instructions
### How to Clone the Project
1. Open a terminal or command prompt.
2. Run the following command to clone the repository:
   ```bash
   git clone https://github.com/abdelrahman1100/originators-todolist-spring-api.git
   cd originators-todolist-spring-api
   ```

### How to Set Up the Environment
1. Install **Java 17+** if not already installed.
2. Install **Maven**
3. Set up a database (MongoDB) and configure connection settings in `src/main/resources/application.properties`:
   ```properties
   spring.data.mongodb.uri=mongodb://localhost:27017/to-do-list
   ```

### How to Run the Application Locally
1. Navigate to the project root directory.
2. Build and run the application using Maven:
   ```bash
   mvn spring-boot:run
   ```
3. The application will start on **http://localhost:8080**.

### How to Test API Endpoints
1. Use **Postman** or any API testing tool.
2. Example requests:
   - **Register a user:**
     ```bash
     curl -X POST http://localhost:8080/auth/register -H "Content-Type: application/json" -d '{"username":"testuser","password":"password123"}'
     ```
   - **Login:**
     ```bash
     curl -X POST http://localhost:8080/auth/login -H "Content-Type: application/json" -d '{"username":"testuser","password":"password123"}'
     ```
   - **Create a task:**
     ```bash
     curl -X POST http://localhost:8080/todo/create-todo -H "Authorization: Bearer YOUR_JWT_TOKEN" -H "Content-Type: application/json" -d '{"title":"New Task","description":"Task details","status":"DONE"}'
     ```

## API Endpoints
### Authentication
| Method | Endpoint         | Description         |
|--------|----------------|---------------------|
| POST   | /auth/register  | Register a user    |
| POST   | /auth/login     | Login and get JWT  |

### Task Management
| Method | Endpoint         | Description                      |
|--------|----------------|----------------------------------|
| GET    | /todos          | Get all tasks                   |
| GET    | /todos/{id}     | Get task by ID                  |
| DELETE | /todos/{id}     | Delete a task                   |

## Contributing
1. Fork the repository.
2. Create a new branch (`feature/your-feature`).
3. Commit your changes.
4. Push to the branch and create a Pull Request.

## Contact
For any inquiries, reach out via [abdulrahmantarek@gmail.com] or open an issue on GitHub.

