# Etapa de build
FROM eclipse-temurin:22-jdk AS buildstage

# Instalar Maven
RUN apt-get update && apt-get install -y maven unzip

WORKDIR /app

# Copiar TODO el proyecto
COPY . .

# Variables para Oracle Wallet
ENV TNS_ADMIN=/app/src/main/resources/wallet

# Compilar
RUN mvn clean package -DskipTests

# Etapa de producción
FROM eclipse-temurin:22-jdk

WORKDIR /app

# Copiar el .jar generado
COPY --from=buildstage /app/target/productos-service-0.0.1-SNAPSHOT.jar .

# Copiar wallet
COPY src/main/resources/wallet ./wallet

# Variables de entorno
ENV TNS_ADMIN=/app/wallet

# Exponer puerto
EXPOSE 8080

# Comando para arrancar
ENTRYPOINT ["java", "-jar", "productos-service-0.0.1-SNAPSHOT.jar"]
