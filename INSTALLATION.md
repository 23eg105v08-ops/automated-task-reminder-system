# Installation & Setup Guide

Complete guide to install and run the Automated Task Reminder System locally.

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

- **Java Development Kit (JDK) 21** or higher
  - Download: https://www.oracle.com/java/technologies/downloads/
  - Verify: `java -version`

- **Apache Maven 3.9** or higher
  - Download: https://maven.apache.org/download.cgi
  - Verify: `mvn --version`

- **Git** (for cloning the repository)
  - Download: https://git-scm.com/download/
  - Verify: `git --version`

## 🔧 Installation Steps

### Step 1: Clone the Repository

```bash
git clone https://github.com/23eg105v08-ops/automated-task-reminder-system.git
cd automated-task-reminder-system/demo
```

Alternatively, download as ZIP and extract.

### Step 2: Verify Java and Maven

```bash
# Check Java version (should be 21+)
java -version

# Check Maven version (should be 3.9+)
mvn --version
```

### Step 3: Build the Project

```bash
# Clear previous builds
mvn clean

# Compile and download dependencies
mvn compile

# Or build complete package
mvn clean package -DskipTests
```

### Step 4: Run the Application

#### Option A: Using Maven directly (Recommended)
```bash
mvn spring-boot:run
```

#### Option B: Using Maven Wrapper (Windows)
```bash
.\mvnw.cmd spring-boot:run
```

#### Option C: Run compiled JAR
```bash
# First build the package
mvn clean package -DskipTests

# Then run the JAR
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

### Step 5: Access the Application

Once the application starts, open your browser and navigate to:

- **Dashboard**: http://localhost:8080
- **Task Management**: http://localhost:8080/tasks
- **H2 Console**: http://localhost:8080/h2-console

## 🖥️ Windows Installation (PowerShell)

```powershell
# Clone repository
git clone https://github.com/23eg105v08-ops/automated-task-reminder-system.git

# Navigate to project
cd automated-task-reminder-system/demo

# Run with Maven wrapper
.\mvnw.cmd -DskipTests spring-boot:run

# Application will be available at http://localhost:8080
```

## 🐧 Linux/Mac Installation

```bash
# Clone repository
git clone https://github.com/23eg105v08-ops/automated-task-reminder-system.git

# Navigate to project
cd automated-task-reminder-system/demo

# Make Maven wrapper executable
chmod +x mvnw

# Run with Maven wrapper
./mvnw spring-boot:run

# Application will be available at http://localhost:8080
```

## 🔍 Troubleshooting Installation Issues

### Issue: Java not recognized

**Solution:**
```bash
# Check if Java is installed
java -version

# If not installed, install JDK 21
# https://www.oracle.com/java/technologies/downloads/

# Verify installation
java -version
```

### Issue: Maven not recognized

**Solution:**
```bash
# Check if Maven is installed
mvn --version

# If not installed, install Maven
# https://maven.apache.org/download.cgi

# Add Maven to PATH if necessary
# Then verify
mvn --version
```

### Issue: Port 8080 already in use

**Solution (Windows):**
```powershell
# Find process using port 8080
netstat -ano | findstr :8080

# Kill the process (replace PID with actual process ID)
taskkill /PID <PID> /F

# Or change port in application.properties
# server.port=8081
```

**Solution (Linux/Mac):**
```bash
# Find process using port 8080
lsof -i :8080

# Kill the process (replace PID with actual process ID)
kill -9 <PID>

# Or change port in application.properties
# server.port=8081
```

### Issue: Build fails with dependency issues

**Solution:**
```bash
# Clear Maven cache
mvn clean

# Update dependencies
mvn dependency:resolve

# Rebuild
mvn clean install
```

### Issue: Database connection error

**Solution:**
- Verify H2 database is enabled in `application.properties`
- Check JDBC URL: `jdbc:h2:mem:taskreminder`
- Access H2 console at http://localhost:8080/h2-console
- Verify username: `sa` and password is empty

## ⚙️ Configuration

### application.properties

Located at `src/main/resources/application.properties`:

```properties
# Application Name
spring.application.name=automated-task-reminder

# Database Configuration
spring.datasource.url=jdbc:h2:mem:taskreminder
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Optional: Change server port
# server.port=8081
```

### Changing the Port

To run the application on a different port:

1. Edit `src/main/resources/application.properties`
2. Add or modify: `server.port=8081`
3. Restart the application

Access at: http://localhost:8081

## 🗄️ Using MySQL Database (Production)

To use MySQL instead of H2:

1. Install MySQL Server
2. Create database: `CREATE DATABASE task_reminder;`
3. Update `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/task_reminder
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```

4. Add MySQL dependency to `pom.xml`:

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>
```

5. Rebuild and run: `mvn clean spring-boot:run`

## 🎯 Default Access

| Feature | URL | Credentials |
|---------|-----|-------------|
| Dashboard | http://localhost:8080 | None required |
| Tasks | http://localhost:8080/tasks | None required |
| H2 Console | http://localhost:8080/h2-console | sa / (empty) |

## 📝 Project Structure

After installation, your project structure will be:

```
automated-task-reminder-system/
├── demo/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/dinesh/demo/
│   │   │   │   ├── controller/
│   │   │   │   ├── service/
│   │   │   │   ├── repository/
│   │   │   │   ├── model/
│   │   │   │   ├── config/
│   │   │   │   └── DemoApplication.java
│   │   │   └── resources/
│   │   │       ├── templates/
│   │   │       ├── static/
│   │   │       └── application.properties
│   │   └── test/
│   ├── pom.xml
│   ├── mvnw
│   ├── mvnw.cmd
│   └── README.md
└── .gitignore
```

## ✅ Verification Checklist

- [x] Java 21+ installed and verified
- [x] Maven 3.9+ installed and verified
- [x] Project cloned/downloaded
- [x] Project built successfully
- [x] Application running on port 8080
- [x] Dashboard accessible at http://localhost:8080
- [x] H2 Console accessible at http://localhost:8080/h2-console

## 🆘 Need Help?

1. Check the main [README.md](README.md)
2. Review [API_DOCUMENTATION.md](API_DOCUMENTATION.md)
3. Check [TROUBLESHOOTING.md](TROUBLESHOOTING.md)
4. Create a GitHub issue

---

**Last Updated:** March 6, 2026  
**Version:** 1.0.0
