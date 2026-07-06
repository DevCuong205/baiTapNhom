# Task Manager

## Giới thiệu
Task Manager là hệ thống quản lý công việc giúp người dùng theo dõi, tạo và quản lý các nhiệm vụ hằng ngày.

## Thành viên nhóm
- Phạm Đức Cường
- Nguyễn Xuân Đông
- Nguyễn Tiến Dũng

## Công nghệ sử dụng
- Java 17
- Spring Boot
- Spring MVC
- Spring Data JPA
- Thymeleaf
- MySQL
- Bootstrap 5
- Maven
- Git & GitHub

## Chức năng
- Đăng ký tài khoản
- Đăng nhập/Đăng xuất
- Quản lý thông tin cá nhân
- Thêm công việc
- Sửa công việc
- Xóa công việc
- Tìm kiếm công việc
- Quản lý người dùng (Admin)

## Yêu cầu
- JDK 17 trở lên
- Maven
- MySQL
- IntelliJ IDEA

## Cài đặt và chạy

### Clone project
```bash
git clone https://github.com/DevCuong205/baiTapNho.git
```

### Cấu hình Database
Tạo database trong MySQL và chỉnh sửa file:

```
src/main/resources/application.properties
```

Cập nhật:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/taskmanager
spring.datasource.username=root
spring.datasource.password=241005
```

### Chạy project
Mở project bằng IntelliJ IDEA và chạy lớp:

```
TaskManagerApplication.java
```

## Repository
https://github.com/DevCuong205/baiTapNhom

## Giấy phép
Project được thực hiện phục vụ mục đích học tập.
