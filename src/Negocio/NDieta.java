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

    public String modificarDieta(String idStr, String titulo, String descripcion) {
        if (idStr == null || idStr.trim().isEmpty()) {
            return "Error: ID no proporcionado.";
        }
        if (titulo == null || titulo.trim().isEmpty() || 
            descripcion == null || descripcion.trim().isEmpty()) {
            return "Error: Título y descripción son obligatorios para modificar.";
        }
        
        try {
            int id = Integer.parseInt(idStr);
            boolean exito = dDieta.editar(id, titulo, descripcion);
            if (exito) {
                return "Éxito: Dieta modificada correctamente.";
            } else {
                return "Error: No se pudo modificar la dieta.";
            }
        } catch (NumberFormatException e) {
            return "Error: ID inválido.";
        }
    }

    public String eliminarDieta(String idStr) {
        if (idStr == null || idStr.trim().isEmpty()) {
            return "Error: ID no proporcionado.";
        }
        try {
            int id = Integer.parseInt(idStr);
            boolean exito = dDieta.eliminar(id);
            if (exito) {
                return "Éxito: Dieta eliminada correctamente.";
            } else {
                return "Error: No se pudo eliminar la dieta. Es posible que esté asignada a alguna rutina.";
            }
        } catch (NumberFormatException e) {
            return "Error: ID inválido.";
        }
    }
}
