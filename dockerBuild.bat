
docker build -t backendapi0212 .
docker-compose up -d
docker run -p 8000:8000 backendapi0212
pause