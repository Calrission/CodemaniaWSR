FROM openjdk:17-jdk-slim

WORKDIR /app

COPY . /app

RUN chmod +x /app/restore-db.sh
RUN chmod +x /app/codemania.jar
RUN chmod +x /app/wait-for-db.sh


