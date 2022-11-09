docker stack rm backend-uae

docker stack rm db-uae

docker network create --driver=overlay --attachable uae

cd ./db

./init.sh

cd ./../

sleep 2m # Waits 5 minutes.

cd ./review-service

./init.sh

cd ./../

sleep 15s

cd ./product-service

./init.sh

cd ./../

read -p "Press any key..."