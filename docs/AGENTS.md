# CHECKTRANS - Guía para Agentes IA

## Resumen del Proyecto

Aplicación web corporativa para gestión de listas de chequeo de transportes.

**Stack:** Java 21, Spring Boot 3.4, Spring MVC, Thymeleaf, Bootstrap 5, JPA/Hibernate, PostgreSQL, Maven

## Estructura del Proyecto

```
checktrans/
├── pom.xml
├── mvnw / mvnw.cmd
├── Dockerfile
├── docker-compose.yml
├── .gitignore
├── docs/
│   ├── AGENTS.md
│   ├── architecture.md
│   ├── domain-model.md
│   └── roadmap.md
├── sql/
│   └── init.sql
├── README.md
├── src/
│   ├── main/
│   │   ├── java/es/araujo/checktrans/
│   │   │   ├── ChecktransApplication.java
│   │   │   ├── config/
│   │   │   │   └── ChecktransProperties.java
│   │   │   ├── controller/
│   │   │   │   ├── HomeController.java
│   │   │   │   ├── ChecklistController.java
│   │   │   │   ├── ChecklistTemplateController.java
│   │   │   │   └── DemoController.java
│   │   │   ├── domain/
│   │   │   │   ├── AuditableEntity.java
│   │   │   │   ├── Checklist.java
│   │   │   │   ├── ChecklistItem.java
│   │   │   │   ├── enums/
│   │   │   │   │   ├── ChecklistStatus.java
│   │   │   │   │   ├── TransportType.java
│   │   │   │   │   ├── ItemType.java
│   │   │   │   │   └── ChecklistExecutionStatus.java
│   │   │   │   └── template/
│   │   │   │       ├── ChecklistTemplate.java
│   │   │   │       ├── ChecklistTemplateVersion.java
│   │   │   │       ├── ChecklistPhase.java
│   │   │   │       └── ChecklistItem.java
│   │   │   ├── dto/
│   │   │   │   ├── ChecklistDTO.java
│   │   │   │   ├── ChecklistCreateDTO.java
│   │   │   │   ├── ChecklistItemDTO.java
│   │   │   │   ├── ChecklistTemplateDTO.java
│   │   │   │   ├── ChecklistTemplateCreateDTO.java
│   │   │   │   └── ChecklistTemplateVersionDTO.java
│   │   │   ├── exception/
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   ├── DuplicateCodeException.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── repository/
│   │   │   │   ├── ChecklistRepository.java
│   │   │   │   ├── ChecklistItemRepository.java
│   │   │   │   └── template/
│   │   │   │       ├── ChecklistTemplateRepository.java
│   │   │   │       ├── ChecklistTemplateVersionRepository.java
│   │   │   │       ├── ChecklistPhaseRepository.java
│   │   │   │       └── ChecklistTemplateItemRepository.java
│   │   │   ├── service/
│   │   │   │   ├── ChecklistService.java
│   │   │   │   └── ChecklistTemplateService.java
│   │   │   ├── mapper/
│   │   │   │   └── package-info.java
│   │   │   ├── audit/
│   │   │   │   └── package-info.java
│   │   │   ├── util/
│   │   │   │   └── package-info.java
│   │   │   └── model/
│   │   │       └── package-info.java
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       ├── ValidationMessages.properties
│   │       ├── checktrans.yml
│   │       ├── static/
│   │       │   ├── css/app.css
│   │       │   └── js/
│   │       │       ├── app.js
│   │       │       └── checklist-form.js
│   │       └── templates/
│   │           ├── index.html
│   │           ├── error/
│   │           │   ├── 404.html
│   │           │   ├── 409.html
│   │           │   └── 500.html
│   │           ├── fragments/
│   │           │   └── layout.html
│   │           ├── checklist/
│   │           │   ├── list.html
│   │           │   ├── form.html
│   │           │   └── detail.html
│   │           └── template/
│   │               ├── list.html
│   │               ├── form.html
│   │               ├── detail.html
│   │               ├── versions.html
│   │               └── version-detail.html
│   └── test/
│       ├── java/es/araujo/checktrans/
│       │   ├── ChecktransApplicationTests.java
│       │   ├── config/
│       │   │   └── ChecktransPropertiesTest.java
│   │       ├── controller/
│   │       │   ├── ChecklistControllerTest.java
│   │       │   ├── ChecklistTemplateControllerTest.java
│   │       │   ├── DemoControllerTest.java
│   │       │   └── HomeControllerTest.java
│       │   ├── domain/
│       │   │   ├── ChecklistTest.java
│       │   │   └── ChecklistItemTest.java
│       │   ├── dto/
│       │   │   ├── ChecklistDTOTest.java
│       │   │   ├── ChecklistCreateDTOTest.java
│       │   │   └── ChecklistItemDTOTest.java
│       │   ├── exception/
│       │   │   ├── DuplicateCodeExceptionTest.java
│       │   │   ├── GlobalExceptionHandlerTest.java
│       │   │   └── ResourceNotFoundExceptionTest.java
│       │   ├── repository/
│       │   │   ├── ChecklistRepositoryTest.java
│       │   │   └── template/
│       │   │       └── ChecklistTemplateRepositoryTest.java
│   │       └── service/
│   │           ├── ChecklistServiceTest.java
│   │           └── ChecklistTemplateServiceTest.java
│       └── resources/
│           └── application.yml
```

