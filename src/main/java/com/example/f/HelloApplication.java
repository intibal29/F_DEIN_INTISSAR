package com.example.f;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

/**
 * La clase {@code HelloApplication} es la clase principal que inicia la aplicación JavaFX.
 * Extiende la clase {@code Application}, que es el punto de entrada para las aplicaciones JavaFX.
 * El objetivo de esta aplicación es cargar una interfaz gráfica de usuario desde un archivo FXML
 * y mostrarla en una ventana de gestión de personas.
 */
public class HelloApplication extends Application {

    /**
     * El método {@code start} es el punto de entrada para la aplicación JavaFX.
     * Este método se ejecuta cuando la aplicación se inicia y se encarga de configurar y mostrar la ventana principal.
     *
     * @param primaryStage El escenario principal (ventana) de la aplicación donde se muestra la interfaz.
     * @throws Exception Si ocurre un error al cargar el archivo FXML o al crear la ventana.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Cargar el archivo FXML que contiene la estructura de la interfaz de usuario.
        // El archivo hello-view.fxml define el diseño y los componentes de la ventana.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/f/hello-view.fxml"));
        Parent root = loader.load();  // Carga el contenido del archivo FXML en un objeto Parent.

        // Establecer el título de la ventana principal.
        primaryStage.setTitle("Personas");

        // Crear una nueva escena con el contenido del archivo FXML cargado y establecer las dimensiones de la ventana.
        // La ventana se establecerá con un tamaño de 600 píxeles de ancho y 400 píxeles de alto.
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.setResizable(false);
        // Mostrar la ventana principal en la pantalla.
        primaryStage.show();
    }

    /**
     * El método principal {@code main} es el punto de entrada estándar para cualquier aplicación Java.
     * En el caso de las aplicaciones JavaFX, este método llama al método {@code launch}, que inicia el ciclo de vida
     * de la aplicación JavaFX.
     *
     * @param args Los argumentos de la línea de comandos (si los hay).
     */
    public static void main(String[] args) {
        // Llama al método launch para iniciar la aplicación JavaFX.
        // El método launch es provisto por la clase Application e inicia la aplicación.
        launch(args);
    }
}
