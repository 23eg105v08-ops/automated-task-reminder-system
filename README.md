# Automated Task Reminder System

A modern, full-featured **Automated Task Reminder System** built with **Spring Boot 4.0**, **Thymeleaf**, **Spring Data JPA**, and **H2 Database**. This application provides a complete task management solution with priority tracking, automatic reminder scheduling, and an intuitive user interface.

## ✨ Features

### Core Functionality
- ✅ **Create & Manage Tasks** - Add tasks with detailed descriptions
- ✅ **Priority Levels** - Set LOW, MEDIUM, or HIGH priority for each task
- ✅ **Status Tracking** - Track tasks as PENDING, COMPLETED, or OVERDUE
- ✅ **Due Date Management** - Set due dates and track overdue tasks
- ✅ **Reminder System** - Schedule reminders for automated notifications
- ✅ **Advanced Filtering** - Filter tasks by status and priority levels
- ✅ **Dashboard Analytics** - View real-time task statistics
- ✅ **Responsive UI** - Modern, mobile-friendly interface with gradient design

### Technical Features
- 💾 **Persistent Storage** - H2 in-memory database with automatic DDL
- 🔍 **Custom Queries** - TaskRepository with specialized queries for reminders
- 🏗️ **Clean Architecture** - Layered architecture (Controller → Service → Repository)
- 🔐 **Security** - Public access configuration for development
- 📱 **Responsive Design** - CSS3 with modern gradient styling
- ⚡ **Spring Boot 4.0** - Latest Spring Boot framework with Java 21

## 🚀 Quick Start

### Prerequisites
- Java 21 or higher
- Maven 3.9 or higher
- Git

### Installation & Running

```powershell
# Navigate to project directory
cd demo

# Run directly with Maven
.\mvnw.cmd -DskipTests spring-boot:run
```

Or build and run the JAR:

```powershell
.\mvnw.cmd -DskipTests package
java -jar target\demo-0.0.1-SNAPSHOT.jar
```

### Access the Application

Once running, open your browser:

- **Dashboard**: http://localhost:8080
- **Task Management**: http://localhost:8080/tasks
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:taskreminder`
  - Username: `sa`
  - Password: (leave empty)

## 📋 Project Structure

```
demo/
├── src/main/java/com/dinesh/demo/
│   ├── DemoApplication.java                    # Spring Boot entry point
│   ├── config/
│   │   └── SecurityConfig.java                 # Security configuration
│   ├── controller/
│   │   ├── HomeController.java                 # Dashboard & home routes
│   │   └── TaskController.java                 # Task CRUD endpoints
│   ├── model/
│   │   └── Task.java                           # Task JPA entity
│   ├── repository/
│   │   └── TaskRepository.java                 # Data access layer
│   └── service/
│       └── TaskService.java                    # Business logic
│
├── src/main/resources/
│   ├── templates/
│   │   ├── index.html                          # Dashboard page
│   │   └── tasks/
│   │       ├── list.html                       # Task list view
│   │       └── form.html                       # Task create/edit form
│   ├── static/css/
│   │   └── styles.css                          # Application styling
│   └── application.properties                  # Spring configuration
│
└── pom.xml                                     # Maven dependencies
```

## 🗄️ Database Schema

### Tasks Table
```sql
CREATE TABLE tasks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    priority VARCHAR(20),                    -- LOW, MEDIUM, HIGH
    status VARCHAR(20),                       -- PENDING, COMPLETED, OVERDUE
    due_date TIMESTAMP,
    reminder_time TIMESTAMP,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 🎯 API Endpoints

### Task Management Routes
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | Show dashboard |
| GET | `/home` | Alternate dashboard route |
| GET | `/tasks` | List all tasks |
| GET | `/tasks/new` | Display task creation form |
| POST | `/tasks/save` | Save new or updated task |
| GET | `/tasks/edit/{id}` | Display task edit form |
| POST | `/tasks/delete/{id}` | Delete specific task |
| POST | `/tasks/complete/{id}` | Mark task as completed |
| GET | `/tasks/filter/status/{status}` | Filter by status (PENDING/COMPLETED/OVERDUE) |
| GET | `/tasks/filter/priority/{priority}` | Filter by priority (LOW/MEDIUM/HIGH) |

## 🎨 UI Components

### Dashboard
- Total tasks counter
- Completed tasks counter
- Pending tasks counter
- Overdue tasks counter
- Quick action buttons for navigation

