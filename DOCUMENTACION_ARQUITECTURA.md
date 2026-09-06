# Arquitectura del Software: Patrón de 3 Niveles

Este proyecto utiliza el patrón arquitectónico de tres capas (o tres niveles) desarrollado en Java sin el uso de frameworks. 

La principal característica de este diseño es que los componentes de una capa sólo pueden hacer referencia a componentes en capas inmediatamente inferiores, reduciendo dependencias y simplificando la organización.

## Estructura de Capas

El software se divide lógicamente en los siguientes paquetes principales:

### 1. Capa de Presentación (`Presentacion`)
Es la encargada de que el sistema interactúe con el usuario. Muestra la información y obtiene los datos del usuario con un mínimo de procesamiento. Esta capa se comunica **únicamente** con la capa intermedia o de negocio.

### 2. Capa de Negocio (`Negocio`)
También conocida como capa de lógica empresarial. Aquí residen las funciones, se reciben las peticiones de presentación, se procesa la información aplicando las reglas de negocio, y se envían las respuestas. 
- Se comunica hacia "arriba" con la capa de presentación (para responder solicitudes).
- Se comunica hacia "abajo" con la capa de acceso a datos (para solicitar almacenamiento o recuperación).

### 3. Capa de Acceso a Datos (`Datos` y `Database`)
Es la encargada exclusiva de almacenar y recuperar los datos del sistema. Su función es devolver datos a la capa de negocio. En esta arquitectura, es la única capa con acceso directo a los medios de almacenamiento o bases de datos (gestionado mediante el paquete Database).

---

## Instrucciones de Ejecución Local

Dado que el proyecto utiliza Java puro y no depende de configuraciones de IDE específicos (como NetBeans o Eclipse) ni de gestores de dependencias (como Maven), puede ser ejecutado directamente desde la terminal o consola de comandos.

**Pasos para ejecutar el servidor web localmente:**

1. **Abrir la terminal** y ubicarse en la carpeta raíz del proyecto (`PrimerP`).
2. **Compilar el código fuente** ejecutando el siguiente comando:
   ```bash
   javac -sourcepath src src/Presentacion/Main.java
   ```
3. **Iniciar la aplicación** ejecutando:
   ```bash
   java -cp src Presentacion.Main
   ```
4. **Acceder a la aplicación:** Una vez que la consola muestre el mensaje de éxito, abre tu navegador web e ingresa a [http://localhost:8080](http://localhost:8080).
