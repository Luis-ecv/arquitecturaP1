package Presentacion;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando aplicación Personal Training (3 Capas - PostgreSQL)...");

        // Iniciar el Servidor Web
        WebServer servidor = new WebServer();
        servidor.iniciarServidor(8080);
    }
}
