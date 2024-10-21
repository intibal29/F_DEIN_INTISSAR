package com.example.f;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * El controlador {@code AgregarPersonaController} gestiona la interacción de la ventana de
 * agregar una nueva persona en la interfaz gráfica. Se encarga de validar la entrada del
 * usuario y crear un objeto {@code Persona} si los datos son correctos.
 */
public class AgregarPersonaController {

    // Campos de texto en la interfaz gráfica para ingresar el nombre, apellidos y edad.
    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtApellidos;

    @FXML
    private TextField txtEdad;

    // La persona creada con los datos ingresados
    private Persona persona;

    /**
     * Este método se ejecuta cuando el usuario hace clic en el botón de guardar.
     * Valida los datos ingresados por el usuario, crea una nueva instancia de {@code Persona}
     * y luego cierra la ventana.
     */
    @FXML
    private void guardarPersona() {
        // Obtener los valores de los campos de texto
        String nombre = txtNombre.getText();
        String apellidos = txtApellidos.getText();
        String edadTexto = txtEdad.getText();

        // Validar que los campos de nombre y apellidos no estén vacíos
        if (nombre.isEmpty() || apellidos.isEmpty()) {
            mostrarAlerta("Error", "Nombre y apellidos son obligatorios.");
            return;
        }

        // Validar que la edad sea un número entero
        int edad;
        try {
            edad = Integer.parseInt(edadTexto);
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "La edad debe ser un número entero.");
            return;
        }

        // Crear una nueva persona con los datos ingresados
        persona = new Persona(nombre, apellidos, edad);

        // Cerrar la ventana después de guardar
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }

    /**
     * Este método se ejecuta cuando el usuario hace clic en el botón de cancelar.
     * Simplemente cierra la ventana sin realizar ninguna acción adicional.
     */
    @FXML
    private void cancelar() {
        // Cerrar la ventana
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }

    /**
     * Carga los datos de una persona en los campos de texto de la interfaz.
     *
     * @param persona La {@code Persona} cuyos datos se cargarán en los campos de texto.
     */
    public void cargarDatos(Persona persona) {
        txtNombre.setText(persona.getNombre());
        txtApellidos.setText(persona.getApellidos());
        txtEdad.setText(String.valueOf(persona.getEdad()));
    }

    /**
     * Devuelve el objeto {@code Persona} que fue creado con los datos ingresados.
     *
     * @return Un objeto {@code Persona} con los datos proporcionados por el usuario,
     * o {@code null} si no se ha creado ninguna persona.
     */
    public Persona getPersona() {
        return persona;
    }

    /**
     * Muestra una alerta de información con un mensaje personalizado.
     * Este método se utiliza para advertir al usuario cuando hay errores en la entrada.
     *
     * @param titulo El título de la ventana de alerta.
     * @param mensaje El mensaje que se mostrará en la alerta.
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        // Crear y configurar una alerta de tipo información
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        // Mostrar la alerta y esperar a que el usuario la cierre
        alerta.showAndWait();
    }
}
