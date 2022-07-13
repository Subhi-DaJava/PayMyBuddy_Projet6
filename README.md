# PayMyBuddy_Projet6

# Technoligies 
#### Java 11
#### Maven 3.8.4
#### Spring-Boot: 2.6.8
#### Spring-Security: 5.6.5
#### Thyemleaf
#### MySQL 8.0.29
#### Bootstrap 5
#### h2 (pour les tests)
#### Oauth2-client (se connecter avec Google ou d'autre)

# Démarrage d'Application
#### Étape 1: Cloner le code sur la branche dev
#### Étape 2: Configurer application.properties (url, username et password --> la connexion avec la bdd)
#### Étape 3: Créé une BDD avec MySQL(ou avec un autre), puis exécuter les requêtes dans schema.sql (sur la bdd de donnée créée à l'étape 3) pour créer les tables
#### Étape 4: Exécuter les requêtes dasn data.sql pour insérer les données
#### Étape 5: Démarrer l'app (PayMyBuddyApplication.java), puis se rendre sur le navigateur puis saisir http://localhost:8089/ (par défaut)
#### Étape 6: Entrer un utilisateur et son mot de passe (Note*)

# Domaine Model

## UML 
![SUBI_Yalimaimaiti_1_UML_072022](https://user-images.githubusercontent.com/90509456/178743080-bd6563fb-59cd-4c42-8a03-8ff83c105867.jpg)

## MPD
![SUBI_Yalimaimaiti_2_mpd_072022](https://user-images.githubusercontent.com/90509456/178743114-4e4df112-cb8b-431d-a6b4-834ac514bec7.jpg)


Note*:
schema.sql et data.sql se trouvent dasn le dossier resources/data
Les mots de passe enregistrés dans la base de données, sont cryptés (encodé avec BCrypt) tous pareil: un, deux, trois et quatre, en chiffre arabique sans espace entre eux.
