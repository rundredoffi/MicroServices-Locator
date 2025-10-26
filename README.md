# Architecture Microservices

## Description

Projet d'architecture microservices avec Spring Boot comprenant une API Gateway et deux microservices : gestion des utilisateurs et gestion des localisations GPS.

## Technologies utilisées

- Spring Boot 3.x
- Spring Cloud Gateway
- MySQL 8.0
- Apache Kafka
- Docker & Docker Compose
- GraphQL

## Architecture

```
Client
  |
API Gateway (8888)
  |
  +-- MS-User (8080) -- MySQL (user-db)
  |
  +-- MS-Locations (8081) -- MySQL (location-db) -- Kafka
```

## Microservices

### API Gateway (Port 8888)
Point d'entrée unique pour tous les services. Route les requêtes vers les microservices appropriés.

**Configuration:**
- Framework: Spring Cloud Gateway
- Routage: /msuser/** vers MS-User, /mslocation/** vers MS-Locations
- CORS: Activé pour tous les endpoints

### MS-User (Port 8080)
Gestion des utilisateurs avec support REST et GraphQL.

**Base de données:** MySQL (user-db)

**APIs disponibles:**

REST classique:
```
GET    /users/all
GET    /users/{id}
POST   /users/add
```

REST-GraphQL:
```
GET    /graphql/users
GET    /graphql/users/{id}
POST   /graphql/add
PUT    /graphql/users/{id}
DELETE /graphql/users/{id}
```

GraphQL natif:
```
POST   /graphql
Body: {"query": "{ users { id nom prenom email } }"}
```

### MS-Locations (Port 8081)
Gestion des localisations GPS avec détection de proximité en temps réel via Kafka.

**Base de données:** MySQL (location-db)

**APIs disponibles:**
```
GET    /locations/all
GET    /locations/{id}
GET    /locations/user/{userId}
POST   /locations/add
```

**Fonctionnalités:**
- Stockage des coordonnées GPS
- Détection de proximité entre utilisateurs
- Notifications via Kafka (topic: proximity-alerts)

## Démarrage

### Prérequis
- Docker
- Docker Compose
- Ports disponibles: 8080, 8081, 8888, 3306, 9092

### Lancement
```bash
docker compose up -d
```

### Vérification
```bash
docker ps
```

Tous les conteneurs doivent être en état "Up" ou "Healthy".

## Tests avec Postman

### Créer un utilisateur
```http
POST http://localhost:8888/msuser/users/add
Content-Type: application/json

{
  "nom": "Dupont",
  "prenom": "Jean",
  "email": "jean@example.com",
  "password": "pwd123"
}
```

### Ajouter une localisation
```http
POST http://localhost:8888/mslocation/locations/add
Content-Type: application/json

{
  "latitude": 48.8566,
  "longitude": 2.3522,
  "userId": 1
}
```

### Requête GraphQL
```http
POST http://localhost:8888/msuser/graphql
Content-Type: application/json

{
  "query": "{ users { id nom prenom email } }"
}
```

## Kafka

Accès au topic des alertes de proximité:
```bash
docker exec kafka /opt/kafka/bin/kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic proximity-alerts \
  --from-beginning
```

## Structure du projet

```
.
├── api-gateway/          # API Gateway Spring Cloud
├── msuser/              # Microservice Utilisateurs
├── ms-locations/        # Microservice Localisations
├── config/              # Fichiers de configuration
├── docker-compose.yml   # Configuration Docker
└── README.md
```