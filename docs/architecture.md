# CHECKTRANS - Arquitectura del Sistema

## Visión General

CHECKTRANS es una aplicación web corporativa monolítica para la gestión de listas de chequeo de transportes. Sigue una arquitectura en capas tradicional con Spring MVC, separando claramente las responsabilidades en dominio, aplicación, infraestructura y presentación.

## Principios Arquitectónicos

1. **Arquitectura Limpia** - Dependencias dirigidas hacia el dominio; las capas externas dependen de las internas.
2. **Monolito Modular** - Una sola aplicación desplegable con módulos internos bien delimitados.
3. **API Primero Web** - La interfaz principal es web (Thymeleaf), no REST. Las vistas se renderizan en servidor.
4. **DTO como frontera** - Las entidades JPA nunca se exponen directamente a la capa de presentación.
5. **Configuración externalizada** - Todo valor variable reside en YAML, no en código.
6. **Testing por capas** - Tests unitarios, de integración y de contrato para cada capa.
7. **Zero Framework JS** - Sin React, Angular, Vue ni librerías JS adicionales. Bootstrap 5 + Vanilla JS.

## Diagrama de Capas

```
┌─────────────────────────────────────────────────────────┐
│                    PRESENTACIÓN                          │
│   Thymeleaf Templates  │  Bootstrap 5  │  Vanilla JS    │
│   CSS  │  Fragmentos  │  Layout Decorator                │
└───────────────────────┬─────────────────────────────────┘
                        │ (HTTP)
┌───────────────────────▼─────────────────────────────────┐
│                    CONTROLLER                            │
│   @Controller  │  @ControllerAdvice  │  Validación      │
│   DTO Request/Response  │  RedirectAttributes            │
└───────────────────────┬─────────────────────────────────┘
                        │
┌───────────────────────▼─────────────────────────────────┐
│                   SERVICE (Aplicación)                   │
│   @Service  │  @Transactional  │  Lógica de negocio     │
│   Mappers (Entity ↔ DTO)  │  Excepciones de dominio    │
└───────────────────────┬─────────────────────────────────┘
                        │
┌───────────────────────▼─────────────────────────────────┐
│                 REPOSITORY (Infraestructura)              │
│   Spring Data JPA  │  PostgreSQL / H2  │  Queries       │
└───────────────────────┬─────────────────────────────────┘
                        │
┌───────────────────────▼─────────────────────────────────┐
│                    DOMINIO                                │
│   @Entity  │  Enums  │  Value Objects  │  Relaciones    │
│   @PrePersist / @PreUpdate  │  Reglas de dominio        │
└─────────────────────────────────────────────────────────┘
```

## Stack Tecnológico Detallado

| Componente           | Tecnología                    | Versión          |
|----------------------|-------------------------------|------------------|
| Lenguaje             | Java                          | 21 (LTS)         |
| Framework Base       | Spring Boot                   | 3.4.4            |
| Web MVC              | Spring MVC                    | 6.2.5            |
| ORM                  | Hibernate / JPA               | 6.6.11           |
| Templates            | Thymeleaf                     | 3.1.3            |
| Layout               | Thymeleaf Layout Dialect      | 3.x              |
| Frontend             | Bootstrap 5 + Font Awesome    | 5.3.3 / 6.7.2    |
| Frontend JS          | Vanilla JavaScript (ES6)      | -                |
| BD Producción        | PostgreSQL                    | 16 Alpine        |
| BD Desarrollo        | H2 (memoria)                  | 2.3.232          |
| Build                | Maven                         | 3.9.x            |
| Contenedores         | Docker / Docker Compose       | 3.9              |
| Testing              | JUnit 5 + Mockito + MockMvc   | 5.11 / 5.14      |
| Validación           | Jakarta Validation            | 3.x              |
| Monitorización       | Spring Boot Actuator          | 3.4.4            |

## Estructura de Paquetes

```
es.araujo.checktrans
├── ChecktransApplication.java          # Punto de entrada
├── config/                             # Configuración Spring
├── controller/                         # Controladores MVC
├── domain/                             # Entidades JPA y lógica de dominio
│   └── enums/                          # Enumeraciones del dominio
├── dto/                                # Data Transfer Objects
├── exception/                          # Excepciones y manejadores globales
├── repository/                         # Repositorios Spring Data JPA
└── service/                            # Servicios de negocio
```

## Estrategia de Perfiles

| Perfil    | Propósito              | BD         | DDL         | SQL Show | Cache Thymeleaf |
|-----------|------------------------|------------|-------------|----------|-----------------|
| default   | Producción             | PostgreSQL | validate    | false    | true            |
| dev       | Desarrollo local       | H2         | create-drop | true     | false           |
| test      | Tests automatizados    | H2         | create-drop | false    | n/a             |

