# Creativ Pressing API

Backend Spring Boot Java 17 pour le SaaS Creativ Pressing.

## Stack

- Java 17
- Spring Boot 3.3.5
- Spring Web
- Spring Data MongoDB
- MongoDB Atlas / MongoDB local
- Docker Compose
- Lombok
- Bean Validation

## Lancement

```bash
docker compose up -d
./mvnw spring-boot:run
```

Si tu n'as pas Maven Wrapper :

```bash
mvn spring-boot:run
```

API disponible sur :

```txt
http://localhost:8080/api
```

## Deploiement Render

Le backend se deploie avec Docker. Voir le guide :

```txt
RENDER_DEPLOY.md
```

Sur Render, configure MongoDB Atlas :

```env
MONGODB_URI=mongodb+srv://Tapha:<ton_mot_de_passe>@cluster0.yarpz78.mongodb.net/pressing?retryWrites=true&w=majority&appName=Cluster0
MONGODB_DATABASE=pressing
CORS_ALLOWED_ORIGINS=https://creativ-pressing.vercel.app,http://localhost:5173,http://localhost:3000
```

## Données seedées

Au démarrage, l'application crée automatiquement :

- 1 boutique
- clients
- commandes
- dépenses
- employés
- photos

Shop ID visible dans les logs au démarrage.

## Endpoints principaux

```txt
GET    /api/shops
POST   /api/shops
GET    /api/shops/{id}

GET    /api/clients?shopId={shopId}&search={optional}
POST   /api/clients
PUT    /api/clients/{id}
DELETE /api/clients/{id}

GET    /api/orders?shopId={shopId}&status={optional}&payment={optional}
POST   /api/orders
PUT    /api/orders/{id}
PATCH  /api/orders/{id}/status?status=Prêt
DELETE /api/orders/{id}

GET    /api/expenses?shopId={shopId}&category={optional}
POST   /api/expenses
PUT    /api/expenses/{id}
DELETE /api/expenses/{id}

GET    /api/employees?shopId={shopId}&active={optional}
POST   /api/employees
PUT    /api/employees/{id}
DELETE /api/employees/{id}

GET    /api/photos?orderId={orderId}
POST   /api/photos
DELETE /api/photos/{id}

GET    /api/dashboard?shopId={shopId}
GET    /api/reports?shopId={shopId}
```
