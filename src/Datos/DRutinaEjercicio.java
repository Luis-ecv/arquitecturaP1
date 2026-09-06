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

    public boolean insertar(int rutina_id, int ejercicio_id) {
        String sql = "INSERT INTO rutina_ejercicio(rutina_id, ejercicio_id) VALUES(?,?)";
        
        Conexion con = new Conexion();
        try (Connection conn = con.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, rutina_id);
            pstmt.setInt(2, ejercicio_id);
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error al insertar rutina_ejercicio: " + e.getMessage());
            return false;
        }
    }

    public List<Map<String, Object>> obtenerTodos() {
        // Hacemos JOIN múltiple para traer toda la información
        String sql = "SELECT re.id, r.nombre AS rutina_nombre, c.nombre AS cliente_nombre, " +
                     "e.nombre AS ejercicio_nombre, e.repeticion, e.duracion, e.imagen_url " +
                     "FROM rutina_ejercicio re " +
                     "JOIN rutina r ON re.rutina_id = r.id " +
                     "JOIN cliente c ON r.cliente_id = c.id " +
                     "JOIN ejercicio e ON re.ejercicio_id = e.id " +
                     "ORDER BY re.id DESC";
                     
        List<Map<String, Object>> lista = new ArrayList<>();
        Conexion con = new Conexion();
        
        try (Connection conn = con.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("id", rs.getInt("id"));
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
}
