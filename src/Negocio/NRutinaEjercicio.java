package Negocio;

import Datos.DRutinaEjercicio;
import java.util.List;
import java.util.Map;

public class NRutinaEjercicio {
    private DRutinaEjercicio dRutinaEjercicio;

    public NRutinaEjercicio() {
        this.dRutinaEjercicio = new DRutinaEjercicio();
    }

    public String registrarAsignacion(List<String> diasRutina, String rutinaIdStr, String ejercicioIdStr) {
        if (diasRutina == null || diasRutina.isEmpty() ||
            rutinaIdStr == null || rutinaIdStr.trim().isEmpty() ||
            ejercicioIdStr == null || ejercicioIdStr.trim().isEmpty()) {
            return "Error: Todos los campos son obligatorios (incluyendo al menos un día).";
        }

        try {
            int rutina_id = Integer.parseInt(rutinaIdStr);
            int ejercicio_id = Integer.parseInt(ejercicioIdStr);

            boolean exito = dRutinaEjercicio.insertar(diasRutina, rutina_id, ejercicio_id);
            if (exito) {
                return "Éxito: Ejercicio asignado a la rutina correctamente.";
            } else {
                return "Error: No se pudo asignar el ejercicio.";
            }
        } catch (NumberFormatException e) {
            return "Error: IDs inválidos.";
        }
    }

    public List<Map<String, Object>> obtenerAsignaciones() {
        return dRutinaEjercicio.obtenerTodos();
    }

    public List<Map<String, Object>> obtenerAsignacionesPorRutina(int rutina_id) {
        return dRutinaEjercicio.obtenerPorRutina(rutina_id);
    }

    public String eliminarAsignacion(String idStr) {
        if (idStr == null || idStr.trim().isEmpty()) {
            return "Error: ID no proporcionado.";
        }
        try {
            int id = Integer.parseInt(idStr);
            boolean exito = dRutinaEjercicio.eliminar(id);
            if (exito) {
                return "Éxito: Asignación eliminada correctamente.";
            } else {
                return "Error: No se pudo eliminar la asignación.";
            }
        } catch (NumberFormatException e) {
            return "Error: ID inválido.";
        }
    }
}
