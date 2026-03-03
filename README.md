# Bootcamp Manager

## Descripción General
Bootcamp Manager es un sistema web académico desarrollado en Java para la gestión de estudiantes y sus inscripciones a cursos. Su propósito es didáctico, orientado a la enseñanza de patrones de arquitectura limpia, MVC y acceso a datos en aplicaciones empresariales modernas.

## Stack Tecnológico
- **Lenguaje:** Java 21 (LTS)
- **Servidor:** Apache Tomcat 11
- **Framework:** Jakarta EE 11 (Servlets, JSP)
- **Base de Datos:** MariaDB
- **Gestor de Dependencias:** Maven

## Análisis de la Estructura
- **controller:** Servlets que gestionan el flujo de la aplicación (EstudianteServlet, InscripcionServlet).
- **dao:** Acceso a datos y lógica de persistencia (EstudianteDAO, InscripcionDAO).
- **model:** DTOs que representan entidades de negocio (Estudiante, Inscripcion).
- **util:** Utilidades de infraestructura, como la conexión Singleton a la base de datos (DatabaseConnection).

## Funcionalidades Descubiertas
### CRUD Completo de Estudiantes
- Listado, creación, edición y eliminación de estudiantes.
- El listado solo muestra estudiantes activos (`activo=1`).
- El formulario permite ingresar RUT, nombre, email y estado activo.
- Eliminar un estudiante elimina físicamente el registro, salvo que existan inscripciones asociadas (restricción de integridad referencial).

### Visualización de Inscripciones
- Desde el listado de estudiantes, se puede acceder a la vista de inscripciones de cada estudiante.
- Se muestra el curso y la fecha de inscripción, consultando la relación entre las tablas `estudiante`, `curso` e `inscripcion`.

### Funcionalidad Avanzada Detectada: Integridad Referencial y Restricción de Eliminación
- El sistema implementa protección a nivel de base de datos: si un estudiante tiene inscripciones asociadas, no puede ser eliminado (por la restricción FOREIGN KEY en la tabla `inscripcion`).
- El método `eliminar` en el DAO captura y reporta el error, pero no elimina el registro si existen dependencias.
- Esto garantiza la integridad de los datos y previene la pérdida accidental de información relacionada.

## Configuración Inicial
1. Ejecuta el script SQL `bootcamp_manager.sql` para crear la base de datos y las tablas con datos de prueba.
2. Ajusta las credenciales de conexión en `DatabaseConnection.java` según tu entorno de MariaDB.
3. Compila el proyecto con Maven y despliega el archivo WAR resultante en Apache Tomcat 11.

## Sprint Backlog - Tickets de Soporte

### Ticket #001 [Mantenimiento Correctivo]: "Fallo en flujo de Actualización" (Resuelto)
**Descripción:** Al intentar editar un alumno, se duplica el registro en la BD.
**Contexto:** Analizar la comunicación entre estudiante-form.jsp y el método doPost del Servlet.

### Ticket #002 [Evolutivo 1]: "Buscador de Alumnos por RUT" (Resuelto)
**Descripción:** El cliente requiere filtrar la tabla de alumnos por su RUT.
**Requisitos:** Crear buscarPorRut() en el DAO y capturar el parámetro en el controlador.

### Ticket #003 [Evolutivo 2]: "Políticas de Soft Delete" (Resuelto)
**Descripción:** Implementar borrado lógico. Los registros no deben desaparecer de la BD.
**Requisitos:** Usar la columna activo, modificar el DELETE por un UPDATE y filtrar el listado principal.

---

## Changelog

### Resolución Ticket #001: Mantenimiento Correctivo
Se corrigió el error de duplicación de registros y desaparición del listado al actualizar un estudiante.

* **Fallo de actualización (Duplicidad):** El formulario HTML no enviaba el ID del estudiante. El Servlet recibía un valor nulo o "0" y ejecutaba un `INSERT` en lugar de un `UPDATE`. Se solucionó agregando el campo oculto `<input type="hidden" name="id" value="${estudiante.id}">` en la vista `estudiante-form.jsp` y ajustando la validación en el Servlet.
* **Fallo de estado (Desaparición):** El parámetro `activo` no se enviaba en la edición, evaluándose como `false` y guardando un `0` en la base de datos. Se forzó el valor `activo = true` en el controlador de manera temporal hasta implementar el borrado lógico (Ticket #003).

**Nota didáctica sobre la variable `${estudiante}`:**
El objeto se origina en el controlador (`doGet`), donde se consulta a la base de datos y se adjunta a la petición HTTP mediante `request.setAttribute("estudiante", objeto)`. Posteriormente, en la vista JSP, Expression Language (`${}`) extrae el objeto de la petición y ejecuta automáticamente sus métodos *getter* para poblar el formulario de forma segura.

### Resolución Ticket #002: Buscador de Alumnos por RUT
Se implementó un filtro de búsqueda dinámico interviniendo las tres capas del patrón MVC:

* **Modelo (DAO):** Se creó el método `buscarPorRut(String rut)` que ejecuta una consulta `SELECT` filtrando por el RUT exacto y manteniendo la regla de negocio `activo = 1`.
* **Controlador (Servlet):** En el método `doGet`, se interceptó el parámetro GET `rutBusqueda`. Si el parámetro existe y no está vacío, el Servlet delega la búsqueda al nuevo método del DAO. De lo contrario, ejecuta el flujo estándar de `listarTodos()`.
* **Vista (JSP):** Se integró un formulario de búsqueda (`method="get"`) en la interfaz. Se utilizó la variable implícita `${param.rutBusqueda}` de Expression Language en el atributo `value` del input para conservar visualmente el texto ingresado tras la recarga de la página.

### Resolución Ticket #003: Políticas de Soft Delete e Integridad Referencial
Se transformó el sistema de eliminación para preservar el histórico de datos y asegurar la integridad del negocio:

* **Modelo (DAO):** Se modificó el método `eliminar(int id)` en `EstudianteDAO` para ejecutar un `UPDATE` de la columna `activo` en lugar de un `DELETE` físico.
* **Controlador (Servlet):** Se implementó un "Guardia de Integridad" en el flujo de eliminación. Antes de llamar al DAO, el Servlet consulta al `InscripcionDAO` para verificar si el estudiante posee registros vinculados. Si existen, la operación se aborta y se notifica al usuario; de lo contrario, se procede con la desactivación.
* **Vista (JSP):** Se integró un sistema de alertas dinámicas que intercepta el parámetro `error=dependencias` para informar al usuario sobre la restricción de integridad aplicada.