# KidTrack - Application de Gestion de Crèche

## 📋 Description
Application de gestion d'une crèche ou d'une école maternelle permettant aux parents de suivre les activités quotidiennes de leurs enfants, aux éducateurs de gérer les activités et repas, et aux administrateurs de gérer l'ensemble du système.

## 🏗️ Architecture

### Backend (Spring Boot)
- **Framework**: Spring Boot 3.5.6
- **Base de données**: PostgreSQL
- **Sécurité**: Spring Security + JWT
- **API Documentation**: SpringDoc OpenAPI (Swagger)

### Frontend (Flutter)
- **Framework**: Flutter 3.9+
- **Gestion d'état**: Provider
- **HTTP Client**: http package
- **Stockage local**: shared_preferences

## 📦 Structure du Projet

```
kidTrack-backend/
├── models/          # Entités JPA (User, Parent, Educator, Children, etc.)
├── repositories/    # Interfaces JPA Repository
├── services/        # Logique métier
├── controllers/     # API REST Controllers
├── dto/            # Data Transfer Objects
├── security/       # Configuration JWT
└── config/         # Configuration Spring

kidtrack/
├── models/         # Modèles Dart
├── services/       # Services API
├── screens/        # Écrans de l'application
│   ├── auth/      # Authentification
│   ├── parent/    # Écrans parent
│   ├── educator/  # Écrans éducateur
│   └── admin/     # Écrans administrateur
└── config/        # Configuration
```

## 🚀 Installation et Démarrage

### Prérequis
- Java 17+
- Maven 3.8+
- PostgreSQL 14+
- Flutter 3.9+
- Dart 3.0+

### Backend

1. **Configurer la base de données**
```sql
CREATE DATABASE kidtrack_db;
```

2. **Modifier application.properties**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/kidtrack_db
spring.datasource.username=votre_username
spring.datasource.password=votre_password
```

3. **Installer les dépendances et démarrer**
```bash
cd kidTrack-backend
mvn clean install
mvn spring-boot:run
```

L'API sera accessible sur `http://localhost:8008`
Swagger UI: `http://localhost:8008/swagger-ui.html`

### Frontend

1. **Installer les dépendances**
```bash
cd kidtrack
flutter pub get
```

2. **Configurer l'URL du backend**
Modifier dans `lib/services/auth_service.dart` et autres services:
```dart
static const String baseUrl = 'http://votre-ip:8008/api';
```

3. **Lancer l'application**
```bash
flutter run
```

## 🔐 Authentification

### Utilisateurs par défaut (à créer via API)

**Administrateur**
```json
{
  "email": "admin@kidtrack.com",
  "password": "admin123",
  "firstName": "Admin",
  "lastName": "System"
}
```

**Éducateur**
```json
{
  "email": "educateur@kidtrack.com",
  "password": "educateur123",
  "firstName": "Marie",
  "lastName": "Dupont",
  "username": "marie.dupont"
}
```

**Parent**
```json
{
  "email": "parent@kidtrack.com",
  "password": "parent123",
  "firstName": "Jean",
  "lastName": "Martin",
  "phone": "0123456789"
}
```

## 📱 Fonctionnalités Principales

### Parent
- ✅ Consultation des activités journalières des enfants
- ✅ Photos et commentaires des activités
- ✅ Inscription aux événements
- ✅ Gestion des préférences/restrictions alimentaires
- ✅ Chat avec les éducateurs
- ✅ Réception de notifications en temps réel

### Éducateur
- ✅ Mise à jour des activités journalières
- ✅ Ajout de photos et commentaires
- ✅ Gestion des repas du groupe
- ✅ Création et gestion d'événements
- ✅ Communication avec les parents

### Administrateur
- ✅ Gestion de tous les utilisateurs
- ✅ Gestion des groupes
- ✅ Configuration du système
- ✅ Vue d'ensemble de toutes les activités

## 🔗 API Endpoints Principaux

### Authentification
- `POST /api/auth/login` - Connexion
- `POST /api/auth/register/parent` - Inscription parent
- `POST /api/auth/register/educator` - Inscription éducateur
- `POST /api/auth/register/admin` - Inscription admin

### Enfants
- `GET /api/children` - Liste tous les enfants
- `GET /api/children/parent/{parentId}` - Enfants d'un parent
- `POST /api/children` - Créer un enfant
- `GET /api/children/{id}/allergies` - Allergies d'un enfant

### Activités
- `GET /api/activities` - Liste toutes les activités
- `GET /api/activities/child/{childId}` - Activités d'un enfant
- `POST /api/activities` - Créer une activité
- `GET /api/activities/{id}/comments` - Commentaires d'une activité

### Événements
- `GET /api/events/upcoming` - Événements à venir
- `POST /api/events` - Créer un événement
- `POST /api/events/{eventId}/register/{childId}` - Inscrire un enfant

### Notifications
- `GET /api/notifications/user/{userId}` - Notifications d'un utilisateur
- `GET /api/notifications/user/{userId}/unread` - Notifications non lues
- `PUT /api/notifications/{id}/read` - Marquer comme lu

 
