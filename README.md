# Dataobject microservice
Ce micro-service met à disposition des routes http permettant de stocker des fichiers sur le cloud.

Les étapes ci-dessous sont à faire depuis le dossier racine du projet.

## Collaborateurs
Développeurs : Luca Coduri & Chloé Fontaine

Professeur : Nicolas Glassey

Assistant : Adrien Allemand

## Prérequis
Vous devez avoir au minimum Java 17 et Maven installés sur votre machine ou alors docker, car ce projet utilise Spring.
Il est aussi nécessaire de créer un fichier .env à la racine. Vous retrouverez un exemple de son contenu dans le fichier .env.example

Nous utilisons AWS dans la version actuelle, il faut donc fournir vos identifiants AWS (access_token, secret_token).

## Récupérer les dépendances
```
mvn dependency:resolve
```

## Compilation
La commande ci-dessous permet de compiler le projet sans lancer les tests unitaires.
```bash
 ./mvnw package -Dmaven.test.skip
```
## Run
Une fois le projet compilé il est possible de lancer le programme avec la commande suivante :
```bash
java -jar /app/target/microservice-dataobject-*.jar #Remplacez l'étoile par le numéro de version.
```

## Tests
Pour lancer les tests, il faut faire la commande suivante :
```bash
./mvnw test
```

## Structure du projet
```
./src/
├── main
│ ├── java.com.groupe2
│ │   └── microservicedataobject
│ │       ├── controller --> contient les controllers
│ │       ├── dataobject --> contient la classe qui permet de gérer la gestion des fichiers dans le cloud
│ │       │ └── aws --> L'implémentation du code pour aws.
│ │       └── domain
│ │         ├── requestbody --> Classe représentant les requêtes http
│ │         └── responsebody --> Classe représentant les réponses http
│ └── resources --> Les ressources Spring
└── test.java.com.groupe2
    └── microservicedataobject
        └── dataobject --> Test sur la gestion des fichiers.
          └── aws --> Test plus spécifique à aws
```

## Sur le VPS AWS
Le projet a déjà été pull sur le VPS AWS.
Rendez-vous dans `~/dataobject` et lancez les commandes détaillées au-dessus.