# Proyecto integrador de JAVA - JAVAFX - MYSQL

## Configuración JavaFX en NetBeans
La documentación oficial la puedes encontrar en [JavaFX](https://openjfx.io/openjfx-docs/).

Hacer clic derecho sobre el proyecto y luego clic en **"Properties"**.

1. Ir a **Libraries** > pestaña **Classpath** > presionar **Add Library...** (o símbolo **+**) y agregar la librería de JavaFX.
2. Ir a **Build** > **Compiling** > desmarcar la opción **Compile on Save**.
3. Ir a **Run** > **VM Options** > pegar lo siguiente y reemplazar **RUTA_DIRECTA** por la ubicación exacta del SDK de JavaFX en la dirección `javafx-sdk/lib` (por ejemplo: `C:\libreries\javafx-sdk-21\lib`):
    ```bash
    --module-path "RUTA_DIRECTA" --add-modules javafx.controls,javafx.fxml,javafx.web
    ```

> **Importante sobre `javafx.web` y el `HTMLEditor`:**  
> Por defecto, la documentación básica de JavaFX únicamente sugiere registrar `--add-modules javafx.controls,javafx.fxml`. Sin embargo, en este proyecto se utiliza el componente de texto enriquecido **`HTMLEditor`**, el cual pertenece al módulo **`javafx.web`**.  
> Si se omite `javafx.web`, Java intentará cargar el editor en el módulo sin nombre (*unnamed module*) provocando la siguiente excepción al abrir la vista:  
> `java.lang.IllegalAccessError: class javafx.scene.web.HTMLEditor (in unnamed module) cannot access class com.sun.javafx.scene.control.ControlHelper (in module javafx.controls)`  
> Por ello, es **estrictamente indispensable** agregar `javafx.web` en la directiva `--add-modules`.

## Dependencias y Librerías Requeridas

Para la compilación y ejecución de la aplicación, es indispensable agregar las siguientes librerías al **Classpath** del proyecto:

### 1. Cifrado Seguro con BCrypt
La funcionalidad de encriptación y verificación de contraseñas hace uso de la biblioteca de Patrick Favre (`at.favre.lib.crypto.bcrypt`), la cual requiere dos archivos `.jar`:

* **BCrypt Core:** [bcrypt-0.10.2.jar (Releases en GitHub)](https://github.com/patrickfav/bcrypt/releases) — Descarga la versión `bcrypt-0.10.2.jar`.
* **Dependencia Bytes Java:** [bytes-1.6.1.jar (Descarga directa)](https://github.com/patrickfav/bytes-java/releases/download/v1.6.1/bytes-1.6.1.jar) — Librería auxiliar requerida para el manejo de arreglos de bytes en memoria.

> **Importante:** Debes incluir ambos `.jar` (`bcrypt` y `bytes`) en el Classpath; de lo contrario se producirá un error de tipo `NoClassDefFoundError: at/favre/lib/bytes/Bytes`.

### 2. Conector JDBC de MySQL
* **MySQL Connector/J:** Asegúrate de contar con el driver JDBC (por ejemplo, `mysql-connector-j-9.7.0.jar` o versión 8.x).

---

### Pasos para agregar las librerías en NetBeans:
1. Haz clic derecho sobre el proyecto y selecciona **Properties**.
2. En la barra lateral izquierda, selecciona la categoría **Libraries**.
3. En la pestaña **Classpath**, presiona el botón **Add JAR/Folder** (o el ícono **+**).
4. Busca y selecciona los archivos `.jar` descargados (`bcrypt-0.10.2.jar`, `bytes-1.6.1.jar` y el conector de MySQL).
5. Haz clic en **OK** para guardar la configuración.