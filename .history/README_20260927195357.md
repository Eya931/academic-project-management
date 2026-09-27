# Academic Project Management

Plateforme de gestion de projets académiques développée avec Spring Boot. Elle permet de gérer les utilisateurs, les rôles, les groupes, les projets, les tâches, les livrables et les documents liés à un projet pédagogique.

## Description

Ce projet a pour objectif de centraliser la gestion des projets universitaires et académiques dans une application backend robuste et sécurisée. Il fournit une base solide pour la gestion collaborative des activités pédagogiques et des ressources associées.

## Fonctionnalités

- Inscription et authentification des utilisateurs
- Gestion des rôles et permissions
- Création et suivi de projets
- Gestion des groupes et des équipes
- Suivi des tâches
- Gestion des livrables (PDF, vidéo, liens)
- Gestion des documents associés
- Validation des comptes par email
- Sécurité via Spring Security et JWT
- API REST documentée avec Swagger/OpenAPI

## Stack technique

- Java 22
- Spring Boot 3.4.4
- Spring Web
- Spring Data JPA
- Spring Security
- PostgreSQL
- Maven
- Lombok
- Thymeleaf
- JWT
- OpenAPI / Swagger

## Structure du projet

```text
academic_project-main/
├── project_managment/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/platform/project_managment/
│   │   │   │       ├── auth/
│   │   │   │       ├── config/
│   │   │   │       ├── project/
│   │   │   │       ├── group/
│   │   │   │       ├── user/
│   │   │   │       ├── role/
│   │   │   │       ├── task/
│   │   │   │       ├── deliverable/
│   │   │   │       ├── documentation/
│   │   │   │       ├── security/
│   │   │   │       ├── email/
│   │   │   │       └── ...
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       ├── application-dev.yml
│   │   │       └── templates/
│   │   └── test/
│   ├── pom.xml
│   ├── mvnw
│   ├── mvnw.cmd
│   └── docker.compose.yml
├── README.md
└── .gitignore
```

## Prérequis

Avant de lancer le projet, il faut avoir :

- Java 22
- Maven ou Maven Wrapper
- PostgreSQL installé et démarré
- Un IDE (IntelliJ IDEA, VS Code, etc.)

## Configuration

Le fichier principal de configuration est :

- project_managment/src/main/resources/application.yml

Le projet est configuré pour utiliser le profil `dev` et expose l’API sous le contexte :

- /api/v1

## Lancement du projet

Depuis le dossier `project_managment` :

```bash
./mvnw clean install
./mvnw spring-boot:run
```

Sous Windows :

```powershell
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```

## Documentation API

Une fois l’application démarrée, la documentation Swagger est disponible ici :

```text
http://localhost:8080/api/v1/swagger-ui/index.html
```

## Base de données

Le projet utilise PostgreSQL. Il faut donc créer et configurer la base de données locale avant de lancer l’application, puis vérifier les paramètres de connexion dans les fichiers de configuration Spring.

## Docker

Un fichier Docker Compose est présent dans le projet pour faciliter le démarrage des services nécessaires au développement.

## Développements possibles

Ce projet peut être étendu avec :

- un front-end moderne,
- des notifications en temps réel,
- des exports PDF/Excel,
- une gestion avancée des permissions,
- des tests unitaires et d’intégration plus complets.

## Licence

Ce projet est destiné à un usage académique et personnel. À ajuster selon votre choix de publication sur GitHub.

## Auteur

Projet académique de gestion collaborative de projets.
