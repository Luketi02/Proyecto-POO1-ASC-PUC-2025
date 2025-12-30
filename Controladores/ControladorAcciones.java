package com.lucas.proyecto.Controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.lucas.proyecto.Clases.Accion;
import com.lucas.proyecto.Clases.ConexionBD;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.ResultSet;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.swing.JOptionPane;


/**
 *
 * @author Lukitas :)
 */

public class ControladorAcciones implements Initializable {

	//Inicializamos todas los componentes

	@FXML
	private TextField  labelID;

	@FXML
	private  TextField labelExpe;
	
	@FXML
	private DatePicker DateFecha;
	
	@FXML
	private TextArea textDescri;
	
	@FXML
	private Button botonAleatorio; 
			  
	@FXML
	private Button BNuevaA;
	
	@FXML
	private Button BEliminarA;
	
	@FXML
	private Button BModificarA;
	
	@FXML
	private Button BActualizarL; 
	
	@FXML
	private Button MenuExpedientes;
	
	@FXML
	private Button MenuReuniones;
	
	@FXML
	private Button MenuPersonas;
	
	@FXML
	private Button MenuMinutas;
	
	@FXML
	private Button MenuBusqueda;  
	
	@FXML
	private Button BLimpiar;  
	
	@FXML
	private Button BBuscar;  	
	
	@FXML
	private TableView<Accion> tablaAcciones;
	
	@FXML
	private TableColumn<Accion, String> columnaID;
    
	@FXML
	private TableColumn<Accion, String> columnaIDExpediente;

	@FXML
	private TableColumn<Accion, LocalDate> columnaFecha;

	@FXML
	private TableColumn<Accion, String> columnaDescripcion;
	
	//Inicializamos dos variables accion y lista de acciones
	
	private ObservableList<Accion> listaAcciones = FXCollections.observableArrayList();
	private String consulta;
	ConexionBD conexion = new ConexionBD();

	
	//--------------------Acción del boton Aleatorio--------------------\\
	@FXML
	private void TocarBotonAleatorio(ActionEvent event){
		boolean existencia = false;
		String randomID = "";
		ConexionBD conexion = new ConexionBD();
		String consulta = "SELECT COUNT(*) AS existe FROM  \"ACCION\" WHERE \"id_Accion\" = ?";
		
		do{
			UUID id = UUID.randomUUID(); //Utilizar esta clases de java.util para crear una ID aleatorio
			randomID = id.toString().replaceAll("-", ""); //Convertir el ID en un string y eliminar los guiones
			randomID = randomID.substring(0, 10); //Seleccionamos longitud (Primeros 10)
			String valorBuscado = randomID;
			ResultSet resultadoConsulta = conexion.buscarValor(consulta,valorBuscado);
			try{
				if (resultadoConsulta.next()){
					int existe = resultadoConsulta.getInt("existe");
					existencia = existe > 0;
				}else{
					existencia = false;
				}
			}catch (Exception e){
				JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado :/ \nError:  " + e.getMessage());
				existencia = false;
			}	
		}while(existencia==true);
		labelID.setText(randomID); //Colocamos el ID random en la casilla labelID 
	}
	
