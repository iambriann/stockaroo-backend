FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/asx-news-app*.jar app.jar
# Install Firefox
RUN apt-get update && apt-get install -y firefox-esr

# Install Geckodriver
RUN apt-get install -y wget
RUN wget -q https://github.com/mozilla/geckodriver/releases/latest/download/geckodriver-v0.35.0-linux64.tar.gz
RUN tar -xvzf geckodriver-v0.35.0-linux64.tar.gz
RUN chmod +x geckodriver
RUN mv geckodriver /usr/local/bin/
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
