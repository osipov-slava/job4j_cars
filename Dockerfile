#Stage1 - Build project
FROM maven:3.6.3-openjdk-17 as maven
WORKDIR /app
COPY . /app
RUN mvn install -Dmaven.test.skip=true
CMD mvn liquibase:update -Pdocker

#Stage2 - Run project
FROM openjdk:17.0.2-jdk
WORKDIR /app
COPY --from=maven /app/target/job4j_cars-1.0-SNAPSHOT.jar cars.jar
CMD java -jar cars.jar
