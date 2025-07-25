FROM openjdk:17-jdk-slim
# please review all the latest versions here:
# https://googlechromelabs.github.io/chrome-for-testing/
ENV CHROMEDRIVER_VERSION=138.0.7204.168

### install chrome
RUN apt-get update && apt-get install -y wget && apt-get install -y zip
RUN wget -q https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
RUN apt-get install -y ./google-chrome-stable_current_amd64.deb

### install chromedriver
RUN wget https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/$CHROMEDRIVER_VERSION/linux64/chromedriver-linux64.zip \
  && unzip chromedriver-linux64.zip && rm -dfr chromedriver_linux64.zip \
  && mv /chromedriver-linux64/chromedriver /usr/bin/chromedriver \
  && chmod +x /usr/bin/chromedriver
WORKDIR /app
COPY target/asx-news-app*.jar app.jar
EXPOSE 443
ENTRYPOINT ["java", "-jar", "app.jar"]