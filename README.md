# Planner

Planner is a feature-rich and intuitive application designed to help you organize and manage your tasks effectively. This repository provides a seamless setup using Docker Compose for quick deployment.

---

## 🚀 Getting Started

Follow these instructions to set up and run the Planner application on your local machine.

### Prerequisites
- **Docker**: [Install Docker](https://docs.docker.com/get-docker/)

---


### 🐳 Download and extract the zip file from releases and start the application with docker-compose

```bash
docker-compose up --build
```

---

### 🌐 Access the Application

```text
http://localhost:8888/swagger-ui.html
```
---

### Manual compilation process - prerequisites
- **Git**: [Install Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)
- **Docker**: [Install Docker](https://docs.docker.com/get-docker/)
- **Maven** [Install Maven](https://maven.apache.org/download.cgi)

---

### 📥 Clone the repository

```bash
git clone https://github.com/ftelega/planner.git
cd planner
```

---


### ☕ Compile the project (make sure docker is running!)

```bash
mvn clean package
```

---


### 🐳 Start the application with docker-compose

```bash
docker-compose up --build
```

---


### 🌐 Access the Application

```text
http://localhost:8888/swagger-ui.html
```


