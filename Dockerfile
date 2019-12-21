FROM java:8

WORKDIR /

ADD dropwizard-jukebox-api-2.0.1-SNAPSHOT.jar jukebox.jar

EXPOSE 8080

CMD java - jar jukebox.jar server src/main/resources/config.yml

