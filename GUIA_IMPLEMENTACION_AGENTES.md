# Guía de Implementación para Agentes de IA (Arquitectura de 3 Capas en Java Puro)

Este documento registra cronológicamente la construcción del sistema web "Personal Training". Está diseñado específicamente para que un Agente de IA entienda el contexto completo, la estructura arquitectónica y los errores críticos que deben evitarse al replicar o expandir este tipo de sistemas.

---

## 1. Fundamentos y Estructura Arquitectónica
El sistema se construyó bajo el patrón estricto de **3 Capas (Presentación, Negocio y Datos)**, sin usar ningún framework (ni Spring, ni Hibernate, ni frameworks de frontend).

**Estructura de Paquetes:**
- `Database/`: Contiene el script SQL y la clase `Conexion.java` para conectarse a PostgreSQL mediante JDBC (`postgresql.jar`).
- `Datos/`: Contiene clases `D[Entidad].java` responsables de ejecutar consultas SQL (CRUD).
- `Negocio/`: Contiene clases `N[Entidad].java` responsables de la lógica de validación. Instancian a la capa de Datos.
- `Presentacion/`: Contiene `Main.java` y `WebServer.java` (usando `com.sun.net.httpserver.HttpServer`). 
  - `Presentacion/views/`: Directorio donde residen los archivos `.html` con CSS puro.

---

## 2. Fase de Base de Datos y Conexión
1. **Migración:** Inicialmente se intentó usar SQLite, pero se migró a **PostgreSQL** por escalabilidad y soporte de llaves foráneas robustas.
2. **Script:** Se creó `script_gym.sql` con 5 tablas: `cliente`, `dieta`, `ejercicio`, `rutina` y la tabla pivote `rutina_ejercicio`.
3. **Conexión:** Se implementó `Conexion.java` devolviendo un objeto `java.sql.Connection` instanciado, no estático.

---

## 3. Cronología de Implementación de Casos de Uso (CU)

### CU1: Gestionar Cliente
- **Datos:** `DCliente` guarda `(nombre, peso, altura, sexo, correo, telefono)`.
- **Negocio:** `NCliente` valida que ningún campo esté vacío.
- **Presentación:** Se creó `clientes.html`. `WebServer.java` levanta en el puerto 8080, maneja peticiones `POST` para capturar el formulario e inyecta la lista de clientes en la tabla HTML reemplazando placeholders como `{{CLIENTES_TABLE_BODY}}`.

### CU2: Gestionar Dietas
- **Evolución UI:** Se añadió una barra de navegación (`<nav class="navbar">`) global para permitir el salto entre módulos.
- Se enrutó `/dietas` creando el `DietaHandler` en el servidor web.
- Se implementó la inserción y visualización de dietas (`titulo`, `descripcion`).
- Se redirigió automáticamente la raíz (`/`) a `/clientes`.

### CU3: Gestionar Ejercicios
- Implementación estándar de CRUD para Ejercicios.
- Se hizo opcional la inserción de una `imagen_url`, agregando lógica condicional en Java para inyectar una "Imagen por defecto" en el HTML si el campo estaba vacío, sin romper el diseño CSS.

### CU4: Gestionar Rutinas (Lógica Relacional)
- **Reto:** La tabla `rutina` tiene llaves foráneas hacia `cliente` y `dieta`.
- **Solución en Presentación:** En `rutinas.html`, en lugar de inputs de texto, se utilizaron menús desplegables `<select>`. `RutinaHandler` inyecta las opciones (`<option>`) consultando las listas de Clientes y Dietas a través de las capas de negocio correspondientes.
- **Solución en Datos:** Se usó un `JOIN` para obtener los nombres reales del cliente y la dieta al armar la tabla visible al usuario final.

### CU5: Asociar Ejercicios a Rutina (Tabla Pivote)
- **Implementación Final:** La interfaz `asignaciones.html` cruza los datos finales. El usuario selecciona una Rutina y un Ejercicio.
- `DRutinaEjercicio` implementa un `JOIN` cuádruple (`rutina_ejercicio`, `rutina`, `cliente`, `ejercicio`) para poblar una tabla de visualización global que condensa toda la información del ecosistema.

---

## 4. 🚨 ERRORES CRÍTICOS Y LECCIONES APRENDIDAS (Para Agentes de IA)

A continuación, los problemas más graves que se enfrentaron y que el Agente debe tener en cuenta para futuras depuraciones:

### Error 1: Fallos Silenciosos del `HttpServer` (Conexión Inesperadamente Cerrada)
- **Problema:** Si el código dentro de un `HttpHandler` (ej. `ClienteHandler.handle()`) lanza una excepción de ejecución (`RuntimeException`, `NullPointerException`, etc.) que no está envuelta en un bloque `try-catch`, **el servidor Java corta la conexión abruptamente** sin enviar código HTTP 500 y **sin imprimir el error en la consola**. El navegador simplemente muestra "Conexión terminada".
- **Solución Obligatoria:** Todo método `handle` debe envolver su código interno en un `try { ... } catch (Throwable t) { t.printStackTrace(); throw new IOException(t); }` para asegurar que el stacktrace sea visible en el registro del sistema.

### Error 2: Caché de Archivos `.class` (NoSuchMethodError)
- **Problema:** Al refactorizar la firma de métodos críticos (ej. cambiar `Conexion.conectar()` por `Conexion.getConnection()`), la invocación del comando `javac` a veces no recompila los archivos dependientes si sus fechas de modificación no lo fuerzan, dejando binarios antiguos (`.class`) incompatibles.
- **Solución Obligatoria:** Si el servidor arroja `NoSuchMethodError` u ocurre un comportamiento errático tras refactorizar código central, el Agente debe ejecutar un borrado forzado de los compilados (`rm *.class` o análogo en Windows) y ejecutar una compilación limpia de todo el proyecto.

### Error 3: Rutas Absolutas vs Navegación Local
- **Problema:** Los botones de la barra de navegación utilizan rutas absolutas (`<a href="/dietas">`). Si el usuario abre el archivo `.html` haciendo doble clic (URL tipo `file:///`), la navegación y el envío de formularios se romperán totalmente.
- **Solución:** Siempre instruir al usuario que acceda a la aplicación tipeando explícitamente `http://localhost:8080/` en su navegador para que el servidor Java intercepte y enrute las peticiones correctamente.

---
*Este documento sirve como contexto primario para que cualquier agente de codificación retome o extienda el proyecto sin cometer los errores del pasado.*
