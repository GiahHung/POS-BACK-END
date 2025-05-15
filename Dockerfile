# Sử dụng OpenJDK 23 làm base image
FROM openjdk:23-jdk-slim

# Đặt thư mục làm thư mục làm việc
WORKDIR /app

# Sao chép file JAR vào container
COPY target/*.jar app.jar

# Chạy ứng dụng khi container khởi động
ENTRYPOINT ["java", "-jar", "app.jar"]
