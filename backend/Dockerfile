# 多阶段构建 - 构建阶段
FROM maven:3.8.6-openjdk-11-slim AS build

WORKDIR /app

# 复制pom.xml并下载依赖 (利用Docker缓存)
COPY pom.xml .
COPY settings.xml /root/.m2/settings.xml
RUN mvn dependency:go-offline -B

# 复制源代码并构建
COPY src ./src
RUN mvn clean package -DskipTests -B

# 运行阶段
FROM eclipse-temurin:11-jre

WORKDIR /app

# 复制构建的jar文件
COPY --from=build /app/target/*.jar app.jar

# 暴露端口
EXPOSE 8080

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]