## Paquetes

Todos los paquetes Java bajo `es.araujo.checktrans`.

## Convenciones

- **Controladores**: Spring MVC `@Controller`, nombres en plural (ChecklistController)
- **Servicios**: `@Service` con `@Transactional`
- **Repositorios**: Spring Data JPA `@Repository`
- **DTOs**: clases planas con getters/setters, sin Lombok
- **Validación**: `jakarta.validation` con anotaciones
- **Vistas**: Thymeleaf con `layout:decorate`, carpetas por recurso
- **Tests**: JUnit 5 + Mockito + MockMvc + @DataJpaTest + @WebMvcTest + @SpringBootTest
- **Cobertura**: JaCoCo 0.8.12, reporte en `target/site/jacoco/index.html`
- **Logging**: SLF4J + Logback (via Spring Boot starter)
- **Configuración**: YAML exclusivamente

## Perfiles

| Perfil   | Base de datos         | DDL  | SQL Show | Thymeleaf Cache |
|----------|----------------------|------|----------|-----------------|
| default  | PostgreSQL (5432)    | validate | false | true          |
| dev      | H2 (memoria)         | create-drop | true | false        |
| test     | H2 (memoria)         | create-drop | false | n/a         |

## Comandos

```bash
# Desarrollo local
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Tests (con cobertura JaCoCo)
./mvnw test

# Tests sin cobertura (más rápido)
./mvnw surefire:test

# Reporte de cobertura
# Se genera automáticamente en target/site/jacoco/index.html tras ./mvnw test

# Package
./mvnw package -DskipTests

# Docker
docker compose up --build
```

## Documentación

| Documento                | Descripción                                      |
|--------------------------|--------------------------------------------------|
| `docs/architecture.md`   | Arquitectura completa del sistema                |
| `docs/domain-model.md`   | Modelo de dominio conceptual                     |
| `docs/roadmap.md`        | Roadmap completo con todas las fases             |
| `docs/AGENTS.md`         | Guía para agentes IA                             |

## Fases del Proyecto

| Fase | Estado       | Descripción                                     |
|------|-------------|-------------------------------------------------|
| 0    | Completada  | Definición arquitectónica y documentación       |
| 1    | Completada  | Proyecto base + CRUD listas de chequeo          |
| 2    | Completada  | Preparación calidad técnica (JaCoCo, Surefire, tests) |
| 3    | Completada  | Sistema visual corporativo                      |
| 4    | Completada  | Modelo JPA de plantillas (templates)            |
| 5    | Completada  | Gestión de plantillas (CRUD servicios/vistas)   |
| 6    | Completada  | Versionado de plantillas                        |
| 7    | Pendiente   | Ejecución de listas de chequeo                  |
| 8    | Pendiente   | Dashboard y reportes                            |
| 9    | Pendiente   | API REST                                        |
| 10   | Pendiente   | Notificaciones y alertas                        |
| 11   | Pendiente   | Despliegue en Railway                           |

## Fase Actual

**Fase 6**: Versionado de plantillas (completada)
**Siguiente**: Fase 7 — Autenticación y autorización (Spring Security)

## Archivos Creados en Fase 4

