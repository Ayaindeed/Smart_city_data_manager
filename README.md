# 🌆 SmartCity Data Manager

Application web moderne de gestion et d'analyse des capteurs urbains permettant de collecter, visualiser et analyser des données en temps réel.

## Fonctionnalités

### Gestion des Capteurs
- Ajout, suppression et gestion de l'état des capteurs
- Interface intuitive avec actions rapides (activation/désactivation)
- Filtrage et recherche dynamique
- Export des données au format CSV
- Auto-refresh des statistiques en temps réel

### Analyse des Données
- Visualisation des statistiques par zone géographique
- Analyse temporelle des mesures
- Tableaux de bord interactifs
- Statistiques en temps réel (moyennes, min/max)
- Navigation fluide avec défilement automatique

### Intégration API
- Collecte automatique de données météorologiques
- Support des mesures de pollution et de bruit
- Génération de données de test réalistes
- API RESTful pour l'intégration externe

## Prérequis

- Java 19 (Corretto)
- Maven 3.x
- MySQL 8.0
- WildFly 36

## Installation

1. Créer la base de données :
```sql
CREATE DATABASE smartcitydata;
CREATE USER 'smart_user'@'localhost' IDENTIFIED BY 'smart_pwd';
GRANT ALL PRIVILEGES ON smartcitydata.* TO 'smart_user'@'localhost';
FLUSH PRIVILEGES;
```

2. Compiler le projet :
```bash
mvn clean package
```

3. Configuration des API :
```bash
# Copier le fichier de configuration exemple
cp src/main/resources/config.properties.example src/main/resources/config.properties

# Éditer le fichier avec votre clé API
# Remplacer 'your_api_key_here' par votre vraie clé API OpenWeather
```

4. Déployer sur WildFly :
- Copier le WAR généré dans `target/smartcity.war`
- Déployer via la console d'administration WildFly

⚠️ **Important : Sécurité**
- Ne jamais commiter le fichier `config.properties` contenant des clés API
- Utiliser des variables d'environnement en production
- La clé API peut être fournie via la variable d'environnement `WEATHER_API_KEY`

## Structure du Projet

```
src/
├── main/
│   ├── java/
│   │   └── smartcity/
│   │       ├── dao/         # Couche d'accès aux données
│   │       ├── model/       # Entités JPA
│   │       ├── servlet/     # Contrôleurs
│   │       └── util/        # Utilitaires
│   ├── resources/
│   │   └── META-INF/       # Configuration JPA
│   └── webapp/
│       ├── pages/          # Pages JSP
│       └── WEB-INF/        # Configuration web
```

## Technologies

- Jakarta EE 10
- Hibernate 6.4
- MySQL Connector/J 8.2
- JAXB Runtime 4.0

## Architecture

- Architecture en couches (MVC)
- Persistance avec JPA/Hibernate
- Interface utilisateur JSP
- Servlets comme contrôleurs
- Base de données MySQL

## Notes de Développement

- Mode de transaction RESOURCE_LOCAL pour la gestion directe des connexions
- Configuration optimisée d'Hibernate (pool de connexions, cache)
- Support des statistiques agrégées par zone et période
