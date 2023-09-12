# Imagen base
FROM openjdk:20-jdk

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo pom.xml al directorio de trabajo
COPY lab5_server/pom.xml .

# Descargar las dependencias del proyecto
RUN mvn dependency:go-offline

# Copiar el resto de los archivos del proyecto al directorio de trabajo
COPY lab5_server/src ./src

# Compilar el proyecto
RUN mvn package

EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "target/lab5_server-1.0.jar"]