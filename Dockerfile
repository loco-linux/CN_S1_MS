# Etapa de build
FROM eclipse-temurin:22-jdk AS buildstage

# Instalar Maven
RUN apt-get update && apt-get install -y maven unzip

WORKDIR /app

# Copiar POM y bajar dependencias (más seguro: skip go-offline)
COPY pom.xml .
COPY src ./src

# Copiar wallet (solo si existe en el proyecto)
COPY src/main/resources/wallet /app/wallet

# Variables para Oracle
ENV TNS_ADMIN=/app/wallet

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

# Exponer puerto de la app
EXPOSE 8080

# Comando para arrancar
ENTRYPOINT ["java", "-jar", "productos-service-0.0.1-SNAPSHOT.jar"]
