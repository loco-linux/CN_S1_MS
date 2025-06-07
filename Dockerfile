# Etapa de construcción
FROM eclipse-temurin:22-jdk AS buildstage

# 1. Instala Maven y unzip
RUN apt-get update && apt-get install -y maven unzip

WORKDIR /app

# 2. Copiar sólo pom.xml para cache de dependencias
COPY pom.xml .

# 3. Descargar dependencias
RUN mvn dependency:go-offline

# 4. Copiar el resto del código
COPY src ./src

# 5. Copiar wallet completo
COPY src/main/resources/wallet /app/wallet

# 6. Variables de entorno para Oracle
ENV TNS_ADMIN=/app/wallet

# 7. Compilar el proyecto
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:22-jdk

WORKDIR /app

# 8. Copia solo el jar ya construido
COPY --from=buildstage /app/target/productos-service-0.0.1-SNAPSHOT.jar .

# 9. Copia wallet
COPY src/main/resources/wallet ./wallet

# 10. Variables de entorno necesarias
ENV TNS_ADMIN=/app/wallet

# 11. Puerto de la aplicación
EXPOSE 8080

# 12. Comando para ejecutar
ENTRYPOINT ["java", "-jar", "productos-service-0.0.1-SNAPSHOT.jar"]
