FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Копіюємо файли Gradle Wrapper у контейнер
COPY gradlew .
COPY gradlew.bat .
COPY gradle/ ./gradle/

# Копіюємо файли проекту
COPY build.gradle .
COPY settings.gradle .
COPY src/ ./src/

# Виконуємо команду збірки проекту Gradle
RUN ./gradlew build

# Вказуємо команду для запуску додатку Spring Boot
CMD ["./gradlew", "bootRun"]