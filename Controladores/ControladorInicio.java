package com.lucas.proyecto.Controladores;

import com.lucas.proyecto.Clases.CambiarPantalla;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;


/**
 *
 * @author Lukitas :)
 */

public class ControladorInicio implements Initializable {
	
	@FXML
	private Button MenuExpedientes;
	
	@FXML
	private Button MenuReuniones;
	
	@FXML
	private Button MenuPersonas;
	
	@FXML
	private Button MenuMinutas;
	
	@FXML
	private Button MenuAcciones;
	
	@FXML
	private Button MenuBusqueda;  

	
	//Acciones de los botones del Menú
        @FXML
        private void TocarMenuExp(ActionEvent event){
            CambiarPantalla.cambiarVentana(event, "Gestion_de_Expedientes.fxml", "Gestión de Expedientes");
        }
	
        @FXML
        private void TocarMenuReu(ActionEvent event){
            CambiarPantalla.cambiarVentana(event, "/Gestion_de_Reuniones.fxml", "Gestión de Reuniones");
        }
	
        @FXML
        private void TocarMenuPer(ActionEvent event){
            CambiarPantalla.cambiarVentana(event, "/Gestion_de_Personas.fxml", "Gestión de Personas");
        }
	
        @FXML
        private void TocarMenuMinu(ActionEvent event){
            CambiarPantalla.cambiarVentana(event, "/Gestion_de_Minutas.fxml", "Gestión de Minutas");
        }
	
        @FXML
        private void TocarMenuAcciones(ActionEvent event){
            CambiarPantalla.cambiarVentana(event, "/Gestion_de_Acciones.fxml", "Gestión de Acciones");
        }
	
	@FXML
	public void TocarMenuBusqueda(ActionEvent event){
		
	}
	
	//Metodo de inicialización
	@Override
	public void initialize (URL url, ResourceBundle rb){
		
		
		
		
	}
}
