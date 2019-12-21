FROM java:8

ADD ./target/dropwizard-jukebox-api-2.0.1-SNAPSHOT.jar jukebox.jar
ADD ./src/main/resources/config.yml config.yml

EXPOSE 8080

CMD java -jar jukebox.jar server config.yml

