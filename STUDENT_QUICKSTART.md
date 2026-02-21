# Student Management - Quickstart

This project provides a minimal Student Management web app using Spring Boot, Thymeleaf and H2.

Run the app (Windows PowerShell):

```powershell
cd "c:\Users\MUNTHA DINESH YADAV\OneDrive\Desktop\project\demo"
.\mvnw.cmd -DskipTests spring-boot:run
```

Open these pages after startup:

- Student list / UI: http://localhost:8080/students
- H2 console: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:demo`, user: `sa`)

To build a jar:

```powershell
.\mvnw.cmd -DskipTests package
java -jar target\demo-0.0.1-SNAPSHOT.jar
```

Want REST APIs, validation, or import/export CSV for students? I can add them next.
