# Proyecto integrador de JAVA - JAVAFX - MYSQL

## Configuración JavaFX en Netbeans
La documentación oficial la puedes encontrar en [JavaFX](https://openjfx.io/openjfx-docs/).

Hacer clic derecho sobre el proyecto y luego clic en **"properties"**.

1. Ir a Librares > pestaña Classpath > símbolo **+** y agregar la librería de javafx.
2. Ir a Build > Compiling > Opción **Compile on sabe** y desmarcar.
3. Ir a Run > VM Options > pegar lo siguiente y reemplazar **RUTA_DIRECTA** por la ubicación exacta del SDK de JavaFX en la dirección javafx-sdk/lib:
    ```
    --module-path "RUTA_DIRECTA" --add-modules javafx.controls,javafx.fxml
    ```