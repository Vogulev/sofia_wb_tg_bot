FROM maven:3.9-amazoncorretto-21 as build
ADD --chown=maven:maven . /application/
WORKDIR /application
RUN mvn clean install

FROM bellsoft/liberica-openjdk-debian:21.0.1-12 as prod
WORKDIR /application
ARG WAR_FILE=/application/target/sofia_wb_tg_bot-1.0.0.jar
ARG VIDEO_FILE=/application/target/classes/video/greet_video.mp4
ARG PHOTO_DIR=/application/target/classes/photo
COPY --from=build ${WAR_FILE} /application/app.jar
COPY --from=build ${VIDEO_FILE} /application/video/greet_video.mp4
COPY --from=build ${PHOTO_DIR} /application/photo
ENV DB_USERNAME=example
ENV DB_PASSWORD=example
ENV DB_NAME=example
ENV DB_HOST=localhost
ENV DB_PORT=5432
ENV APP_PORT=8080
ENV BOT_TOKEN=example
ENV BOT_ADMINS=example
ENTRYPOINT ["java", "-Dspring.datasource.password=${DB_PASSWORD}", "-Dspring.datasource.username=${DB_USERNAME}", "-Dspring.datasource.url=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}", "-Dserver.port=${APP_PORT}", "-jar", "/application/app.jar"]