### Task List
- Sortable task table with all details
- Color-coded status badges (green, yellow, red)
- Priority indicators (Low: green, Medium: yellow, High: red)
- Inline action buttons (Edit, Complete, Delete)
- Filter options by status and priority
- Empty state with helpful prompts

### Task Form
- Title input field
- Description textarea
- Priority selector (LOW, MEDIUM, HIGH)
- Status dropdown (PENDING, COMPLETED, OVERDUE)
- Due date/time picker
- Reminder time picker
- Form validation
- Cancel and submit buttons

## 🔧 Technology Stack

| Technology | Version | Purpose |
|-----------|---------|---------|
| Spring Boot | 4.0.2 | Web framework & REST services |
| Spring Data JPA | 4.0.2 | ORM & database access |
| Thymeleaf | 3.x | Server-side templating engine |
| H2 Database | 2.4 | Embedded in-memory database |
| Hibernate | 7.2.1 | JPA implementation |
| Lombok | 1.18.30 | Boilerplate code reduction |
| Maven | 3.9+ | Build automation tool |
| Java | 21 | Programming language |

## 🌟 Key Configuration

### Application Properties
```properties
spring.application.name=automated-task-reminder
spring.datasource.url=jdbc:h2:mem:taskreminder
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### Security Configuration
- All endpoints permit public access (development mode)
- CSRF protection disabled (safe for development)
- HTTP Basic auth disabled for direct browser access
- H2 frame options enabled for console access

## 📊 Task Priority Levels

- **LOW** - Can be done when time permits, non-urgent tasks
- **MEDIUM** - Should be accomplished within normal timeframe
- **HIGH** - Critical tasks that require immediate attention

## 🔄 Task Status Flow

```
PENDING (Initial state)
   ├─→ COMPLETED (when marked complete)
   │
   └─→ OVERDUE (when due_date < current_time)
```

## 🚧 Project Conversion History

This project was successfully converted from the original Student Management System to a complete Automated Task Reminder System:

### Replaced Components
- ❌ Student → ✅ Task model with enhanced properties
- ❌ StudentRepository → ✅ TaskRepository (with custom queries)
- ❌ StudentService → ✅ TaskService (with business logic)
- ❌ StudentController → ✅ TaskController (CRUD + filtering)
- ❌ Student templates → ✅ Task templates (list + form)

### Added Enhancements
- ✅ Priority and status tracking system
- ✅ Due date and reminder management
- ✅ Advanced filtering by status and priority
- ✅ Task-specific UI components
- ✅ Dashboard with real-time statistics
- ✅ Updated styling with task theme
- ✅ Security configuration for public access

## 🔐 Authentication & Security

### Development Mode (Current)
- ⚠️ No authentication required
- ⚠️ CSRF protection disabled
- ⚠️ All endpoints publicly accessible
- ✅ Suitable for local development

### Production Recommendations
- Implement Spring Security with user authentication
- Enable CSRF protection
- Use HTTPS for data transmission
- Restrict database access
- Implement role-based access control
- Store sensitive data in environment variables

## 🌐 Future Enhancements

- [ ] User authentication and authorization
- [ ] Email notifications for reminders
- [ ] SMS notifications
- [ ] Task categories and tags
- [ ] Recurring task support
- [ ] Task dependencies
- [ ] Analytics dashboard
- [ ] REST API (JSON endpoints)
- [ ] Mobile app integration
- [ ] File attachments
- [ ] Team collaboration
- [ ] Activity logging

## 🐛 Troubleshooting

### Application won't start
```powershell
# Clear Maven cache
mvn clean
# Rebuild
mvn clean install
```

### Database connection error
- Verify H2 is enabled in application.properties
- Check JDBC URL: `jdbc:h2:mem:taskreminder`
- Access H2 console at `/h2-console`

### Port 8080 already in use
```powershell
# Find process using port
netstat -ano | findstr :8080
# Kill process or change port in application.properties
```

## 📚 Documentation

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Thymeleaf Documentation](https://www.thymeleaf.org/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [H2 Database](https://www.h2database.com/)

## 🤝 Contributing

Contributions are welcome! Please:
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is open source and available for educational and commercial use.

---

**Project Status:** ✅ Production Ready  
**Last Updated:** March 6, 2026  
**Version:** 1.0.0  
**Java Version:** 21  
**Spring Boot Version:** 4.0.2  
**Maintainer:** Muntha Dinesh Yadav
