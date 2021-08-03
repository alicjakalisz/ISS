# International Space Station API
## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [API Contract](#api-contract)
* [Setup](#setup)

## General info
This project is a Spring API that represents classic REST API with unit tests. It retrieves data about ISS at point in time.
Details of ISS at specific date and time are first checked if already stored in db and if so returned from there. In other case the information is taken from two widely avaiable APIS:
People in Space **http://open-notify.org/Open-Notify-API/People-In-Space/** and Where The Iss At **https://wheretheiss.at/w/developer** and saved in db before returning to the client.

A client is able to get all recipes available in database, find a recipe by its id, update and delete a recipe.

## Public Endpoint Example
This Rest API has been deployed in AWS in an EC2 instance and using a MySQL RDS database.

```http://ec2-18-130-4-145.eu-west-2.compute.amazonaws.com:8084/ISS```
## Technologies/Main Libraries
Project is created with:

* Maven
* Maven Profiles for local and production deployments
* Java 1.11
* Spring Boot
* Json version: 20210307
* Junit
* Gson version: 2.8.2
* MySQL db
* H2 db for testing
* Mockito
* AWS: EC2, RDS and Security Groups

## API contract

Get ISS without date time parameter:
###  HTTP GET /ISS
#### EXAMPLE OF HTTP 200 response:
```
{
    "latitide": "-45.914333570029",
    "longitude": "96.81198346862",
    "velocity": "27544.408280204",
    "timeStamp": "1628018915",
    "timeZone": "Etc/GMT-6",
    "mapUrl": "https://maps.google.com/maps?q=-45.914333570029,96.81198346862&z=4",
    "crewList": [
        "Mark Vande Hei",
        "Oleg Novitskiy",
        "Pyotr Dubrov",
        "Thomas Pesquet",
        "Megan McArthur",
        "Shane Kimbrough",
        "Akihiko Hoshide"
    ]
}
```


Get ISS at the choosen date and time: :
### HTTP GET /ISS?dateTime=2020-05-21 9
#### EXAMPLE OF HTTP 200 response:
```
{
    "latitide": "51.605280086802",
    "longitude": "-154.79995890103",
    "velocity": "27585.82264073",
    "timeStamp": "1590048000",
    "timeZone": "Etc/GMT+10",
    "mapUrl": "https://maps.google.com/maps?q=51.605280086802,-154.79995890103&z=4",
    "crewList": []
}
```



## Setup
To run this project locally:
* H2 is created in memory by Spring (not required to do anything). In case you want a different db, add the driver in the pom.xml.
* Modify application.properties.dev with your Database credentials.
* Run it from IntellIJ or from command line:
  `mvn clean install -P dev`

## Run Application Locally
* Build the jar:

  `mvn clean install`

* Run the application:

  `java -jar target/ISS-0.0.1-SNAPSHOT.jar `

* Navigate to this URL: 
 
  `http://localhost:8084/ISS`
## Deployment to Production
* Connect to remote db from your computer and create there a database "recipes".
* Modify application.properties.prod with the correct database connection of AWS RDS .
* Generate the jar for production:

  `mvn clean install -P prod`
* Connect to Remote machine:

  `ssh -i ${location_pem}  ec2-user@ec2-18-130-4-145.eu-west-2.compute.amazonaws.com`

* Upload jar to remote machine:

  `scp -i ${location_pem} ${WORKSPACE_PATH}/ISS/target/ISS-0.0.1-SNAPSHOT.jar ec2-user@ec2-18-130-4-145.eu-west-2.compute.amazonaws.com:/home/ec2-user
  ISS-0.0.1-SNAPSHOT.jar`

* Run spring boot app in a remote machine:

  A.The process will be automatically killed after ending connection with EC2

  `java -jar ISS-0.0.1-SNAPSHOT.jar > output.txt &`
    * to kill it manually:

      `ps`

      `ps | grep java`

      `kill -9 process ref`

  B. Keep the process as a daemon (even after ending connection):
    * create a screen named recipes and run the program

      `screen -dmS ISS java -jar ISS-0.0.1-SNAPSHOT.jar > iss.log &
      `
    * to kill the process, list all the running processes and kill the screen recipes:

      `screen -ls
      `  
      `screen -X -S ISS quit`

* Ready to use GET endpoints
