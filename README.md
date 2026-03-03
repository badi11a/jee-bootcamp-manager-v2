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

### Ticket #001 [Mantenimiento Correctivo]: "Fallo en flujo de Actualización"
**Descripción:** Al intentar editar un alumno, se duplica el registro en la BD.
**Contexto:** Analizar la comunicación entre estudiante-form.jsp y el método doPost del Servlet.

### Ticket #002 [Evolutivo 1]: "Buscador de Alumnos por RUT"
**Descripción:** El cliente requiere filtrar la tabla de alumnos por su RUT.
**Requisitos:** Crear buscarPorRut() en el DAO y capturar el parámetro en el controlador.

### Ticket #003 [Evolutivo 2]: "Políticas de Soft Delete"
**Descripción:** Implementar borrado lógico. Los registros no deben desaparecer de la BD.
**Requisitos:** Usar la columna activo, modificar el DELETE por un UPDATE y filtrar el listado principal.
