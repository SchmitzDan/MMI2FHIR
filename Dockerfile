FROM gradle:7.4.2-jdk17 AS build
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . .
RUN gradle build -x test --no-daemon --info


FROM eclipse-temurin:17-jre-alpine
COPY --from=build /home/gradle/src/build/libs/*.jar /opt/mmi-to-fhir.jar

ARG VERSION=0.0.0
ENV APP_VERSION=${VERSION}
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=85", "-jar", "/opt/mmi-to-fhir.jar"]
