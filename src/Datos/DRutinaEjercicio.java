package Datos;

import Database.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DRutinaEjercicio {

    public boolean insertar(List<String> dias_rutina, int rutina_id, int ejercicio_id) {
        String sql = "INSERT INTO rutina_ejercicio(dia_rutina, rutina_id, ejercicio_id) VALUES(?,?,?)";
        
        Conexion con = new Conexion();
        try (Connection conn = con.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (String dia : dias_rutina) {
                pstmt.setString(1, dia);
                pstmt.setInt(2, rutina_id);
                pstmt.setInt(3, ejercicio_id);
                pstmt.executeUpdate();
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error al insertar rutina_ejercicio: " + e.getMessage());
            return false;
        }
    }

    public List<Map<String, Object>> obtenerTodos() {
        // Hacemos JOIN múltiple para traer toda la información
        String sql = "SELECT re.id, re.dia_rutina, r.nombre AS rutina_nombre, c.nombre AS cliente_nombre, " +
                     "e.nombre AS ejercicio_nombre, e.repeticion, e.duracion, e.imagen_url " +
                     "FROM rutina_ejercicio re " +
                     "JOIN rutina r ON re.rutina_id = r.id " +
                     "JOIN cliente c ON r.cliente_id = c.id " +
                     "JOIN ejercicio e ON re.ejercicio_id = e.id " +
                     "ORDER BY CASE re.dia_rutina " +
                     "  WHEN 'Lunes' THEN 1 " +
                     "  WHEN 'Martes' THEN 2 " +
                     "  WHEN 'Miércoles' THEN 3 " +
                     "  WHEN 'Jueves' THEN 4 " +
                     "  WHEN 'Viernes' THEN 5 " +
                     "  WHEN 'Sábado' THEN 6 " +
                     "  WHEN 'Domingo' THEN 7 " +
                     "  ELSE 8 END ASC, re.id DESC";
                     
        List<Map<String, Object>> lista = new ArrayList<>();
        Conexion con = new Conexion();
        
        try (Connection conn = con.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("id", rs.getInt("id"));
                fila.put("dia_rutina", rs.getString("dia_rutina"));
                fila.put("rutina_nombre", rs.getString("rutina_nombre"));
                fila.put("cliente_nombre", rs.getString("cliente_nombre"));
                fila.put("ejercicio_nombre", rs.getString("ejercicio_nombre"));
                fila.put("repeticion", rs.getString("repeticion"));
                fila.put("duracion", rs.getString("duracion"));
                fila.put("imagen_url", rs.getString("imagen_url"));
                lista.add(fila);
            }
        } catch (Exception e) {
            System.out.println("Error al obtener asignaciones: " + e.getMessage());
        }
        return lista;
    }

    public List<Map<String, Object>> obtenerPorRutina(int rutina_id) {
        String sql = "SELECT re.id, re.dia_rutina, r.nombre AS rutina_nombre, c.nombre AS cliente_nombre, " +
                     "e.nombre AS ejercicio_nombre, e.repeticion, e.duracion, e.imagen_url " +
                     "FROM rutina_ejercicio re " +
                     "JOIN rutina r ON re.rutina_id = r.id " +
                     "JOIN cliente c ON r.cliente_id = c.id " +
                     "JOIN ejercicio e ON re.ejercicio_id = e.id " +
                     "WHERE re.rutina_id = ? " +
                     "ORDER BY CASE re.dia_rutina " +
                     "  WHEN 'Lunes' THEN 1 " +
                     "  WHEN 'Martes' THEN 2 " +
                     "  WHEN 'Miércoles' THEN 3 " +
                     "  WHEN 'Jueves' THEN 4 " +
                     "  WHEN 'Viernes' THEN 5 " +
                     "  WHEN 'Sábado' THEN 6 " +
                     "  WHEN 'Domingo' THEN 7 " +
                     "  ELSE 8 END ASC, re.id DESC";
                     
        List<Map<String, Object>> lista = new ArrayList<>();
        Conexion con = new Conexion();
        
        try (Connection conn = con.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, rutina_id);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> fila = new HashMap<>();
                    fila.put("id", rs.getInt("id"));
                    fila.put("dia_rutina", rs.getString("dia_rutina"));
                    fila.put("rutina_nombre", rs.getString("rutina_nombre"));
                    fila.put("cliente_nombre", rs.getString("cliente_nombre"));
                    fila.put("ejercicio_nombre", rs.getString("ejercicio_nombre"));
                    fila.put("repeticion", rs.getString("repeticion"));
                    fila.put("duracion", rs.getString("duracion"));
                    fila.put("imagen_url", rs.getString("imagen_url"));
                    lista.add(fila);
                }
            }
        } catch (Exception e) {
            System.out.println("Error al obtener asignaciones por rutina: " + e.getMessage());
        }
        return lista;
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM rutina_ejercicio WHERE id = ?";
        Conexion con = new Conexion();
        try (Connection conn = con.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error al eliminar asignacion: " + e.getMessage());
            return false;
        }
    }
}
