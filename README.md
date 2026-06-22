# CHECKTRANS

Sistema de Gesti&oacute;n de Listas de Chequeo de Transportes.

## Stack

| Tecnolog&iacute;a | Versi&oacute;n |
|-------------------|----------------|
| Java              | 21 (LTS)       |
| Spring Boot       | 3.4.4          |
| Thymeleaf         | 3.1.3          |
| Bootstrap         | 5.3.3          |
| JPA / Hibernate   | 3.4.4 / 6.6.11 |
| PostgreSQL        | 16 Alpine      |
| Maven             | 3.9.x          |

## Requisitos

- Java 21 JDK
- Docker Desktop (opcional, para PostgreSQL)
- Maven 3.9+ (opcional, usar wrapper `mvnw`)

## Ejecuci&oacute;n

### Desarrollo (H2 embebido)

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Abrir http://localhost:8080

### Producci&oacute;n (PostgreSQL + Docker)

```bash
docker compose up --build
```

### Tests

```bash
./mvnw test
```

## Perfiles

| Perfil  | BD          | DDL         | Uso              |
|---------|-------------|-------------|------------------|
| default | PostgreSQL  | validate    | Producci&oacute;n |
| dev     | H2 memoria  | create-drop | Desarrollo local |
| test    | H2 memoria  | create-drop | Tests            |

## Estructura

```
es.araujo.checktrans
+-- config/       Configuracion y propiedades
+-- controller/   Controladores Spring MVC
+-- service/      Logica de negocio
+-- repository/   Acceso a datos (Spring Data JPA)
+-- model/        Entidades JPA
+-- dto/          Data Transfer Objects
+-- mapper/       Conversion entre entidades y DTOs
+-- audit/        Componentes de auditoria
+-- exception/    Excepciones y manejadores globales
+-- util/         Clases utilitarias
```

## Licencia

Uso interno corporativo.
