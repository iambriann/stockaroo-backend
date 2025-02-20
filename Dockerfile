FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y \
    wget \
    unzip \
    libxss1 \
    libappindicator3-1 \
    libindicator7 \
    fonts-liberation \
    libnss3 \
    libx11-6 \
    libx11-xcb1 \
    && rm -rf /var/lib/apt/lists/*  # Clean up to reduce image size
WORKDIR /app
COPY target/asx-news-app*.jar app.jar
RUN apt-get update && apt-get install -y wget unzip
RUN wget -q https://storage.googleapis.com/chrome-for-testing-public/133.0.6943.126/linux64/chrome-linux64.zip
RUN unzip chrome-linux64.zip -d /usr/local/bin/
RUN wget -q https://storage.googleapis.com/chrome-for-testing-public/133.0.6943.126/linux64/chromedriver-linux64.zip
RUN unzip chromedriver_linux64.zip -d /usr/local/bin/
RUN chmod +x /usr/local/bin/chromedriver
RUN rm -rf chrome-linux64* chromedriver-linux64*
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
