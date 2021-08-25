# Overview

## Build
Within top directory
 
	mvn clean install

##Modules
The project has 3 modules

  - ***dictionary***: This is the JAR that contains the basic Source, Destination and SimpleEnum for mapping
  - ***standalone***: This is a standard SE app that demonstrates the AtlasMap library mapping a null Enum from Source to Destination
  - ***camel***: This is a Camel application that demonstrates the Camel environment failing
       - REST Endpoints can be invoked as follows:
       - ***curl http://localhost:8080/camel/sample/full***  
         
         This will demonstrate a route that maps the enum correctly when non null

       - ***curl http://localhost:8080/camel/sample/null***
         
         This sets the enum to null and generates the map error

			io.atlasmap.api.AtlasException: Errors: [Failed to write field value into target document: Unable to create value for segment: sample parentObject: com.accenture.atlasmap.dict.Destination: Document='com.accenture.atlasmap.dict.Destination(ID:com.accenture.atlasmap.dict.Destination)', path='/sample'], 

       - ***curl http://localhost:8080/camel/sample/manual***
           
          This demonstrates manually using a process to create the AtlasMap session and map with a null value.
           It works .. so something inside the Camel component must be causing this issue.
    

## Run Camel           
To run camel from the command line

   ``cd camel/target``
	``java -Dadm-location=../../atlasmap/atlasmap-mapping.adm -jar camel-atlasmap-1.0-SNAPSHOT.jar``

## AtlasMap

The mapping is in the directory atlasmap
The adm file is copied into the projects target classes target directory to be packaged via a POM plugin

***Mapping***
![atlasmap/Mapping.png](atlasmap/Mapping.png?raw=true "Title")


