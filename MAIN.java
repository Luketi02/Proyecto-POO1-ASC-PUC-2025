package com.lucas.proyecto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

/**
 *
 * @author GhOsT B)
 */
public class MAIN extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("=== INICIANDO APLICACIÓN ===");
        System.out.println("Buscando: Views/Inicio.fxml");
        
        URL fxmlUrl = getClass().getClassLoader().getResource("Views/Inicio.fxml");
        System.out.println("URL encontrada: " + fxmlUrl);
        
        if (fxmlUrl == null) {
            // Debug: mostrar qué recursos hay disponibles
            System.out.println("Recursos disponibles en classpath:");
            java.util.Enumeration<URL> resources = getClass().getClassLoader().getResources("");
            while (resources.hasMoreElements()) {
                System.out.println(" - " + resources.nextElement());
            }
            throw new RuntimeException("ERROR: No se pudo encontrar Views/Inicio.fxml");
        }
        
        Parent root = FXMLLoader.load(fxmlUrl);
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("Sistema de Gestión Administrativa");
        stage.show();
        
        System.out.println("Aplicación iniciada correctamente!");
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}