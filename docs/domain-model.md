# CHECKTRANS - Modelo de Dominio Conceptual

## Visión General

El dominio de CHECKTRANS gira en torno a la creacion, ejecucion y seguimiento de **listas de chequeo** aplicadas a **transportes**. El modelo distingue entre la definicion (plantilla con versionado y fases), la instancia concreta (lista de chequeo), y la ejecucion con resultados.

## Mapa de Conceptos

```
PLANTILLA (definicion)
    |
    +-- Version (1..*, versionado)
    |       |
    |       +-- Fase (1..*, agrupacion)
    |               |
    |               +-- Item de definicion (1..*)
    |                       +-- Tipo: YES_NO_NA, TEXT, NUMBER, DATE, TIME, SELECT, etc.
    |
    +-- se instancia en -------------------+
                                           v
                              LISTA DE CHEQUEO (instancia)
                                    |
                                    +-- Item de ejecucion (1..*)
                                    |       +-- isPass / observaciones
                                    |
                                    +-- se ejecuta en ----------------+
                                                                     v
                                                             EJECUCION (resultados)
                                                                     |
                                                                     +-- Respuesta (1..*)
                                                                     |       +-- APTO / NO APTO / N/A
                                                                     |       +-- Observaciones
                                                                     |
                                                                     +-- Firma
                                                                             +-- Inspector
                                                                             +-- Fecha
```

## Entidades Conceptuales

### Checklist (Lista de Chequeo)

Lista de chequeo concreta asociada a un transporte.

| Atributo       | Tipo         | Descripcion                         |
|----------------|--------------|-------------------------------------|
| code           | String       | Codigo unico identificativo         |
| transportPlate | String       | Matricula del transporte            |
| transportType  | TransportType| Tipo de transporte                  |
| inspectorName  | String       | Nombre del inspector                |
| checkDate      | LocalDateTime| Fecha de la revision                |
| status         | ChecklistStatus | Estado del ciclo de vida         |
| observations   | String       | Observaciones generales             |
| createdAt      | LocalDateTime| Fecha de creacion (auditoria)       |
| updatedAt      | LocalDateTime| Fecha de modificacion (auditoria)   |
| template       | Template (opcional) | Plantilla de origen (Fase 2)  |
| createdBy      | User (opcional) | Usuario creador (Fase 3)         |

**Estados (ChecklistStatus):**
```
DRAFT --> COMPLETED --> APPROVED
                  \-> REJECTED
```

### ChecklistItem (Item de Lista)

Elemento individual dentro de una lista de chequeo.

| Atributo     | Tipo      | Descripcion                    |
|--------------|-----------|--------------------------------|
| description  | String    | Descripcion del elemento       |
| isPass       | Boolean   | Resultado (true/false/null)    |
| observations | String    | Observaciones del item         |
| itemOrder    | Integer   | Orden del item en la lista     |
| checklist    | Checklist | Lista a la que pertenece       |

### AuditableEntity (Base Auditable)

Clase base abstracta `@MappedSuperclass` que provee auditoria a todas las entidades del modelo de plantillas.

| Atributo   | Tipo           | Descripcion                       |
|------------|----------------|-----------------------------------|
| id         | Long           | Identificador unico (autoincremental) |
| createdAt  | LocalDateTime  | Fecha de creacion (automatica)    |
| updatedAt  | LocalDateTime  | Fecha de modificacion (automatica)|

### ChecklistTemplate (Plantilla) -- Fase 4

Definicion raiz de una plantilla reutilizable.

| Atributo    | Tipo                   | Descripcion                    |
|-------------|------------------------|--------------------------------|
| code        | String (unique)        | Codigo unico de la plantilla   |
| name        | String                 | Nombre de la plantilla         |
| description | String (TEXT)          | Descripcion                    |
| active      | Boolean                | Disponible para usar           |
| versions    | List<ChecklistTemplateVersion> | Versiones de la plantilla |

