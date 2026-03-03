package controller;

import dao.EstudianteDAO;
import model.Estudiante;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


import dao.InscripcionDAO;
import model.Estudiante;
import java.util.ArrayList;

@WebServlet("/estudiantes")
public class EstudianteServlet extends HttpServlet {
    private EstudianteDAO estudianteDAO = new EstudianteDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "listar";
        switch (action) {
            case "nuevo":
                request.setAttribute("estudiante", new Estudiante());
                request.getRequestDispatcher("estudiante-form.jsp").forward(request, response);
                break;
            case "editar":
                int idEditar = Integer.parseInt(request.getParameter("id"));
                Estudiante estudianteEditar = estudianteDAO.obtenerPorId(idEditar);
                request.setAttribute("estudiante", estudianteEditar);
                request.getRequestDispatcher("estudiante-form.jsp").forward(request, response);
                break;
            case "eliminar":
                try {
                    int idEliminar = Integer.parseInt(request.getParameter("id"));
                    InscripcionDAO inscripcionDAO = new InscripcionDAO();
                    
                    // Validación con el nuevo método de conteo
                    if (inscripcionDAO.tieneInscripciones(idEliminar)) {
                        response.sendRedirect("estudiantes?error=dependencias");
                        return; // Detiene la ejecución del hilo inmediatamente
                    } else {
                        estudianteDAO.eliminar(idEliminar);
                        response.sendRedirect("estudiantes");
                        return;
                    }
                } catch (NumberFormatException e) {
                    response.sendRedirect("estudiantes");
                    return;
                }    
            
            default:
                String rutBusqueda = request.getParameter("rutBusqueda");
                List<Estudiante> estudiantes;
                
                if (rutBusqueda != null && !rutBusqueda.trim().isEmpty()) {
                    Estudiante encontrado = estudianteDAO.buscarPorRut(rutBusqueda.trim());
                    estudiantes = new java.util.ArrayList<>();
                    if (encontrado != null) {
                        estudiantes.add(encontrado);
                    }
                } else {
                    estudiantes = estudianteDAO.listarTodos();
                }
                
                request.setAttribute("estudiantes", estudiantes);
                request.getRequestDispatcher("estudiantes.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        String rut = request.getParameter("rut");
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        //boolean activo = request.getParameter("activo") != null;
        boolean activo = true; // Forzamos el 1 en la BD para que no desaparezca de la lista
        //Cuando es un nuevo registro se lleva a id = 0
        int id = 0;
        if (idStr != null && !idStr.isEmpty()) {
            id = Integer.parseInt(idStr);
        }
        
        if (id == 0) {
            // Insertar
            Estudiante nuevo = new Estudiante();
            nuevo.setRut(rut);
            nuevo.setNombre(nombre);
            nuevo.setEmail(email);
            nuevo.setActivo(activo);
            estudianteDAO.insertar(nuevo);
        } else {
            // Actualizar
            Estudiante existente = new Estudiante();
            existente.setId(id);
            existente.setRut(rut);
            existente.setNombre(nombre);
            existente.setEmail(email);
            existente.setActivo(activo);
            estudianteDAO.actualizar(existente);
        }
        response.sendRedirect("estudiantes");
    }
}
