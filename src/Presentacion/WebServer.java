package Presentacion;

import Negocio.NCliente;
import Negocio.NDieta;
import Negocio.NEjercicio;
import Negocio.NRutina;
import Negocio.NRutinaEjercicio;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebServer {
    private NCliente nCliente;
    private NDieta nDieta;
    private NEjercicio nEjercicio;
    private NRutina nRutina;
    private NRutinaEjercicio nRutinaEjercicio;

    public WebServer() {
        this.nCliente = new NCliente();
        this.nDieta = new NDieta();
        this.nEjercicio = new NEjercicio();
        this.nRutina = new NRutina();
        this.nRutinaEjercicio = new NRutinaEjercicio();
    }

    public void iniciarServidor(int puerto) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(puerto), 0);
            
            // Enrutamiento de Módulos
            server.createContext("/", new DefaultHandler()); // Redirige a clientes
            server.createContext("/clientes", new ClienteHandler());
            server.createContext("/dietas", new DietaHandler());
            server.createContext("/ejercicios", new EjercicioHandler());
            server.createContext("/rutinas", new RutinaHandler());
            server.createContext("/asignaciones", new AsignacionHandler());
            
            server.setExecutor(null);
            server.start();
            System.out.println("Servidor web iniciado correctamente en http://localhost:" + puerto + "/");
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor web: " + e.getMessage());
        }
    }

    class DefaultHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Location", "/clientes");
            exchange.sendResponseHeaders(302, -1);
            exchange.close();
        }
    }

    class ClienteHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                String method = exchange.getRequestMethod();
                String mensajeResult = "";
                String tipoMensaje = "none";
                String displayMensaje = "none";

                if ("POST".equalsIgnoreCase(method)) {
                    byte[] requestBody = exchange.getRequestBody().readAllBytes();
                    String formData = new String(requestBody, "UTF-8");
                    Map<String, String> params = parseFormData(formData);

                    if ("crear".equals(params.get("action"))) {
                        mensajeResult = nCliente.registrarCliente(
                                params.get("altura"),
                                params.get("correo"),
                                params.get("nombre"),
                                params.get("peso"),
                                params.get("sexo"),
                                params.get("telefono")
                        );
                        displayMensaje = "block";
                        tipoMensaje = mensajeResult.startsWith("Éxito") ? "success" : "error";
                    }
                }
                enviarVista(exchange, mensajeResult, tipoMensaje, displayMensaje);
            } catch (Throwable t) {
                t.printStackTrace();
                throw new IOException(t);
            }
        }

        private void enviarVista(HttpExchange exchange, String mensaje, String tipo, String display) throws IOException {
            File file = new File("src/Presentacion/views/clientes.html");
            if (!file.exists()) {
                String err = "Plantilla no encontrada.";
                exchange.sendResponseHeaders(404, err.length());
                exchange.getResponseBody().write(err.getBytes());
                exchange.getResponseBody().close();
                return;
            }

            String html = new String(Files.readAllBytes(file.toPath()), "UTF-8");
            List<Map<String, Object>> clientes = nCliente.obtenerClientes();
            StringBuilder tableBody = new StringBuilder();
            if (clientes == null || clientes.isEmpty()) {
                tableBody.append("<tr><td colspan='5' style='text-align:center;'>No hay clientes registrados.</td></tr>");
            } else {
                for (Map<String, Object> c : clientes) {
                    tableBody.append("<tr>")
                             .append("<td>").append(c.get("nombre")).append("</td>")
                             .append("<td>").append(c.get("peso")).append(" kg</td>")
                             .append("<td>").append(c.get("altura")).append(" m</td>")
                             .append("<td>").append(c.get("sexo")).append("</td>")
                             .append("<td>").append(c.get("correo")).append("<br>").append(c.get("telefono")).append("</td>")
                             .append("</tr>");
                }
            }

            html = html.replace("{{CLIENTES_TABLE_BODY}}", tableBody.toString());
            html = html.replace("{{MENSAJE}}", mensaje);
            html = html.replace("{{TIPO_MENSAJE}}", tipo);
            html = html.replace("{{DISPLAY_MENSAJE}}", display);

            byte[] responseBytes = html.getBytes("UTF-8");
            exchange.sendResponseHeaders(200, responseBytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(responseBytes);
            os.close();
        }
    }

    class DietaHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                String method = exchange.getRequestMethod();
                String mensajeResult = "";
                String tipoMensaje = "none";
                String displayMensaje = "none";

                if ("POST".equalsIgnoreCase(method)) {
                    byte[] requestBody = exchange.getRequestBody().readAllBytes();
                    String formData = new String(requestBody, "UTF-8");
                    Map<String, String> params = parseFormData(formData);

                    if ("crear".equals(params.get("action"))) {
                        mensajeResult = nDieta.registrarDieta(
                                params.get("titulo"),
                                params.get("descripcion")
                        );
                        displayMensaje = "block";
                        tipoMensaje = mensajeResult.startsWith("Éxito") ? "success" : "error";
                    }
                }
                enviarVista(exchange, mensajeResult, tipoMensaje, displayMensaje);
            } catch (Throwable t) {
                t.printStackTrace();
                throw new IOException(t);
            }
        }

        private void enviarVista(HttpExchange exchange, String mensaje, String tipo, String display) throws IOException {
            File file = new File("src/Presentacion/views/dietas.html");
            if (!file.exists()) {
                String err = "Plantilla no encontrada.";
                exchange.sendResponseHeaders(404, err.length());
                exchange.getResponseBody().write(err.getBytes());
                exchange.getResponseBody().close();
                return;
            }

            String html = new String(Files.readAllBytes(file.toPath()), "UTF-8");
            List<Map<String, Object>> dietas = nDieta.obtenerDietas();
            StringBuilder tableBody = new StringBuilder();
            if (dietas == null || dietas.isEmpty()) {
                tableBody.append("<tr><td colspan='2' style='text-align:center;'>No hay dietas registradas.</td></tr>");
            } else {
                for (Map<String, Object> d : dietas) {
                    tableBody.append("<tr>")
                             .append("<td><strong>").append(d.get("titulo")).append("</strong></td>")
                             .append("<td>").append(d.get("descripcion")).append("</td>")
                             .append("</tr>");
                }
            }

            html = html.replace("{{DIETAS_TABLE_BODY}}", tableBody.toString());
            html = html.replace("{{MENSAJE}}", mensaje);
            html = html.replace("{{TIPO_MENSAJE}}", tipo);
            html = html.replace("{{DISPLAY_MENSAJE}}", display);

            byte[] responseBytes = html.getBytes("UTF-8");
            exchange.sendResponseHeaders(200, responseBytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(responseBytes);
            os.close();
        }
    }

    class EjercicioHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                String method = exchange.getRequestMethod();
                String mensajeResult = "";
                String tipoMensaje = "none";
                String displayMensaje = "none";

                if ("POST".equalsIgnoreCase(method)) {
                    byte[] requestBody = exchange.getRequestBody().readAllBytes();
                    String formData = new String(requestBody, "UTF-8");
                    Map<String, String> params = parseFormData(formData);

                    if ("crear".equals(params.get("action"))) {
                        mensajeResult = nEjercicio.registrarEjercicio(
                                params.get("nombre"),
                                params.get("duracion"),
                                params.get("repeticion"),
                                params.get("imagen_url")
                        );
                        displayMensaje = "block";
                        tipoMensaje = mensajeResult.startsWith("Éxito") ? "success" : "error";
                    }
                }
                enviarVista(exchange, mensajeResult, tipoMensaje, displayMensaje);
            } catch (Throwable t) {
                t.printStackTrace();
                throw new IOException(t);
            }
        }

        private void enviarVista(HttpExchange exchange, String mensaje, String tipo, String display) throws IOException {
            File file = new File("src/Presentacion/views/ejercicios.html");
            if (!file.exists()) {
                String err = "Plantilla no encontrada.";
                exchange.sendResponseHeaders(404, err.length());
                exchange.getResponseBody().write(err.getBytes());
                exchange.getResponseBody().close();
                return;
            }

            String html = new String(Files.readAllBytes(file.toPath()), "UTF-8");
            List<Map<String, Object>> ejercicios = nEjercicio.obtenerEjercicios();
            StringBuilder tableBody = new StringBuilder();
            
            if (ejercicios == null || ejercicios.isEmpty()) {
                tableBody.append("<tr><td colspan='4' style='text-align:center;'>No hay ejercicios registrados.</td></tr>");
            } else {
                for (Map<String, Object> e : ejercicios) {
                    String imgUrl = (String) e.get("imagen_url");
                    String imgHtml = (imgUrl != null && !imgUrl.trim().isEmpty()) 
                        ? "<img src='" + imgUrl + "' class='img-preview' alt=''>"
                        : "<div class='img-preview' style='background:#ccc; display:flex; align-items:center; justify-content:center; font-size:0.7em;'>Sin Img</div>";
                    
                    tableBody.append("<tr>")
                             .append("<td>").append(imgHtml).append("</td>")
                             .append("<td><strong>").append(e.get("nombre")).append("</strong></td>")
                             .append("<td>").append(e.get("repeticion")).append("</td>")
                             .append("<td>").append(e.get("duracion")).append("</td>")
                             .append("</tr>");
                }
            }

            html = html.replace("{{EJERCICIOS_TABLE_BODY}}", tableBody.toString());
            html = html.replace("{{MENSAJE}}", mensaje);
            html = html.replace("{{TIPO_MENSAJE}}", tipo);
            html = html.replace("{{DISPLAY_MENSAJE}}", display);

            byte[] responseBytes = html.getBytes("UTF-8");
            exchange.sendResponseHeaders(200, responseBytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(responseBytes);
            os.close();
        }
    }

    class RutinaHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                String method = exchange.getRequestMethod();
                String mensajeResult = "";
                String tipoMensaje = "none";
                String displayMensaje = "none";

                if ("POST".equalsIgnoreCase(method)) {
                    byte[] requestBody = exchange.getRequestBody().readAllBytes();
                    String formData = new String(requestBody, "UTF-8");
                    Map<String, String> params = parseFormData(formData);

                    if ("crear".equals(params.get("action"))) {
                        mensajeResult = nRutina.registrarRutina(
                                params.get("nombre"),
                                params.get("tipo"),
                                params.get("cliente_id"),
                                params.get("dieta_id")
                        );
                        displayMensaje = "block";
                        tipoMensaje = mensajeResult.startsWith("Éxito") ? "success" : "error";
                    }
                }
                enviarVista(exchange, mensajeResult, tipoMensaje, displayMensaje);
            } catch (Throwable t) {
                t.printStackTrace();
                throw new IOException(t);
            }
        }

        private void enviarVista(HttpExchange exchange, String mensaje, String tipo, String display) throws IOException {
            File file = new File("src/Presentacion/views/rutinas.html");
            if (!file.exists()) {
                String err = "Plantilla no encontrada.";
                exchange.sendResponseHeaders(404, err.length());
                exchange.getResponseBody().write(err.getBytes());
                exchange.getResponseBody().close();
                return;
            }

            String html = new String(Files.readAllBytes(file.toPath()), "UTF-8");
            
            // Inyectar Clientes
            List<Map<String, Object>> clientes = nCliente.obtenerClientes();
            StringBuilder clientesOptions = new StringBuilder();
            if (clientes != null) {
                for (Map<String, Object> c : clientes) {
                    clientesOptions.append("<option value='").append(c.get("id")).append("'>")
                                   .append(c.get("nombre"))
                                   .append("</option>");
                }
            }
            html = html.replace("{{CLIENTES_OPTIONS}}", clientesOptions.toString());

            // Inyectar Dietas
            List<Map<String, Object>> dietas = nDieta.obtenerDietas();
            StringBuilder dietasOptions = new StringBuilder();
            if (dietas != null) {
                for (Map<String, Object> d : dietas) {
                    dietasOptions.append("<option value='").append(d.get("id")).append("'>")
                                 .append(d.get("titulo"))
                                 .append("</option>");
                }
            }
            html = html.replace("{{DIETAS_OPTIONS}}", dietasOptions.toString());

            // Tabla
            List<Map<String, Object>> rutinas = nRutina.obtenerRutinas();
            StringBuilder tableBody = new StringBuilder();
            if (rutinas == null || rutinas.isEmpty()) {
                tableBody.append("<tr><td colspan='4' style='text-align:center;'>No hay rutinas registradas.</td></tr>");
            } else {
                for (Map<String, Object> r : rutinas) {
                    tableBody.append("<tr>")
                             .append("<td><strong>").append(r.get("cliente_nombre")).append("</strong></td>")
                             .append("<td>").append(r.get("nombre")).append("</td>")
                             .append("<td><span class='badge'>").append(r.get("tipo")).append("</span></td>")
                             .append("<td><span class='badge badge-dieta'>").append(r.get("dieta_titulo")).append("</span></td>")
                             .append("</tr>");
                }
            }

            html = html.replace("{{RUTINAS_TABLE_BODY}}", tableBody.toString());
            html = html.replace("{{MENSAJE}}", mensaje);
            html = html.replace("{{TIPO_MENSAJE}}", tipo);
            html = html.replace("{{DISPLAY_MENSAJE}}", display);

            byte[] responseBytes = html.getBytes("UTF-8");
            exchange.sendResponseHeaders(200, responseBytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(responseBytes);
            os.close();
        }
    }

    class AsignacionHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                String method = exchange.getRequestMethod();
                String mensajeResult = "";
                String tipoMensaje = "none";
                String displayMensaje = "none";

                if ("POST".equalsIgnoreCase(method)) {
                    byte[] requestBody = exchange.getRequestBody().readAllBytes();
                    String formData = new String(requestBody, "UTF-8");
                    Map<String, String> params = parseFormData(formData);

                    if ("crear".equals(params.get("action"))) {
                        mensajeResult = nRutinaEjercicio.registrarAsignacion(
                                params.get("rutina_id"),
                                params.get("ejercicio_id")
                        );
                        displayMensaje = "block";
                        tipoMensaje = mensajeResult.startsWith("Éxito") ? "success" : "error";
                    }
                }
                enviarVista(exchange, mensajeResult, tipoMensaje, displayMensaje);
            } catch (Throwable t) {
                t.printStackTrace();
                throw new IOException(t);
            }
        }

        private void enviarVista(HttpExchange exchange, String mensaje, String tipo, String display) throws IOException {
            File file = new File("src/Presentacion/views/asignaciones.html");
            if (!file.exists()) {
                String err = "Plantilla no encontrada.";
                exchange.sendResponseHeaders(404, err.length());
                exchange.getResponseBody().write(err.getBytes());
                exchange.getResponseBody().close();
                return;
            }

            String html = new String(Files.readAllBytes(file.toPath()), "UTF-8");
            
            // Inyectar Rutinas
            List<Map<String, Object>> rutinas = nRutina.obtenerRutinas();
            StringBuilder rutinasOptions = new StringBuilder();
            if (rutinas != null) {
                for (Map<String, Object> r : rutinas) {
                    rutinasOptions.append("<option value='").append(r.get("id")).append("'>")
                                  .append(r.get("nombre")).append(" (").append(r.get("cliente_nombre")).append(")")
                                  .append("</option>");
                }
            }
            html = html.replace("{{RUTINAS_OPTIONS}}", rutinasOptions.toString());

            // Inyectar Ejercicios
            List<Map<String, Object>> ejercicios = nEjercicio.obtenerEjercicios();
            StringBuilder ejerciciosOptions = new StringBuilder();
            if (ejercicios != null) {
                for (Map<String, Object> e : ejercicios) {
                    ejerciciosOptions.append("<option value='").append(e.get("id")).append("'>")
                                     .append(e.get("nombre"))
                                     .append("</option>");
                }
            }
            html = html.replace("{{EJERCICIOS_OPTIONS}}", ejerciciosOptions.toString());

            // Tabla de Asignaciones
            List<Map<String, Object>> asignaciones = nRutinaEjercicio.obtenerAsignaciones();
            StringBuilder tableBody = new StringBuilder();
            if (asignaciones == null || asignaciones.isEmpty()) {
                tableBody.append("<tr><td colspan='5' style='text-align:center;'>No hay ejercicios asignados.</td></tr>");
            } else {
                for (Map<String, Object> a : asignaciones) {
                    String imgUrl = (String) a.get("imagen_url");
                    String imgHtml = (imgUrl != null && !imgUrl.trim().isEmpty()) 
                        ? "<img src='" + imgUrl + "' class='img-preview' alt=''>"
                        : "<div class='img-preview' style='background:#ccc; display:flex; align-items:center; justify-content:center; font-size:0.6em;'>Sin Img</div>";

                    tableBody.append("<tr>")
                             .append("<td><strong>").append(a.get("rutina_nombre")).append("</strong><br><small>").append(a.get("cliente_nombre")).append("</small></td>")
                             .append("<td>").append(imgHtml).append("</td>")
                             .append("<td>").append(a.get("ejercicio_nombre")).append("</td>")
                             .append("<td>").append(a.get("repeticion")).append("</td>")
                             .append("<td>").append(a.get("duracion")).append("</td>")
                             .append("</tr>");
                }
            }

            html = html.replace("{{ASIGNACIONES_TABLE_BODY}}", tableBody.toString());
            html = html.replace("{{MENSAJE}}", mensaje);
            html = html.replace("{{TIPO_MENSAJE}}", tipo);
            html = html.replace("{{DISPLAY_MENSAJE}}", display);

            byte[] responseBytes = html.getBytes("UTF-8");
            exchange.sendResponseHeaders(200, responseBytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(responseBytes);
            os.close();
        }
    }

    private Map<String, String> parseFormData(String formData) throws java.io.UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        String[] pairs = formData.split("&");
        for (String pair : pairs) {
            String[] split = pair.split("=");
            if (split.length == 2) {
                map.put(URLDecoder.decode(split[0], "UTF-8"), URLDecoder.decode(split[1], "UTF-8"));
            } else if (split.length == 1) {
                map.put(URLDecoder.decode(split[0], "UTF-8"), "");
            }
        }
        return map;
    }
}