### ChecklistTemplateVersion (Version de Plantilla) -- Fase 4

Version concreta de una plantilla, con su propio ciclo de vida.

| Atributo      | Tipo                       | Descripcion                    |
|---------------|----------------------------|--------------------------------|
| template      | ChecklistTemplate          | Plantilla a la que pertenece   |
| versionNumber | Integer                    | Numero de version              |
| status        | ChecklistExecutionStatus   | Estado del ciclo de vida       |
| effectiveDate | LocalDate                  | Fecha de entrada en vigor      |
| phases        | List<ChecklistPhase>       | Fases de la version            |

**Estados (ChecklistExecutionStatus):**
```
CREADA --> VERIFICADA --> COMPROBADA
                    \-> ANULADA
```

### ChecklistPhase (Fase de Plantilla) -- Fase 4

Agrupacion logica de items dentro de una version.

| Atributo    | Tipo                      | Descripcion                    |
|-------------|---------------------------|--------------------------------|
| version     | ChecklistTemplateVersion  | Version a la que pertenece     |
| phaseOrder  | Integer                   | Orden de la fase               |
| name        | String                    | Nombre de la fase              |
| description | String (TEXT)             | Descripcion                    |
| items       | List<ChecklistItem>       | Items de definicion            |

### ChecklistItem (Item de Definicion) -- Fase 4

Item predefinido dentro de una fase de plantilla. Define el tipo de respuesta esperada.

| Atributo    | Tipo                 | Descripcion                           |
|-------------|----------------------|---------------------------------------|
| phase       | ChecklistPhase       | Fase a la que pertenece               |
| itemOrder   | Integer              | Orden del item                        |
| description | String (500)         | Descripcion del elemento              |
| itemType    | ItemType             | Tipo de respuesta esperada            |
| required    | Boolean              | Si la respuesta es obligatoria        |
| options     | String (TEXT)        | Opciones para tipo SELECT (opcional)  |

### ItemType

| Valor      | Descripcion                       |
|------------|-----------------------------------|
| YES_NO_NA  | Seleccion Si / No / No Aplica     |
| TEXT       | Texto libre                       |
| NUMBER     | Valor numerico                    |
| DATE       | Fecha                             |
| TIME       | Hora                              |
| SELECT     | Seleccion de opciones predefinidas|
| SIGNATURE  | Firma digital                     |
| PHOTO      | Fotografia                        |
| DOCUMENT   | Documento adjunto                 |

### ChecklistExecutionStatus

| Valor       | Descripcion                     |
|-------------|---------------------------------|
| CREADA      | Version creada, en edicion      |
| VERIFICADA  | Version verificada              |
| COMPROBADA  | Version comprobada y aprobada   |
| ANULADA     | Version anulada                 |

### Execution (Ejecucion) -- Fase 6

Ejecucion real de una lista de chequeo sobre un transporte.

| Atributo   | Tipo            | Descripcion                    |
|------------|-----------------|--------------------------------|
| checklist  | Checklist       | Lista ejecutada                |
| executedAt | LocalDateTime   | Fecha de ejecucion             |
| status     | ExecutionStatus | Estado de la ejecucion         |
| location   | String          | Ubicacion donde se realizo     |
| notes      | String          | Notas generales                |
| signature  | Signature       | Firma de la ejecucion          |
| responses  | List<Response>  | Respuestas a cada item         |

### Response (Respuesta) -- Fase 4

Respuesta a un item concreto durante la ejecucion.

| Atributo      | Tipo       | Descripcion                   |
|---------------|------------|-------------------------------|
| result        | ResultType | APTO, NO_APTO, N/A            |
| observations  | String     | Observaciones                 |
| evidencePhoto | String(URL)| Foto de evidencia (opcional)  |

### Signature (Firma) -- Fase 4

Registro de firma de una ejecucion.