	//--------------------Acción del boton de nueva minuta--------------------\\
	@FXML
	private void TocarBotonNuevaAccion(ActionEvent event){
		String ID = labelID.getText();
		LocalDate Fecha = DateFecha.getValue();
		String Descripcion = textDescri.getText();
		String IDExpediente = labelExpe.getText();

		//--------------------Se verifica si los demás datos son correctos
		if (ID != null && IDExpediente != null && Fecha != null & Descripcion != null ) {
			boolean existenciaE = false;
			boolean existenciaR = false;
			boolean existenciaA = false;
			boolean estadoB = false;
			java.sql.Timestamp fechaIngreso = null;
			ResultSet resultadoConsulta = null;
			String valorBuscado="";
			String id = null;
			String textoNota = null;
			String estado = "Cerrado";
			
			//--------------------Verificamos que el ID de la Accón no se repita.
			consulta = "SELECT COUNT(*) AS existe FROM  \"ACCION\" WHERE \"id_Accion\" = ?";
			valorBuscado =  ID;
			resultadoConsulta = conexion.buscarValor(consulta,valorBuscado);
			try{
				if (resultadoConsulta.next()){
					int existe = resultadoConsulta.getInt("existe");
					if (existe > 0){
						JOptionPane.showMessageDialog(null,"\t\tOcurrió un error, al parecer el ID de la Acción ya se encuentra en uso\n Verifique los datos y vuelva a seleccionar \"Nueva Acción\"");
						existenciaA = true;
					}else{
						existenciaA = false;
					}
				}else{
					existenciaA = false;
				}
			}catch (Exception e){
				JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado al verificar el ID de la acción :/ \nError:  " + e.getMessage());
				existenciaA = false;
			}
			if(existenciaA==false){
				//--------------------Se verifica si el ID del expediente existe en la Base de datos.
				consulta = "SELECT COUNT(*) AS existe FROM  \"EXPEDIENTE\" WHERE \"id_Expediente\" = ?";
				valorBuscado= IDExpediente ;
				resultadoConsulta = conexion.buscarValor(consulta,valorBuscado);
				try{
					if (resultadoConsulta.next()){
						int existe = resultadoConsulta.getInt("existe");
						existenciaE = existe > 0;
						if (existenciaE){
							consulta = "SELECT \"id_Expediente\",\"textoNota\", \"fechaIngreso\", \"estado\" FROM \"EXPEDIENTE\" WHERE \"id_Expediente\" = '" + IDExpediente +"';";
							resultadoConsulta = conexion.hacerConsulta(consulta);
							resultadoConsulta.next();
							try{
								id = resultadoConsulta.getString(1);
								textoNota = resultadoConsulta.getString(2);
								fechaIngreso = resultadoConsulta.getTimestamp(3);
								estadoB = resultadoConsulta.getBoolean(4);
							}catch(Exception e){
								JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado al obtener los datos del expediente :/ \nError:  " + e.getMessage());
							}
						}else{
							JOptionPane.showMessageDialog(null,"\t\tOcurrió un error, al parecer el ID del expediente no se encuentra\n Verifique los datos y vuelva a seleccionar \"Nueva Acción\"");
						}
					}else{
						existenciaE = false;
						JOptionPane.showMessageDialog(null,"\t\tOcurrió un error, al parecer el ID del expediente no se encuentra\n Verifique los datos y vuelva a seleccionar \"Nueva Acción\"");
					}
				}catch (Exception e){
					JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado al verificar el ID del expediente :/ \nError:  " + e.getMessage());
					existenciaE = false;
				}
				
				if (estadoB){
					estado = "Abierto";
				}
				
			}
			
			if(existenciaA==false & existenciaE==true){
				Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
				alerta.setTitle("Confirmación");
				alerta.setHeaderText(null);
				alerta.setContentText("¿Estás seguro que desea cargar esta nueva acción?\n Los datos son los siguientes: \n\tID = "+ ID +"\n\tID del Expediente asociado = "+ IDExpediente +"\n\tFecha de acción = "+ Fecha +"\n\tDescripción = "+Descripcion + "\n\n Datos del Expediente asociado:  \n\tID = "+ id + "\n\tTextoNota = " + textoNota + "\n\tFecha de Ingreso = " + fechaIngreso + "\n\tEstado = " + estado);
				
				//Creación de los botones de aceptar y cancelar
				ButtonType botonAceptar = new ButtonType("Aceptar");
				ButtonType botonCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

				Optional<ButtonType> resultado = alerta.showAndWait();
				
				// Inicializar la lista de acciones
				if (resultado.isPresent() && resultado.get() == ButtonType.OK) {				
					consulta = "INSERT INTO \"ACCION\" (\"id_Accion\", \"id_Expediente\", fecha, descripcion)\n" +"VALUES ('"+ID+"', '"+IDExpediente+"', '"+Fecha+"', '"+Descripcion+"');";
					
					try{
						if(conexion.hacerCambios(consulta)){
							JOptionPane.showMessageDialog(null,"\t\tSe realizó la carga de la nueva acción a la base de datos con exito!  ");
						}else{
							JOptionPane.showMessageDialog(null,"\t\tNo se ha realizó la carga de la nueva acción a la base de datos :/ \n\n Ocurrió un error inesperado");
						}
					}catch (Exception e){
						JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado al intentar cargar en la base de datos :/ \nError:  " + e.getMessage());
					}
				} else {
					// El usuario ha hecho clic en "Cancelar" o ha cerrado la ventana, No guardar la acción
				}
			}	
		}	
	}

