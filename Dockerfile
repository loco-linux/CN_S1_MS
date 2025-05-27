FROM eclipse-temurin:22-jdk AS buildstage

# 1. Instala Maven y unzip
RUN apt-get update && apt-get install -y maven unzip

WORKDIR /app

# 2. Copia dependencias
COPY pom.xml . 
COPY src ./src

# 3. Copia wallet completo (debe incluir todos los archivos .ora, .sso, .pem, etc.)
COPY src/main/resources/wallet /app/wallet

# 4. Variable de entorno para Oracle
ENV TNS_ADMIN=/app/wallet

# 5. Compila la app
RUN mvn clean package -DskipTests

# ------------- RUNTIME -------------
FROM eclipse-temurin:22-jdk

WORKDIR /app

# 6. Copia el JAR desde la etapa de compilación
COPY --from=buildstage /app/target/productos-service-0.0.1-SNAPSHOT.jar .

# 7. Copia la wallet al contenedor final
COPY src/main/resources/wallet ./wallet

# 8. Define variable de entorno para el cliente Oracle
ENV TNS_ADMIN=/app/wallet

# 9. Exponer el puerto
EXPOSE 8080

# 10. Ejecutar la app
ENTRYPOINT ["java", "-jar", "productos-service-0.0.1-SNAPSHOT.jar"]
