# CHECKTRANS - Roadmap

## Fases del Proyecto

### Fase 0 — Definición Arquitectónica (Completada)

**Objetivo:** Documentar la arquitectura completa del sistema antes de escribir código de negocio.

**Entregables:**
- `docs/AGENTS.md` — Guía para agentes IA
- `docs/architecture.md` — Documento de arquitectura
- `docs/roadmap.md` — Roadmap del proyecto
- `docs/domain-model.md` — Modelo de dominio conceptual

**Criterios de éxito:**
- Toda la documentación arquitectónica revisada y aprobada
- Stack tecnológico definido y congelado
- Convenciones de código establecidas
- Estrategia de testing definida

---

### Fase 1 — Proyecto Base + CRUD Listas de Chequeo (Completada)

**Objetivo:** Implementar el esqueleto del proyecto Spring Boot con CRUD completo de listas de chequeo.

**Entregables:**
- Proyecto Maven con Spring Boot 3.4, JPA, Thymeleaf, Bootstrap 5
- Modelo de dominio: `Checklist`, `ChecklistItem`, enums
- Repositorios JPA con métodos de búsqueda
- Servicio con lógica CRUD y mapeo DTO
- Controlador MVC con listado paginado, creación y detalle
- Vistas Thymeleaf: listado, formulario, detalle
- Manejo global de excepciones con vistas personalizadas
- Perfiles: `default` (PostgreSQL), `dev` (H2), `test` (H2)
- Dockerfile multi-etapa + docker-compose.yml
- Tests unitarios (dominio + servicio)
- Tests de integración (repositorio + controlador)
- 24 tests pasando

---

### Fase 2 — Preparación Calidad Técnica (Completada)

**Objetivo:** Configurar herramientas de calidad y ampliar cobertura de tests.

**Entregables:**
- JaCoCo 0.8.12 configurado con `prepare-agent` y `report`
- Surefire 3.5.2 configurado explícitamente
- Reporte de cobertura en `target/site/jacoco/index.html`
- Tests de config (`ChecktransPropertiesTest`)
- Tests de controladores (`HomeControllerTest`)
- Tests de DTOs (`ChecklistDTOTest`, `ChecklistCreateDTOTest`, `ChecklistItemDTOTest`)
- Tests de excepciones (`GlobalExceptionHandlerTest`, `ResourceNotFoundExceptionTest`, `DuplicateCodeExceptionTest`)
- 40 tests pasando, BUILD SUCCESS

---

### Fase 3 — Sistema Visual Corporativo (Completada)

**Objetivo:** Implementar un sistema visual corporativo con CSS modular, fragmentos Thymeleaf y demo UI.

**Funcionalidades:**
- Variables CSS con colores corporativos
- Navbar, footer y flash-messages como fragmentos Thymeleaf
- Página demo con todos los componentes UI
- Componentes: cards con gradiente, tablas corporativas, formularios, badges, empty-state
- Responsive design (mobile-first)

**Archivos creados/modificados:**
- `app.css` (reescrito) — Sistema de diseño CSS con variables y componentes
- `fragments/navbar.html`, `footer.html`, `flash-messages.html` — Fragmentos UI
- `fragments/layout.html` (actualizado) — Layout con fragmentos
- `index.html`, `checklist/*.html`, `error/*.html` — Vistas rediseñadas
- `DemoController.java`, `demo-ui.html` — Página demo
- Tests: `DemoControllerTest` + ajustes

**Cobertura de tests:** 45 tests, BUILD SUCCESS

---

### Fase 4 — Modelo JPA de Plantillas (Completada)

**Objetivo:** Implementar el modelo JPA completo para la gestión de plantillas con versionado y fases.

**Entregables:**
- `AuditableEntity` — Clase base `@MappedSuperclass` con id, createdAt, updatedAt
- `ChecklistTemplate` — Plantilla raíz con code único, name, description, active
- `ChecklistTemplateVersion` — Versión con versionNumber, status (ChecklistExecutionStatus), effectiveDate
- `ChecklistPhase` — Fase con phaseOrder, name, description
- `ChecklistItem` (template) — Item de definición con itemType (ItemType), required, options
- `ItemType` enum — YES_NO_NA, TEXT, NUMBER, DATE, TIME, SELECT, SIGNATURE, PHOTO, DOCUMENT
- `ChecklistExecutionStatus` enum — CREADA, VERIFICADA, COMPROBADA, ANULADA
- Repositorios JPA para cada entidad
- Tests de integración (14 tests)

**Cobertura de tests:** 58 tests (44 anteriores + 14 nuevos), BUILD SUCCESS

---

### Fase 5 — Gestión de Plantillas (CRUD servicios/vistas)

**Objetivo:** Implementar servicios, DTOs, controladores y vistas para el CRUD de plantillas.

**Funcionalidades:**
- Servicio con lógica CRUD para plantillas, versiones, fases e ítems
- DTOs para crear/visualizar plantillas
- Controlador MVC con listado paginado, creación y detalle de plantillas
- Vistas Thymeleaf para gestión de plantillas
- Validación de datos de entrada
- Manejo de versionado (crear nueva versión, activar versión)

**Cobertura de tests esperada:** ~75 tests

---

### Fase 6 — Autenticación y Autorización

**Objetivo:** Implementar seguridad con Spring Security y control de acceso basado en roles.

