FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/asx-news-app*.jar app.jar
# Install Firefox
RUN apt-get update && apt-get install -y firefox-esr

# Install Geckodriver
RUN apt-get install -y wget && \
    wget -q https://github.com/mozilla/geckodriver/releases/latest/download/geckodriver-linux64.tar.gz && \
    tar -xvzf geckodriver-linux64.tar.gz && \
    chmod +x geckodriver && \
    mv geckodriver /usr/local/bin/
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
