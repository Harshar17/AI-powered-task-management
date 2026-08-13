# AI-Powered Task Management

A full-stack task management application built using React.js and Spring Boot.
The application allows users to securely manage their tasks and uses Google Gemini AI
to generate task information based on a task title.

---

## 1. Project Overview

The application provides a simple platform where users can:

- Register and login
- Securely access their own tasks
- Create tasks
- Edit tasks
- Delete tasks
- Change task status
- Set task priority
- Search and filter tasks
- Generate task information using AI
- Logout securely

The application follows a frontend-backend architecture where React handles
the user interface and Spring Boot provides REST APIs and business logic.

---

## 2. Technologies Used

### Frontend

- React.js
- JavaScript
- HTML
- CSS
- Axios
- React Router

### Backend

- Java
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA
- REST APIs
- Maven

### Database

- MySQL

### AI

- Google Gemini AI API

### Development Tools

- Eclipse
- Visual Studio Code
- MySQL Workbench
- Git
- GitHub

---

## 3. Application Architecture

The application follows a three-layer architecture.

```text
                User
                 |
                 v
        +------------------+
        |  React Frontend  |
        +------------------+
                 |
              Axios
                 |
                 v
        +------------------+
        | Spring Boot API  |
        +------------------+
                 |
        +--------+---------+
        |                  |
        v                  v
   Spring Security     Service Layer
        |                  |
        |                  v
        |             Repository Layer
        |                  |
        |                  v
        |              MySQL DB
        |
        v
   JWT Authentication

                 |
                 v
          Google Gemini AI
