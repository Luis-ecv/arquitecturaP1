package Datos;

import Database.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DCliente {

    public boolean insertar(double altura, String correo, String nombre, double peso, String sexo, String telefono) {
        String sql = "INSERT INTO cliente(altura, correo, nombre, peso, sexo, telefono) VALUES(?,?,?,?,?,?)";
        
        // Instanciamos Conexion como en el proyecto de referencia
        Conexion con = new Conexion();
        
        try (Connection conn = con.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, altura);
            pstmt.setString(2, correo);
            pstmt.setString(3, nombre);
            pstmt.setDouble(4, peso);
            pstmt.setString(5, sexo);
            pstmt.setString(6, telefono);
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error al insertar cliente: " + e.getMessage());
            return false;
        }
    }

    public List<Map<String, Object>> obtenerTodos() {
        String sql = "SELECT * FROM cliente";
        List<Map<String, Object>> lista = new ArrayList<>();
        
        Conexion con = new Conexion();
        
        try (Connection conn = con.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("id", rs.getInt("id"));
                fila.put("nombre", rs.getString("nombre"));
                fila.put("altura", rs.getDouble("altura"));
                fila.put("peso", rs.getDouble("peso"));
                fila.put("sexo", rs.getString("sexo"));
                fila.put("correo", rs.getString("correo"));
                fila.put("telefono", rs.getString("telefono"));
                lista.add(fila);
            }
        } catch (Exception e) {
            System.out.println("Error al obtener clientes: " + e.getMessage());
        }
        return lista;
    }
}
