# 📝 Task Manager Application

A **Spring Boot–based Task Manager backend application** that allows users to manage tasks efficiently with **secure authentication**, **background automation**, **caching**, and **analytics**.

---

## 🚀 Features

- 🔐 **JWT-based Authentication & Authorization**
- 👤 **User-specific Task Management**
- ➕ Create tasks with due dates
- ✏️ Update task status (completed / pending)
- 📅 View today’s tasks
- 📆 Filter tasks by date range
- ⏰ **Automated overdue task detection**
- 📊 **Daily Analytics Dashboard**
- ⚡ **Redis caching** for fast performance
- 📡 **Kafka-based event processing**
- 🧹 Automated background jobs using **Spring Scheduler**

---

## 🛠 Tech Stack

- **Backend**: Spring Boot  
- **Security**: Spring Security + JWT  
- **Database**: MySQL  
- **ORM**: Spring Data JPA (Hibernate)  
- **Caching**: Redis (String-based, JSON stored)  
- **Messaging**: Apache Kafka  
- **Scheduler**: Spring Scheduler  
- **Build Tool**: Maven  

---

## 🧠 Architecture Overview

Client
→ Controller
→ Service
→ Repository
→ Database (MySQL)
→ Redis (Cache)
→ Kafka (Events)
→ Scheduler (Background Jobs)
---
## ⏰ Automated Background Operations

- Daily **overdue task check**
- Automatic task status updates
- Daily **analytics refresh**
- Redis cache refresh handled automatically

Implemented using Spring Scheduler:
java
@Scheduled(cron = "0 0 0 * * ?")


## 📊 Analytics Dashboard

Daily analytics include:
- Total tasks  
- Completed tasks  
- Pending tasks  
- Overdue tasks  

Analytics are:
- Recalculated daily using Spring Scheduler  
- Stored in Redis for fast access  
- Updated safely even if a single user fails  

---

## ⚡ Redis Usage

- Redis is used as a **String-based cache**
- Objects are stored as **JSON strings**
- Serialization is handled manually using **ObjectMapper**
- TTL is applied to cached analytics (**24 hours**)

---

## 📡 Kafka Usage

- Kafka is used for **asynchronous processing**
- Daily events are published for:
  - Remaining (pending) tasks
  - Task analytics updates
- Kafka consumers handle **email notifications** for daily remaining tasks
- Ensures non-blocking APIs and better scalability

---

## 🔐 Security

- **JWT-based stateless authentication**
- Each user can access **only their own tasks**
- All API endpoints are secured using **Spring Security**

## ▶️ How to Run the Project

### Prerequisites
- Java 17+
- MySQL
- Redis
- Kafka & Zookeeper

### Steps
```bash
git clone https://github.com/SAHILSALUNKHE007/TaskManager.git
cd TaskManager
mvn clean install
mvn spring-boot:run


