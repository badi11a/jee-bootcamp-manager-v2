package dao;

import model.Estudiante;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO {
    public List<Estudiante> listarTodos() {
        List<Estudiante> estudiantes = new ArrayList<>();
        String sql = "SELECT * FROM estudiante WHERE activo = 1";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Estudiante e = new Estudiante();
                e.setId(rs.getInt("id"));
                e.setRut(rs.getString("rut"));
                e.setNombre(rs.getString("nombre"));
                e.setEmail(rs.getString("email"));
                e.setActivo(rs.getBoolean("activo"));
                estudiantes.add(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return estudiantes;
    }

    public void insertar(Estudiante estudiante) {
        String sql = "INSERT INTO estudiante (rut, nombre, email, activo) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estudiante.getRut());
            ps.setString(2, estudiante.getNombre());
            ps.setString(3, estudiante.getEmail());
            ps.setBoolean(4, estudiante.isActivo());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Estudiante obtenerPorId(int id) {
        String sql = "SELECT * FROM estudiante WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Estudiante e = new Estudiante();
                    e.setId(rs.getInt("id"));
                    e.setRut(rs.getString("rut"));
                    e.setNombre(rs.getString("nombre"));
                    e.setEmail(rs.getString("email"));
                    e.setActivo(rs.getBoolean("activo"));
                    return e;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void actualizar(Estudiante estudiante) {
        String sql = "UPDATE estudiante SET rut = ?, nombre = ?, email = ?, activo = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estudiante.getRut());
            ps.setString(2, estudiante.getNombre());
            ps.setString(3, estudiante.getEmail());
            ps.setBoolean(4, estudiante.isActivo());
            ps.setInt(5, estudiante.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void eliminar(int id) {
        String sql = "UPDATE estudiante SET activo = 0 WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    //Ticket#2
    public Estudiante buscarPorRut(String rut) {
        String sql = "SELECT * FROM estudiante WHERE rut = ? AND activo = 1";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rut);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Estudiante e = new Estudiante();
                    e.setId(rs.getInt("id"));
                    e.setRut(rs.getString("rut"));
                    e.setNombre(rs.getString("nombre"));
                    e.setEmail(rs.getString("email"));
                    e.setActivo(rs.getBoolean("activo"));
                    return e;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
