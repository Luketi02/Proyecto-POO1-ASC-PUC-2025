package com.lucas.proyecto.Clases;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;

public class CambiarPantalla {
    
    /**
     * Cambia a una nueva pantalla cerrando la actual
     * @param event Evento del botón que dispara el cambio
     * @param nombreArchivoFxml Nombre del archivo FXML (ej: "Gestion_de_Expedientes.fxml")
     * @param tituloVentana Título de la nueva ventana (opcional)
     */
    public static void cambiarVentana(ActionEvent event, String nombreArchivoFxml, String tituloVentana) {
        try {
            String rutaCompleta = "Views/" + nombreArchivoFxml;
            System.out.println("Cargando: " + rutaCompleta); // Para debug
            
            URL fxmlUrl = CambiarPantalla.class.getClassLoader().getResource(rutaCompleta);
            if (fxmlUrl == null) {
                throw new IOException("No se pudo encontrar el archivo: " + rutaCompleta);
            }
            
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            
            if (tituloVentana != null && !tituloVentana.isEmpty()) {
                stage.setTitle(tituloVentana);
            }
            
            stage.setScene(scene);
            
            // Cierra la ventana actual
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
            
            stage.show();
            
        } catch (IOException e) {
            mostrarError("Error al cargar la pantalla", 
                        "No se pudo abrir la ventana: " + nombreArchivoFxml, e);
        }
    }
    
    /**
     * Cambia a una nueva pantalla sin título personalizado
     */
    public static void cambiarVentana(ActionEvent event, String nombreArchivoFxml) {
        cambiarVentana(event, nombreArchivoFxml, null);
    }
    
    /**
     * Muestra un mensaje de error personalizado
     */
    private static void mostrarError(String titulo, String mensaje, Exception e) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
        e.printStackTrace();
    }
}