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

# Crear usuario no-root por seguridad
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

WORKDIR /app

# Copiar solo el JAR compilado
COPY --from=buildstage /app/target/*.jar app.jar

# Copiar wallet si es necesario (esto ya es en runtime)
COPY src/main/resources/wallet ./wallet

# Variables de entorno para el runtime
ENV TNS_ADMIN=/app/wallet



# Crear directorio EFS con permisos adecuados
RUN mkdir -p /app/efs && \
    chown appuser:appgroup /app/efs && \
    chmod 755 /app/efs

# Cambiar ownership del archivo
RUN chown appuser:appgroup app.jar

# Cambiar a usuario no-root
USER appuser

# Crear un volume point para EFS
VOLUME ["/app/efs"]

# Exponer el puerto 8080
EXPOSE 8080


# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]