| Archivo | Propósito |
|---------|-----------|
| `src/main/java/.../domain/enums/ItemType.java` | Enum con 9 tipos de ítem (YES_NO_NA, TEXT, NUMBER, DATE, TIME, SELECT, SIGNATURE, PHOTO, DOCUMENT) |
| `src/main/java/.../domain/enums/ChecklistExecutionStatus.java` | Enum con estados de versión (CREADA, VERIFICADA, COMPROBADA, ANULADA) |
| `src/main/java/.../domain/AuditableEntity.java` | Clase base abstracta `@MappedSuperclass` con id, createdAt, updatedAt y callbacks @PrePersist/@PreUpdate |
| `src/main/java/.../domain/template/ChecklistTemplate.java` | Entidad raíz de plantilla con code único, name, description, active, y OneToMany a versiones |
| `src/main/java/.../domain/template/ChecklistTemplateVersion.java` | Versión de plantilla con versionNumber, status, effectiveDate, y OneToMany a fases |
| `src/main/java/.../domain/template/ChecklistPhase.java` | Fase/grupo dentro de una versión con phaseOrder, name, description, y OneToMany a ítems |
| `src/main/java/.../domain/template/ChecklistItem.java` | Ítem de definición con itemOrder, description, itemType (enum), required, options (para SELECT) |
| `src/main/java/.../repository/template/ChecklistTemplateRepository.java` | Repositorio con findByCode, findByActiveTrue, existsByCode |
| `src/main/java/.../repository/template/ChecklistTemplateVersionRepository.java` | Repositorio con findByTemplateIdOrderByVersionNumberDesc, findTopByTemplateIdOrderByVersionNumberDesc |
| `src/main/java/.../repository/template/ChecklistPhaseRepository.java` | Repositorio con findByVersionIdOrderByPhaseOrderAsc |
| `src/main/java/.../repository/template/ChecklistTemplateItemRepository.java` | Repositorio con findByPhaseIdOrderByItemOrderAsc |
| `src/test/java/.../repository/template/ChecklistTemplateRepositoryTest.java` | 14 tests de integración JPA: CRUD, relaciones, cascada, tipos de ítem, transición de estados |

## Archivos Creados en Fase 6

| Archivo | Propósito |
|---------|-----------|
| `src/main/java/.../dto/ChecklistTemplateVersionDTO.java` | DTO de versión con id, templateId, templateCode, templateName, versionNumber, activeVersion, status, publicationDate, effectiveDate, createdAt, updatedAt |
| `src/main/resources/templates/template/versions.html` | Vista de historial de versiones con tabla responsive, badges de estado (CREADA/VERIFICADA/COMPROBADA/ANULADA), columna activa, paginación implícita |
| `src/main/resources/templates/template/version-detail.html` | Vista de detalle de versión con número, estado, activa, fechas de publicación y efectividad |

## Archivos Creados en Fase 5

| Archivo | Propósito |
|---------|-----------|
| `src/main/java/.../dto/ChecklistTemplateDTO.java` | DTO de vista con id, code, name, description, active, createdAt, updatedAt |
| `src/main/java/.../dto/ChecklistTemplateCreateDTO.java` | DTO para crear/editar con Bean Validation (@NotBlank, @Size) y message keys |
| `src/main/java/.../service/ChecklistTemplateService.java` | Servicio CRUD + soft deactivate con findAll (Pageable), findById, create, update, deactivate, searchByName |
| `src/main/java/.../controller/ChecklistTemplateController.java` | MVC controller con endpoints GET /templates (lista paginada + búsqueda), GET/POST /templates/new, GET/POST /templates/{id}/edit, POST /templates/{id}/deactivate |
| `src/main/resources/ValidationMessages.properties` | Mensajes de validación en español para las anotaciones de los DTOs |
| `src/main/resources/templates/template/list.html` | Vista de listado con paginación, búsqueda por nombre, tabla responsive, estado activo/inactivo con badges |
| `src/main/resources/templates/template/form.html` | Vista de formulario con validación cliente (HTML5) + servidor (Thymeleaf errors) |
| `src/main/resources/templates/template/detail.html` | Vista de detalle con info general, fechas de creación/actualización, acciones de editar/desactivar |
| `src/test/java/.../controller/ChecklistTemplateControllerTest.java` | 9 tests MVC con MockMvc: list, create form, create success, validation errors, detail, edit form, update, update validation errors, deactivate |
| `src/test/java/.../service/ChecklistTemplateServiceTest.java` | 11 tests unitarios con Mockito: create, duplicate code on create, findById, not found, update, duplicate code on update, update same code, deactivate, deactivate not found, pagination, searchByName |

## Modificaciones en Fase 6

