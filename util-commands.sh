# Run gradle project.
./gradlew bootRun

# or if you are using maven
./mvnw spring-boot:run

# Refresh gradle dependencies.
./gradlew build --refresh-dependencies

# https://stackoverflow.com/questions/66514436/difference-between-docker-compose-and-docker-compose
docker-compose rm -v
docker-compose up
docker-compose down