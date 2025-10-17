# Architecture Microservices avec API Gateway

## Vue d'ensemble

Cette architecture comprend 3 microservices avec Spring Boot, 2 bases de données MySQL et une API Gateway pour le routage.

## Architecture

```
                     Client (Browser/Postman)
                                |
                            Port 8888
                                |
                          API GATEWAY
                         (Spring Cloud)
                        /             \
                       /               \
                /msuser/**        /mslocation/**
                     |                  |
                  ms-user          ms-locations
                 (port 8080)       (port 8081)
                     |                  |
                  user-db           location-db
                 (MySQL 8.0)        (MySQL 8.0)
```

## Réseaux Docker

- **gateway_net** : Réseau partagé pour communication entre microservices
- **user_net** : Réseau privé isolé pour MS-User ↔ user-db
- **location_net** : Réseau privé isolé pour MS-Locations ↔ location-db

## Services

### 1. API Gateway (Port 8888)
- **Framework** : Spring Cloud Gateway
- **Version** : Spring Boot 3.4.5 + Spring Cloud 2024.0.0
- **Rôle** : Point d'entrée unique, routage des requêtes

**Routes configurées :**
- `/msuser/**` → http://ms-user:8080
- `/mslocation/**` → http://ms-locations:8081

### 2. MS-User (Port 8080)
- **Framework** : Spring Boot 3.5.6
- **Base de données** : user-db (MySQL 8.0)
- **Entité** : User (id, nom, prenom, email, password)

**Endpoints via Gateway :**
```
POST   http://localhost:8888/msuser/users/add
       Body: nom, prenom, email, password
```

**Endpoints directs :**
```
POST   http://localhost:8080/users/add
```

### 3. MS-Locations (Port 8081)
- **Framework** : Spring Boot 3.5.6
- **Base de données** : location-db (MySQL 8.0)
- **Entité** : Location (id, latitude, longitude, timestamp, userId)

**Endpoints via Gateway :**
```
POST   http://localhost:8888/mslocation/locations/add
       Body: latitude, longitude, userId (optionnel)

GET    http://localhost:8888/mslocation/locations/all
       Retourne toutes les localisations

GET    http://localhost:8888/mslocation/locations/user/{userId}
       Retourne les localisations d'un utilisateur

GET    http://localhost:8888/mslocation/locations/{id}
       Retourne une localisation par ID
```

**Endpoints directs :**
```
POST   http://localhost:8081/locations/add
GET    http://localhost:8081/locations/all
GET    http://localhost:8081/locations/user/{userId}
GET    http://localhost:8081/locations/{id}
```
