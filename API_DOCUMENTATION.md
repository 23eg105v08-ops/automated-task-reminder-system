# API Documentation

Complete API documentation for the Automated Task Reminder System.

## Base URL

```
http://localhost:8080
```

## Authentication

Currently, the API is publicly accessible without authentication (development mode).

For production deployment, implement Spring Security with authentication.

## Endpoints

### Home & Dashboard

#### Get Dashboard
- **URL:** `/`
- **Method:** `GET`
- **Description:** Display the main dashboard with task statistics
- **Parameters:** None
- **Response:** HTML page with task statistics

```
GET http://localhost:8080/
```

#### Get Home (Alternate Route)
- **URL:** `/home`
- **Method:** `GET`
- **Description:** Alternate route to dashboard
- **Parameters:** None
- **Response:** HTML page (same as dashboard)

```
GET http://localhost:8080/home
```

---

### Task Management

#### Get All Tasks
- **URL:** `/tasks`
- **Method:** `GET`
- **Description:** Retrieve and display list of all tasks
- **Parameters:** None
- **Response:** HTML page with task list and statistics

```
GET http://localhost:8080/tasks
```

**Dashboard Stats Returned:**
- `tasks.size()` - Total number of tasks
- `pendingCount` - Number of pending tasks
- `completedCount` - Number of completed tasks
- `overdueCount` - Number of overdue tasks

---

#### Get Create Task Form
- **URL:** `/tasks/new`
- **Method:** `GET`
- **Description:** Display form to create a new task
- **Parameters:** None
- **Response:** HTML form

```
GET http://localhost:8080/tasks/new
```

---

#### Create or Update Task
- **URL:** `/tasks/save`
- **Method:** `POST`
- **Description:** Save a new task or update existing task
- **Parameters:**
  - `id` (optional) - Task ID for updates
  - `title` (required) - Task title
  - `description` (optional) - Task description
  - `priority` (required) - LOW, MEDIUM, or HIGH
  - `status` (optional) - PENDING, COMPLETED, or OVERDUE
  - `dueDateStr` (optional) - Due date in format `yyyy-MM-dd'T'HH:mm`
  - `reminderTimeStr` (optional) - Reminder time in format `yyyy-MM-dd'T'HH:mm`

**Response:** Redirect to task list

```
POST http://localhost:8080/tasks/save
Content-Type: application/x-www-form-urlencoded

title=Buy Groceries&description=Milk, eggs, bread&priority=MEDIUM&dueDateStr=2026-03-15T10:00&reminderTimeStr=2026-03-14T08:00
```

---

#### Get Edit Task Form
- **URL:** `/tasks/edit/{id}`
- **Method:** `GET`
- **Description:** Display form to edit a specific task
- **Parameters:**
  - `id` (required) - Task ID to edit
- **Response:** HTML form with task data populated

```
GET http://localhost:8080/tasks/edit/1
```

---

#### Complete Task
- **URL:** `/tasks/complete/{id}`
- **Method:** `POST`
- **Description:** Mark a task as completed
- **Parameters:**
  - `id` (required) - Task ID to complete
- **Response:** Redirect to task list

```
POST http://localhost:8080/tasks/complete/1
```

---

#### Delete Task
- **URL:** `/tasks/delete/{id}`
- **Method:** `POST`
- **Description:** Delete a specific task
- **Parameters:**
  - `id` (required) - Task ID to delete
- **Response:** Redirect to task list
- **Note:** Browser shows confirmation dialog before deletion

```
POST http://localhost:8080/tasks/delete/1
```

---

### Task Filtering

#### Filter Tasks by Status
- **URL:** `/tasks/filter/status/{status}`
- **Method:** `GET`
- **Description:** Get tasks filtered by status
- **Parameters:**
  - `status` (required) - PENDING, COMPLETED, or OVERDUE
- **Response:** HTML page with filtered tasks

**Examples:**
```
GET http://localhost:8080/tasks/filter/status/PENDING
GET http://localhost:8080/tasks/filter/status/COMPLETED
GET http://localhost:8080/tasks/filter/status/OVERDUE
```

---

#### Filter Tasks by Priority
- **URL:** `/tasks/filter/priority/{priority}`
- **Method:** `GET`
- **Description:** Get tasks filtered by priority level
- **Parameters:**
  - `priority` (required) - LOW, MEDIUM, or HIGH
- **Response:** HTML page with filtered tasks

**Examples:**
```
GET http://localhost:8080/tasks/filter/priority/HIGH
GET http://localhost:8080/tasks/filter/priority/MEDIUM
GET http://localhost:8080/tasks/filter/priority/LOW
```

---

### Utility Endpoints

#### H2 Database Console
- **URL:** `/h2-console`
- **Method:** GET
- **Description:** Access the H2 database console for debugging
- **Note:** Only available in development mode

```
GET http://localhost:8080/h2-console
```

**Console Access:**
- JDBC URL: `jdbc:h2:mem:taskreminder`
- Username: `sa`
- Password: (leave empty)

---

## Request/Response Examples

### Example 1: Create a New Task

