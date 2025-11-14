# INSA Student Collaboration Platform

Application d'entraide entre étudiants basée sur une architecture microservices Spring Boot.

## Description

Cette plateforme permet aux étudiants de s'entraider en proposant ou en demandant de l'aide. Le système recommande automatiquement les étudiants les plus adaptés pour répondre à chaque demande d'aide, en fonction de leurs compétences, disponibilités et réputation.

## Architecture Microservices

L'application est composée de 5 microservices :

### 1. Eureka Server (Port 8761)
Serveur de découverte de services permettant l'enregistrement et la localisation des microservices.

### 2. API Gateway (Port 8080)
Point d'entrée unique pour toutes les requêtes vers les microservices.
- Route `/api/students/**` → Student Service
- Route `/api/help-requests/**` → Help Request Service
- Route `/api/recommendations/**` → Recommendation Service

### 3. Student Service (Port 8081)
Gestion des étudiants avec les fonctionnalités suivantes :
- Inscription d'un nouvel étudiant
- Connexion d'un étudiant
- Modification des informations personnelles
- Consultation du profil
- Gestion des compétences et disponibilités
- Système d'avis et notation

**Entités :**
- `Student` : id, nom, email, password, établissement, filière, compétences, disponibilités
- `Avis` : id, student, auteur, note, commentaire, dateCreation

### 4. Help Request Service (Port 8082)
Gestion des demandes d'aide :
- Création de demande d'aide ou offre d'aide
- Modification d'une demande
- Changement de statut (EN_ATTENTE, EN_COURS, TERMINEE, ANNULEE)
- Filtrage par mot-clé, type ou statut

**Entité :**
- `HelpRequest` : id, studentId, titre, description, motsCles, dateCreation, statut, type

### 5. Recommendation Service (Port 8083)
Recommandation automatique des étudiants les plus adaptés pour répondre à une demande :
- Analyse des compétences correspondant aux mots-clés
- Prise en compte de la disponibilité
- Pondération par la réputation (moyenne des avis)
- Retourne les 10 meilleurs candidats avec score et raison

## Technologies Utilisées

- **Spring Boot 3.1.5** : Framework principal
- **Spring Cloud** : Architecture microservices
- **Hibernate/JPA** : ORM pour la persistance des données
- **H2 Database** : Base de données en mémoire pour chaque service
- **Netflix Eureka** : Service discovery
- **Spring Cloud Gateway** : API Gateway
- **OpenFeign** : Communication inter-services
- **Lombok** : Réduction du code boilerplate
- **Maven** : Gestionnaire de dépendances

## Prérequis

- Java 17 ou supérieur
- Maven 3.6 ou supérieur

## Installation et Démarrage

### 1. Compiler le projet
```bash
mvn clean install
```

### 2. Démarrer les services dans l'ordre suivant :

#### a. Eureka Server
```bash
cd eureka-server
mvn spring-boot:run
```
Accès : http://localhost:8761

#### b. Student Service
```bash
cd student-service
mvn spring-boot:run
```

#### c. Help Request Service
```bash
cd help-request-service
mvn spring-boot:run
```

#### d. Recommendation Service
```bash
cd recommendation-service
mvn spring-boot:run
```

#### e. API Gateway
```bash
cd api-gateway
mvn spring-boot:run
```

## Utilisation de l'API

Toutes les requêtes passent par l'API Gateway sur le port 8080.

### Endpoints Student Service

#### Inscription
```bash
POST http://localhost:8080/api/students/register
Content-Type: application/json

{
  "nom": "Jean Dupont",
  "email": "jean.dupont@insa.fr",
  "password": "password123",
  "etablissement": "INSA Lyon",
  "filiere": "Informatique",
  "competences": ["Java", "Spring Boot", "Python"],
  "disponibilites": ["Lundi 14h-16h", "Mercredi 10h-12h"]
}
```

#### Connexion
```bash
POST http://localhost:8080/api/students/login
Content-Type: application/json

{
  "email": "jean.dupont@insa.fr",
  "password": "password123"
}
```

#### Obtenir un étudiant
```bash
GET http://localhost:8080/api/students/{id}
```

