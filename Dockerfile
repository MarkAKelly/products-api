FROM openjdk:8-jre-alpine

ADD target/scala-2.13/products-api-assembly-2.8.x.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
