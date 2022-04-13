FROM maven:3.6.0-jdk-8
#MAINTAINER yurii.di@onyx.com
MAINTAINER dmytro.kor@onyx.com
WORKDIR /usr/src/carid-ui-tests
COPY . /usr/src/carid-ui-tests
RUN mvn clean compile
ENTRYPOINT ["mvn", "test"]