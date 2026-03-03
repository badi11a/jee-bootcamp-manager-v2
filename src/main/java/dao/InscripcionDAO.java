package dao;

import model.Inscripcion;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InscripcionDAO {
    public List<Inscripcion> listarPorEstudiante(int estudianteId) {
        List<Inscripcion> lista = new ArrayList<>();
        String sql = "SELECT i.id, i.estudiante_id, i.curso_id, c.nombre as nombreCurso, i.fecha_inscripcion " +
                "FROM inscripcion i JOIN curso c ON i.curso_id = c.id WHERE i.estudiante_id = ?";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, estudianteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Inscripcion ins = new Inscripcion();
                    ins.setId(rs.getInt("id"));
                    ins.setEstudianteId(rs.getInt("estudiante_id"));
                    ins.setCursoId(rs.getInt("curso_id"));
                    ins.setNombreCurso(rs.getString("nombreCurso"));
                    ins.setFechaInscripcion(rs.getString("fecha_inscripcion"));
                    lista.add(ins);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }
    public boolean tieneInscripciones(int estudianteId) {
    String sql = "SELECT COUNT(*) FROM inscripcion WHERE estudiante_id = ?";
    try (Connection conn = DatabaseConnection.getInstance();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, estudianteId);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace(); // Revisa la consola si esto falla
    }
    return true; // Por seguridad: si falla la BD, asumimos que tiene para no borrar
}
}
