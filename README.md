# futures_bot

sudo docker run --name futures_bot_db -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e TZ=Europe/Kyiv -e PGTZ=Europe/Kyiv -d postgres:15.3

sudo docker run --name futures_bot_db -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e -d postgres:15.3

sudo docker start futures_bot_db


sudo docker ps -a

sudo docker ps
