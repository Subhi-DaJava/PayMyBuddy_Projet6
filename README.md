# PayMyBuddy_Projet6

# Technologies 
#### Java 11
#### Maven 3.8.4
#### Spring-Boot: 2.6.7
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
#### Étape 6 : Entrer un utilisateur(`email`) et son mot de passe `(Note*)`

## Démarrge avec le fichier Jar 
#### Linge de commande ou avec un terminal exécute : `mvn package` dans le répertoir racine du projet, puis exécute `java -jar target/Pay_My_Buddy-0.0.1-SNAPSHOT.jar`, les démarches suivantes sont même au-dessus

Pay_My_Buddy-0.0.1-SNAPSHOT.jar
# Domaine Model
## UML 
![SUBI_Yalimaimaiti_1_UML_072022](https://user-images.githubusercontent.com/90509456/178743080-bd6563fb-59cd-4c42-8a03-8ff83c105867.jpg)

## MPD
![SUBI_Yalimaimaiti_2_mpd_072022](https://user-images.githubusercontent.com/90509456/178831180-ca78fba0-fcb9-43c8-b641-8a45cda5b7eb.jpg)


Note* :
`schema.sql` et `data.sql` se trouvent dans le dossier `resources/data`.
Les mots de passe enregistrés dans la base de données, sont cryptés (encodé avec BCrypt) tous pareils : `un, deux, trois, quatre et cinq`, en chiffre arabique sans espace entre eux.
