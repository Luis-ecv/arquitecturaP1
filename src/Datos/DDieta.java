package Datos;

import Database.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DDieta {

    public boolean insertar(String titulo, String descripcion) {
        String sql = "INSERT INTO dieta(titulo, descripcion) VALUES(?,?)";
        
        Conexion con = new Conexion();
        try (Connection conn = con.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, titulo);
            pstmt.setString(2, descripcion);
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error al insertar dieta: " + e.getMessage());
            return false;
        }
    }

    public List<Map<String, Object>> obtenerTodas() {
        String sql = "SELECT * FROM dieta ORDER BY id DESC";
        List<Map<String, Object>> lista = new ArrayList<>();
        
        Conexion con = new Conexion();
        try (Connection conn = con.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("id", rs.getInt("id"));
                fila.put("titulo", rs.getString("titulo"));
                fila.put("descripcion", rs.getString("descripcion"));
                lista.add(fila);
            }
        } catch (Exception e) {
            System.out.println("Error al obtener dietas: " + e.getMessage());
        }
        return lista;
    }
}
