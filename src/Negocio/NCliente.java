package Negocio;

import Datos.DCliente;
import java.util.List;
import java.util.Map;

public class NCliente {
    private DCliente dCliente;

    public NCliente() {
        this.dCliente = new DCliente();
    }

    public String registrarCliente(String alturaStr, String correo, String nombre, String pesoStr, String sexo, String telefono) {
        // Validación de entradas
        if (nombre == null || nombre.trim().isEmpty() ||
            correo == null || correo.trim().isEmpty() ||
            telefono == null || telefono.trim().isEmpty() ||
            sexo == null || sexo.trim().isEmpty()) {
            return "Error: Ningún campo de texto puede estar vacío.";
        }

        double altura, peso;
        try {
            altura = Double.parseDouble(alturaStr);
            peso = Double.parseDouble(pesoStr);
        } catch (NumberFormatException e) {
            return "Error: Altura y peso deben ser números válidos.";
        }

        if (altura <= 0 || peso <= 0) {
            return "Error: Altura y peso deben ser mayores a cero.";
        }

        // Llamada a la capa de datos
        boolean exito = dCliente.insertar(altura, correo, nombre, peso, sexo, telefono);
        if (exito) {
            return "Éxito: Cliente registrado correctamente.";
        } else {
            return "Error: No se pudo registrar el cliente en la base de datos.";
        }
    }

    public List<Map<String, Object>> obtenerClientes() {
        return dCliente.obtenerTodos();
    }
}
