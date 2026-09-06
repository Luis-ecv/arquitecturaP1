package Database;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {
    Connection connection;

    public Connection getConnection() {
        String url = "jdbc:postgresql://localhost:5432/DB_gym";
        // Asumiendo credenciales por defecto de postgres,
        // puedes cambiarlas aquí si las tuyas son distintas.
        String user = "postgres";
        String password = "chingatamare";
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            System.err.println("Error al conectar db: " + e.getMessage());
        }
        return connection;
    }
}
