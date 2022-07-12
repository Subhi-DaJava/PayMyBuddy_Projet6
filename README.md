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

## Démarrage avec le fichier Jar
#### Linge de commande ou avec un terminal exécute : `mvn package` dans le répertoir racine du projet, puis exécute `java -jar target/Pay_My_Buddy-0.0.1-SNAPSHOT.jar`, les démarches suivantes sont même au-dessus

Pay_My_Buddy-0.0.1-SNAPSHOT.jar
# Domaine Model
## UML
![SUBI_Yalimaimaiti_1_UML_072022](https://user-images.githubusercontent.com/90509456/178743080-bd6563fb-59cd-4c42-8a03-8ff83c105867.jpg)

## MPD
![SUBI_Yalimaimaiti_2_mpd_072022](https://user-images.githubusercontent.com/90509456/178831180-ca78fba0-fcb9-43c8-b641-8a45cda5b7eb.jpg)

## Interfaces Web
### Page Home sans authentificaton
![homeSansAuthentification](https://user-images.githubusercontent.com/90509456/178951700-d0ca52de-f9bd-404f-9758-8a954952e21e.jpg)
### Page Login personnalisé
![loginPersonnalise](https://user-images.githubusercontent.com/90509456/178951968-78f7305a-5b66-417d-85b9-e05c69620bc3.jpg)
### Page Home avec authentification
![homeAvecAuthentification](https://user-images.githubusercontent.com/90509456/178951840-13bbf767-0209-46f7-95f0-8f4f05b6d516.jpg)
### Page principale Transfer
![pagePrincipalTransfer](https://user-images.githubusercontent.com/90509456/178952030-17f61d21-daa0-40f6-9466-4f014f3daa0c.jpg)
### Transaction
![transaction](https://user-images.githubusercontent.com/90509456/178952102-849231cc-a6f0-4cb1-8fee-25acd1ea4c9a.jpg)
### Page Add Connections
![addConnection](https://user-images.githubusercontent.com/90509456/178952175-3854884a-b9c5-4ba6-8d3b-8eda9ea9dee4.jpg)
### Versement entre PayMyBuddy et Banque
![virementEntreAppEtBank](https://user-images.githubusercontent.com/90509456/178952257-21ed9711-05fc-4aa1-8688-96e283d6e308.jpg)
### Page Contact
![contact](https://user-images.githubusercontent.com/90509456/178952391-a5f640eb-5ff0-492d-a424-d190d300e61f.jpg)
### Log Off
![logOff](https://user-images.githubusercontent.com/90509456/178952456-692d3f2b-8385-427f-a1db-6d33c35ed431.jpg)

## Note* :
`schema.sql` et `data.sql` se trouvent dans le dossier `resources/data`.
Les mots de passe enregistrés dans la base de données, sont cryptés (encodé avec BCrypt) tous pareils : `un, deux, trois, quatre et cinq`, en chiffre arabique sans espace entre eux.
