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
│   │   │   │   ├── ChecklistPhaseController.java
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
│   │   │   │   ├── ChecklistPhaseDTO.java
│   │   │   │   ├── ChecklistPhaseCreateDTO.java
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
│   │   │   │   ├── ChecklistPhaseService.java
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
| 7    | Completada  | Gestión de fases                                |
| 8    | En progreso | Ejecución de Checklists + Dashboard + Documentos (Fase 8-B: cabeceras, creación desde plantilla, items extra en fase 0) |
| 9    | Pendiente   | API REST                                        |
| 10   | Pendiente   | Notificaciones y alertas                        |
| 11   | Pendiente   | Despliegue en Railway                           |

## Funcionalidades Principales (Completadas)

### 1. Gestión de Plantillas (CRUD)
- Creación, edición y eliminación de plantillas de checklist (`ChecklistTemplate`)
- Cada plantilla contiene fases ordenadas (`ChecklistPhase`) y cada fase contiene ítems de verificación (`ChecklistItem`)
- Definición de campos de cabecera (`CabeceraPlantilla`) con valores dinámicos opcionales
- Gestión inline de fases e ítems vía REST API con modales Bootstrap

### 2. Sistema de Versionado de Plantillas
- Creación automática de versión inicial (v1) al crear una plantilla
- Nueva versión por cada actualización (versionNumber+1), marcada como activa
- Historial completo de versiones con estados: CREADA, VERIFICADA, COMPROBADA, ANULADA
- Badges de estado, columna activa, fechas de publicación y efectividad

### 3. Gestión de Fases
- CRUD completo de fases dentro de cada versión de plantilla
- Código único por fase dentro de cada versión (`@UniqueConstraint`)
- Reordenación con moveUp/moveDown intercambiando phaseOrder
- Vistas anidadas bajo `/templates/{templateId}/versions/{versionId}/phases`

### 4. CRUD de Listas de Chequeo
- Creación, edición, visualización y eliminación de listas de chequeo
- Listado paginado con búsqueda por inspector y matrícula
- Estados: DRAFT, COMPLETED, APPROVED, REJECTED

