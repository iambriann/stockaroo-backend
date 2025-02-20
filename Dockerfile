FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/asx-news-app*.jar app.jar
RUN apt-get update && apt-get install -y wget unzip
RUN wget -q https://archive.org/download/google-chrome-stable_91.0.4472.114-1/google-chrome-stable_91.0.4472.114-1_amd64.deb
RUN apt-get install -y ./google-chrome-stable_91.0.4472.114-1_amd64.deb
RUN wget -q https://chromedriver.storage.googleapis.com/91.0.4472.101/chromedriver_linux64.zip
RUN unzip chromedriver_linux64.zip -d /usr/local/bin/
RUN chmod +x /usr/local/bin/chromedriver
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
