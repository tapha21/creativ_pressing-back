# Deploiement Render

Le backend est en Spring Boot + Docker + MongoDB Atlas.

## Option recommandee

1. Cree un nouveau service **Web Service** pour le backend.
2. Choisis l'environnement **Docker**.
3. Mets comme root directory :

```txt
pressingBack/creativ-pressing-api
```

4. Ajoute ces variables d'environnement :

```env
MONGODB_URI=mongodb+srv://Tapha:<ton_mot_de_passe>@cluster0.yarpz78.mongodb.net/pressing?retryWrites=true&w=majority&appName=Cluster0
SPRING_DATA_MONGODB_URI=mongodb+srv://Tapha:<ton_mot_de_passe>@cluster0.yarpz78.mongodb.net/pressing?retryWrites=true&w=majority&appName=Cluster0
MONGODB_DATABASE=pressing
CORS_ALLOWED_ORIGINS=https://creativ-pressing.vercel.app,http://localhost:5173,http://localhost:3000
```

Important : remplace `<ton_mot_de_passe>` par le mot de passe MongoDB Atlas, sans les chevrons.
Si Render continue de tenter `localhost:27017`, c'est que la variable d'environnement n'est pas ajoutee sur le service web Render ou que le service n'a pas ete redeploye apres l'ajout.

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
