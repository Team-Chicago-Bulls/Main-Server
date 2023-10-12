# Imagen base
FROM alpine:latest

#Instalar Java y Maven
RUN apk add openjdk17-jdk maven

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo pom.xml al directorio de trabajo
COPY server/pom.xml . 


#Copiar el código fuente al directorio de trabajo
COPY server/src ./src

# Compilar el proyecto
RUN mvn clean package

# Exponer el puerto 8080
EXPOSE 8080

# Exponer el puerto 27017
EXPOSE 27017

# Compilar el proyecto
CMD ["mvn", "exec:java", "-Dexec.mainClass='com.example.demo.Main'"]
