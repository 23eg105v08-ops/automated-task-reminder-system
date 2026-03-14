# Automated Task Reminder System

Automated Task Reminder System is a Spring Boot web application for creating, tracking, and acting on tasks with reminders, escalation support, and AI-assisted productivity features.

## Overview

This project provides:

- Task creation, editing, completion, and deletion
- Priority and status-based tracking
- Reminder and due-date management
- Settings page for integration and notification configuration
- Optional AI assistant endpoints for task help
- Integration test endpoints for external service verification

## Tech Stack

- Java 21
- Spring Boot
- Spring Data JPA
- Thymeleaf
- H2 Database
- Maven

## Project Structure

src/main/java/com/dinesh/demo

- config: Security configuration
- controller: Web and API controllers
- model: Domain model entities
- repository: Data access interfaces
- service: Business services, notifications, scheduling, AI support

src/main/resources

- templates: Thymeleaf views
- static/css: Stylesheets
- static/js: Frontend scripts
- application.properties: Runtime configuration

## Run Locally

Prerequisites:

- Java 21+
- Maven 3.9+

Run with Maven Wrapper on Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Or with Maven:

```powershell
mvn spring-boot:run
```

Application URLs:

- Home: http://localhost:8080/
- Tasks: http://localhost:8080/tasks
- Settings: http://localhost:8080/settings
- H2 Console (if enabled): http://localhost:8080/h2-console

## Build and Test

```powershell
mvn clean test
mvn clean package
```

## Notes

- Environment-specific values should be managed through application properties or environment variables.
- For production use, enable proper authentication, CSRF protection, and hardened security settings.

## License

This project is provided for educational and development use.
