# Use official OpenJDK 17 image as the base image
FROM eclipse-temurin:17-alpine

# Create a new group with specific uid and non-root user called "admin"
#Be sure that group id are not present on host. if already exist change by arbitrary other uid
RUN addgroup -g 1028 devopsc \
    && adduser -D -G devopsc admin

#Create a new mount point at /tmp on native hostbecause a volume is mor efficient and speed that filesystem
VOLUME /tmp

# Copiamos el jar a la imagen
ARG JAR_FILE
ARG ARG DB_PASSWORD
COPY ${JAR_FILE} /tmp/app.jar

ENV DB_PASSWORD=$DB_PASSWORD

# Change ownership of the /app directory to the "admin" user
RUN chown -R admin:devopsc /tmp

# Switch to non-root "admin" user for added security
USER admin

# Ejecutamos el jar al iniciar el contenedor
ENTRYPOINT ["java","-jar","/tmp/app.jar"]
