package Negocio;

import Datos.DRutinaEjercicio;
import java.util.List;
import java.util.Map;

public class NRutinaEjercicio {
    private DRutinaEjercicio dRutinaEjercicio;

    public NRutinaEjercicio() {
        this.dRutinaEjercicio = new DRutinaEjercicio();
    }

    public String registrarAsignacion(String rutinaIdStr, String ejercicioIdStr) {
        if (rutinaIdStr == null || rutinaIdStr.trim().isEmpty() ||
            ejercicioIdStr == null || ejercicioIdStr.trim().isEmpty()) {
            return "Error: Debe seleccionar una rutina y un ejercicio.";
        }

        try {
            int rutina_id = Integer.parseInt(rutinaIdStr);
            int ejercicio_id = Integer.parseInt(ejercicioIdStr);

            boolean exito = dRutinaEjercicio.insertar(rutina_id, ejercicio_id);
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
}
