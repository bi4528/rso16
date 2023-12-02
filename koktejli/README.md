# RSO: Image metadata microservice

## Prerequisites

```bash
docker run -d --name pg-koktejli-metadata -e POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=koktejli-metadata -p 5432:5432 postgres:13
```

## Build and run commands
```bash
mvn clean package
cd api/target
java -jar koktejli-api-1.0.0-SNAPSHOT.jar
```
Available at: localhost:8080/v1/koktejli

## Docker commands
```bash
docker build -t koktejli-slika .   
docker images
docker run koktejli-slika    
docker tag koktejli-slika bi4528/koktejli-slika   
docker push bi4528/koktejli-slika  