#### Modifier un étudiant
```bash
PUT http://localhost:8080/api/students/{id}
Content-Type: application/json

{
  "nom": "Jean Dupont",
  "email": "jean.dupont@insa.fr",
  "etablissement": "INSA Lyon",
  "filiere": "Informatique",
  "competences": ["Java", "Spring Boot", "Python", "Docker"],
  "disponibilites": ["Lundi 14h-16h", "Mercredi 10h-12h"]
}
```

#### Lister tous les étudiants
```bash
GET http://localhost:8080/api/students
```

#### Rechercher par compétence
```bash
GET http://localhost:8080/api/students/competence/Java
```

### Endpoints Help Request Service

#### Créer une demande d'aide
```bash
POST http://localhost:8080/api/help-requests
Content-Type: application/json

{
  "studentId": 1,
  "titre": "Besoin d'aide en Spring Boot",
  "description": "Je cherche quelqu'un pour m'aider à comprendre les annotations Spring",
  "motsCles": ["Spring Boot", "Java", "Annotations"],
  "type": "DEMANDE_AIDE"
}
```

#### Créer une offre d'aide
```bash
POST http://localhost:8080/api/help-requests
Content-Type: application/json

{
  "studentId": 2,
  "titre": "J'offre mon aide en Python",
  "description": "Je peux aider pour des projets Python niveau débutant/intermédiaire",
  "motsCles": ["Python", "Data Science", "Machine Learning"],
  "type": "OFFRE_AIDE"
}
```

#### Obtenir une demande
```bash
GET http://localhost:8080/api/help-requests/{id}
```

#### Mettre à jour le statut
```bash
PATCH http://localhost:8080/api/help-requests/{id}/status?statut=EN_COURS
```

#### Lister toutes les demandes
```bash
GET http://localhost:8080/api/help-requests
```

#### Filtrer par type
```bash
GET http://localhost:8080/api/help-requests/type/DEMANDE_AIDE
```

#### Filtrer par mot-clé
```bash
GET http://localhost:8080/api/help-requests/keyword/Java
```

### Endpoints Recommendation Service

#### Obtenir les recommandations pour une demande
```bash
GET http://localhost:8080/api/recommendations/help-request/{helpRequestId}
```

Retourne une liste de recommandations avec :
- Les informations de l'étudiant recommandé
- Un score de compatibilité (0-100)
- La raison de la recommandation

## Algorithme de Recommandation

Le système calcule un score pour chaque étudiant basé sur :

1. **Correspondance des compétences (40 points max)** : Nombre de compétences correspondant aux mots-clés de la demande
2. **Disponibilité (20 points)** : L'étudiant a indiqué des disponibilités
3. **Réputation (40 points max)** : Moyenne des avis × 8

Score total maximum : 100 points

Les 10 étudiants avec les meilleurs scores sont retournés.

## Bases de Données

Chaque service utilise une base H2 en mémoire :
- Student Service : `jdbc:h2:mem:studentdb`
- Help Request Service : `jdbc:h2:mem:helprequestdb`

Consoles H2 accessibles sur :
- http://localhost:8081/h2-console (Student Service)
- http://localhost:8082/h2-console (Help Request Service)

## Tests

Exécuter les tests :
```bash
mvn test
```

## Structure du Projet

```
student-collab/
├── eureka-server/           # Service de découverte
├── api-gateway/             # API Gateway
├── student-service/         # Gestion des étudiants
├── help-request-service/    # Gestion des demandes d'aide
├── recommendation-service/  # Recommandations
└── pom.xml                  # POM parent
```

## Fonctionnalités Principales

✅ Architecture microservices avec Spring Cloud  
✅ Inscription et authentification des étudiants  
✅ Gestion du profil (compétences, disponibilités)  
✅ Création de demandes et offres d'aide  
✅ Système d'avis et de notation  
✅ Recommandation automatique des étudiants  
✅ Utilisation de Hibernate/JPA comme ORM  
✅ API Gateway pour le routage  
✅ Service Discovery avec Eureka  

## Auteur

INSA 5A - Student Collaboration Platform