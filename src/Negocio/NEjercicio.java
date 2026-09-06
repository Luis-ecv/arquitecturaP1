package Negocio;

import Datos.DEjercicio;
import java.util.List;
import java.util.Map;

public class NEjercicio {
    private DEjercicio dEjercicio;

    public NEjercicio() {
        this.dEjercicio = new DEjercicio();
    }

    public String registrarEjercicio(String nombre, String duracion, String repeticion, String imagen_url) {
        if (nombre == null || nombre.trim().isEmpty() || 
            duracion == null || duracion.trim().isEmpty() ||
            repeticion == null || repeticion.trim().isEmpty()) {
            return "Error: Nombre, duración y repetición son obligatorios.";
        }

        // Permitimos que imagen_url sea nulo/vacio
        if (imagen_url == null) {
            imagen_url = "";
        }

        boolean exito = dEjercicio.insertar(nombre, duracion, repeticion, imagen_url);
        if (exito) {
            return "Éxito: Ejercicio registrado correctamente.";
        } else {
            return "Error: No se pudo registrar el ejercicio en la base de datos.";
        }
    }

    public List<Map<String, Object>> obtenerEjercicios() {
        return dEjercicio.obtenerTodos();
    }
}
