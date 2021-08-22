FROM openjdk:11
VOLUME /tmp
EXPOSE 8080
ADD ./target/decatalog-0.0.1-SNAPSHOT.jar decatalog.jar
ENTRYPOINT ["java","-jar","/decatalog.jar"]

CMD ["bash"]