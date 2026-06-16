# Deploiement Render

Le backend reste en Spring Boot + Docker + PostgreSQL.

## Option recommandee

1. Sur Render, cree une base **PostgreSQL**.
2. Cree un nouveau service **Web Service** pour le backend.
3. Choisis l'environnement **Docker**.
4. Mets comme root directory :

```txt
pressingBack/creativ-pressing-api
```

5. Ajoute ces variables d'environnement :

```env
DATABASE_URL=<Internal Database URL de ta base PostgreSQL Render>
CORS_ALLOWED_ORIGINS=https://creativ-pressing.vercel.app,http://localhost:5173,http://localhost:3000
```

Important : prends l'URL interne Render de PostgreSQL, meme si elle commence par `postgres://`.
Le backend la convertit automatiquement en URL JDBC.

## Front Vercel

Quand le backend Render est deploye, ajoute dans Vercel :

```env
VITE_API_URL=https://ton-back-render.onrender.com
```

Ne mets pas `/api` a la fin.

## Comptes seed

Au premier demarrage sur une base vide :

```txt
tapha@creativpressing.sn / 1234
lamine@creativpressing.sn / 1234
```
