package com.example.f;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.List;

/**
 * El controlador {@code HelloController} gestiona la lógica de la interfaz principal,
 * donde se pueden agregar personas a una tabla, seleccionarlas y mostrar sus datos.
 */
public class HelloController {

    // Campos de texto de la interfaz para capturar nombre, apellidos y edad
    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtApellidos;

    @FXML
    private TextField txtEdad;

    // Campo de texto para filtrar
    @FXML
    private TextField txtFiltro;

    // Tabla donde se mostrarán las personas
    @FXML
    private TableView<Persona> tablaPersonas;

    // Columnas de la tabla para mostrar los nombres, apellidos y edades
    @FXML
    private TableColumn<Persona, String> colNombre;

    @FXML
    private TableColumn<Persona, String> colApellidos;

    @FXML
    private TableColumn<Persona, Integer> colEdad;

    // Lista observable que contiene las personas para ser mostradas en la tabla
    private final ObservableList<Persona> listaPersonas = FXCollections.observableArrayList();

    /**
     * El método {@code initialize} es llamado automáticamente cuando se carga la interfaz.
     * Enlaza las columnas de la tabla con las propiedades de la clase {@code Persona}
     * y configura un listener para cargar los datos de la persona seleccionada.
     */
    @FXML
    public void initialize() {
        // Enlazar las propiedades de la clase Persona con las columnas de la tabla
        colNombre.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        colApellidos.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getApellidos()));
        colEdad.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getEdad()));

        // Asignar la lista observable a la tabla
        tablaPersonas.setItems(listaPersonas);

        // Listener para el campo de filtrado
        txtFiltro.textProperty().addListener((observable, oldValue, newValue) -> filtrarLista());
    }

    // Método para filtrar la lista
    @FXML
    private void filtrarLista() {
        String filtro = txtFiltro.getText().toLowerCase();
        ObservableList<Persona> listaFiltrada = FXCollections.observableArrayList();

        for (Persona persona : listaPersonas) {
            if (persona.getNombre().toLowerCase().contains(filtro)) {
                listaFiltrada.add(persona);
            }
        }
        tablaPersonas.setItems(listaFiltrada);
    }

    /**
     * Abre una ventana modal para agregar o modificar una persona.
     * Si los datos son válidos, la persona es agregada a la lista y mostrada en la tabla.
     */
    @FXML
    private void agregarPersona() {
        mostrarVentanaModal("Agregar Persona", null);
    }

    /**
     * Abre una ventana modal para modificar la persona seleccionada.
     * Si los datos son válidos, actualiza la información en la tabla.
     */
    @FXML
    private void modificarPersona() {
        Persona personaSeleccionada = tablaPersonas.getSelectionModel().getSelectedItem();
        if (personaSeleccionada == null) {
            mostrarAlerta("Error", "No hay ninguna persona seleccionada.");
            return;
        }
        mostrarVentanaModal("Modificar Persona", personaSeleccionada);
    }

    /**
     * Método para eliminar la persona seleccionada en la tabla.
     * Borra los datos de la persona y muestra un mensaje de éxito.
     */
    @FXML
    private void eliminarPersona() {
        Persona personaSeleccionada = tablaPersonas.getSelectionModel().getSelectedItem();
        if (personaSeleccionada == null) {
            mostrarAlerta("Error", "No hay ninguna persona seleccionada.");
            return;
        }
        listaPersonas.remove(personaSeleccionada);
        mostrarAlerta("Éxito", "Persona eliminada correctamente.");
    }

    /**
     * Abre una ventana modal para agregar o modificar una persona.
     *
     * @param titulo  Título de la ventana.
     * @param persona La persona a modificar, o null para agregar una nueva.
     */
    private void mostrarVentanaModal(String titulo, Persona persona) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/f/AgregarPersona.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            AgregarPersonaController agregarPersonaController = loader.getController();
            if (persona != null) {
                agregarPersonaController.cargarDatos(persona);
            }

            stage.showAndWait();

            Persona nuevaPersona = agregarPersonaController.getPersona();
            if (nuevaPersona != null) {
                if (persona == null) {
                    listaPersonas.add(nuevaPersona);  // Añadir a la lista observable
                    mostrarAlerta("Éxito", "Persona agregada correctamente.");
                } else {
                    // Si se está modificando, actualizar el objeto en la lista
                    int index = listaPersonas.indexOf(persona);
                    listaPersonas.set(index, nuevaPersona);
                    mostrarAlerta("Éxito", "Persona modificada correctamente.");
                }
            }
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo abrir la ventana.");
        }
    }

    /**
     * Método para importar datos desde un archivo CSV.
     */
    @FXML
    private void importarDatos() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importar Datos");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File file = fileChooser.showOpenDialog(tablaPersonas.getScene().getWindow());
        if (file != null) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                br.readLine(); // Ignorar la cabecera
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length == 3) {
                        String nombre = data[0].trim();
                        String apellidos = data[1].trim();
                        int edad = Integer.parseInt(data[2].trim());

                        // Comprobar si la persona ya existe
                        if (listaPersonas.stream().noneMatch(p -> p.getNombre().equals(nombre) && p.getApellidos().equals(apellidos))) {
                            listaPersonas.add(new Persona(nombre, apellidos, edad));
                        } else {
                            mostrarAlerta("Error", "La persona " + nombre + " " + apellidos + " ya existe.");
                        }
                    } else {
                        mostrarAlerta("Error", "Formato de línea no válido: " + line);
                    }
                }
                mostrarAlerta("Éxito", "Datos importados correctamente.");
            } catch (IOException | NumberFormatException e) {
                mostrarAlerta("Error", "No se pudo importar el archivo: " + e.getMessage());
            }
        }
    }

    /**
     * Método para exportar datos a un archivo CSV.
     */
    @FXML
    private void exportarDatos() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar Datos");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File file = fileChooser.showSaveDialog(tablaPersonas.getScene().getWindow());
        if (file != null) {
            if (!file.getName().endsWith(".csv")) {
                file = new File(file.getAbsolutePath() + ".csv");
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write("Nombre,Apellidos,Edad");
                bw.newLine();
                for (Persona persona : listaPersonas) {
                    bw.write(persona.getNombre() + "," + persona.getApellidos() + "," + persona.getEdad());
                    bw.newLine();
                }
                mostrarAlerta("Éxito", "Datos exportados correctamente.");
            } catch (IOException e) {
                mostrarAlerta("Error", "No se pudo exportar el archivo: " + e.getMessage());
            }
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}

