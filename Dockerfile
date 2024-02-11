# 빌드 스테이지
FROM gradle:8.5-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon -x test

# 실행 스테이지
FROM --platform=linux/x86_64 openjdk:17.0
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-app.jar
ENTRYPOINT ["java", "-jar", "/app/spring-app.jar"]