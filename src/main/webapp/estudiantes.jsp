<%@ include file="/WEB-INF/jspf/header.jspf" %>
<h2 class="mb-4">Listado de Estudiantes</h2>
<%-- Alerta de validación de regla de negocio --%>
<c:if test="${param.error == 'dependencias'}">
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        <strong>Operaci&oacute;n denegada:</strong> No se puede desactivar un estudiante que tiene inscripciones activas en cursos.
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>
<div class="d-flex justify-content-between align-items-center mb-3">
    <a href="estudiantes?action=nuevo" class="btn btn-primary">Nuevo Alumno</a>
    <form action="estudiantes" method="get" class="d-flex">
        <input type="text" name="rutBusqueda" class="form-control me-2" placeholder="Buscar por RUT..." value="${param.rutBusqueda}">
        <button type="submit" class="btn btn-outline-secondary">Buscar</button>
        <c:if test="${not empty param.rutBusqueda}">
            <a href="estudiantes" class="btn btn-link text-decoration-none">Limpiar</a>
        </c:if>
    </form>
</div>
<table class="table table-striped">
    <thead>
        <tr>
            <th>ID</th>
            <th>RUT</th>
            <th>Nombre</th>
            <th>Email</th>
            <th>Acciones</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="e" items="${estudiantes}">
            <tr>
                <td>${e.id}</td>
                <td>${e.rut}</td>
                <td>${e.nombre}</td>
                <td>${e.email}</td>
                <td>
                    <a href="estudiantes?action=editar&id=${e.id}" class="btn btn-sm btn-warning">Editar</a>
                    <a href="estudiantes?action=eliminar&id=${e.id}" class="btn btn-sm btn-danger" onclick="return confirm('\u00BFDesactivar estudiante? El registro se mantendr\u00E1 en el sistema.');">Eliminar</a>
                    <a href="inscripciones?estudianteId=${e.id}" class="btn btn-sm btn-info">Ver Inscripciones</a>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
