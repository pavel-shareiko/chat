FROM gradle:jdk19-alpine
COPY ../. /chat

WORKDIR /chat 

RUN chmod +x gradlew && ./gradlew assemble -Pprod
ENTRYPOINT ["java","-jar", "./build/libs/chat-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]
