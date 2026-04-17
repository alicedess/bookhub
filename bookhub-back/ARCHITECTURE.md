# 📚 Architecture BookHub - Backend

## 🏗️ Vue d'ensemble globale

```
┌─────────────────────────────────────────────────────────────────────┐
│                      CLIENT (Frontend)                               │
│                   (React / Angular / Vue)                            │
└────────────────────┬────────────────────────────────────────────────┘
                     │ HTTP REST API
                     ▼
┌─────────────────────────────────────────────────────────────────────┐
│                  PRESENTATION LAYER                                  │
│                   (Controllers)                                      │
├─────────────────────────────────────────────────────────────────────┤
│  • AuthentificationController   • LivreController                   │
│  • UtilisateurController         • EmpruntController                │
│  • AuteurController              • EvaluationController             │
│  • CategorieController           • StatsController                  │
│  • GlobalExceptionHandler        • DefaultController                │
└────────────────────┬────────────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────────────┐
│              SECURITY LAYER (JWT Authentication)                     │
├─────────────────────────────────────────────────────────────────────┤
│  • SecurityConfig.java          • JwtAuthenticationFilter           │
│  • JwtUtil.java                 • AuthEntryPoint                    │
└────────────────────┬────────────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────────────┐
│              BUSINESS LOGIC LAYER (Services)                         │
├─────────────────────────────────────────────────────────────────────┤
│  • AuthService            • EmpruntService      • StatsService      │
│  • UtilisateurService     • EvaluationService                       │
│  • LivreService           • ExemplaireService   • AuteurService     │
│  • CategorieService                                                 │
└────────────────────┬────────────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────────────┐
│          DATA ACCESS LAYER (Repositories) - Spring Data JPA         │
├─────────────────────────────────────────────────────────────────────┤
│  • UtilisateurRepository   • EmpruntRepository    • RoleRepository  │
│  • LivreRepository         • EvaluationRepository                   │
│  • AuteurRepository        • ExemplaireRepository                   │
│  • CategorieRepository                                              │
└────────────────────┬────────────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────────────┐
│           PERSISTENCE LAYER (Database)                              │
├─────────────────────────────────────────────────────────────────────┤
│  • MySQL / SQL Server                                               │
│  • Flyway (Database Migrations)                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 🔄 Flux des données - Exemple: Emprunt d'un livre

```
┌─────────────────┐
│  Frontend       │  Envoie une requête POST /api/loans?idLivre=1
└────────┬────────┘
         │
         ▼
┌─────────────────────────────────────────┐
│ EmpruntController.emprunterLivre()      │  1️⃣ Reçoit la requête
│ @PostMapping("/api/loans")              │
└────────┬────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────┐
│ EmpruntService.emprunterLivreDto()      │  2️⃣ Logique métier
│ - Vérifier disponibilité                │
│ - Vérifier limite emprunt               │
│ - Créer l'emprunt                       │
└────────┬────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────┐
│ EmpruntRepository.save(emprunt)         │  3️⃣ Persister en BD
│ LivreRepository.update(disponibilite)   │
└────────┬────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────┐
│ Base de Données (MySQL/SQL Server)      │  4️⃣ Stockage persistant
└────────┬────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────┐
│ EmpruntDTO (réponse)                    │  5️⃣ Retour au frontend
│ {                                       │
│   "message": "Emprunt créé...",        │
│   "emprunt": { ... }                    │
│ }                                       │
└────────┬────────────────────────────────┘
         │
         ▼
┌─────────────────┐
│  Frontend       │  Affiche le résultat
└─────────────────┘
```

---

## 🏢 Architecture en couches détaillée

### 1️⃣ **Couche Présentation (Controllers)**
- **Responsabilité**: Traiter les requêtes HTTP, valider les entrées
- **Technologies**: Spring Web MVC, Swagger/OpenAPI
- **Patterns**: RESTful, Exception Handling

### 2️⃣ **Couche Sécurité**
- **Responsabilité**: Authentification JWT, Autorisation RBAC
- **Technologies**: Spring Security, JWT (jjwt 0.13.0)
- **Composants**: 
  - `JwtAuthenticationFilter`: Valide les tokens
  - `JwtUtil`: Génère/valide les tokens JWT
  - `AuthEntryPoint`: Gère les erreurs d'auth

### 3️⃣ **Couche Métier (Services)**
- **Responsabilité**: Logique applicative, règles métier
- **Technologies**: Spring Service, Transactionnelle
- **Patterns**: Service Locator, Business Logic

### 4️⃣ **Couche Accès aux données (Repositories)**
- **Responsabilité**: Requêtes à la base de données
- **Technologies**: Spring Data JPA, Hibernate
- **Patterns**: Repository Pattern, ORM

### 5️⃣ **Couche Modèle (Entities & DTOs)**
- **Entities**: Représentation en BD (JPA)
- **DTOs**: Transfert de données (entre couches)
- **Mapper**: Conversion automatique via ModelMapper

---

## 🔐 Sécurité & Authentification

```
┌──────────────────────┐
│  Client envoie JWT   │
│  dans Authorization  │
│  Bearer header       │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────────────────────────┐
│ JwtAuthenticationFilter                  │
│ - Extrait le token                       │
│ - Valide la signature (JwtUtil)          │
│ - Charge l'utilisateur (UtilisateurRepo)│
└──────────┬───────────────────────────────┘
           │
           ▼
        ✅ Token valide
           │
           ▼
┌──────────────────────────────────────────┐
│ SecurityContext établi                   │
│ @PreAuthorize("hasRole('USER')")         │
│ @PreAuthorize("hasRole('LIBRARIAN')")    │
│ @PreAuthorize("hasRole('ADMIN')")        │
└──────────────────────────────────────────┘
```

---

## 🛠️ Technologies & Dépendances

| Couche | Technologie | Version |
|--------|-------------|---------|
| **Framework** | Spring Boot | 4.0.5 |
| **Langage** | Java | 21 |
| **ORM** | JPA Hibernate | Intégré |
| **BD** | MySQL / SQL Server | Latest |
| **Migrations** | Flyway | 6.4.3 |
| **Authentification** | JWT (jjwt) | 0.13.0 |
| **Mapping** | ModelMapper | 3.2.0 |
| **Documentation API** | Springdoc OpenAPI | 3.0.2 |
| **Validation** | Validation | Spring Boot |
| **Lombok** | Code Generation | Latest |

---

## 📋 Résumé des composants

| Composant | Nombre | Role |
|-----------|--------|------|
| Controllers | 10 | Entrée HTTP |
| Services | 9 | Logique métier |
| Repositories | 8 | Accès BD |
| Entities | 8 | Modèle BD |
| DTOs | 15+ | Transfert données |
| Enums | N/A | Valeurs constantes |

---

## ✅ Validations & Exceptions

- **GlobalExceptionHandler**: Capture toutes les exceptions
- **Bean Validation**: @Valid, @NotNull, @Email, etc.
- **Exceptions métier**: Levées par les services
- **Gestion des erreurs**: Réponses HTTP standardisées

---

## 🎯 Points clés de l'architecture

✅ **Séparation des responsabilités** en couches  
✅ **Sécurité JWT** avec roles (USER, LIBRARIAN, ADMIN)  
✅ **DTOs** pour éviter les expositions de données  
✅ **Repository Pattern** pour flexibilité BD  
✅ **Service Layer** pour logique réutilisable  
✅ **Exception Handling** centralisé  
✅ **Flyway** pour versioning BD  
✅ **API Documentation** avec Swagger  
✅ **ModelMapper** pour conversion automatique  
✅ **Spring Boot 4.0.5** avec Java 21  