## Estrategia de Versionado

**Formato:** SemVer estricto `MAJOR.MINOR.PATCH`

- **MAJOR**: Cambios que rompen compatibilidad (ej: cambio de BD, cambio arquitectónico)
- **MINOR**: Nueva funcionalidad sin rotura (ej: nueva fase completa)
- **PATCH**: Correcciones, refactors, documentación

**Versión actual:** `1.0.0-SNAPSHOT` (Fase 1 completada)

## Estrategia de Despliegue

### Entornos

| Entorno  | URL                        | BD          | Perfil     |
|----------|----------------------------|-------------|------------|
| Local    | http://localhost:8080       | H2          | dev        |
| Docker   | http://localhost:8080       | PostgreSQL  | default    |
| Railway  | https://checktrans.railway.app | PostgreSQL | default  |

### Pipeline (a implementar en fases futuras)

```
Commit → Tests → Package → Build Docker → Deploy Railway
```

### Docker

- `Dockerfile` multi-etapa: build con `maven:3.9-amazoncorretto-21`, runtime con `amazoncorretto:21`
- `docker-compose.yml` con servicio `db` (PostgreSQL 16 Alpine) y `app`

## Estrategia de Testing

### Pirámide de Tests

```
       ╱╲
      ╱  ╲        E2E (futuro)
     ╱    ╲
    ╱──────╲
   ╱        ╲     Integración (Controladores, Repositorios)
  ╱──────────╲
 ╱            ╲   Unitarios (Dominio, Servicios)
╱──────────────╲
```

### Cobertura por Capa

| Capa        | Tipo Test              | Framework               | Cobertura Mínima |
|-------------|------------------------|-------------------------|------------------|
| Dominio     | Unitario               | JUnit 5                 | 100% lógica      |
| Servicios   | Unitario + Mockito     | JUnit 5 + Mockito       | 100% casos       |
| Repositorios| Integración (@DataJpaTest) | JUnit 5 + H2        | 100% queries     |
| Controladores| Integración (@WebMvcTest) | MockMvc + Mockito   | 100% endpoints   |
| Contexto   | Smoke (@SpringBootTest)| JUnit 5                  | 1 test           |

### Convenciones de Testing

- Los tests NO usan Lombok (código explícito)
- Nombres de test: `should<Comportamiento>_when<Condicion>`
- Aislados: cada test crea sus propios datos
- Repositorios usan H2 embebido (perfil `test`)
- Controladores usan MockMvc con servicios mockeados

## Convenciones de Código

### Nomenclatura
- **Clases Java:** PascalCase (ChecklistService)
- **Métodos:** camelCase (findAllByOrder)
- **Constantes:** UPPER_SNAKE_CASE (DRAFT)
- **Paquetes:** minúsculas (es.araujo.checktrans.service)
- **Tablas BD:** snake_case plural (checklists, checklist_items)
- **Columnas BD:** snake_case (transport_plate, inspector_name)
- **Tests:** <Clase>Test (ChecklistServiceTest)
- **Templates:** kebab-case (checklist-form.html)

### Estilo
- Sin Lombok en producción (código explícito)
- Sin comentarios en código (el código se auto-documenta)
- Inyección por constructor (no @Autowired en campos)
- Logging con SLF4J (LoggerFactory)
- Validación con Jakarta Validation (@NotBlank, @Size, @Valid)
- Manejo de errores con @ControllerAdvice
- Sin anotaciones @Transactional en tests

## Flujo de Datos Típico

```
1. Usuario → Navegador → GET /checklists
2. ChecklistController.list()
3. ChecklistService.findAll(pageable)
4. ChecklistRepository.findAll(pageable) → Page<Checklist>
5. ChecklistService → mapea a Page<ChecklistDTO>
6. ChecklistController → añade al modelo
7. Thymeleaf → renderiza list.html
8. Navegador → HTML + Bootstrap
```

## Gestión de Errores

| Condición                     | HTTP Status | Vista     |
|-------------------------------|-------------|-----------|
| Recurso no encontrado         | 404         | error/404 |
| Código duplicado              | 409         | error/409 |
| Error interno                 | 500         | error/500 |
| Validación fallida            | 400         | mismo form |

## Dependencias Externas (WebJars)

- Bootstrap 5.3.3 (CSS + JS bundle)
- Font Awesome 6.7.2 (iconos vectoriales)
- Ambos servidos estáticamente vía webjars-locator-lite
