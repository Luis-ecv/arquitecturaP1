package Datos;

import Database.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DRutina {

    public boolean insertar(String nombre, String tipo, int cliente_id, int dieta_id) {
        String sql = "INSERT INTO rutina(nombre, tipo, cliente_id, dieta_id) VALUES(?,?,?,?)";
        
        Conexion con = new Conexion();
        try (Connection conn = con.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, tipo);
            pstmt.setInt(3, cliente_id);
            pstmt.setInt(4, dieta_id);
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error al insertar rutina: " + e.getMessage());
            return false;
        }
    }

    public List<Map<String, Object>> obtenerTodos() {
        // Hacemos JOIN para traer los nombres del cliente y la dieta, además de teléfono y correo para envíos
        String sql = "SELECT r.id, r.nombre, r.tipo, c.nombre AS cliente_nombre, c.telefono, c.correo, d.titulo AS dieta_titulo " +
                     "FROM rutina r " +
                     "JOIN cliente c ON r.cliente_id = c.id " +
                     "JOIN dieta d ON r.dieta_id = d.id " +
                     "ORDER BY r.id DESC";
                     
        List<Map<String, Object>> lista = new ArrayList<>();
        Conexion con = new Conexion();
        
        try (Connection conn = con.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("id", rs.getInt("id"));
                fila.put("nombre", rs.getString("nombre"));
                fila.put("tipo", rs.getString("tipo"));
                fila.put("cliente_nombre", rs.getString("cliente_nombre"));
                fila.put("telefono", rs.getString("telefono"));
                fila.put("correo", rs.getString("correo"));
                fila.put("dieta_titulo", rs.getString("dieta_titulo"));
                lista.add(fila);
            }
        } catch (Exception e) {
            System.out.println("Error al obtener rutinas: " + e.getMessage());
        }
        return lista;
    }

    public boolean editar(int id, String nombre, String tipo, int cliente_id, int dieta_id) {
        String sql = "UPDATE rutina SET nombre = ?, tipo = ?, cliente_id = ?, dieta_id = ? WHERE id = ?";
        Conexion con = new Conexion();
        try (Connection conn = con.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, tipo);
            pstmt.setInt(3, cliente_id);
            pstmt.setInt(4, dieta_id);
            pstmt.setInt(5, id);
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error al editar rutina: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM rutina WHERE id = ?";
        Conexion con = new Conexion();
        try (Connection conn = con.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error al eliminar rutina: " + e.getMessage());
            return false;
        }
    }
}
