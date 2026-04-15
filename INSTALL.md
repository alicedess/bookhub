# Guide d'installation — BookHub

Ce guide permet à un nouveau développeur d'installer et de lancer BookHub en local depuis zéro.

---

## Prérequis

| Outil | Version minimale | Lien |
|-------|-----------------|------|
| Java (JDK) | 21 | https://adoptium.net |
| Node.js | 20 LTS | https://nodejs.org |
| npm | 11 (inclus avec Node) | — |
| SQL Server | 2019 ou 2022 | https://www.microsoft.com/fr-fr/sql-server |
| SSMS (optionnel) | toute version | https://aka.ms/ssms |

> **Vérification rapide :**
> ```bash
> java -version      # doit afficher 21.x
> node -v            # doit afficher v20.x ou supérieur
> npm -v             # doit afficher 11.x ou supérieur
> ```

---

## 1. Cloner le dépôt

```bash
git clone <url-du-dépôt>
cd bookhub
```

---

## 2. Configuration de SQL Server

### 2.1 Activer la connexion TCP/IP

SQL Server n'accepte pas les connexions réseau par défaut. Il faut activer TCP/IP :

1. Ouvrir **SQL Server Configuration Manager** en mode administrateur :
   `C:\Windows\SysWOW64\SQLServerManager15.msc`
2. Aller dans **SQL Server Network Configuration → Protocols for MSSQLSERVER**
3. Faire un clic droit sur **TCP/IP** → **Enable**
4. Ouvrir `services.exe` et **redémarrer le service SQL Server (MSSQLSERVER)**

> Sans cette étape, Spring Boot ne pourra pas se connecter à la base.

### 2.2 Créer la base de données

Dans SSMS (ou tout autre client SQL), se connecter à l'instance locale et exécuter :

```sql
CREATE DATABASE bookhub;
```

### 2.3 Initialiser le schéma et les données

Exécuter dans cet ordre les deux fichiers situés dans `bookhub-back/src/main/resources/migrations/` :

```
1. schema.sql  — crée toutes les tables
2. data.sql    — insère des données de test (livres, auteurs, utilisateurs)
```

Dans SSMS : **Fichier → Ouvrir** puis exécuter chaque fichier après avoir sélectionné la base `bookhub`.

---

## 3. Configuration du backend

Ouvrir `bookhub-back/src/main/resources/application.properties` et adapter les identifiants SQL Server :

```properties
spring.datasource.url=jdbc:sqlserver://localhost;database=bookhub;integratedSecurity=false;encrypt=false;trustServerCertificate=false
spring.datasource.username=sa
spring.datasource.password=Pa$$w0rd   ← remplacer par votre mot de passe
```

> Le port SQL Server par défaut est `1433`. Si votre instance utilise un port différent, l'ajouter à l'URL :
> `jdbc:sqlserver://localhost:1433;database=bookhub;...`

---

## 4. Lancer le backend

Ouvrir le projet depuis le dossier bookhub-back et lancer l'application.

Le serveur démarre sur **http://localhost:8080**.

La documentation Swagger est disponible à : **http://localhost:8080/api/docs**

> La première exécution télécharge Gradle et toutes les dépendances — prévoir quelques minutes.

---

## 5. Lancer le frontend

Dans un **nouveau terminal** :

```bash
cd bookhub-front
npm install
npm start
```

L'application est disponible sur **http://localhost:4200**.

> `npm install` n'est nécessaire qu'une seule fois (ou après modification de `package.json`).

---

## 6. Comptes de test

Les comptes suivants sont créés par `data.sql`. Le mot de passe est identique pour tous :

| Rôle | Email | Mot de passe |
|------|-------|-------------|
| Administrateur | admin@bookhub.fr | mon_super_password |
| Bibliothécaire | librarian@bookhub.fr | mon_super_password |

> Les utilisateurs membres sont également insérés par `data.sql` (voir le fichier pour les détails).

---

## 7. Résumé des ports

| Service | URL |
|---------|-----|
| Frontend Angular | http://localhost:4200 |
| API Spring Boot | http://localhost:8080/api |
| Swagger UI | http://localhost:8080/swagger-ui/index.html |

---

## Problèmes courants

**`Connection refused` au démarrage du backend**
→ Vérifier que TCP/IP est activé dans SQL Server Configuration Manager et que le service SQL Server a bien été redémarré.
