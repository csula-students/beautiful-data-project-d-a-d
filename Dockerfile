FROM java:openjdk-8-jdk

RUN rm /bin/sh && ln -s /bin/bash /bin/sh

WORKDIR /usr/local/src/big-data

ADD build.gradle /usr/local/src/big-data
ADD gradle /usr/local/src/big-data/gradle
ADD gradlew /usr/local/src/big-data
ADD src /usr/local/src/big-data/src

RUN ./gradlew shadowJar && mv build/libs/big-data-1.0-SNAPSHOT-all.jar /usr/local/bin/big-data.jar

ENTRYPOINT ["java", "-jar", "/usr/local/bin/big-data.jar"]
