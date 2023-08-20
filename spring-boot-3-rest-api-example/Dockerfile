FROM openjdk:11
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/spring-boot-3-rest-api-tutotrial-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} tutorialapi.jar
ENTRYPOINT ["java","-jar","/tutorialapi.jar"]

### create docker image
    # docker build -f Dockerfile -t springtutorial .
    
### run image as continer
    # docker run -p 9900:8080 springtutorial
    
### delete image [first delete container]
    # docker rmi <IMAGE ID>

###FROM creates a layer from an existing Java base image that exists locally or
   #in any container registry that runs our container.
 
###VOLUME creates a specific space to persist some data in your container. 
   #The tmp folder will store information.
 
###EXPOSE informs Docker that the container listens to the specified network ports at runtime.

###ARG defines a variable that can be passed to the application at runtime. 
   #For example, we pass the location of the final jar file within the target folder and save 
   #it in a JAR_FILE variable. You can also pass more arguments like credentials, keys, and 
   #environment variables with their respective values.

###ADD copies new files, directories or remote file URLs from the source and adds them to the 
   #filesystem of the image at the provided path. In our case we add the Spring Boot application to 
   #the Docker image from the source path (the JAR_FILE variable) to a destination named app.jar.

###ENTRYPOINT specifies the command that Docker will use to run our app. In this case it will pass 
   #the common command to run a jar — java -jar <name of the jar> — so in this case it is java -jar app.jar
   #to our ENTRYPOINT option (remember that we renamed the spring-boot-3-rest-api-tutotrial-0.0.1-SNAPSHOT.jar file to app.jar).