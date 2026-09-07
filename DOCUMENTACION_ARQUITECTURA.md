# Arquitectura del Software: Patrón de 3 Niveles (Personal Training)

Este proyecto utiliza el patrón arquitectónico de tres capas (o tres niveles) desarrollado en Java puro sin el uso de frameworks, ofreciendo una aplicación web funcional de gestión deportiva.

La principal característica de este diseño es que los componentes de una capa sólo pueden hacer referencia a componentes en capas inmediatamente inferiores, garantizando la independencia del código y facilitando el mantenimiento.

## Estructura de Capas

El software se divide lógicamente en los siguientes paquetes principales:

### 1. Capa de Presentación (`Presentacion`)
Es la encargada de que el sistema interactúe con el usuario a través de una interfaz Web. 
- Contiene el servidor HTTP (`WebServer.java`) y los manejadores de ruta (`Handlers`).
- Intercepta los formularios POST de la web, extrae los datos y los envía a la Capa de Negocio.
- Las vistas (HTML y CSS puro) se encuentran en la carpeta `Presentacion/views`.

### 2. Capa de Negocio (`Negocio`)
También conocida como capa de lógica empresarial. Aquí residen las funciones y las validaciones de las reglas de negocio. 
- Valida que los datos ingresados no estén vacíos o corruptos antes de ser procesados.
- Se comunica hacia "arriba" con la capa de presentación (devolviendo mensajes de Éxito o Error).
- Se comunica hacia "abajo" con la capa de acceso a datos invocando el guardado o la lectura.

### 3. Capa de Acceso a Datos (`Datos` y `Database`)
Es la encargada exclusiva de almacenar y recuperar los datos del sistema en la base de datos PostgreSQL. 
- Ejecuta las sentencias SQL (CRUD) y retorna listas de datos procesables a la capa de Negocio.
- La clase `Conexion.java` dentro del paquete `Database` gestiona las conexiones usando JDBC.

---

## Casos de Uso Implementados

El sistema consta de 5 módulos principales interconectados, los cuales demuestran la evolución relacional del sistema:

1. **Gestión de Clientes (CU1):** 
   - Alta y listado de usuarios del gimnasio con sus medidas biométricas.
2. **Gestión de Dietas (CU2):** 
   - Registro de planes nutricionales independientes.
3. **Gestión de Ejercicios (CU3):** 
   - Biblioteca de ejercicios individuales, con soporte para inyección de URLs de imágenes.
4. **Gestión de Rutinas (CU4 - Cruce Relacional):** 
   - El sistema vincula a un Cliente específico con una Dieta.
   - La capa de presentación inyecta dinámicamente listas desplegables (`<select>`) leyendo desde la BD para evitar ingresos manuales de IDs.
5. **Asignaciones (CU5 - Tabla Pivote):** 
   - Representa la culminación de la arquitectura. Relaciona la `Rutina` creada con múltiples `Ejercicios`. 
   - Ejecuta un cuádruple `JOIN` SQL (`rutina_ejercicio`, `rutina`, `ejercicio`, `cliente`) para mostrar una vista condensada de todo el ecosistema.

---

## Instrucciones de Ejecución Local

Dado que el proyecto utiliza Java puro acoplado a PostgreSQL, debe ser ejecutado incluyendo el driver JDBC de Postgres (`lib/postgresql.jar`) en el *classpath*.

**Pasos para ejecutar el servidor web localmente en Windows:**

1. **Abrir la terminal** (PowerShell o CMD) y ubicarse en la carpeta raíz del proyecto (`PrimerP`).
2. **Compilar el código fuente** ejecutando el siguiente comando:
   ```bash
   javac -cp "lib\postgresql.jar;src" src\Database\*.java src\Datos\*.java src\Negocio\*.java src\Presentacion\*.java
   ```
3. **Iniciar la aplicación** ejecutando:
   ```bash
   java -cp "lib\postgresql.jar;src" Presentacion.Main
   ```
4. **Acceder a la aplicación:** 
   Una vez que la consola muestre `Servidor web iniciado correctamente en http://localhost:8080/`, abre tu navegador web e ingresa exactamente a:
   [http://localhost:8080](http://localhost:8080)

*(Nota importante: Nunca abras los archivos `.html` haciendo doble clic desde el explorador de archivos, de lo contrario la navegación y el sistema de ruteo fallarán).*
