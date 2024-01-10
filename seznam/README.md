# RSO: Image metadata microservice

## Prerequisites

```bash
docker run -d --name pg-seznam-metadata -e POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=seznam-metadata -p 5432:5432 postgres:13
```

## Build and run commands
```bash
mvn clean package
cd api/target
java -jar seznam-api-1.0.0-SNAPSHOT.jar
```
Available at: localhost:8080/v1/seznam

## Docker commands
```bash
docker build -t seznam-slika .   
docker images
docker run seznam-slika    
docker tag seznam-slika bi4528/seznam-slika   
docker push bi4528/seznam-slika  
```

## Kubernetes
```bash
kubectl version
kubectl --help
kubectl get nodes
kubectl create -f image-catalog-deployment.yaml 
kubectl apply -f image-catalog-deployment.yaml 
kubectl get services 
kubectl get deployments
kubectl get pods
kubectl logs image-catalog-deployment-6f59c5d96c-rjz46
kubectl delete pod image-catalog-deployment-6f59c5d96c-rjz46
```