# 📋 Task Manager

Ứng dụng quản lý công việc được xây dựng bằng **Spring Boot + MySQL + Docker**.

## 🚀 Công nghệ sử dụng

- Java 17
- Spring Boot 3
- Spring Data JPA
- Spring Security
- Thymeleaf
- Bootstrap 5
- MySQL 8
- Docker & Docker Compose

## ✨ Chức năng

- Đăng nhập
- Quản lý người dùng
- Quản lý công việc
- Dashboard thống kê
- Upload avatar
- Activity Log
- Phân quyền Admin/User

---

## 🐳 Chạy bằng Docker

### Clone project

```bash
git clone https://github.com/DevCuong205/baiTapNhom.git
```

### Di chuyển vào project

```bash
cd baiTapNhom
```

### Chạy

```bash
docker compose up --build -d
```

Ứng dụng sẽ chạy tại

```
http://localhost:8080
```

---

## 🗄 Database

MySQL

```
Host: localhost
Port: 3307
Username: root
Password: 241005
Database: taskmanager
```

Dữ liệu sẽ được import tự động từ file `init.sql`.

---

## 👤 Tài khoản Demo

### Admin

```
username: admin
password: 12345
```

### User

```
username: user1
password: 123456
```

---

## 📸 Giao diện

(Login)

(Dashboard)

(Tasks)

(Users)

---

## 👨‍💻 Tác giả

Phạm Đức Cường
Nguyễn Xuân Đông 
Nguyễn Tiến Dũng
