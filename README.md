# PayMyBuddy_Projet6

# Technologies 
#### Java 11
#### Maven 3.8.4
#### Spring-Boot: 2.6.8
#### Spring-Security: 5.6.5
#### Thymeleaf
#### MySQL 8.0.29
#### Bootstrap 5
#### H2 (pour les tests)
#### Oauth2-client (se connecter avec le compte Google ou d'autre, if faut configurer `client-id, client-secret et scope` dans application.properties)

# Démarrage d'Application
#### Étape 1 : Cloner le code sur la branche `dev`
#### Étape 2 : Configurer `application.properties` (url, username et password -→ la connexion avec la BDD)
#### Étape 3 : Créé une BDD avec MySQL(ou avec une autre), puis exécuter les requêtes dans `schema.sql` (sur la bdd de donnée créée à l'étape 3) pour créer les tables
#### Étape 4 : Exécuter les requêtes dans `data.sql` pour insérer les données
#### Étape 5 : Démarrer l'app (PayMyBuddyApplication.java), puis se rendre sur le navigateur puis saisir `http://localhost:8089/` (par défaut: server.port=8089)
#### Étape 6 : Entrer un utilisateur(`email`) et son mot de passe (Note*)

# Domaine Model
## UML 
![UML](https://user-images.githubusercontent.com/90509456/178481078-28fa3d39-c077-4b02-862a-e66cfa8b77da.jpg)
## MPD
![MPD_Model_physique_de_donnees](https://user-images.githubusercontent.com/90509456/178481127-70c57ef5-7182-457c-8e59-54675a946644.jpg)

Note* :
`schema.sql` et `data.sql` se trouvent dans le dossier `resources/data`.
Les mots de passe enregistrés dans la base de données, sont cryptés (encodé avec BCrypt) tous pareils : `un, deux, trois, quatre et cinq`, en chiffre arabique sans espace entre eux.
