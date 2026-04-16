# bookhub

## Prod

./gradlew bootRun --args='--spring.profiles.active=prod'

### ou via variable d'environnement

SPRING_PROFILES_ACTIVE=prod DB_HOST=srv DB_NAME=bookhub DB_USER=app DB_PASSWORD=xxx JWT_SECRET=yyy ./gradlew bootRun
