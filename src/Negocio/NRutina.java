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

    public String modificarRutina(String idStr, String nombre, String tipo, String clienteIdStr, String dietaIdStr) {
        if (idStr == null || idStr.trim().isEmpty()) {
            return "Error: ID no proporcionado.";
        }
        if (nombre == null || nombre.trim().isEmpty() || 
            tipo == null || tipo.trim().isEmpty() ||
            clienteIdStr == null || clienteIdStr.trim().isEmpty() ||
            dietaIdStr == null || dietaIdStr.trim().isEmpty()) {
            return "Error: Todos los campos son obligatorios para modificar.";
        }

        try {
            int id = Integer.parseInt(idStr);
            int clienteId = Integer.parseInt(clienteIdStr);
            int dietaId = Integer.parseInt(dietaIdStr);

            boolean exito = dRutina.editar(id, nombre, tipo, clienteId, dietaId);
            if (exito) {
                return "Éxito: Rutina modificada correctamente.";
            } else {
                return "Error: No se pudo modificar la rutina.";
            }
        } catch (NumberFormatException e) {
            return "Error: ID de rutina, cliente o dieta deben ser numéricos.";
        }
    }

    public String eliminarRutina(String idStr) {
        if (idStr == null || idStr.trim().isEmpty()) {
            return "Error: ID no proporcionado.";
        }
        try {
            int id = Integer.parseInt(idStr);
            boolean exito = dRutina.eliminar(id);
            if (exito) {
                return "Éxito: Rutina eliminada correctamente.";
            } else {
                return "Error: No se pudo eliminar la rutina. Es posible que tenga ejercicios asignados.";
            }
        } catch (NumberFormatException e) {
            return "Error: ID inválido.";
        }
    }
}
