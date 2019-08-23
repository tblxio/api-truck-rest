Lego Truck Rest API
=============
A REST API to complement the [Smart Lego Truck Project](https://github.com/TechhubLisbon/sinfo-rpi-truck)
## System Requirements

1. Java JVM (>= 11)
2. Maven (Apache Maven)
3. DOCKER

## Build application

1. Compile JAVA code

        mvn clean package

2. Build dockers

        docker-compose build

## Setup the database
In order for the application to work, a Timescale DB, which is an extension of PostGresSQL,
needs to be set up before the first run, in order to do that:
1. Start database

        docker-compose up -d mypostgres
2. Copy the [timescaleSetup.sql](timescaleSetup.sql) to your database

        docker cp /FULL/PATH/TO/timescaleSetup.sql YOURCONTAINER:/timescaleSetup.sql
3. Connect to your database in the PostGresSQL container

        docker exec -it myContainer psql -U demouser mydb
4. Run the setup script

        \i timescaleSetup.sql


## Run application

1. Start the application

        docker-compose up
        
## Documentation
        
To access the Swagger documentation, using a browser connect to:

        http://localhost:8080/lego/swagger-ui.html
        
        http://localhost:8080/lego/v2/api-docs
        
    
## Configuration

The following environment variables have to be provided when executing the docker image:
    
    #MQTT BROKER
    MQTT_HOST=tcp://<host>
    MQTT_PORT=<PORT>
    MQTT_USER=<USERNAME>
    MQTT_PASS=<PASSWORD>
    
    # TIMESCALE DB
    DB_INSTANCE=<HOST IP>
    DB_PORT=<HOST PORT>
    DB_USER=<USERNAME>
    DB_PASS=<PASSWORD>
    DB_NAME=<name of the database inside host>
    
    
    # Security
    SECURITY_USERNAME=<basic auth username>
    SECURITY_PASSWORD=<basic auth password>
    
## Adding new components
In the case that the Lego Truck project be expanded in the future to 
contain more sensors, their data can be acquired easily by adding it
the message received method in [MqttCallback](src/main/java/io/techhublisbon/lego/truck/rest/events/acquisition/control/MqttDataCallback.java) 
class and well as implementing a handler for its message, alongside 
its extension of the [Event](src/main/java/io/techhublisbon/lego/truck/rest/events/acquisition/entity/Event.java) 
class and its repository handler.
