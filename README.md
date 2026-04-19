docker-compose build --no-cache

docker-compose up -d

docker-compose down

Доступ к сервисам:
- Eureka Dashboard: http://localhost:8761
- Kafdrop (Kafka UI): http://localhost:9000
- PostgreSQL: localhost:5432
- User-service: http://localhost:8081/
- Notification-service: http://localhost:8082/