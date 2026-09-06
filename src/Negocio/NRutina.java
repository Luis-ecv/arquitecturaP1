package Negocio;

import Datos.DRutina;
import java.util.List;
import java.util.Map;

public class NRutina {
    private DRutina dRutina;

    public NRutina() {
        this.dRutina = new DRutina();
    }

    public String registrarRutina(String nombre, String tipo, String clienteIdStr, String dietaIdStr) {
        if (nombre == null || nombre.trim().isEmpty() || 
            tipo == null || tipo.trim().isEmpty() ||
            clienteIdStr == null || clienteIdStr.trim().isEmpty() ||
            dietaIdStr == null || dietaIdStr.trim().isEmpty()) {
            return "Error: Todos los campos son obligatorios.";
        }

        try {
            int cliente_id = Integer.parseInt(clienteIdStr);
            int dieta_id = Integer.parseInt(dietaIdStr);

            boolean exito = dRutina.insertar(nombre, tipo, cliente_id, dieta_id);
            if (exito) {
                return "Éxito: Rutina registrada correctamente.";
            } else {
                return "Error: No se pudo registrar la rutina en la base de datos.";
            }
        } catch (NumberFormatException e) {
            return "Error: ID de cliente o dieta inválido.";
        }
    }

    public List<Map<String, Object>> obtenerRutinas() {
        return dRutina.obtenerTodos();
    }
}
