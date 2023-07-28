# futures_bot

sudo docker run --name futures_bot_db -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e TZ=Europe/Kyiv -e PGTZ=Europe/Kyiv -d postgres:15.3

sudo docker run --name futures_bot_db -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e -d postgres:15.3

sudo docker start futures_bot_db


sudo docker ps -a

sudo docker ps

sudo docker images

sudo docker build --tag grid_bot_v03 .
sudo docker save -o grid_bot_v03.tar grid_bot_v03

sudo ssh -i "key_bot_v02.pem" ubuntu@3.71.53.20
sudo ssh -i "key_bot_v02.pem" ubuntu@18.194.233.15


sudo scp -i "key_bot_v02.pem" grid_bot_v03.tar ubuntu@3.71.53.20:grid_bot
sudo scp -i "key_bot_v02.pem" grid_bot_valia_v01.tar ubuntu@18.194.233.15:grid_bot

sudo docker load -i grid_bot/grid_bot_v03.tar
sudo docker load -i grid_bot/grid_bot_valia_v01.tar

sudo docker rm 53d4d6f0539e
sudo docker stop 42e7106d511b

sudo docker logs 53d4d6f0539e

sudo docker logs -f bf48bc9aa7e2 

