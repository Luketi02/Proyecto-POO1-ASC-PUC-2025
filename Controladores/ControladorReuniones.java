package com.lucas.proyecto.Controladores;


import javafx.geometry.Insets;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.lucas.proyecto.Clases.ConexionBD;
import com.lucas.proyecto.Clases.Persona;
import com.lucas.proyecto.Clases.Reunion;
import com.lucas.proyecto.Clases.Expediente;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.swing.JOptionPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import javafx.beans.property.BooleanProperty;


/**
 *
 * @author Lukitas :)
 */

public class ControladorReuniones implements Initializable {

	//Inicializamos todas los componentes

	@FXML
	private TextField labelID;

	@FXML
	private  TextField labelE;
	
	@FXML
	private DatePicker DateFecha;
	
	@FXML
	private TextField labelHora;
	
	@FXML
	private TextField labelDNI;
	
	@FXML
	private Button botonAleatorio; 
			  
	@FXML
	private Button BNuevo;
	
	@FXML
	private Button BEliminar;
	
	@FXML
	private Button BModificar;
	
	@FXML
	private Button BAP;
	
	@FXML
	private Button BQP;
	
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
	private Button BAE;
	
	@FXML
	private Button BQE;  	
	
	@FXML
	private TableView<Reunion> tablaReuniones;
	
	@FXML
	private TableColumn<Reunion, String> columnaIDR;

	@FXML
	private TableColumn<Reunion, String> columnaFechaR;
	
	@FXML
	private TableView<Persona> tablaAsistencia;
	
	@FXML
	private TableColumn<Persona, String> columnaDNI;
    
	@FXML
	private TableColumn<Persona, String> columnaNombre;

	@FXML
	private TableColumn<Persona, String> columnaApellido;
	
	@FXML
	private TableColumn<Persona, String> columnaCorreo;
	
	@FXML
	private TableView<Expediente> tablaExpediente;
	
	@FXML
	private TableColumn<Expediente, String> columnaIDE;
    
	@FXML
	private TableColumn<Expediente, String> columnaNotaE;

	@FXML
	private TableColumn<Expediente, String> columnaFechaE;
	
	@FXML
	private TableColumn<Expediente, String> columnaEstadoE;
	
	//Inicializamos dos variables accion y lista de acciones
	
	private ObservableList<Reunion> listaReuniones = FXCollections.observableArrayList();
	private ObservableList<Persona> listaPersonas = FXCollections.observableArrayList();
	private ObservableList<Expediente> listaExpedientes = FXCollections.observableArrayList();
	private String consulta;
	ConexionBD conexion = new ConexionBD();
	