**Funcionalidades:**
- Registro y login de usuarios
- Roles: `ADMIN`, `INSPECTOR`, `VIEWER`
- Protección de rutas por rol
- Página de login personalizada con Thymeleaf
- Sesión y cierre de sesión
- Registro de auditoría (quién creó/modificó cada checklist)

**Modelo de dominio a implementar:**
- `User` — Usuario del sistema
- `Role` — Rol (enum)

**Cobertura de tests esperada:** ~60 tests

---

### Fase 7 — Gestión de Fases (Completada)

**Objetivo:** Implementar gestión CRUD de fases dentro de versiones de plantilla, con reordenación.

**Funcionalidades:**
- CRUD completo de fases con código único dentro de cada versión
- Reordenación (moveUp/moveDown) intercambiando phaseOrder
- Vistas anidadas bajo `/templates/{templateId}/versions/{versionId}/phases`
- Validación de unicidad de código a nivel BD

**Cobertura de tests:** 109 tests (14 service + 10 controller nuevos), BUILD SUCCESS

---

### Fase 8 — Ejecución de Checklists + Dashboard y Reportes

**Objetivo:** Implementar el flujo completo de ejecución de listas de chequeo, generación de documentos PDF/Excel y dashboard de KPIs.

**Funcionalidades:**

*Ejecución de Checklists (Workflow)*
- Inicio de ejecución: selección de plantilla, nombre de ejecución y relleno de campos de cabecera
- Evaluación fase por fase: cada ítem se califica como SI / NO / NA, con comentarios opcionales
- Resumen previo a la finalización con conteo por fase
- Firma digital mediante pad HTML5 Canvas (ratón y táctil)
- Finalización con guardado de firma como Base64 en BD
- Visualización de ejecuciones completadas en modo solo lectura

*Generación de Documentos*
- **PDF**: documento profesional con logo corporativo, tabla de cabeceras, ítems codificados por color (SI=verde, NO=rojo, NA=gris), firma y sello, números de página, pie con versión y fecha
- **Excel (XLSX)**: mismo contenido estructurado con estilos, celdas coloreadas, anchos de columna y bordes

*Histórico de Ejecuciones*
- Listado completo con filtro por texto en cliente
- Acceso a vista detallada, PDF y Excel desde el listado

*Dashboard*
- KPIs: total ejecuciones, por estado, por inspector, por tipo de transporte
- Filtros avanzados (fechas, estado, inspector)

**Tecnologías adicionales:**
- Apache POI para generación de Excel
- iText o Flying Saucer para PDF
- Thymeleaf + Bootstrap 5 para dashboard (sin Chart.js ni frameworks JS adicionales)

**Modelo de dominio a implementar:**
- `Execution` — Ejecución de una lista de chequeo
- `ExecutionItem` — Respuesta de un ítem en una ejecución
- `Signature` — Firma digital (Base64)

**Cobertura de tests esperada:** ~150 tests

---

### Fase 9 — API REST

**Objetivo:** Exponer una API REST para integraciones con sistemas externos.

**Funcionalidades:**
- Endpoints REST para listas de chequeo
- Endpoints REST para ejecuciones
- Endpoints REST para plantillas
- Documentación OpenAPI/Swagger
- Autenticación con API Key o JWT

**Cobertura de tests esperada:** ~120 tests

---

### Fase 10 — Notificaciones y Alertas

**Objetivo:** Sistema de notificaciones para eventos del ciclo de vida de las listas.

**Funcionalidades:**
- Notificaciones in-app
- Alertas por email (SMTP)
- Webhooks para integraciones
- Suscripciones a eventos por usuario

**Cobertura de tests esperada:** ~130 tests

---

### Fase 11 — Despliegue en Railway

**Objetivo:** Desplegar la aplicación en Railway.app con CI/CD.

**Funcionalidades:**
- Pipeline CI/CD con Railway
- Variables de entorno para configuración
- Base de datos PostgreSQL gestionada por Railway
- Dominio personalizado
- SSL/TLS automático
- Backup de base de datos

---

## Resumen de Versiones

| Versión | Fases      | Funcionalidad Principal               | Tests |
|---------|------------|---------------------------------------|-------|
| 1.0.0   | Fase 1     | Proyecto base + CRUD listas           | 24    |
| 1.1.0   | Fase 2     | Calidad técnica (JaCoCo, cobertura)   | 40    |
| 1.2.0   | Fase 3     | Sistema visual corporativo            | 45    |
| 2.0.0   | Fase 4     | Modelo JPA de plantillas              | 58    |
| 2.1.0   | Fase 5     | Gestión de plantillas (CRUD)          | ~75   |
| 2.2.0   | Fase 6     | Versionado de plantillas              | 95    |
| 2.3.0   | Fase 7     | Gestión de fases                      | 109   |
| 3.0.0   | Fase 8     | Ejecución + Dashboard + Documentos    | ~150  |
| 3.1.0   | Fase 9     | API REST                              | ~155  |
| 3.2.0   | Fase 10    | Notificaciones                        | ~165  |
| 4.0.0   | Fase 11    | Despliegue Railway                    | ~165  |

## Estado Actual

**Fase activa:** Fase 7 — Gestión de fases (completada)
**Próxima fase:** Fase 8 — Ejecución de Checklists + Dashboard y Reportes
**Versión:** 2.0.0-SNAPSHOT
**Tests:** 109 tests, BUILD SUCCESS
