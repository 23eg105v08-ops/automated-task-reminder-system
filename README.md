# Student Management System

This repository contains a simple Student Management web application built with Spring Boot, Thymeleaf and H2 (in-memory) for development.

What you get:

- A `Student` JPA entity and `StudentRepository` for persistence
- A `StudentService` with basic CRUD operations
- A `StudentController` serving a Thymeleaf UI at `/students`
- H2 console available at `/h2-console` for inspecting the database

## Run the app (Quickstart)

From the project root (Windows PowerShell):

```powershell
cd "c:\Users\MUNTHA DINESH YADAV\OneDrive\Desktop\project\demo"
.\mvnw.cmd -DskipTests spring-boot:run
```

Or build and run the JAR:

```powershell
.\mvnw.cmd -DskipTests package
java -jar target\demo-0.0.1-SNAPSHOT.jar
```

Open in your browser:

- Student UI: http://localhost:8080/students
- H2 console: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:demo`, user: `sa`)

## Development notes

- The scheduler and Twilio integrations from the original template were removed and replaced with the Student management functionality.
- To use a persistent database like MySQL, update `src/main/resources/application.properties` with the appropriate `spring.datasource.*` values.

## Next steps (optional)

- Add REST API endpoints for CRUD operations
- Add validation and error handling on forms
- Add pagination and sorting to the student list
- Add CSV import/export for students

If you want any of the above, tell me which and I'll implement it.
