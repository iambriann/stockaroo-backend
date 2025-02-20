FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/asx-news-app*.jar app.jar
RUN apt-get update && apt-get install -y wget unzip
RUN wget -q https://storage.googleapis.com/chrome-for-testing-public/133.0.6943.126/linux64/chrome-linux64.zip
RUN apt-get install -y ./chrome-linux64.zip
RUN wget -q https://storage.googleapis.com/chrome-for-testing-public/133.0.6943.126/linux64/chromedriver-linux64.zip
RUN unzip chromedriver_linux64.zip -d /usr/local/bin/
RUN chmod +x /usr/local/bin/chromedriver
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
