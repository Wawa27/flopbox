# Project Flopbox

For this project, I've opted for Quarkus for multiple reasons, the main ones being that I wanted to try to deploy
microservices (hence the use of JWT) and to test the performance of Quarkus using GraalVM.

# Setup

To build the server, you need to use maven

    mvnw clean package

# Use

To start the server, run the .jar file in the target/quarkus-app/ folder.

The "flopbox-1.0-SNAPSHOT.jar" only contains the classes and resources of the projects, it is NOT the runnable jar.

Example :

    java -jar PATH_TO_TARGET\quarkus-app\quarkus-run.jar

You need to create an account by adding it in the users.json file, or you can use the already created user :

Username: wawa Password: 123456a

## Production use

In order to use this in production, you'll need to create your own private key instead of using the one created for
Quarkus demos.