| Atributo | Tipo          | Descripcion                |
|----------|---------------|----------------------------|
| signedBy | String        | Nombre de quien firma      |
| signedAt | LocalDateTime | Fecha y hora de la firma   |
| role     | String        | Rol de quien firma         |

### User (Usuario) -- Fase 3

Usuario del sistema.

| Atributo  | Tipo          | Descripcion               |
|-----------|---------------|---------------------------|
| username  | String        | Nombre de usuario unico   |
| password  | String        | Hash de contrasena        |
| email     | String        | Correo electronico        |
| fullName  | String        | Nombre completo           |
| role      | Role          | Rol del usuario           |
| enabled   | Boolean       | Si la cuenta esta activa  |
| createdAt | LocalDateTime | Fecha de registro         |

## Enumeraciones

### ChecklistStatus

| Valor     | Descripcion                       |
|-----------|-----------------------------------|
| DRAFT     | En edicion, no completada         |
| COMPLETED | Todos los items respondidos       |
| APPROVED  | Aprobada por supervisor           |
| REJECTED  | Rechazada, requiere revision      |

### TransportType

| Valor     | Descripcion      |
|-----------|------------------|
| TRUCK     | Camion           |
| VAN       | Furgoneta        |
| TRAILER   | Remolque         |
| CONTAINER | Contenedor       |
| OTHER     | Otro             |

### ResultType (Fase 4)

| Valor    | Descripcion      |
|----------|------------------|
| APTO     | Cumple requisito |
| NO_APTO  | No cumple        |
| N_A      | No aplica        |

### Role (Fase 3)

| Valor     | Descripcion                     |
|-----------|---------------------------------|
| ADMIN     | Acceso total                    |
| INSPECTOR | Crear/ejecutar listas           |
| VIEWER    | Solo lectura                    |

## Relaciones Clave

```
ChecklistTemplate           1 ----- * ChecklistTemplateVersion
ChecklistTemplateVersion    1 ----- * ChecklistPhase
ChecklistPhase              1 ----- * ChecklistItem (template)
Checklist                   1 ----- * ChecklistItem (execution)
ChecklistTemplate           1 ----- 0..* Checklist
Checklist                   1 ----- 0..* Execution
Execution                   1 ----- 1 Signature
Execution                   1 ----- * Response
User                        1 ----- * Checklist (createdBy)
User                        1 ----- * Execution (executedBy)
```

## Reglas de Negocio

1. **Codigo unico:** No pueden existir dos listas con el mismo codigo.
2. **Transicion de estados:** Solo DRAFT->COMPLETED, COMPLETED->APPROVED, COMPLETED->REJECTED.
3. **Items obligatorios:** Una lista debe tener al menos un item para ser completada.
4. **Fecha de revision:** No puede ser futura en el momento de completar la ejecucion.
5. **Firma obligatoria:** Toda ejecucion completada debe tener una firma.
6. **Integridad referencial:** Al eliminar una lista se eliminan sus items en cascada.
7. **Auditoria:** Toda creacion/modificacion registra fecha y usuario responsable.

## Glosario

| Termino              | Definicion                                         |
|----------------------|----------------------------------------------------|
| Lista de Chequeo     | Conjunto de items a verificar sobre un transporte  |
| Item (ejecucion)     | Elemento individual con resultado (isPass/obs)     |
| Item (definicion)    | Elemento de plantilla con tipo (YES_NO_NA, TEXT...)|
| Plantilla            | Definicion reutilizable con versionado y fases     |
| Version              | Version concreta de una plantilla con su ciclo     |
| Fase                 | Agrupacion de items dentro de una version          |
| Ejecucion            | Acto de realizar la verificacion sobre el terreno  |
| Respuesta            | Resultado de un item en una ejecucion              |
| Firma                | Validacion digital de la ejecucion                 |
| Transporte           | Vehiculo o unidad de transporte a inspeccionar     |
| Inspector            | Persona que realiza la inspeccion                  |
