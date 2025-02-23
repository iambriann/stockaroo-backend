FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y \
    wget \
    unzip \
    libxss1 \
    libappindicator3-1 \
    libnss3 \
    libx11-6 \
    libx11-xcb1 \
    libgbm1 \
    --no-install-recommends && \
    rm -rf /var/lib/apt/lists/*
RUN wget -q https://storage.googleapis.com/chrome-for-testing-public/133.0.6943.126/linux64/chromedriver-linux64.zip
RUN ls -lh chromedriver-linux64.zip
RUN unzip chromedriver-linux64.zip -d /usr/local/bin/
RUN rm chromedriver-linux64.zip
RUN chmod +x /usr/local/bin/chromedriver-linux64/chromedriver
RUN rm -rf chromedriver-linux64*
WORKDIR /app
COPY target/asx-news-app*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]