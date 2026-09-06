package Datos;

import Database.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DEjercicio {

    public boolean insertar(String nombre, String duracion, String repeticion, String imagen_url) {
        String sql = "INSERT INTO ejercicio(nombre, duracion, repeticion, imagen_url) VALUES(?,?,?,?)";
        
        Conexion con = new Conexion();
        try (Connection conn = con.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, duracion);
            pstmt.setString(3, repeticion);
            pstmt.setString(4, imagen_url);
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error al insertar ejercicio: " + e.getMessage());
            return false;
        }
    }

    public List<Map<String, Object>> obtenerTodos() {
        String sql = "SELECT * FROM ejercicio ORDER BY id DESC";
        List<Map<String, Object>> lista = new ArrayList<>();
        
        Conexion con = new Conexion();
        try (Connection conn = con.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("id", rs.getInt("id"));
                fila.put("nombre", rs.getString("nombre"));
                fila.put("duracion", rs.getString("duracion"));
                fila.put("repeticion", rs.getString("repeticion"));
                fila.put("imagen_url", rs.getString("imagen_url"));
                lista.add(fila);
            }
        } catch (Exception e) {
            System.out.println("Error al obtener ejercicios: " + e.getMessage());
        }
        return lista;
    }
}