	//--------------------Acción del boton de busqueda--------------------\\
	@FXML
	private void TocarBBuscar(ActionEvent event){
		
		Accion accionBusqueda = new Accion();
		accionBusqueda.setId(labelID.getText());
		accionBusqueda.setIdExpediente(labelExpe.getText());
		
		if (accionBusqueda.getId() != null && accionBusqueda.getIdExpediente() != null) {
			
			consulta = "SELECT * FROM \"ACCION\" WHERE \"id_Accion\" = '" + accionBusqueda.getId() + "'  OR \"id_Expediente\" = '"+ accionBusqueda.getIdExpediente() + "';";
		
			tablaAcciones.getItems().clear();
			
			columnaID.setCellValueFactory(new PropertyValueFactory<>("id"));
			columnaIDExpediente.setCellValueFactory(new PropertyValueFactory<>("idExpediente"));
			columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
			columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
			
			try(ResultSet resultado = conexion.hacerConsulta(consulta)){
				while (resultado.next()){
					
					Accion accionTabla = new Accion();

					accionTabla.setId(resultado.getString("id_Accion"));
					accionTabla.setIdExpediente(resultado.getString("id_Expediente"));
					accionTabla.setFecha(resultado.getDate("fecha").toLocalDate()); //Manera de pasar de Date a DateTime
					accionTabla.setDescripcion(resultado.getString("descripcion"));

					listaAcciones.add(accionTabla);
					
				}
			}catch (Exception e){
				JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado al intentar cargar en la base de datos :/ \nError:  " + e.getMessage());
			}

			tablaAcciones.setItems(listaAcciones);

		}else{
			if (accionBusqueda.getId() != null && accionBusqueda.getIdExpediente() == null){
				consulta = "SELECT * FROM \"ACCION\" WHERE \"id_Accion\" = '" + accionBusqueda.getId() + "';";
		
				tablaAcciones.getItems().clear();

				columnaID.setCellValueFactory(new PropertyValueFactory<>("id"));
				columnaIDExpediente.setCellValueFactory(new PropertyValueFactory<>("idExpediente"));
				columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
				columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

				try(ResultSet resultado = conexion.hacerConsulta(consulta)){
					while (resultado.next()){

						Accion accionTabla = new Accion();

						accionTabla.setId(resultado.getString("id_Accion"));
						accionTabla.setIdExpediente(resultado.getString("id_Expediente"));
						accionTabla.setFecha(resultado.getDate("fecha").toLocalDate()); //Manera de pasar de Date a DateTime
						accionTabla.setDescripcion(resultado.getString("descripcion"));

						listaAcciones.add(accionTabla);

					}
				}catch (Exception e){
					JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado al intentar cargar en la base de datos :/ \nError:  " + e.getMessage());
				}

				tablaAcciones.setItems(listaAcciones);
			}else{
				consulta = "SELECT * FROM \"ACCION\" WHERE \"id_Expediente\" = '"+ accionBusqueda.getIdExpediente() + "';";
		
				tablaAcciones.getItems().clear();

				columnaID.setCellValueFactory(new PropertyValueFactory<>("id"));
				columnaIDExpediente.setCellValueFactory(new PropertyValueFactory<>("idExpediente"));
				columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
				columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

				try(ResultSet resultado = conexion.hacerConsulta(consulta)){
					while (resultado.next()){

						Accion accionTabla = new Accion();

						accionTabla.setId(resultado.getString("id_Accion"));
						accionTabla.setIdExpediente(resultado.getString("id_Expediente"));
						accionTabla.setFecha(resultado.getDate("fecha").toLocalDate()); //Manera de pasar de Date a DateTime
						accionTabla.setDescripcion(resultado.getString("descripcion"));

						listaAcciones.add(accionTabla);

					}
				}catch (Exception e){
					JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado al intentar cargar en la base de datos :/ \nError:  " + e.getMessage());
				}

				tablaAcciones.setItems(listaAcciones);
				
			}
			
		}
		
		ObservableList<Accion> items = tablaAcciones.getItems();
		if(items.isEmpty()){
			ActualizarLista();
			JOptionPane.showMessageDialog(null,"\t\tNo se encontraron resultados");	
		}
	}
	
	
	//--------------------Acción del boton Eliminar Acción--------------------\\
	@FXML
	private void TocarBotonEliminar(ActionEvent event){
		
		Accion accion = new Accion();
		
		accion.setId(labelID.getText());
		accion.setIdExpediente(labelExpe.getText());
		accion.setFecha(DateFecha.getValue());
		accion.setDescripcion(textDescri.getText());
		
		Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
		alerta.setTitle("Confirmación");
		alerta.setHeaderText(null);
		alerta.setContentText("¿Estás seguro que desea eliminar esta acción?\n Los datos son los siguientes: \n\tID = "+ accion.getId() +"\n\tID del Expediente asociado = "+ accion.getIdExpediente() +"\n\tFecha de acción = "+ accion.getFecha() +"\n\tDescripción = "+accion.getDescripcion());

		//Creación de los botones de aceptar y cancelar
		ButtonType botonAceptar = new ButtonType("Aceptar");
		ButtonType botonCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

		Optional<ButtonType> resultado = alerta.showAndWait();

		// Inicializar la lista de acciones
		if (resultado.isPresent() && resultado.get() == ButtonType.OK) {				
			String consulta = "DELETE FROM \"ACCION\" WHERE \"id_Accion\" = '"+ accion.getId() +"';";
			try{
				if(conexion.hacerCambios(consulta)){
					JOptionPane.showMessageDialog(null,"\t\tSe realizó la eliminación de la acción en base de datos con exito!  ");
				}else{
					JOptionPane.showMessageDialog(null,"\t\tOcurrió un problema y no se logró eliminar la acción ");
				}
			}catch (Exception e){
				JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado al intentar cargar en la base de datos :/ \nError:  " + e.getMessage());
			}
		} else {
			// El usuario ha hecho clic en "Cancelar" o ha cerrado la ventana, No guardar la acción
		}
		ActualizarLista();
		
	}	
		
		
	