**Request:**
```http
POST /tasks/save HTTP/1.1
Host: localhost:8080
Content-Type: application/x-www-form-urlencoded

title=Complete Project Report&description=Finish quarterly report by Friday&priority=HIGH&status=PENDING&dueDateStr=2026-03-14T17:00&reminderTimeStr=2026-03-13T09:00
```

**Response:**
```
302 Found
Location: /tasks
```

---

### Example 2: Filter High Priority Pending Tasks

**Request:**
```http
GET /tasks/filter/priority/HIGH HTTP/1.1
Host: localhost:8080
```

**Response:**
```http
200 OK
Content-Type: text/html; charset=UTF-8

<!-- HTML page with filtered tasks -->
```

---

### Example 3: Mark Task as Complete

**Request:**
```http
POST /tasks/complete/1 HTTP/1.1
Host: localhost:8080
```

**Response:**
```
302 Found
Location: /tasks
```

---

## Data Model

### Task Object

```java
{
  "id": 1,
  "title": "Buy Groceries",
  "description": "Milk, eggs, bread, butter",
  "priority": "HIGH",              // LOW, MEDIUM, HIGH
  "status": "PENDING",              // PENDING, COMPLETED, OVERDUE
  "dueDate": "2026-03-15T17:00",
  "reminderTime": "2026-03-14T08:00",
  "createdDate": "2026-03-06T11:00"
}
```

### Priority Levels

| Priority | Level | Urgency |
|----------|-------|---------|
| LOW | 1 | Non-urgent, can wait |
| MEDIUM | 2 | Normal, scheduled |
| HIGH | 3 | Urgent, immediate |

### Status Values

| Status | Meaning |
|--------|---------|
| PENDING | Task not yet completed |
| COMPLETED | Task finished successfully |
| OVERDUE | Task past due date, still pending |

---

## Error Handling

### Missing Required Field
- Status: 400 Bad Request
- Response: Form re-rendered with validation error messages

### Task Not Found
- Status: 302 Redirect
- Response: Redirects to task list (empty form shows if editing non-existent task)

### Invalid Date Format
- Status: 400 Bad Request
- Response: Form re-rendered with error message

---

## Dashboard Statistics API

The dashboard exposes the following statistics:

```json
{
  "totalTasks": 15,
  "pendingTasks": 5,
  "completedTasks": 8,
  "overdueTasks": 2
}
```

These are automatically calculated and displayed on all dashboard pages.

---

## Query Methods (Service Layer)

The `TaskService` provides these query methods that can be extended:

```java
// Retrieve operations
List<Task> findAll()
Optional<Task> findById(Long id)

// Statistics
long getTotalTasksCount()
long getPendingTasksCount()
long getCompletedTasksCount()
long getOverdueTasksCount()

// Filtering
List<Task> findByStatus(String status)
List<Task> findByPriority(String priority)
List<Task> findPendingTasks()
List<Task> findOverdueTasks()
List<Task> findTasksForReminder(LocalDateTime time)

// Actions
Task save(Task task)
void deleteById(Long id)
void markAsCompleted(Long id)
```

---

## Database Queries (Repository Layer)

The `TaskRepository` provides these custom queries:

```java
// Find pending tasks ordered by due date
@Query("SELECT t FROM Task t WHERE t.status = 'PENDING' ORDER BY t.dueDate ASC")
List<Task> findPendingTasksOrderedByDueDate()

// Find tasks with upcoming reminders
@Query("SELECT t FROM Task t WHERE t.reminderTime <= ?1 AND t.status = 'PENDING'")
List<Task> findTasksForReminder(LocalDateTime time)

// Find overdue tasks
@Query("SELECT t FROM Task t WHERE t.dueDate < ?1 AND t.status = 'PENDING'")
List<Task> findOverdueTasks(LocalDateTime time)
```

---

## Rate Limiting

Currently no rate limiting is implemented. For production:

- Implement API rate limiting per user/IP
- Add request throttling
- Monitor API usage

---

## API Versioning

The current API is version 1.0. Future versions may implement:

- `/api/v2/tasks`
- Versioned response formats
- Backward compatibility

---

## Testing the API

### Using cURL

```bash
# Get all tasks
curl http://localhost:8080/tasks

# Get high priority tasks
curl http://localhost:8080/tasks/filter/priority/HIGH

# Create a task (form data)
curl -X POST http://localhost:8080/tasks/save \
  -d "title=Test Task&description=Test&priority=MEDIUM&status=PENDING"

# Complete a task
curl -X POST http://localhost:8080/tasks/complete/1
```

### Using Postman

1. Import the following collection (JSON):
   - All endpoints are available at `http://localhost:8080`
   - Method types: GET for fetch, POST for create/update/delete

---

## Environment Variables

Future versions may support environment configuration:

```bash
TASK_APP_PORT=8080
TASK_DB_URL=jdbc:h2:mem:taskreminder
TASK_DB_USER=sa
TASK_DB_PASSWORD=
```

---

**Last Updated:** March 6, 2026  
**Version:** 1.0.0  
**API Status:** Development
