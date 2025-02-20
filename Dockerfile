FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/asx-news-app*.jar app.jar
RUN apt-get update && apt-get install -y
RUN wget -q https://storage.googleapis.com/chrome-for-testing-public/133.0.6943.126/linux64/chrome-linux64.zip
RUN unzip chrome-linux64.zip -d /usr/local/bin/
RUN wget -q https://storage.googleapis.com/chrome-for-testing-public/133.0.6943.126/linux64/chromedriver-linux64.zip
RUN unzip chromedriver_linux64.zip -d /usr/local/bin/
RUN chmod +x /usr/local/bin/chromedriver
RUN rm -rf chrome-linux64* chromedriver-linux64*
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