	//--------------------Acción del boton Aleatorio--------------------\\ TODO LISTO
	@FXML
	private void TocarBotonAleatorio(ActionEvent event){
		boolean existencia = false;
		String randomID = "";
		ConexionBD conexion = new ConexionBD();
		String consulta = "SELECT COUNT(*) AS existe FROM  \"REUNION\" WHERE \"id_Reunion\" = ?";
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
	
	//--------------------Acción del boton de nueva Reunion--------------------\\ TODO LISTO
	@FXML
	private void TocarBotonNueva(ActionEvent event){
		Reunion reunion = new Reunion();
		reunion.setId(labelID.getText());
		//Se verifica que hayan expedientes y personas asociadas
		if(!listaPersonas.isEmpty() && !listaExpedientes.isEmpty()){
			//Se verifica que no falten datos para la reunión
			if (reunion.getId() != null && DateFecha != null && labelHora != null) {
				//Verificamos que la hora y la fecha tengan sus formatos
				if (verificarFormatoHora(labelHora.getText())){
					try{
						LocalTime hora = LocalTime.parse(labelHora.getText());
						LocalDate dia = DateFecha.getValue();
						if (verificarHora(hora)){
							reunion.setFecha(crearFechaYHora(hora,dia));
							
							// Creamos el mensaje con el contructor de string													
							StringBuilder mensaje = new StringBuilder();
							
							//Agregams todos los mensajes al constructor
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
							mensaje.append("¿Estás seguro que desea cargar esta nueva reunión?\n");
							mensaje.append("Los datos son los siguientes:\n");
							mensaje.append("\tID = ").append(reunion.getId()).append("\n");
							mensaje.append("\tProgramado para el día ").append(dia.format(formatter))
								   .append(", en el horario de ").append(hora).append("\n");
							
							// Agregar las personas que asistirán a la reunión
							mensaje.append("Personas que asistirán:\n");
							for (Persona persona : listaPersonas) {
								mensaje.append("\t-> ").append(persona.getDni()).append(" - ").append(persona.getNombre()).append(" - ").append(persona.getApellido()).append("\n");
							}
							
							//Agregamos los expedientes que se tratan en la reunión
							mensaje.append("Expedientes que se tratarán:\n");
							String estado = "";
							for (Expediente expediente : listaExpedientes) {
								if (expediente.isEstado()){
									estado = "Abierto";
								}else{
									estado = "Cerrado";
								}
								mensaje.append("\t-> ").append(expediente.getId()).append(" - ").append(expediente.getTextoNota()).append(" - ").append(estado).append("\n");
							}
							
							//Creamos la alerta
							Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
							alerta.setTitle("Confirmación");
							alerta.setHeaderText(null);
							alerta.setContentText(mensaje.toString());

							//Creación de los botones de aceptar y cancelar
							ButtonType botonAceptar = new ButtonType("Aceptar");
							ButtonType botonCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

							Optional<ButtonType> resultadoAlerta = alerta.showAndWait();

							// Comenzamos el guardado
							if (resultadoAlerta.isPresent() && resultadoAlerta.get() == ButtonType.OK) {
							
								consulta = "INSERT INTO \"REUNION\" (\"id_Reunion\", \"fecha\") VALUES ('"+reunion.getId()+"', '"+reunion.getFecha()+"');";
								
								try{
									if(conexion.hacerCambios(consulta)){ //Ya se guarda en la bd la reunión
										//Vinculamos las personas que asistirán
										for (Persona persona : listaPersonas) {
											consulta = "INSERT INTO \"ASISTENCIA\" (\"dni\", \"id_Reunion\",\"presencia\") VALUES ('"+persona.getDni()+"', '"+reunion.getId()+"', FALSE);";
											if(conexion.hacerCambios(consulta)==false){
												JOptionPane.showMessageDialog(null,"\t\tOcurrió un error, no se vincular a la siguiente persona con la reunión \n\tDNI = "+persona.getDni()+"\n\tNombre = "+persona.getNombre()+"\n\tApellido = "+persona.getApellido(),"Error",JOptionPane.ERROR_MESSAGE);
											}
										}
										//Vinculamos los expedientes que se tratarán
										for (Expediente expediente : listaExpedientes) {
											consulta = "INSERT INTO \"REUNION_EXPEDIENTE\" (\"id_Reunion\",\"id_Expediente\") VALUES ('"+reunion.getId()+"', '"+expediente.getId()+"');";
											if(conexion.hacerCambios(consulta)==false){
												JOptionPane.showMessageDialog(null,"\t\tOcurrió un error, no se logró vincular el siguiente expedente con la reunión \n\tID = "+expediente.getId()+"\n\tTexto de la nota = "+expediente.getTextoNota()+"\n\tFecha de Ingreso = "+expediente.getFechaIngreso(),"Error",JOptionPane.ERROR_MESSAGE);
											}
										}
										
										JOptionPane.showMessageDialog(null,"\t\tLa reunión y los datos asociados se guardaron correctamente!!","Realizado",JOptionPane.INFORMATION_MESSAGE);
										
									}else{
										JOptionPane.showMessageDialog(null,"\t\tOcurrió un error, no se logró guardar la reunión \n\t","Error",JOptionPane.ERROR_MESSAGE);
									}
								}catch(Exception e){
									JOptionPane.showMessageDialog(null,"\t\tOcurrió un error\n\t"+e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
								}
							}						
						}else{
							JOptionPane.showMessageDialog(null,"\t\tOcurrió un error, al parecer la hora no se encuentra en el rango permitido. \n\t El rango permitido es desde las 08:00 hasta las 20:00","Error",JOptionPane.ERROR_MESSAGE);
						}
					}catch(Exception e){
						JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado \n"+ e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
					}
				}else{
					JOptionPane.showMessageDialog(null,"\t\tOcurrió un error, Al parecer la hora no se encuentra en un formato correcto! \n\t Recuerde que el formato es \"HH:mm\" ","Error",JOptionPane.ERROR_MESSAGE);
				}				
			}else{
				JOptionPane.showMessageDialog(null,"\t\tOcurrió un error, Alguno de los campos es vacio, tiene que rellenar el ID, fecha y hora ","Error",JOptionPane.ERROR_MESSAGE);
			}
		}else{
			JOptionPane.showMessageDialog(null,"\t\tOcurrió un error, Al parecer no estableció ningún ningún expediente o persona que asista a la reunion! \n\t Por favor establezca almenos 1 expediente y 1 persona que asista a la reunión  ","Error",JOptionPane.ERROR_MESSAGE);
		}
		listaPersonas.clear();
		tablaAsistencia.setItems(listaPersonas);
		listaExpedientes.clear();
		tablaExpediente.setItems(listaExpedientes);

		labelID.setText("");
		labelE.setText("");
		labelHora.setText("");
		labelDNI.setText("");
		DateFecha.setValue(null);
		ActualizarLista();
	}

	//--------------------Acción del boton de busqueda--------------------\\ TODO LISTO
	@FXML
	private void TocarBBuscar(ActionEvent event){
		
		
		Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
		alerta.setTitle("Confirmación");
		alerta.setHeaderText(null);
		alerta.setContentText("¿Como desea buscar la reunión?\nA través del campo ID o por fecha? \n¡Tenga en cuenta que los datos en las tablas y los campos serán borrados!");

		// Creación de los botones
		ButtonType botonID = new ButtonType("ID");
		ButtonType botonFYH = new ButtonType("Fecha");

		// Agregar los botones a la ventana de alerta
		alerta.getButtonTypes().setAll(botonID, botonFYH, ButtonType.CANCEL);

		// Mostrar la ventana de alerta y obtener la respuesta
		Optional<ButtonType> resultadoM = alerta.showAndWait();
		
		Reunion reunion = new Reunion();
		
		if (resultadoM.isPresent()) {
			if (resultadoM.get() == botonID) {
					
					// Crear el TextField para ingresar el ID de la reunión
					TextField textFieldID = new TextField();
					textFieldID.setPromptText("ID");

					// Establecer estilos para el TextField
					textFieldID.setStyle("-fx-font-size: 14;");

					// Crear la etiqueta para el TextField
					Label lblID = new Label("Ingresar ID:");
					lblID.setStyle("-fx-font-size: 14;");

					// Crear el botón de búsqueda
					Button btnBuscar = new Button("Buscar");
					btnBuscar.setStyle("-fx-font-size: 14; -fx-padding: 8 16; -fx-background-color: #4CAF50; -fx-text-fill: white;");
					btnBuscar.setOnAction(e -> {
						
						if (!textFieldID.getText().isEmpty()){
							reunion.setId(textFieldID.getText());
							listaReuniones.clear();
							consulta = "SELECT * FROM \"REUNION\" WHERE \"id_Reunion\" = '"+reunion.getId()+"';";
							ResultSet resultado = conexion.hacerConsulta(consulta);
							try{
								if(resultado.next()){

									String fechaString = resultado.getString("fecha");
									LocalDateTime fecha = LocalDateTime.parse(fechaString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
									reunion.setFecha(fecha);
									listaReuniones.add(reunion);
									
								}
								
							}catch (Exception t){
								JOptionPane.showMessageDialog(null,"\t\tOcurrió un error error inesperado. \n\tError: "+t.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);	
							}

							tablaReuniones.setItems(listaReuniones);

							// Obtener la ventana actual
							Stage stage = (Stage) btnBuscar.getScene().getWindow();

							// Cerrar la ventana
							stage.close();						

							JOptionPane.showMessageDialog(null,"\t\tBusqueda Completada ","Busqueda",JOptionPane.INFORMATION_MESSAGE);
						}else{
							JOptionPane.showMessageDialog(null, "Por favor, ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
						}
						
					});

					// Crear el contenedor para los componentes
					VBox vbox = new VBox(10);
					vbox.setAlignment(Pos.CENTER);
					vbox.setPadding(new Insets(20));
					vbox.getChildren().addAll(lblID, textFieldID, btnBuscar);

					// Crear una nueva ventana emergente y configurarla
					Stage stage = new Stage();
					stage.initModality(Modality.APPLICATION_MODAL);
					stage.setTitle("Buscar Reunión por ID");
					stage.setScene(new Scene(vbox));

					// Establecer estilos para la ventana emergente
					stage.getScene().getRoot().setStyle("-fx-background-color: #f4f4f4;");
					stage.setResizable(false);

					// Mostrar la ventana emergente
					stage.showAndWait();
			} else if (resultadoM.get() == botonFYH) {
				
				// Crear los DatePickers para seleccionar las fechas
				DatePicker datePickerInicio = new DatePicker();
				DatePicker datePickerFin = new DatePicker();
				
				datePickerInicio.setEditable(false);
				datePickerFin.setEditable(false);
				
				// Establecer estilos para los DatePickers
				datePickerInicio.setStyle("-fx-font-size: 14;");
				datePickerFin.setStyle("-fx-font-size: 14;");

				// Crear etiquetas "Desde" y "Hasta"
				Label lblDesde = new Label("Desde:");
				Label lblHasta = new Label("Hasta:");
				lblDesde.setStyle("-fx-font-size: 14;");
				lblHasta.setStyle("-fx-font-size: 14;");

				// Crear el botón de búsqueda
				Button btnBuscar = new Button("Buscar");
				btnBuscar.setStyle("-fx-font-size: 14; -fx-padding: 8 16; -fx-background-color: #4CAF50; -fx-text-fill: white;");
				btnBuscar.setOnAction(e -> {
					
					String fechaInicio = "";
					String fechaFin = "";

					LocalDate fechaInicioSeleccionada = datePickerInicio.getValue();
					LocalDate fechaFinSeleccionada = datePickerFin.getValue();
					if(fechaInicioSeleccionada != null && fechaFinSeleccionada != null){
						fechaInicio = fechaInicioSeleccionada.toString();
						fechaFin = fechaFinSeleccionada.toString();
						consulta = "SELECT * FROM \"REUNION\" WHERE \"fecha\" BETWEEN '"+fechaInicio+"' AND '"+fechaFin+"';";
						listaReuniones.clear();
						try{
							ResultSet resultado = conexion.hacerConsulta(consulta);
							while(resultado.next()){
								Reunion reunionTabla = new Reunion();
								reunionTabla.setId(resultado.getString("id_Reunion"));
								String fechaString = resultado.getString("fecha");
								LocalDateTime fecha = LocalDateTime.parse(fechaString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
								reunionTabla.setFecha(fecha);
								listaReuniones.add(reunionTabla);
							}
						}catch(Exception t){
							JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado. \n\t Error: "+t.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
						}
					}else if(fechaInicioSeleccionada != null && fechaFinSeleccionada == null){
						fechaInicio = fechaInicioSeleccionada.toString();
						listaReuniones.clear();
						consulta = "SELECT * FROM \"REUNION\" WHERE \"fecha\" > '"+fechaInicio+"';";
						try{
							
							ResultSet resultado = conexion.hacerConsulta(consulta);
							while(resultado.next()){
								Reunion reunionTabla = new Reunion();
								reunionTabla.setId(resultado.getString("id_Reunion"));
								String fechaString = resultado.getString("fecha");
								LocalDateTime fecha = LocalDateTime.parse(fechaString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
								reunionTabla.setFecha(fecha);
								listaReuniones.add(reunionTabla);
							}
						}catch(Exception t){
							JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado. \n\t Error: "+t.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
						}
					}else if(fechaInicioSeleccionada == null && fechaFinSeleccionada != null){
						fechaFin = fechaFinSeleccionada.toString();
						listaReuniones.clear();
						consulta = "SELECT * FROM \"REUNION\" WHERE \"fecha\" < '"+fechaFin+"';";
						try{
							ResultSet resultado = conexion.hacerConsulta(consulta);
							while(resultado.next()){
								Reunion reunionTabla = new Reunion();
								reunionTabla.setId(resultado.getString("id_Reunion"));
								String fechaString = resultado.getString("fecha");
								LocalDateTime fecha = LocalDateTime.parse(fechaString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
								reunionTabla.setFecha(fecha);
								listaReuniones.add(reunionTabla);
							}
						}catch(Exception t){
							JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado. \n\t Error: "+t.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
						}
					}else{
						JOptionPane.showMessageDialog(null,"\t\tOcurrió un error, debe ingresar almenos la fecha de inicio");
					}

					
					tablaReuniones.setItems(listaReuniones);
					// Obtener la ventana actual
					Stage stage = (Stage) btnBuscar.getScene().getWindow();

					// Cerrar la ventana
					stage.close();
				});

				// Crear el contenedor para los componentes
				VBox vbox = new VBox(10);
				vbox.setAlignment(Pos.CENTER);
				vbox.setPadding(new Insets(20));
				vbox.getChildren().addAll(lblDesde, datePickerInicio, lblHasta, datePickerFin, btnBuscar);

				// Crear una nueva ventana emergente y configurarla
				Stage stage = new Stage();
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.setTitle("Buscar Reuniones entre Fechas");
				stage.setScene(new Scene(vbox));

				// Establecer estilos para la ventana emergente
				stage.getScene().getRoot().setStyle("-fx-background-color: #f4f4f4;");
				stage.setResizable(false);

				// Mostrar la ventana emergente
				stage.showAndWait();
			
			} else {
				// El usuario seleccionó cancelar o cerró la ventana
			}
		}
		
		listaPersonas.clear();
		tablaAsistencia.setItems(listaPersonas);
		listaExpedientes.clear();
		tablaExpediente.setItems(listaExpedientes);
		
	}
	
	//--------------------Acción del boton Eliminar Reunion--------------------\\
	@FXML
	private void TocarBotonEliminar(ActionEvent event){
		
		Reunion reunion = new Reunion();
		reunion.setId(labelID.getText());
		
		consulta = "select * from \"REUNION\" where \"id_Reunion\" = '"+reunion.getId()+"';";
		ResultSet resultado = conexion.hacerConsulta(consulta);
		try{
			if(resultado.next()){
				String fechaString = resultado.getString("fecha");
				LocalDateTime fecha = LocalDateTime.parse(fechaString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				reunion.setFecha(fecha);
				
				Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
				alerta.setTitle("Confirmación");
				alerta.setHeaderText(null);
				alerta.setContentText("¿Estás seguro que desea eliminar esta Reunion?\n Los datos son los siguientes: \n\tID = "+ reunion.getId() +"\n\tFecha y hora de la reunión = "+ reunion.getFecha());

				//Creación de los botones de aceptar y cancelar
				ButtonType botonAceptar = new ButtonType("Aceptar");
				ButtonType botonCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

				Optional<ButtonType> resultadoA = alerta.showAndWait();

				// Inicializar la lista de acciones
				if (resultadoA.isPresent() && resultadoA.get() == ButtonType.OK) {
					
					//Eliminamos todas las personas que asistirán
					
					consulta = "DELETE FROM \"ASISTENCIA\" WHERE \"id_Reunion\" = '"+reunion.getId()+"';";
					if(conexion.hacerCambios(consulta)){
						JOptionPane.showMessageDialog(null,"\t\t¡Asistencias asociadas eliminadas!");
					}
					
					//Eliminamos las asociaciones entre Reunión y Expediente
					
					consulta = "DELETE FROM \"REUNION_EXPEDIENTE\" WHERE \"id_Reunion\" = '"+reunion.getId()+"';";
					if(conexion.hacerCambios(consulta)){
						JOptionPane.showMessageDialog(null,"\t\t¡Expedientes asociadas eliminados!");
					}
					
					//Eliminamos todas las minutas relacionadas con la Reunión
					
					consulta = "DELETE FROM \"MINUTA\" WHERE \"id_Reunion\" = '"+reunion.getId()+"';";
					if(conexion.hacerCambios(consulta)){
						JOptionPane.showMessageDialog(null,"\t\t¡Minutas asociadas eliminadas!");
					}
					
					//Eliminamos la Reunion
					consulta = "DELETE FROM \"REUNION\" WHERE \"id_Reunion\" = '"+reunion.getId()+"';";
					if(conexion.hacerCambios(consulta)){
						JOptionPane.showMessageDialog(null,"\t\t¡Reunión eliminada satisfactoriamente!");
					}
				}	
			}
			listaPersonas.clear();
			tablaAsistencia.setItems(listaPersonas);
			listaExpedientes.clear();
			tablaExpediente.setItems(listaExpedientes);

			labelID.setText("");
			labelE.setText("");
			labelHora.setText("");
			labelDNI.setText("");
			DateFecha.setValue(null);
			ActualizarLista();
		}catch(Exception e){
			JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado \nError:  " + e.getMessage());
		}
		
	}
	
	//--------------------Acción del boton de Modificar Reunion--------------------\\ TODO LISTO
	@FXML
	private void TocarBotonModificar(ActionEvent event){
		
		
		Reunion reunion = new Reunion();
		reunion.setId(labelID.getText());
		consulta = "SELECT * FROM \"REUNION\" WHERE \"id_Reunion\" = '"+reunion.getId()+"';";
		ResultSet resultado = conexion.hacerConsulta(consulta);
		boolean verificacionFecha = false;
		try{
			if(resultado.next()){
				String fechaString = resultado.getString("fecha");
				LocalDateTime fecha = LocalDateTime.parse(fechaString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				reunion.setFecha(fecha);
				verificacionFecha = reunion.getFecha().toLocalDate().isBefore(LocalDate.now());
			}else{
				verificacionFecha = false;
				JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado");
			}
		}catch(Exception e){
			JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado \nError:  " + e.getMessage());
			verificacionFecha = false;
		}
		
		if(!verificacionFecha){
			//Verificamos que la hora y la fecha tengan sus formatos
			if (verificarFormatoHora(labelHora.getText())){
				try{
					LocalTime hora = LocalTime.parse(labelHora.getText());
					LocalDate dia = DateFecha.getValue();
					if (verificarHora(hora)){
						reunion.setFecha(crearFechaYHora(hora,dia));

						// Creamos el mensaje con el contructor de string													
						StringBuilder mensaje = new StringBuilder();

						//Agregams todos los mensajes al constructor
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
						mensaje.append("¿Estás seguro que desea modificar esta reunión?\n");
						mensaje.append("Los datos son los siguientes:\n");
						mensaje.append("\tID = ").append(reunion.getId()).append("\n");
						mensaje.append("\tProgramado para el día ").append(dia.format(formatter))
							   .append(", en el horario de ").append(hora).append("\n");

						// Agregar las personas que asistirán a la reunión
						mensaje.append("Personas que asistirán:\n");
						for (Persona persona : listaPersonas) {
							mensaje.append("\t-> ").append(persona.getDni()).append(" - ").append(persona.getNombre()).append(" - ").append(persona.getApellido()).append("\n");
						}

						//Agregamos los expedientes que se tratan en la reunión
						mensaje.append("Expedientes que se tratarán:\n");
						String estado = "";
						for (Expediente expediente : listaExpedientes) {
							if (expediente.isEstado()){
								estado = "Abierto";
							}else{
								estado = "Cerrado";
							}
							mensaje.append("\t-> ").append(expediente.getId()).append(" - ").append(expediente.getTextoNota()).append(" - ").append(estado).append("\n");
						}

						//Creamos la alerta
						Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
						alerta.setTitle("Confirmación");
						alerta.setHeaderText(null);
						alerta.setContentText(mensaje.toString());

						//Creación de los botones de aceptar y cancelar
						ButtonType botonAceptar = new ButtonType("Aceptar");
						ButtonType botonCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

						Optional<ButtonType> resultadoAlerta = alerta.showAndWait();

						// Comenzamos el guardado
						if (resultadoAlerta.isPresent() && resultadoAlerta.get() == ButtonType.OK) {

							consulta = "UPDATE \"REUNION\" SET \"fecha\" = '"+reunion.getFecha()+"'WHERE \"id_Reunion\" = '"+reunion.getId()+"';";

							try{
								if(conexion.hacerCambios(consulta)){ //Ya se guarda en la bd la reunión
									//Vinculamos las personas que asistirán
									consulta = "SELECT P.DNI, P.Nombre, P.Apellido, P.Correo FROM \"PERSONA\" P JOIN \"ASISTENCIA\" A ON P.DNI = A.DNI WHERE A.\"id_Reunion\" = '"+ reunion.getId() +"';";
									resultado = conexion.hacerConsulta(consulta);;
									ObservableList<Persona> listaPersonasOld = FXCollections.observableArrayList();
									try{
										while(resultado.next()){
											Persona personaOld = new Persona();

											personaOld.setDni(resultado.getString("dni"));
											personaOld.setNombre(resultado.getString("nombre"));
											personaOld.setApellido(resultado.getString("apellido"));
											personaOld.setCorreo(resultado.getString("correo"));

											listaPersonasOld.add(personaOld);
										} 
									}catch(Exception e){
										JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado \n\n error : "+ e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
									}
									for (Persona personaOld : listaPersonasOld) {
										if (listaPersonas.contains(personaOld)==false) {
											// Si la persona en listaPersonasOld no está en listaPersonas, eliminarla de la base de datos
											consulta = "DELETE FROM \"ASISTENCIA\" WHERE \"dni\" = '"+personaOld.getDni()+"' AND \"id_Reunion\" = '"+reunion.getId()+"';";	
											if(!conexion.hacerCambios(consulta)){
												JOptionPane.showMessageDialog(null,"\t\tOcurrió un error al intentar eliminar a la persona con DNI "+personaOld.getDni()+" de la base de datos\n\t","Error",JOptionPane.ERROR_MESSAGE);
											}
										}
									}
									//Vinculamos los expedientes que se tratarán
									for (Persona persona : listaPersonas) {
										if (listaPersonasOld.contains(persona) == false) {
											// Si la persona en listaPersonas no está en listaPersonasOld, agregarla a la base de datos
											consulta = "INSERT INTO \"ASISTENCIA\" (\"dni\", \"id_Reunion\") VALUES ('"+persona.getDni()+"','"+reunion.getId()+"');";
											if(!conexion.hacerCambios(consulta)){
												JOptionPane.showMessageDialog(null,"\t\tOcurrió un error al intentar insertar a la persona con DNI "+persona.getDni()+" de la base de datos\n\t","Error",JOptionPane.ERROR_MESSAGE);
											}
										}
									}
									
									consulta = "SELECT E.* FROM \"EXPEDIENTE\" E JOIN \"REUNION_EXPEDIENTE\" RE ON E.\"id_Expediente\" = RE.\"id_Expediente\" WHERE RE.\"id_Reunion\" = '"+reunion.getId()+"';";
									resultado = conexion.hacerConsulta(consulta);
									ObservableList<Expediente> listaExpedientesOld = FXCollections.observableArrayList();
									try{
										while(resultado.next()){
											Expediente expedienteOld = new Expediente();

											expedienteOld.setId(resultado.getString("id_Expediente"));
											expedienteOld.setTextoNota(resultado.getString("textoNota"));
											String fechaString = resultado.getString("fechaIngreso");
											LocalDateTime fechaE = LocalDateTime.parse(fechaString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
											expedienteOld.setFechaIngreso(fechaE);
											expedienteOld.setEstado(resultado.getBoolean("estado"));

											listaExpedientesOld.add(expedienteOld);
										} 
									}catch(Exception e){
										JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado\n\n error : "+ e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
									}
									
									
									for (Expediente expedienteOld : listaExpedientesOld) {
										if (listaPersonas.contains(expedienteOld)==false) {
											// Si la persona en listaPersonasOld no está en listaPersonas, eliminarla de la base de datos
											consulta = "DELETE FROM \"REUNION_EXPEDIENTE\" WHERE \"id_Reunion\" = '"+reunion.getId()+"' AND \"id_Expediente\" = '"+expedienteOld.getId()+"';";	
											if(!conexion.hacerCambios(consulta)){
												JOptionPane.showMessageDialog(null,"\t\tOcurrió un error al intentar eliminar al expediente con ID: "+expedienteOld.getId()+" de la base de datos\n\t","Error",JOptionPane.ERROR_MESSAGE);
											}
										}
									}
									//Vinculamos los expedientes que se tratarán
									for (Expediente expediente : listaExpedientes) {
										if (listaExpedientesOld.contains(expediente) == false) {
											// Si la persona en listaPersonas no está en listaPersonasOld, agregarla a la base de datos
											consulta = "INSERT INTO \"REUNION_EXPEDIENTE\" (\"id_Reunion\", \"id_Expediente\") VALUES ('"+reunion.getId()+"','"+expediente.getId()+"');";
											if(!conexion.hacerCambios(consulta)){
												JOptionPane.showMessageDialog(null,"\t\tOcurrió un error al intentar insertar al expediente con ID: "+expediente.getId()+" en la base de datos\n\t","Error",JOptionPane.ERROR_MESSAGE);
											}
										}
									}
									JOptionPane.showMessageDialog(null,"\t\tLa reunión y los datos asociados se guardaron correctamente!!","Realizado",JOptionPane.INFORMATION_MESSAGE);
							
								}else{
									JOptionPane.showMessageDialog(null,"\t\tOcurrió un error, no se logró guardar la reunión \n\t","Error",JOptionPane.ERROR_MESSAGE);
								}
							}catch(Exception e){
								JOptionPane.showMessageDialog(null,"\t\tOcurrió un error\n\t"+e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
							}
							ActualizarLista();
							listaPersonas.clear();
							tablaAsistencia.setItems(listaPersonas);
							listaExpedientes.clear();
							tablaExpediente.setItems(listaExpedientes);
						}
					}else{
						JOptionPane.showMessageDialog(null,"\t\tOcurrió un error, al parecer la hora no se encuentra en el rango permitido. \n\t El rango permitido es desde las 08:00 hasta las 20:00","Error",JOptionPane.ERROR_MESSAGE);
					}
				}catch(Exception e){
					JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado \n"+ e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
				}
			}else{
				JOptionPane.showMessageDialog(null,"\t\tOcurrió un error, Al parecer la hora no se encuentra en un formato correcto! \n\t Recuerde que el formato es \"HH:mm\" ","Error",JOptionPane.ERROR_MESSAGE);
			}
		}else{
			JOptionPane.showMessageDialog(null,"\t\t No se puede modificar reuniones que ya se realizaron");
		}
		
		
	}
	
	//--------------------Acción del boton de Actualizar lista--------------------\\ Todo listo
	@FXML
	private void TocarBotonActualizar(ActionEvent event){
		
		ActualizarLista();
		
	}
	
	//--------------------Acción del boton de Limpiar--------------------\\ Todo listo
	@FXML
	private void TocarBLimpiar(ActionEvent event){
		
		Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
		alerta.setTitle("Confirmación");
		alerta.setHeaderText(null);
		alerta.setContentText("¿Que desea vaciar?\nLas tablas, los campos o ambos?");

		// Creación de los botones
		ButtonType botonTabla = new ButtonType("Tablas");
		ButtonType botonCampos = new ButtonType("Campos");
		ButtonType botonAmbos = new ButtonType("Ambos");

		// Agregar los botones a la ventana de alerta
		alerta.getButtonTypes().setAll(botonTabla, botonCampos, botonAmbos, ButtonType.CANCEL);

		// Mostrar la ventana de alerta y obtener la respuesta
		Optional<ButtonType> resultado = alerta.showAndWait();
		
		// Verificar la opción seleccionada por el usuario
		if (resultado.isPresent()) {
			if (resultado.get() == botonTabla) {
				listaPersonas.clear();
				tablaAsistencia.setItems(listaPersonas);
				listaExpedientes.clear();
				tablaExpediente.setItems(listaExpedientes);
			} else if (resultado.get() == botonCampos) {
				labelID.setText("");
				labelE.setText("");
				labelHora.setText("");
				labelDNI.setText("");
				DateFecha.setValue(null);
			} else if (resultado.get() == botonAmbos) {
				listaPersonas.clear();
				tablaAsistencia.setItems(listaPersonas);
				listaExpedientes.clear();
				tablaExpediente.setItems(listaExpedientes);
				
				labelID.setText("");
				labelE.setText("");
				labelHora.setText("");
				labelDNI.setText("");
				DateFecha.setValue(null);
			} else {
				// El usuario seleccionó cancelar o cerró la ventana
			}
		}		
		
		tablaReuniones.getSelectionModel().clearSelection();
		
	}
	
	//--------------------Acción de agregar persona--------------------\\ Todo listo
	@FXML
	private void TocarBAP(ActionEvent event){
		Persona persona = new Persona();
		
		persona.setDni(labelDNI.getText());
		
		consulta = "select * from \"PERSONA\" where \"dni\" = '"+persona.getDni()+"'";
		ResultSet resultado = conexion.hacerConsulta(consulta);
		try{
			if(resultado.next()){
				persona.setNombre(resultado.getString("nombre"));
				persona.setApellido(resultado.getString("apellido"));
				persona.setCorreo(resultado.getString("correo"));
				
				listaPersonas.add(persona);
				
			}else{
				JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado, no se encontró a la persona con DNI " +persona.getDni()+". Intentelo nuevamente");
			}
		}catch(Exception e){
			
		}
		
		tablaAsistencia.setItems(listaPersonas);
		
		labelDNI.clear();
		labelDNI.setEditable(true);
	}
	
	//--------------------Acción de quitar persona--------------------\\ Todo listo
	@FXML
	private void TocarBQP(ActionEvent event){
		boolean i = false;
		for (Persona persona : listaPersonas) {
			// Verifica si el DNI de la persona coincide con el valor deseado
			if (persona.getDni().equals(labelDNI.getText())) {
				// Elimina la persona de la lista
				listaPersonas.remove(persona);
				i=true;
				break; 
			}
		}
		if (!i){
			JOptionPane.showMessageDialog(null,"\t\tOcurrió un error, no se encontró la persona que desea quitar","Error",JOptionPane.ERROR_MESSAGE);
		}
		labelDNI.clear();
		labelDNI.setEditable(true);
		tablaAsistencia.setItems(listaPersonas);

	}
	
	//--------------------Acción de agregar expediente--------------------\\ Todo listo
	@FXML
	private void TocarBAE(ActionEvent event){
		Expediente expediente = new Expediente();
		
		expediente.setId(labelE.getText());
		
		consulta = "select * from \"EXPEDIENTE\" where \"id_Expediente\" = '"+expediente.getId()+"'";
		ResultSet resultado = conexion.hacerConsulta(consulta);
		try{
			if(resultado.next()){
				expediente.setTextoNota(resultado.getString("textoNota"));
				String fechaString = resultado.getString("fechaIngreso");
				LocalDateTime fecha = LocalDateTime.parse(fechaString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				expediente.setFechaIngreso(fecha);
				expediente.setEstado(resultado.getBoolean("estado"));
				if(expediente.isEstado()==false){
					Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
					alerta.setTitle("Confirmación");
					alerta.setHeaderText(null);
					alerta.setContentText("Está a punto de vincular un expediente cerrado\n\t ¿Desea continuar?");

					//Creación de los botones de aceptar y cancelar
					ButtonType botonAceptar = new ButtonType("Si, deseo continuar");
					ButtonType botonCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

					Optional<ButtonType> resultadoR = alerta.showAndWait();

					if (resultadoR.isPresent() && resultadoR.get() == ButtonType.OK) {				
						listaExpedientes.add(expediente);
					} else {
						// El usuario ha hecho clic en "Cancelar" o ha cerrado la ventana
					}
				}else{
					listaExpedientes.add(expediente);
				}
			}else{
				JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado, no se encontró el expediente con ID " +expediente.getId()+". Intentelo nuevamente","Error",JOptionPane.ERROR_MESSAGE);
			}
		}catch(Exception e){
			JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado \n\n" + e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
		tablaExpediente.setItems(listaExpedientes);
		labelE.clear();
		labelE.setEditable(true);
	}
	
	//--------------------Acción de quitar Expediente--------------------\\ Todo listo
	@FXML
	private void TocarBQE(ActionEvent event){
		boolean i = false;
		for (Expediente expediente : listaExpedientes) {
			// Verifica si el ID del expediente coincide con el valor deseado
			if (expediente.getId().equals(labelE.getText())) {
				// Elimina la persona de la lista
				listaExpedientes.remove(expediente);
				i=true;
				break; 
			}
		}
		if (!i){
			JOptionPane.showMessageDialog(null,"\t\tOcurrió un error, no se encontró el expediente que desea quitar","Error",JOptionPane.ERROR_MESSAGE);
		}
		tablaExpediente.setItems(listaExpedientes);
		labelE.clear();
		labelE.setEditable(true);
	}
			
	//--------------------Acción de los botones del menú--------------------\\ Falta
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
	
	//--------------------Metodos privados extras --------------------\\
	
	private  void ActualizarLista(){
		// Vaciamos la lista por completo
		tablaReuniones.getItems().clear();

		// Agregar las columnas a la tabla
		if (tablaReuniones.getColumns().isEmpty()) {
			tablaReuniones.getColumns().addAll(columnaIDR, columnaFechaR);
		}

		// Conección a la bd y mostrar los contenidos.  
		ConexionBD conexion = new ConexionBD();
		conexion.getConexion();
		String consulta = "SELECT * FROM \"REUNION\" WHERE \"fecha\" > '"+LocalDate.now()+"';";

		try (ResultSet resultadoConsulta = conexion.hacerConsulta(consulta)) {

			while (resultadoConsulta.next()) {
				Reunion reunionTabla = new Reunion();
				reunionTabla.setId(resultadoConsulta.getString("id_Reunion"));
				String fechaString = resultadoConsulta.getString("fecha");
				LocalDateTime fecha = LocalDateTime.parse(fechaString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				reunionTabla.setFecha(fecha);
				listaReuniones.add(reunionTabla);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado al intentar cargar en la base de datos :/ \nError:  " + e.getMessage());
		}
		
		listaPersonas.clear();
		tablaAsistencia.setItems(listaPersonas);
		listaExpedientes.clear();
		tablaExpediente.setItems(listaExpedientes);

		labelID.setText("");
		labelE.setText("");
		labelHora.setText("");
		labelDNI.setText("");
		DateFecha.setValue(null);
		tablaReuniones.setItems(listaReuniones);
	}

	private String formatoFechaHora(LocalDateTime fechaHora) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy   HH:mm");
		return fechaHora.format(formatter);
	}
	
	private boolean verificarHora(LocalTime hora){
		boolean horaCheck;

		// Verifica si la hora está dentro del rango permitido (08:00 - 20:00)
		if (hora.isAfter(LocalTime.of(7, 59)) && hora.isBefore(LocalTime.of(20, 1))) {
			horaCheck = true;
		} else {
			horaCheck = false;
		}
		
		return horaCheck;
	}
	
	private LocalDateTime crearFechaYHora(LocalTime hora, LocalDate fecha){
		LocalDateTime fechaC;
		
		if (verificarHora(hora)){
			fechaC = LocalDateTime.of(fecha, hora);
		}else{
			JOptionPane.showMessageDialog(null,"\t\tLa hora no se encuentra entre las 08:00 y las 20:00");
			fechaC=null;
		}
		
		return fechaC;
	}
	
	private boolean verificarFormatoHora(String horaString){
		try {
			// Intenta analizar el String como LocalTime
			LocalTime.parse(horaString);
			// Si no se lanza una excepción, el formato es válido
			return true;
		} catch (Exception e) {
			// Si se lanza una excepción, el formato no es válido
			return false;
        }
		
	}
	
	//--------------------Metodo de inicialización--------------------\\
	@Override
	public void initialize (URL url, ResourceBundle rb){
		
		// BooleanBinding para verificar si hay un elemento seleccionado en la tabla de reuniones
		BooleanBinding elementoSeleccionadoTablaReuniones = Bindings.isNotEmpty(tablaReuniones.getSelectionModel().getSelectedItems());

		// Asociar el BooleanBinding con la propiedad disable de labelID
		labelID.disableProperty().bind(elementoSeleccionadoTablaReuniones);

		// BooleanBinding para verificar si labelID está habilitado para edición
		BooleanProperty labelIDEditable = labelID.editableProperty();

		// BooleanBinding para verificar si todos los campos están completos, incluyendo las listas
		BooleanBinding camposCompletos = Bindings.createBooleanBinding(() ->
				!labelID.getText().isEmpty() &&
						DateFecha.getValue() != null &&
						!labelHora.getText().isEmpty() &&
						!listaExpedientes.isEmpty() &&
						!listaPersonas.isEmpty(),
				labelID.textProperty(), DateFecha.valueProperty(), labelHora.textProperty(), 
				listaExpedientes, listaPersonas);

		// Obtener la fecha actual
		LocalDate fechaActual = LocalDate.now();

		// Crear el BooleanBinding para verificar si la fecha seleccionada es anterior a la fecha actual
		BooleanBinding fechaAnterior = Bindings.createBooleanBinding(() ->
				DateFecha.getValue() != null && DateFecha.getValue().isBefore(fechaActual),
				DateFecha.valueProperty());

		// Crear el BooleanBinding para verificar si hay un elemento seleccionado en la tablaReuniones
		BooleanBinding elementoSeleccionado = Bindings.isNotEmpty(tablaReuniones.getSelectionModel().getSelectedItems());

		// Asociar el BooleanBinding con las propiedades disable de los botones
		BNuevo.disableProperty().bind(elementoSeleccionado.or(labelIDEditable.not().or(camposCompletos.not())));
		BEliminar.disableProperty().bind(labelIDEditable.not());
		BModificar.disableProperty().bind(labelIDEditable.not().or(fechaAnterior));
		BAE.disableProperty().bind(fechaAnterior);
		BQE.disableProperty().bind(fechaAnterior);
		BAP.disableProperty().bind(fechaAnterior);
		BQP.disableProperty().bind(fechaAnterior);
		botonAleatorio.disableProperty().bind(elementoSeleccionadoTablaReuniones);
		
		
		
		//Hacer que solo se puedan seleccionar fechas a partir de mañana.
		DateFecha.setDayCellFactory(picker -> new DateCell() {
			@Override
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);

				// Deshabilitar las fechas anteriores a hoy
				setDisable(date.isBefore(LocalDate.now().plusDays(1)));
			}
		});
		// Configurar el manejo de cambios de selección en la tabla
		tablaAsistencia.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Persona>() {
			@Override
			public void changed(ObservableValue<? extends Persona> observable, Persona oldValue, Persona nuevoValor) {
				if (nuevoValor != null) {
					// Actualizar los TextField y DatePicker con los valores seleccionados
					labelDNI.setText(nuevoValor.getDni());
					labelDNI.setEditable(false);
				}
			}
		});
		
		tablaExpediente.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Expediente>() {
			@Override
			public void changed(ObservableValue<? extends Expediente> observable, Expediente oldValue, Expediente nuevoValor) {
				if (nuevoValor != null) {
					// Actualizar los TextField y DatePicker con los valores seleccionados
					labelE.setText(nuevoValor.getId());
				}
			}
		});
		
		tablaReuniones.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Reunion>() {
			@Override
			public void changed(ObservableValue<? extends Reunion> observable, Reunion viejaReunion, Reunion nuevaReunion) {
				if (nuevaReunion != null) {
					//Limpiamos las tablas
					tablaExpediente.getItems().clear();
					tablaAsistencia.getItems().clear();
					
					//Obtengo el Id
					labelID.setText(nuevaReunion.getId());
					
					//Obtengo la fecha
					LocalDateTime dateTime = nuevaReunion.getFecha();
					
					//Separo el LocalDateTime a LocalDate y LocalTime por separados
					LocalDate fecha = dateTime.toLocalDate();
					LocalTime hora = dateTime.toLocalTime();
					
					//Coloco el valo de la fecha en el DatePicker
					DateFecha.setValue(fecha);
					
					//Formateo la hora en formato HH:mm
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
					String horaFormateada = hora.format(formatter);
					
					//Coloco la hora formateada en el TextField
					labelHora.setText(horaFormateada);
					
					//Consulta para obtener todas las personas asociadas a dicha reunión.
					consulta = "SELECT P.DNI, P.Nombre, P.Apellido, P.Correo FROM \"PERSONA\" P JOIN \"ASISTENCIA\" A ON P.DNI = A.DNI WHERE A.\"id_Reunion\" = '"+ nuevaReunion.getId() +"';";
					ResultSet resultado = conexion.hacerConsulta(consulta);
					try{
						listaPersonas.clear();
						while(resultado.next()){
							Persona persona = new Persona();
							
							persona.setDni(resultado.getString("dni"));
							persona.setNombre(resultado.getString("nombre"));
							persona.setApellido(resultado.getString("apellido"));
							persona.setCorreo(resultado.getString("correo"));
							
							listaPersonas.add(persona);
						} 
					}catch(Exception e){
						JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado \n\n error : "+ e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
					}
					
					//Consulta para obtener todas las personas asociadas a dicha reunión.
					consulta = "SELECT E.* FROM \"EXPEDIENTE\" E JOIN \"REUNION_EXPEDIENTE\" RE ON E.\"id_Expediente\" = RE.\"id_Expediente\" WHERE RE.\"id_Reunion\" = '"+nuevaReunion.getId()+"';";
					resultado = conexion.hacerConsulta(consulta);
					try{
						listaExpedientes.clear();
						while(resultado.next()){
							Expediente expediente = new Expediente();
							
							expediente.setId(resultado.getString("id_Expediente"));
							expediente.setTextoNota(resultado.getString("textoNota"));
							String fechaString = resultado.getString("fechaIngreso");
							LocalDateTime fechaE = LocalDateTime.parse(fechaString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
							expediente.setFechaIngreso(fechaE);
							expediente.setEstado(resultado.getBoolean("estado"));

							listaExpedientes.add(expediente);
						} 
					}catch(Exception e){
						JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado\n\n error : "+ e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
					}
					
					tablaExpediente.setItems(listaExpedientes);
					tablaAsistencia.setItems(listaPersonas);
				}
			}
		});
		
		// Configuración de las tablas
		columnaIDR.setCellValueFactory(new PropertyValueFactory<Reunion,String>("id")); 
		columnaFechaR.setCellValueFactory(cellData -> {
			Object value = cellData.getValue();
			if (value instanceof Reunion) {
				Reunion reunion = (Reunion) value;
				LocalDateTime fecha = reunion.getFecha();
				return new SimpleStringProperty(formatoFechaHora(fecha));
			}
			return null;
		});
		columnaIDE.setCellValueFactory(new PropertyValueFactory<Expediente,String>("id"));
		columnaNotaE.setCellValueFactory(new PropertyValueFactory<Expediente,String>("textoNota"));
		columnaFechaE.setCellValueFactory(cellData -> {
			Object value = cellData.getValue();
			if (value instanceof Expediente) {
				Expediente expediente = (Expediente) value;
				LocalDateTime fecha = expediente.getFechaIngreso();
				return new SimpleStringProperty(formatoFechaHora(fecha));
			}
			return null;
		});
		columnaEstadoE.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Expediente, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<Expediente, String> expediente) {
						String estadoTexto = expediente.getValue().isEstado() ? "Abierto" : "Cerrado";
						return new SimpleStringProperty(estadoTexto);
					}
				});
		columnaDNI.setCellValueFactory(new PropertyValueFactory<Persona,String>("dni"));
		columnaNombre.setCellValueFactory(new PropertyValueFactory<Persona,String>("nombre"));
		columnaApellido.setCellValueFactory(new PropertyValueFactory<Persona,String>("apellido"));
		columnaCorreo.setCellValueFactory(new PropertyValueFactory<Persona,String>("correo"));
		ActualizarLista();
		
	}
}
