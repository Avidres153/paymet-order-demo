#!/bin/bash

################# Build java projects #################
echo "-------> Building projects"
CURRENT_DIR="$(pwd)"
echo "Current dir: $CURRENT_DIR"

PROJECT_DIR="$CURRENT_DIR/payment-order-service"
echo "Dir to build microservice: $PROJECT_DIR"

echo "Building microservice..."
cd $PROJECT_DIR
mvn clean package

if [ $? -eq 0 ]; then
    echo "The compilation was successful!!!"
else
    echo "Error to build the microservice."
    exit 1
fi

echo "-------> Copying compiled artifacts to docker directory"
DOCKER_DIR="$CURRENT_DIR/docker"

mkdir -p "$DOCKER_DIR/tmp"

echo "-------> Copying artifact"
cp "$PROJECT_DIR/target/"*.jar "$DOCKER_DIR/tmp"


################# Build docker images #################

cd $DOCKER_DIR

echo "-------> Building microservice image"

docker build --build-arg APP_NAME=payment-order-service-1.0.0-SNAPSHOT.jar -t payment-orders-microservice .


################# Starting docker containers #################

echo "-------> Stating docker containers"

docker compose up -d --build

if [ $? -eq 0 ]; then
    echo "-------> Apps are ready to use!!!"
else
    echo "Error to get up Docker compose"
fi