	//--------------------Acción del boton de Modificar acción--------------------\\
	@FXML
	private void TocarBotonModificar(ActionEvent event){
		
		Accion accion = new Accion();
		
		accion.setId(labelID.getText());
		accion.setIdExpediente(labelExpe.getText());
		accion.setFecha(DateFecha.getValue());
		accion.setDescripcion(textDescri.getText());
		
		boolean existenciaE;
		ResultSet resultadoConsulta;
		String valorBuscado;
		
		String id = "";
		String textoNota="";
		java.sql.Timestamp fechaIngreso = null;
		boolean estadoB = false;
		String estado = "Cerrado";
		
		//--------------------Se verifica si el ID del expediente existe en la Base de datos.
		consulta = "SELECT COUNT(*) AS existe FROM  \"EXPEDIENTE\" WHERE \"id_Expediente\" = ?";
		valorBuscado= accion.getIdExpediente();
		resultadoConsulta = conexion.buscarValor(consulta,valorBuscado);
		try{
			if (resultadoConsulta.next()){
				int existe = resultadoConsulta.getInt("existe");
				existenciaE = existe > 0;
				if (existenciaE){
					consulta = "SELECT \"id_Expediente\",\"textoNota\", \"fechaIngreso\", \"estado\" FROM \"EXPEDIENTE\" WHERE \"id_Expediente\" = '" + accion.getIdExpediente() +"';";
					resultadoConsulta = conexion.hacerConsulta(consulta);
					resultadoConsulta.next();
					try{
						id = resultadoConsulta.getString(1);
						textoNota = resultadoConsulta.getString(2);
						fechaIngreso = resultadoConsulta.getTimestamp(3);
						estadoB = resultadoConsulta.getBoolean(4);
					}catch(Exception e){
						JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado al obtener los datos del expediente :/ \nError:  " + e.getMessage());
					}
				}else{
					JOptionPane.showMessageDialog(null,"\t\tOcurrió un error, al parecer el ID del expediente no se encuentra\n Verifique los datos y vuelva a seleccionar \"Nueva Acción\"");
				}
			}else{
				existenciaE = false;
				JOptionPane.showMessageDialog(null,"\t\tOcurrió un error, al parecer el ID del expediente no se encuentra\n Verifique los datos y vuelva a seleccionar \"Nueva Acción\"");
			}
		}catch (Exception e){
			JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado al verificar el ID del expediente :/ \nError:  " + e.getMessage());
			existenciaE = false;
		}
		
		if (estadoB){
			estado = "Abierto";
		}
		
			
		if (existenciaE){
			Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
			alerta.setTitle("Confirmación");
			alerta.setHeaderText(null);
			alerta.setContentText("¿Estás seguro que desea modificar esta acción?\n Los datos son los siguientes: \n\tID = "+ accion.getId() +"\n\tID del Expediente asociado = "+ accion.getIdExpediente() +"\n\tFecha de acción = "+ accion.getFecha() +"\n\tDescripción = "+accion.getDescripcion() + "\n\n Datos del Expediente asociado:  \n\tID = "+ id + "\n\tTextoNota = " + textoNota + "\n\tFecha de Ingreso = " + fechaIngreso + "\n\tEstado = " + estado);
			
			//Creación de los botones de aceptar y cancelar
			ButtonType botonAceptar = new ButtonType("Aceptar");
			ButtonType botonCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
			Optional<ButtonType> resultado = alerta.showAndWait();
			
			// Inicializar la lista de acciones
			if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
				String consulta = "UPDATE \"ACCION\" SET \"id_Expediente\" = '"+ accion.getIdExpediente() + "',\"fecha\" = '" + accion.getFecha() + "',\"descripcion\" = '"+ accion.getDescripcion() + "' WHERE \"id_Accion\" = '"+ accion.getId() +"';";

				try{
					if(conexion.hacerCambios(consulta)){
						JOptionPane.showMessageDialog(null,"\t\tSe realizaron los cambios para la acción con ID \""+ accion.getId() +"\" en la base de datos con exito!  ");
					}else{
						JOptionPane.showMessageDialog(null,"\t\tOcurrió un problema y no se logró modificar la acción :/");
					}
				}catch (Exception e){
					JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado al intentar modificar la acción :/ \nError:  " + e.getMessage());
				}
			ActualizarLista();
			}
		}
	}
	
	//--------------------Acción del boton de Actualizar lista--------------------\\
	@FXML
	private void TocarBotonActualizar(ActionEvent event){
		
		ActualizarLista();
		
	}
	
	//--------------------Acción del boton de Limpiar--------------------\\
	@FXML
	private void TocarBLimpiar(ActionEvent event){
		
		labelID.setText("");
		labelExpe.setText("");
		textDescri.setText("");
		DateFecha.setValue(null);
		
		labelID.setEditable(true);
		
	}
	
	//--------------------Acción de los botones del menú--------------------\\
	@FXML
	private void TocarMenuExp(ActionEvent event){
		/*
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gestion_de_Expedientes.fxml"));
			Parent root = loader.load();

			// Obtén el controlador de la nueva escena (ControladorExpedientes)
			ControladorExpedientes controladorExpedientes = loader.getController();

			// Configura cualquier dato necesario antes de mostrar la nueva escena

			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);

			// Cierra la ventana actual 
			Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			currentStage.close();

			// Muestra la nueva ventana
			stage.show();
		} catch (IOException e) {
			Alert alerta = new Alert(Alert.AlertType.ERROR);
			alerta.setTitle("Error");
			alerta.setHeaderText(null);
			alerta.setContentText("Lamentablemente no se logró abrir la pestaña de Acciones debido a un error");
			alerta.showAndWait();
			e.printStackTrace(); // Maneja la excepción de carga del FXML
		}
		*/
	}
	
	@FXML
	private void TocarMenuReu(ActionEvent event){
		/*
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gestion_de_Reuniones.fxml"));
			Parent root = loader.load();

			// Obtén el controlador de la nueva escena (ControladorAcciones)
			ControladorReuniones controladorReuniones = loader.getController();

			// Configura cualquier dato necesario antes de mostrar la nueva escena

			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);

			// Cierra la ventana actual 
			Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			currentStage.close();

			// Muestra la nueva ventana
			stage.show();
		} catch (IOException e) {
			Alert alerta = new Alert(Alert.AlertType.ERROR);
			alerta.setTitle("Error");
			alerta.setHeaderText(null);
			alerta.setContentText("Lamentablemente no se logró abrir la pestaña de Acciones debido a un error");
			alerta.showAndWait();
			e.printStackTrace(); // Maneja la excepción de carga del FXML
		}
		*/
	}
	
	@FXML
	private void TocarMenuPer(ActionEvent event){
		/*
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gestion_de_Personas.fxml"));
			Parent root = loader.load();

			// Obtén el controlador de la nueva escena (ControladorPersonas)
			ControladorPersonas controladorPersonas = loader.getController();

			// Configura cualquier dato necesario antes de mostrar la nueva escena

			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);

			// Cierra la ventana actual 
			Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			currentStage.close();

			// Muestra la nueva ventana
			stage.show();
		} catch (IOException e) {
			Alert alerta = new Alert(Alert.AlertType.ERROR);
			alerta.setTitle("Error");
			alerta.setHeaderText(null);
			alerta.setContentText("Lamentablemente no se logró abrir la pestaña de Acciones debido a un error");
			alerta.showAndWait();
			e.printStackTrace(); // Maneja la excepción de carga del FXML
		}
		*/
	}
	
	@FXML
	private void TocarMenuMinu(ActionEvent event){
		/*
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gestion_de_Minutas.fxml"));
			Parent root = loader.load();

			// Obtén el controlador de la nueva escena (ControladorPersonas)
			ControladorMinutas controladorMinutas = loader.getController();

			// Configura cualquier dato necesario antes de mostrar la nueva escena

			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);

			// Cierra la ventana actual 
			Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			currentStage.close();

			// Muestra la nueva ventana
			stage.show();
		} catch (IOException e) {
			Alert alerta = new Alert(Alert.AlertType.ERROR);
			alerta.setTitle("Error");
			alerta.setHeaderText(null);
			alerta.setContentText("Lamentablemente no se logró abrir la pestaña de Acciones debido a un error");
			alerta.showAndWait();
			e.printStackTrace(); // Maneja la excepción de carga del FXML
		}
		*/
	}
	
	@FXML
	private void TocarMenuBusqueda(ActionEvent event){
		/*
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Busqueda.fxml"));
			Parent root = loader.load();

			// Obtén el controlador de la nueva escena (ControladorPersonas)
			ControladorBusqueda controladorBusqueda = loader.getController();

			// Configura cualquier dato necesario antes de mostrar la nueva escena

			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);

			// Cierra la ventana actual 
			Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			currentStage.close();

			// Muestra la nueva ventana
			stage.show();
		} catch (IOException e) {
			Alert alerta = new Alert(Alert.AlertType.ERROR);
			alerta.setTitle("Error");
			alerta.setHeaderText(null);
			alerta.setContentText("Lamentablemente no se logró abrir la pestaña de Acciones debido a un error");
			alerta.showAndWait();
			e.printStackTrace(); // Maneja la excepción de carga del FXML
		}
		*/
	}
	
	//--------------------Metodo publico para actualizar la lista con los datos recientes de la base de datos--------------------\\
	public void ActualizarLista(){
		//Vaciamos la lista por completo
		
		tablaAcciones.getItems().clear();
		
		//Configuración de la tabla para mostrar el contenido
		columnaID.setCellValueFactory(new PropertyValueFactory<>("id"));
		columnaIDExpediente.setCellValueFactory(new PropertyValueFactory<>("idExpediente"));
		columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
		columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

		// Agregar las columnas a la tabla
		if (tablaAcciones.getColumns().isEmpty()) {
			tablaAcciones.getColumns().addAll(columnaID, columnaIDExpediente, columnaFecha, columnaDescripcion);
		}
		
		//Conección a la bd y mostrar los contenidos.	
		ConexionBD conexion = new ConexionBD();
		conexion.getConexion();
		
		String consulta = "SELECT * FROM \"ACCION\"";
		
		try(ResultSet resultadoConsulta = conexion.hacerConsulta(consulta)){
			while (resultadoConsulta.next()){
				Accion accionTabla = new Accion();
				
				accionTabla.setId(resultadoConsulta.getString("id_Accion"));
				accionTabla.setIdExpediente(resultadoConsulta.getString("id_Expediente"));
				accionTabla.setFecha(resultadoConsulta.getDate("fecha").toLocalDate()); //Manera de pasar de Date a DateTime
				accionTabla.setDescripcion(resultadoConsulta.getString("descripcion"));
				
				listaAcciones.add(accionTabla);
				
				
			}
		}catch (Exception e){
			JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado al intentar cargar en la base de datos :/ \nError:  " + e.getMessage());
		}
		
		tablaAcciones.setItems(listaAcciones);
	}
	
	//--------------------Metodo de inicialización--------------------\\
	@Override
	public void initialize (URL url, ResourceBundle rb){
		
		BooleanBinding camposVacios = Bindings.createBooleanBinding(
		() -> labelID.getText().isEmpty() || labelExpe.getText().isEmpty() || textDescri.getText().isEmpty() || DateFecha.getValue() == null,
		labelID.textProperty(), labelExpe.textProperty(), textDescri.textProperty(), DateFecha.valueProperty()
		);
		
		BooleanBinding camposVaciosNueva = Bindings.createBooleanBinding(
		() -> !labelID.isEditable() || labelID.getText().isEmpty() || labelExpe.getText().isEmpty() || textDescri.getText().isEmpty() || DateFecha.getValue() == null,
		labelID.textProperty(), labelExpe.textProperty(), textDescri.textProperty(), DateFecha.valueProperty(), labelID.editableProperty()
		);
		
		BooleanBinding camposLimpiar = Bindings.createBooleanBinding(
		() -> labelID.getText().isEmpty() && labelExpe.getText().isEmpty() && textDescri.getText().isEmpty() && DateFecha.getValue() == null,
		labelID.textProperty(), labelExpe.textProperty(), textDescri.textProperty(), DateFecha.valueProperty()
		);
		
		
		labelID.editableProperty().addListener((observable, oldValue, newValue) -> {
			// Actualizar la propiedad del botón
			botonAleatorio.setDisable(!newValue);
		});
				
		BooleanBinding Busqueda = Bindings.createBooleanBinding(
		() -> labelID.getText().isEmpty() && labelExpe.getText().isEmpty(),
		labelID.textProperty(), labelExpe.textProperty()
		);
		
		BNuevaA.disableProperty().bind(camposVaciosNueva);
		BEliminarA.disableProperty().bind(camposVacios);
		BModificarA.disableProperty().bind(camposVacios);
		BLimpiar.disableProperty().bind(camposLimpiar);
		BBuscar.disableProperty().bind(Busqueda.or(labelID.editableProperty().not()));
		DateFecha.setEditable(false);
		
		
		
		// Configurar el manejo de cambios de selección en la tabla
		tablaAcciones.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Accion>() {
			@Override
			public void changed(ObservableValue<? extends Accion> observable, Accion oldValue, Accion nuevoValor) {
				if (nuevoValor != null) {
					// Actualizar los TextField y DatePicker con los valores seleccionados
					labelID.setText(nuevoValor.getId());
					labelExpe.setText(nuevoValor.getIdExpediente());
					textDescri.setText(nuevoValor.getDescripcion());
					DateFecha.setValue(nuevoValor.getFecha());

					labelID.setEditable(false);
				}
			}
		});

		//Configuración de la tabla para mostrar el contenido
		columnaID.setCellValueFactory(new PropertyValueFactory<>("id"));
		columnaIDExpediente.setCellValueFactory(new PropertyValueFactory<>("idExpediente"));
		columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
		columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

		// Agregar las columnas a la tabla
		if (tablaAcciones.getColumns().isEmpty()) {
			tablaAcciones.getColumns().addAll(columnaID, columnaIDExpediente, columnaFecha, columnaDescripcion);
		}
		
		//Conección a la bd y mostrar los contenidos.	
		ConexionBD conexion = new ConexionBD();
		conexion.getConexion();
		
		String consulta = "SELECT * FROM \"ACCION\"";
		
		try(ResultSet resultadoConsulta = conexion.hacerConsulta(consulta)){
			while (resultadoConsulta.next()){
				Accion accionTabla = new Accion();
				
				accionTabla.setId(resultadoConsulta.getString("id_Accion"));
				accionTabla.setIdExpediente(resultadoConsulta.getString("id_Expediente"));
				accionTabla.setFecha(resultadoConsulta.getDate("fecha").toLocalDate()); //Manera de pasar de Date a DateTime
				accionTabla.setDescripcion(resultadoConsulta.getString("descripcion"));
				
				listaAcciones.add(accionTabla);
			}
		}catch (Exception e){
			JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado al intentar cargar en la base de datos :/ \nError:  " + e.getMessage());
		}
		
		tablaAcciones.setItems(listaAcciones);
	}
}
