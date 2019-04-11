FROM java:8


#
# When we build the image, we copy the executable jar in the image file system. 
#
COPY ./MockMock.jar /opt/app/mockmock.jar

#
EXPOSE 25
EXPOSE 8282

#
# This is the command that is executed when the Docker container starts
#
CMD ["java", "-jar", "/opt/app/mockmock.jar"]