FROM java:alpine
VOLUME /tmp
ADD aiops-prometheus.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
