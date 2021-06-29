FROM adoptopenjdk:11-jre-openj9
ADD build/libs/dia-0.0.1-SNAPSHOT.jar dia-0.0.1-SNAPSHOT.jar
EXPOSE 8443
ENTRYPOINT ["java","-jar","dia-0.0.1-SNAPSHOT.jar"]


