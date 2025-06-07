# Stage 1: Build
FROM eclipse-temurin:17-jdk AS buildstage

# Instala Maven
RUN apt-get update && apt-get install -y maven unzip

WORKDIR /app

# Copia el pom.xml y src
COPY pom.xml .
COPY src ./src

# Variables para Oracle Wallet durante el build (si es necesario)
ENV TNS_ADMIN=/app/src/main/resources/wallet

# Compilar el proyecto
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copiar solo el JAR compilado
COPY --from=buildstage /app/target/*.jar app.jar

# Copiar wallet si es necesario (esto ya es en runtime)
COPY src/main/resources/wallet ./wallet

# Variables de entorno para el runtime
ENV TNS_ADMIN=/app/wallet

# Exponer el puerto 8080
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]