| Archivo | Cambio |
|---------|--------|
| `src/main/java/.../domain/template/ChecklistTemplateVersion.java` | Se añadieron campos `activeVersion` (Boolean) y `publicationDate` (LocalDate) |
| `src/main/java/.../dto/ChecklistTemplateDTO.java` | Se añadieron campos `currentVersionNumber` (Integer) y `currentVersionDate` (LocalDate) |
| `src/main/java/.../service/ChecklistTemplateService.java` | `create()` ahora crea versión inicial (v1, activa, fecha actual); `update()` crea nueva versión (versionNumber+1), la marca activa y desactiva las anteriores; nuevos métodos `findVersionsByTemplateId()` y `findVersionById()` |
| `src/main/java/.../controller/ChecklistTemplateController.java` | Nuevos endpoints `GET /{id}/versions` (historial) y `GET /{id}/versions/{versionId}` (detalle versión) |
| `src/main/resources/templates/template/detail.html` | Añadida sección "Versión Actual" con número y fecha; botón "Historial de Versiones" |
| `src/main/resources/templates/template/list.html` | Añadida columna "Versión" con badge v{N} |
| `src/test/java/.../service/ChecklistTemplateServiceTest.java` | 16 tests: actualizados create/update para verificar creación de versiones; nuevos tests: findVersionsByTemplateId, findVersionById, version mismatch |
| `src/test/java/.../controller/ChecklistTemplateControllerTest.java` | 11 tests: nuevos tests para version history y version detail endpoints |
| `src/test/java/.../repository/template/ChecklistTemplateRepositoryTest.java` | Actualizados todos los tests de versión para incluir activeVersion y publicationDate |

## Notas Técnicas

### JPA y Hibernate
- `AuditableEntity` es `@MappedSuperclass` usada por todas las entidades de template. No debe anotarse con `@Entity`.
- `domain.template.ChecklistItem` usa `@Entity(name = "ChecklistTemplateItem")` para evitar colisión con `domain.ChecklistItem` (mismo nombre simple).
- Los repositorios de template usan nombres únicos (`ChecklistTemplateItemRepository`) para evitar colisión de beans con `repository.ChecklistItemRepository`.
- Las relaciones en cascada se construyen con los métodos helper `addVersion()`, `addPhase()`, `addItem()` en las entidades padre, que establecen la referencia bidireccional. Para cascade delete correcto, guardar solo la raíz del grafo (templateRepository.save), no los hijos individualmente.

### Entidades (4 nuevas + 2 enums + 1 base)
```
ChecklistTemplate (1) ─── * ChecklistTemplateVersion (1) ─── * ChecklistPhase (1) ─── * ChecklistItem
```

### Tests
- **Totales**: 85 (78 anteriores + 5 service + 2 controller), BUILD SUCCESS.
- Fase 4: 14 tests de integración cubren CRUD por código, activos, versiones, fases, ítems, cascade delete, variantes de ItemType, transición de ChecklistExecutionStatus.
- Fase 5: 11 tests unitarios de servicio (Mockito) + 9 tests MVC (MockMvc) cubren CRUD completo, validación, búsqueda, soft delete.
- Fase 6: 5 nuevos tests unitarios de servicio (findVersionsByTemplateId, findVersionById, version mismatch, error cases) + 2 nuevos tests MVC (version history, version detail). 16 tests de servicio total + 11 tests MVC total.

### Versionado de Plantillas (Fase 6)
- `ChecklistTemplate.create()` crea automáticamente la versión inicial (v1) con `activeVersion=true` y `publicationDate=LocalDate.now()`.
- `ChecklistTemplate.update()` calcula el siguiente `versionNumber` (`findTopByTemplateIdOrderByVersionNumberDesc + 1`), crea una nueva versión activa, y marca todas las demás versiones como inactivas.
- Las versiones se almacenan en la relación `@OneToMany` dentro de `ChecklistTemplate` y se persisten en cascada.
- La columna `active_version` en `checklist_template_versions` permite identificar rápidamente la versión activa de una plantilla.
- `ChecklistTemplateDTO` incluye `currentVersionNumber` y `currentVersionDate` para mostrar en listados y detalle.
- Versión detalle muestra: número de versión, estado, activa/inactiva, fecha de publicación, fecha de efectividad, timestamps de auditoría.

### JaCoCo
- Configurado con `prepare-agent` (pre-test) y `report` (post-test, fase `test`). Reporte HTML en `target/site/jacoco/index.html`. El argumento del agente se pasa a Surefire vía `<argLine>${jacoco.agent.argLine}</argLine>`.

### Surefire
- Configurado con includes `**/*Test.java` y `**/*Tests.java`, excludes `**/Abstract*.java`.

### PowerShell / Windows
- El repackage de Spring Boot falla en Windows si el `.jar` está bloqueado por otro proceso. Solución: limpiar `target/` o usar `.\mvnw.cmd clean test`.
- Para pasar `-D` args con puntos: `.\mvnw.cmd package '-Dspring-boot.repackage.skip=true'`.
