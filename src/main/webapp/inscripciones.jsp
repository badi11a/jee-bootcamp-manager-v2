<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<h2 class="mb-4">Inscripciones del Estudiante</h2>
<table class="table table-striped">
    <thead>
        <tr>
            <th>ID</th>
            <th>Curso</th>
            <th>Fecha de Inscripción</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="i" items="${inscripciones}">
            <tr>
                <td>${i.id}</td>
                <td>${i.nombreCurso}</td>
                <td>${i.fechaInscripcion}</td>
            </tr>
        </c:forEach>
    </tbody>
</table>
<a href="estudiantes" class="btn btn-secondary">Volver</a>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
