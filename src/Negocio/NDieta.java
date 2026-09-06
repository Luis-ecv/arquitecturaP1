package Negocio;

import Datos.DDieta;
import java.util.List;
import java.util.Map;

public class NDieta {
    private DDieta dDieta;

    public NDieta() {
        this.dDieta = new DDieta();
    }

    public String registrarDieta(String titulo, String descripcion) {
        if (titulo == null || titulo.trim().isEmpty() || 
            descripcion == null || descripcion.trim().isEmpty()) {
            return "Error: El título y la descripción no pueden estar vacíos.";
        }

        boolean exito = dDieta.insertar(titulo, descripcion);
        if (exito) {
            return "Éxito: Dieta registrada correctamente.";
        } else {
            return "Error: No se pudo registrar la dieta en la base de datos.";
        }
    }

    public List<Map<String, Object>> obtenerDietas() {
        return dDieta.obtenerTodas();
    }
}
