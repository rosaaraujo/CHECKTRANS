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

### Fase 7 — Ejecución de Listas de Chequeo

**Objetivo:** Implementar el flujo completo de ejecución de una lista de chequeo sobre un transporte.

**Funcionalidades:**
- Crear una ejecución a partir de una plantilla o lista en blanco
- Registrar respuestas por ítem (APTO / NO APTO / N/A)
- Observaciones por ítem
- Firmar digitalmente la ejecución (nombre + fecha)
- Cambio de estado: `IN_PROGRESS` → `COMPLETED` → `APPROVED` / `REJECTED`
- Histórico de ejecuciones por transporte

**Modelo de dominio a implementar:**
- `Execution` — Ejecución de una lista de chequeo
- `ExecutionItem` — Respuesta de un ítem en una ejecución
- `Signature` — Firma de la ejecución

**Cobertura de tests esperada:** ~80 tests

---

### Fase 8 — Dashboard y Reportes

**Objetivo:** Proporcionar dashboards visuales y reportes exportables.

**Funcionalidades:**
- Dashboard con KPIs: total listas, aprobadas, rechazadas, pendientes
- Gráficos por inspector, tipo de transporte, período
- Reporte PDF de una ejecución
- Exportación CSV de listados
- Filtros avanzados (fechas, inspector, estado, tipo transporte)

**Tecnología:** Chart.js (única excepción JS) o tablas HTML con Bootstrap

**Cobertura de tests esperada:** ~100 tests

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
| 2.2.0   | Fase 6     | Autenticación y autorización          | ~95   |
| 2.3.0   | Fase 7     | Ejecución de listas de chequeo        | ~115  |
| 3.0.0   | Fase 8     | Dashboard y reportes                  | ~135  |
| 3.1.0   | Fase 9     | API REST                              | ~155  |
| 3.2.0   | Fase 10    | Notificaciones                        | ~165  |
| 4.0.0   | Fase 11    | Despliegue Railway                    | ~165  |

## Estado Actual

**Fase activa:** Fase 4 — Modelo JPA de plantillas (completada)
**Próxima fase:** Fase 5 — Gestión de plantillas (CRUD servicios/vistas)
**Versión:** 2.0.0-SNAPSHOT
