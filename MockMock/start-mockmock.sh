
# Build the Docker image locally
docker build --tag java-mockmock .

docker run -d --name mockmock --rm -p 2525:25 -p 8282:8282 java-mockmock  