### 5. Sistema Visual Corporativo
- Paleta corporativa: azul marino oscuro (#1e3c78) como color primario
- Navbar, footer y mensajes flash como fragmentos Thymeleaf reutilizables
- Layout maestro (`layout.html`) con `layout:decorate`
- Componentes: cards con gradiente, tablas corporativas, badges, empty-state
- Diseño responsivo (mobile-first) con Bootstrap 5
- Página demo con todos los componentes UI

## Funcionalidades Pendientes (Próximas Fases)

### 6. Ejecución de Checklists (Workflow) — Fase 8
- Inicio de ejecución: selección de plantilla, nombre de ejecución y relleno de campos de cabecera
- Evaluación fase por fase: cada ítem se califica como SI (correcto), NO (incorrecto) o NA (no aplica), con comentarios opcionales
- Resumen previo a la finalización con conteo de SI/NO/NA por fase
- Firma digital mediante pad HTML5 Canvas (captura de firma con ratón y táctil)
- Finalización con guardado de firma como Base64 en base de datos
- Visualización de ejecuciones completadas en modo solo lectura

### 7. Generación de Documentos — Fase 8
- **PDF**: documento profesional con logo corporativo, tabla de cabeceras, ítems codificados por color (SI=verde, NO=rojo, NA=gris), firma y sello, números de página, pie con versión y fecha
- **Excel (XLSX)**: mismo contenido estructurado con estilos, celdas coloreadas, anchos de columna y bordes

### 8. Histórico de Ejecuciones — Fase 8
- Listado completo de ejecuciones con filtro por texto en cliente
- Acceso a vista detallada, PDF y Excel desde el listado

## Diseño de UI (Implementado)
- Server-side rendering con Thymeleaf, sin frameworks SPA
- Layout maestro (`fragments/layout.html`) con navbar, footer y mensajes flash
- Bootstrap 5 como base con personalización extensa en `css/app.css`
- Paleta de colores corporativa: azul marino oscuro (#1e3c78) como color primario
- Codificación por color: verde para SI, rojo para NO, gris para NA
- Diseño responsivo: las tablas colapsan a layout tipo card en móviles (<768px)
- Micro-interacciones: sombras, escalas en hover, animación fadeInUp en tarjetas

## Fase Actual

**Fase 7**: Gestión de fases (completada)
**Fase 8 (8-B)**: Cabeceras de plantilla, creación de checklist desde plantilla con items extra en fase 0 (en progreso)
**Siguiente**: Fase 8 — Ejecución de listas de chequeo + Dashboard y reportes

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

## Archivos Creados en Fase 7

| Archivo | Propósito |
|---------|-----------|
| `src/main/java/.../dto/ChecklistPhaseDTO.java` | DTO de fase con id, versionId, code, phaseOrder, name, description, createdAt, updatedAt |
| `src/main/java/.../dto/ChecklistPhaseCreateDTO.java` | DTO de creación/edición con Bean Validation (@NotBlank, @NotNull, @Size) |
| `src/main/java/.../service/ChecklistPhaseService.java` | Servicio CRUD + reorder: create vía cascada desde versión, update directo, delete vía orphanRemoval, moveUp/moveDown intercambiando phaseOrder |
| `src/main/java/.../controller/ChecklistPhaseController.java` | MVC controller anidado bajo /templates/{templateId}/versions/{versionId}/phases con endpoints list, create, edit, update, delete, moveUp, moveDown |
| `src/main/resources/templates/phase/list.html` | Vista de listado con tabla responsive, botones de reordenación (up/down), editar y eliminar por fase |
| `src/main/resources/templates/phase/form.html` | Vista de formulario con campos orden, código, nombre, descripción y validación |
| `src/test/java/.../service/ChecklistPhaseServiceTest.java` | 14 tests unitarios: CRUD, duplicado, reordenación (moveUp, moveDown, límites) |
| `src/test/java/.../controller/ChecklistPhaseControllerTest.java` | 10 tests MVC: list, create form, create validation errors, create success, edit form, update, update validation errors, delete, moveUp, moveDown |

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

## Modificaciones en Fase 7

| Archivo | Cambio |
|---------|--------|
| `src/main/java/.../domain/template/ChecklistPhase.java` | Añadido campo `code` (String, length=20) y `@UniqueConstraint(columnNames = {"version_id", "code"})` a nivel de tabla |
| `src/main/java/.../repository/template/ChecklistPhaseRepository.java` | Añadidos findByVersionIdAndId, existsByVersionIdAndCode, findTopByVersionIdOrderByPhaseOrderDesc |
| `src/main/resources/templates/template/version-detail.html` | Añadido botón "Gestionar Fases" en la tarjeta de acciones |
| `src/main/resources/ValidationMessages.properties` | Añadidas 6 claves de mensaje para validación de fase (code, order, name, description) |
| `src/test/java/.../repository/template/ChecklistTemplateRepositoryTest.java` | Actualizados tests de fase para incluir campo `code` |

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

## Archivos Creados en Fase 8-B

| Archivo | Propósito |
|---------|-----------|
| `src/main/java/.../domain/enums/ChecklistHeaderType.java` | Enum TEXT, NUMBER, DATE, SELECT para tipos de cabecera de plantilla |
| `src/main/java/.../domain/template/ChecklistTemplateHeader.java` | Entidad cabecera de plantilla con code, label, headerOrder, headerType, options, required y @ManyToOne->ChecklistTemplate |
| `src/main/java/.../domain/ChecklistHeaderValue.java` | Entidad valor de cabecera en ejecución (headerId, headerCode, headerLabel, headerType, value, headerOrder) |
| `src/main/java/.../dto/ChecklistTemplateHeaderDTO.java` | DTO de lectura para cabeceras de plantilla |
| `src/main/java/.../dto/ChecklistTemplateHeaderCreateDTO.java` | DTO de creación/edición con Bean Validation |
| `src/main/java/.../dto/ChecklistHeaderValueDTO.java` | DTO para valores de cabecera en ejecución |
| `src/main/java/.../dto/PhaseGroupDTO.java` | DTO para agrupar items por fase (phaseOrder, phaseName, items) |
| `src/main/java/.../repository/template/ChecklistTemplateHeaderRepository.java` | Repositorio con findByTemplateIdOrderByHeaderOrderAsc, findByTemplateIdAndId, existsByTemplateIdAndCode, deleteByTemplateId |
| `src/main/java/.../repository/ChecklistHeaderValueRepository.java` | Repositorio para ChecklistHeaderValue |
| `src/main/java/.../service/ChecklistTemplateHeaderService.java` | Servicio CRUD con DuplicateCodeException / ResourceNotFoundException |
| `src/main/java/.../controller/ChecklistTemplateHeaderController.java` | MVC controller bajo /templates/{templateId}/headers (list, create, edit, update, delete) |
| `src/main/resources/templates/header/list.html` | Vista listado de cabeceras con tabla + empty state |
| `src/main/resources/templates/header/form.html` | Vista formulario con toggle SELECT para tipo, textarea de opciones |

## Modificaciones en Fase 8-B

| Archivo | Cambio |
|---------|--------|
| `src/main/java/.../domain/template/ChecklistTemplate.java` | Añadido `@OneToMany` headers + helpers addHeader/removeHeader |
| `src/main/java/.../domain/Checklist.java` | Añadidos templateId (nullable) y templateName |
| `src/main/java/.../domain/ChecklistItem.java` | Añadidos phaseOrder, phaseName, code, itemType, required |
| `src/main/java/.../dto/ChecklistTemplateDTO.java` | Añadido currentVersionId |
| `src/main/java/.../dto/ChecklistDTO.java` | Añadidos templateId, templateName, headerValues, nuevos campos de item |
| `src/main/java/.../dto/ChecklistCreateDTO.java` | Añadido templateId |
| `src/main/java/.../dto/ChecklistItemDTO.java` | Añadidos code, itemType, required, phaseOrder, phaseName |
| `src/main/java/.../service/ChecklistTemplateService.java` | toDTO() ahora setea currentVersionId |
| `src/main/java/.../service/ChecklistService.java` | Nuevos 3 deps (template, version, header repos); create() copia items/headers desde template activa y permite items manuales (phaseOrder=0); se cambió `else if` por `if` para permitir items manuales + template |
| `src/main/java/.../controller/ChecklistController.java` | Añadido ChecklistTemplateService dep; nuevo endpoint /from-template/{templateId}; detail() agrupa items por fase en PhaseGroupDTO |
| `src/main/resources/ValidationMessages.properties` | Añadidos mensajes para cabeceras |
| `src/main/resources/templates/checklist/form.html` | Añadido selector de plantilla; items manuales siempre visibles; info alert cuando hay plantilla; **eliminados matrícula y tipo de transporte** |
| `src/main/resources/templates/checklist/detail.html` | **Eliminados matrícula y tipo de transporte** del detalle; items agrupados por fase |
| `src/main/resources/templates/checklist/list.html` | **Eliminadas columnas matrícula y tipo de transporte** del listado |
| `src/main/java/.../domain/Checklist.java` | **Eliminados campos transportPlate y transportType** (se definen como cabeceras de plantilla si se necesitan) |
| `src/main/java/.../dto/ChecklistDTO.java` | **Eliminados transportPlate y transportType** |
| `src/main/java/.../dto/ChecklistCreateDTO.java` | **Eliminados transportPlate y transportType** y sus validaciones |
| `src/main/java/.../service/ChecklistService.java` | **Eliminado searchByPlate** y referencias a transportPlate/transportType en create() y toDTO() |
| `src/main/java/.../repository/ChecklistRepository.java` | **Eliminados findByTransportPlateContainingIgnoreCase** |
| `src/main/java/.../controller/ChecklistController.java` | detail() agrupa items por fase en PhaseGroupDTO |
| `src/main/resources/templates/checklist/detail.html` | Muestra template name/link, header values, items agrupados por fase con PhaseGroupDTO |
| `src/main/resources/templates/template/detail.html` | Botones "Gestionar Cabeceras", "Gestionar Fases" directo, "Crear Lista desde Plantilla" |
| `src/main/resources/templates/template/list.html` | Botón "Crear Lista" por fila |
| `src/main/resources/templates/fragments/navbar.html` | Añadido enlace "Plantillas" |

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
- **Totales**: 133, BUILD SUCCESS.
- Fase 4: 14 tests de integración cubren CRUD por código, activos, versiones, fases, ítems, cascade delete, variantes de ItemType, transición de ChecklistExecutionStatus.
- Fase 5: 11 tests unitarios de servicio (Mockito) + 9 tests MVC (MockMvc) cubren CRUD completo, validación, búsqueda, soft delete.
- Fase 6: 5 nuevos tests unitarios de servicio (findVersionsByTemplateId, findVersionById, version mismatch, error cases) + 2 nuevos tests MVC (version history, version detail). 16 tests de servicio total + 11 tests MVC total.
- Fase 7: 14 tests unitarios de servicio (CRUD, duplicado, reordenación, límites) + 10 tests MVC (list, create/edit/delete, validación, moveUp/moveDown).

### Gestión de Fases (Fase 7)
- `ChecklistPhaseService` gestiona fases dentro de una versión de plantilla.
- Alta: se crea la fase, se añade a la versión via `version.addPhase()`, y se persiste la versión (cascada JPA).
- Edición: se actualiza la fase directamente via `phaseRepository.save()`.
- Eliminación: se usa `version.removePhase()` + `versionRepository.save()` para activar orphanRemoval JPA, manteniendo integridad referencial (cascade delete de items hijos).
- Reordenación: `moveUp()` y `moveDown()` intercambian `phaseOrder` entre la fase actual y su adyacente.
- `@UniqueConstraint(columnNames = {"version_id", "code"})` garantiza códigos únicos dentro de cada versión a nivel BD.
- Las fases se gestionan bajo la URL `/templates/{templateId}/versions/{versionId}/phases`.

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
