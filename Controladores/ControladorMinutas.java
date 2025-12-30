package com.lucas.proyecto.Controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.lucas.proyecto.Clases.ConexionBD;
import com.lucas.proyecto.Clases.Minuta;
import com.lucas.proyecto.Clases.Reunion;
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

public class ControladorMinutas implements Initializable {

	//Inicializamos todas los componentes

	@FXML
	private TextField  labelID;

	@FXML
	private  TextField labelExp;
	
	@FXML
	private TextField  labelReu;
	
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
	private TableView<Minuta> tabla;
	
	@FXML
	private TableColumn<Minuta, String> columnaID;
    
	@FXML
	private TableColumn<Minuta, String> columnaIDExpediente;

	@FXML
	private TableColumn<Minuta, String> columnaReunion;

	@FXML
	private TableColumn<Minuta, String> columnaDescripcion;
	
	//Inicializamos dos variables accion y lista de acciones
	
	private ObservableList<Minuta> listaMinutas = FXCollections.observableArrayList();
	private String consulta;
	ConexionBD conexion = new ConexionBD();

	
	//--------------------Acción del boton Aleatorio--------------------\\
	@FXML
	private void TocarBotonAleatorio(ActionEvent event){
		boolean existencia = false;
		String randomID = "";
		ConexionBD conexion = new ConexionBD();
		String consulta = "SELECT COUNT(*) AS existe FROM  \"MINUTA\" WHERE \"id_Minuta\" = ?";
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
		String Reunion = labelReu.getText();
		String Descripcion = textDescri.getText();
		String IDExpediente = labelExp.getText();

		//--------------------Se verifica si los demás datos son correctos
		if (ID != null && IDExpediente != null && Reunion != null & Descripcion != null ) {
			boolean existenciaE = false;
			boolean existenciaR = false;
			boolean existenciaM = false;
			boolean estadoB = false;
			LocalDate Fecha = null;
			java.sql.Timestamp fechaIngreso = null;
			ResultSet resultadoConsulta = null;
			String valorBuscado="";
			String IDReunion = "";
			String estado = "Cerrado";
			String id = null;
			String textoNota = null;
			//--------------------Verificamos que el ID de la minuta no se repita.
			consulta = "SELECT COUNT(*) AS existe FROM  \"MINUTA\" WHERE \"id_Minuta\" = ?";
			valorBuscado =  ID;
			resultadoConsulta = conexion.buscarValor(consulta,valorBuscado);
			try{
				if (resultadoConsulta.next()){
					int existe = resultadoConsulta.getInt("existe");
					if (existe > 0){
						JOptionPane.showMessageDialog(null,"\t\tOcurrió un error, al parecer el ID de la Minuta ya se encuentra en uso\n Verifique los datos y vuelva a seleccionar \"Nueva Minuta\"");
						existenciaM = true;
					}else{
						existenciaM = false;
					}
				}else{
					existenciaM = false;
				}
			}catch (Exception e){
				JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado al verificar el ID de la minuta :/ \nError:  " + e.getMessage());
				existenciaM = false;
			}
			if(existenciaM==false){
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
				
				if(existenciaE){
					//--------------------Verificamos que el ID de la reunion si exista
					valorBuscado = Reunion;
					consulta = "SELECT COUNT(*) AS existe FROM  \"REUNION\" WHERE \"id_Reunion\" = ?";
					resultadoConsulta = conexion.buscarValor(consulta,valorBuscado);
					Fecha = null;
					try{
						if (resultadoConsulta.next()){
							int existe = resultadoConsulta.getInt("existe");
							existenciaR = existe > 0;
							resultadoConsulta=null;
							if (existenciaR){
								consulta = "SELECT * FROM \"REUNION\" WHERE \"id_Reunion\" = '" + Reunion +"';";
								resultadoConsulta = conexion.hacerConsulta(consulta);
								resultadoConsulta.next();
								try{
									IDReunion = resultadoConsulta.getString(1);
									Fecha = resultadoConsulta.getDate(2).toLocalDate();
								}catch(Exception e){
									JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado al obtener los datos de la reunion :/ \nError:  " + e.getMessage());
								}
							}else{
								JOptionPane.showMessageDialog(null,"\t\tOcurrió un error, al parecer el ID de la reunion no se encuentra\n Verifique los datos y vuelva a seleccionar \"Nueva Minuta\"");
							}
						}else{
							existenciaR = false;
							JOptionPane.showMessageDialog(null,"\t\tOcurrió un error, al parecer el ID de la reunion no se encuentra\n Verifique los datos y vuelva a seleccionar \"Nueva Minuta\"");
						}
					}catch (Exception e){
						JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado al verificar el ID de la reunion :/ \nError:  " + e.getMessage());
						existenciaR = false;
					}
				}
				
			}
			if(existenciaM==false && existenciaE==true&&existenciaR==true){
				Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
				alerta.setTitle("Confirmación");
				alerta.setHeaderText(null);
				alerta.setContentText("¿Estás seguro que desea cargar esta nueva minuta?\n Los datos son los siguientes: \n\tID = "+ ID +"\n\tID del Expediente asociado = "+ IDExpediente +"\n\tID de la Reunion asociada = "+ Reunion +"\n\tDescripción = "+Descripcion + "\n\n Datos del Expediente asociado:  \n\tID = "+ id + "\n\tTextoNota = " + textoNota + "\n\tFecha de Ingreso = " + fechaIngreso + "\n\tEstado = " + estado+"\n\n Datos del Expediente asociado:  \n\tID = "+IDReunion+"\n\tFecha  = "+Fecha);
				
				//Creación de los botones de aceptar y cancelar
				ButtonType botonAceptar = new ButtonType("Aceptar");
				ButtonType botonCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

				Optional<ButtonType> resultado = alerta.showAndWait();
				
				// Inicializar la lista de acciones
				if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
										
					consulta = "INSERT INTO \"MINUTA\" (\"id_Minuta\",\"id_Reunion\", \"id_Expediente\", contenido) VALUES ('"+ID+"', '"+Reunion+"', '"+IDExpediente+"', '"+Descripcion+"');";
					
					try{
						if(conexion.hacerCambios(consulta)){
							JOptionPane.showMessageDialog(null,"\t\tSe realizó la carga de la nueva minuta a la base de datos con exito!  ");
						}else{
							JOptionPane.showMessageDialog(null,"\t\tNo se ha realizó la carga de la nueva minuta a la base de datos :/ \n\n Ocurrió un error inesperado");
						}
					}catch (Exception e){
						JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado al intentar cargar en la base de datos :/ \nError:  " + e.getMessage());
					}
				} else {
					// El usuario ha hecho clic en "Cancelar" o ha cerrado la ventana, No guardar la acción, En efectigirigillo
				}
				
				ActualizarLista();
			}	
		}	
	}

	//--------------------Acción del boton de busqueda--------------------\\
	@FXML
	private void TocarBBuscar(ActionEvent event){
		
		Minuta minutaBusqueda = new Minuta();
		minutaBusqueda.setId(labelID.getText());
		minutaBusqueda.setIdExpediente(labelExp.getText());
		minutaBusqueda.setIdReunion(labelReu.getText());
		
		if (minutaBusqueda.getId() != null && minutaBusqueda.getIdExpediente() != null && minutaBusqueda.getIdReunion() != null) {
			
			consulta = "SELECT * FROM \"MINUTA\" WHERE \"id_Minuta\" = '" + minutaBusqueda.getId() + "' OR \"id_Expediente\" = '"+ minutaBusqueda.getIdExpediente() + "'  OR \"id_Reunion\" = '"+ minutaBusqueda.getIdReunion() + "';";
		
			tabla.getItems().clear();
			
			columnaID.setCellValueFactory(new PropertyValueFactory<Minuta,String>("id")); 
			columnaIDExpediente.setCellValueFactory(new PropertyValueFactory<Minuta,String>("idExpediente"));
			columnaReunion.setCellValueFactory(new PropertyValueFactory<Minuta,String>("IDReunion"));
			columnaDescripcion.setCellValueFactory(new PropertyValueFactory<Minuta,String>("Descripcion"));
			
			try(ResultSet resultado = conexion.hacerConsulta(consulta)){
				while (resultado.next()){
					
					Minuta MinutaTabla = new Minuta();

					MinutaTabla.setId(resultado.getString("id_Accion"));
					MinutaTabla.setIdExpediente(resultado.getString("id_Expediente"));
					MinutaTabla.setIdReunion(resultado.getString("id_Reunion")); 
					MinutaTabla.setContenido(resultado.getString("contenido"));

					listaMinutas.add(MinutaTabla);
					
				}
			}catch (Exception e){
				JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado al intentar cargar en la base de datos :/ \nError:  " + e.getMessage());
			}

			tabla.setItems(listaMinutas);

		}else{
			if (minutaBusqueda.getId() != null && minutaBusqueda.getIdExpediente() == null && minutaBusqueda.getIdReunion() == null){
				consulta = "SELECT * FROM \"MINUTA\" WHERE \"id_Minuta\" = '" + minutaBusqueda.getId() + "';";
		
				tabla.getItems().clear();

				columnaID.setCellValueFactory(new PropertyValueFactory<Minuta,String>("id")); 
				columnaIDExpediente.setCellValueFactory(new PropertyValueFactory<Minuta,String>("idExpediente"));
				columnaReunion.setCellValueFactory(new PropertyValueFactory<Minuta,String>("IDReunion"));
				columnaDescripcion.setCellValueFactory(new PropertyValueFactory<Minuta,String>("Contenido"));

				try(ResultSet resultado = conexion.hacerConsulta(consulta)){
					while (resultado.next()){

						Minuta MinutaTabla = new Minuta();

						MinutaTabla.setId(resultado.getString("id_Minuta"));
						MinutaTabla.setIdExpediente(resultado.getString("id_Expediente"));
						MinutaTabla.setIdReunion(resultado.getString("id_Reunion")); 
						MinutaTabla.setContenido(resultado.getString("contenido"));

						listaMinutas.add(MinutaTabla);

					}
				}catch (Exception e){
					JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado al intentar cargar en la base de datos :/ \nError:  " + e.getMessage());
				}

				tabla.setItems(listaMinutas);
			}else if(minutaBusqueda.getId() != null && minutaBusqueda.getIdExpediente() != null && minutaBusqueda.getIdReunion() == null){
				consulta = "SELECT * FROM \"MINUTA\" WHERE \"id_Minuta\" = '"+ minutaBusqueda.getId() + "' OR \"id_Expediente\" = '"+ minutaBusqueda.getIdExpediente() + "';";
		
				tabla.getItems().clear();

				columnaID.setCellValueFactory(new PropertyValueFactory<Minuta,String>("id")); 
				columnaIDExpediente.setCellValueFactory(new PropertyValueFactory<Minuta,String>("idExpediente"));
				columnaReunion.setCellValueFactory(new PropertyValueFactory<Minuta,String>("idReunion"));
				columnaDescripcion.setCellValueFactory(new PropertyValueFactory<Minuta,String>("contenido"));

				try(ResultSet resultado = conexion.hacerConsulta(consulta)){
					while (resultado.next()){

						Minuta MinutaTabla = new Minuta();

						MinutaTabla.setId(resultado.getString("id_Minuta"));
						MinutaTabla.setIdExpediente(resultado.getString("id_Expediente"));
						MinutaTabla.setIdReunion(resultado.getString("id_Reunion")); 
						MinutaTabla.setContenido(resultado.getString("contenido"));

						listaMinutas.add(MinutaTabla);

					}
				}catch (Exception e){
					JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado al intentar cargar en la base de datos :/ \nError:  " + e.getMessage());
				}

				tabla.setItems(listaMinutas);
				
			}else if(minutaBusqueda.getId() == null && minutaBusqueda.getIdExpediente() != null && minutaBusqueda.getIdReunion() == null){
				consulta = "SELECT * FROM \"MINUTA\" WHERE \"id_Expediente\" = '"+ minutaBusqueda.getIdExpediente() + "';";
		
				tabla.getItems().clear();

				columnaID.setCellValueFactory(new PropertyValueFactory<Minuta,String>("id")); 
				columnaIDExpediente.setCellValueFactory(new PropertyValueFactory<Minuta,String>("idExpediente"));
				columnaReunion.setCellValueFactory(new PropertyValueFactory<Minuta,String>("idReunion"));
				columnaDescripcion.setCellValueFactory(new PropertyValueFactory<Minuta,String>("contenido"));

				try(ResultSet resultado = conexion.hacerConsulta(consulta)){
					while (resultado.next()){

						Minuta MinutaTabla = new Minuta();

						MinutaTabla.setId(resultado.getString("id_Minuta"));
						MinutaTabla.setIdExpediente(resultado.getString("id_Expediente"));
						MinutaTabla.setIdReunion(resultado.getString("id_Reunion")); 
						MinutaTabla.setContenido(resultado.getString("contenido"));

						listaMinutas.add(MinutaTabla);

					}
				}catch (Exception e){
					JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado al intentar cargar en la base de datos :/ \nError:  " + e.getMessage());
				}

				tabla.setItems(listaMinutas);
			}else if(minutaBusqueda.getId() == null && minutaBusqueda.getIdExpediente() != null && minutaBusqueda.getIdReunion() != null){
				
				consulta = "SELECT * FROM \"MINUTA\" WHERE \"id_Expediente\" = '"+ minutaBusqueda.getIdExpediente() + "' OR \"id_Reunion\" = '"+ minutaBusqueda.getIdReunion() + "';";
		
				tabla.getItems().clear();

				columnaID.setCellValueFactory(new PropertyValueFactory<Minuta,String>("id")); 
				columnaIDExpediente.setCellValueFactory(new PropertyValueFactory<Minuta,String>("idExpediente"));
				columnaReunion.setCellValueFactory(new PropertyValueFactory<Minuta,String>("idReunion"));
				columnaDescripcion.setCellValueFactory(new PropertyValueFactory<Minuta,String>("contenido"));

				try(ResultSet resultado = conexion.hacerConsulta(consulta)){
					while (resultado.next()){

						Minuta MinutaTabla = new Minuta();

						MinutaTabla.setId(resultado.getString("id_Minuta"));
						MinutaTabla.setIdExpediente(resultado.getString("id_Expediente"));
						MinutaTabla.setIdReunion(resultado.getString("id_Reunion")); 
						MinutaTabla.setContenido(resultado.getString("contenido"));

						listaMinutas.add(MinutaTabla);

					}
				}catch (Exception e){
					JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado al intentar cargar en la base de datos :/ \nError:  " + e.getMessage());
				}

				tabla.setItems(listaMinutas);
			}else if(minutaBusqueda.getId() == null && minutaBusqueda.getIdExpediente() == null && minutaBusqueda.getIdReunion() != null){
				consulta = "SELECT * FROM \"MINUTA\" WHERE \"id_Reunion\" = '"+ minutaBusqueda.getIdReunion() + "';";
		
				tabla.getItems().clear();

				columnaID.setCellValueFactory(new PropertyValueFactory<Minuta,String>("id")); 
				columnaIDExpediente.setCellValueFactory(new PropertyValueFactory<Minuta,String>("idExpediente"));
				columnaReunion.setCellValueFactory(new PropertyValueFactory<Minuta,String>("idReunion"));
				columnaDescripcion.setCellValueFactory(new PropertyValueFactory<Minuta,String>("contenido"));

				try(ResultSet resultado = conexion.hacerConsulta(consulta)){
					while (resultado.next()){

						Minuta MinutaTabla = new Minuta();

						MinutaTabla.setId(resultado.getString("id_Minuta"));
						MinutaTabla.setIdExpediente(resultado.getString("id_Expediente"));
						MinutaTabla.setIdReunion(resultado.getString("id_Reunion")); 
						MinutaTabla.setContenido(resultado.getString("contenido"));

						listaMinutas.add(MinutaTabla);

					}
				}catch (Exception e){
					JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado al intentar cargar en la base de datos :/ \nError:  " + e.getMessage());
				}

				tabla.setItems(listaMinutas);
			}else if(minutaBusqueda.getId() != null && minutaBusqueda.getIdExpediente() == null && minutaBusqueda.getIdReunion() != null){
				consulta = "SELECT * FROM \"MINUTA\" WHERE \"id_Minuta\" = '" + minutaBusqueda.getId() + "' OR \"id_Reunion\" = '"+ minutaBusqueda.getIdReunion() + "';";
		
				tabla.getItems().clear();

				columnaID.setCellValueFactory(new PropertyValueFactory<Minuta,String>("id")); 
				columnaIDExpediente.setCellValueFactory(new PropertyValueFactory<Minuta,String>("idExpediente"));
				columnaReunion.setCellValueFactory(new PropertyValueFactory<Minuta,String>("idReunion"));
				columnaDescripcion.setCellValueFactory(new PropertyValueFactory<Minuta,String>("contenido"));

				try(ResultSet resultado = conexion.hacerConsulta(consulta)){
					while (resultado.next()){

						Minuta MinutaTabla = new Minuta();

						MinutaTabla.setId(resultado.getString("id_Minuta"));
						MinutaTabla.setIdExpediente(resultado.getString("id_Expediente"));
						MinutaTabla.setIdReunion(resultado.getString("id_Reunion")); 
						MinutaTabla.setContenido(resultado.getString("contenido"));

						listaMinutas.add(MinutaTabla);

					}
				}catch (Exception e){
					JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado al intentar cargar en la base de datos :/ \nError:  " + e.getMessage());
				}

				tabla.setItems(listaMinutas);
			}else{
				JOptionPane.showMessageDialog(null,"\t\tNo se ingresó un criterio de busqueda correcto :/");
			}
			
		}
		
		ObservableList<Minuta> items = tabla.getItems();
		if(items.isEmpty()){
			ActualizarLista();
			JOptionPane.showMessageDialog(null,"\t\tNo se encontraron resultados");
			
		}
		
	}
	
	
	//--------------------Acción del boton Eliminar Acción--------------------\\
	@FXML
	private void TocarBotonEliminar(ActionEvent event){
		
		Minuta minuta = new Minuta();
		
		minuta.setId(labelID.getText());
		minuta.setIdExpediente(labelExp.getText());
		minuta.setIdReunion(labelReu.getText());
		minuta.setContenido(textDescri.getText());
		
		
		Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
			alerta.setTitle("Confirmación");
			alerta.setHeaderText(null);
			alerta.setContentText("¿Estás seguro que desea cargar esta nueva minuta?\n Los datos son los siguientes: \n\tID = "+ minuta.getId() +"\n\tID del Expediente asociado = "+ minuta.getIdExpediente() +"\n\tID de la Reunion asociada = "+ minuta.getIdReunion() +"\n\tContenido = "+ minuta.getContenido());

			//Creación de los botones de aceptar y cancelar
			ButtonType botonAceptar = new ButtonType("Aceptar");
			ButtonType botonCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

			Optional<ButtonType> resultado = alerta.showAndWait();

			// Inicializar la lista de acciones
			if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
				String consulta = "DELETE FROM \"MINUTA\" WHERE \"id_Minuta\" = '"+ minuta.getId() +"';";
		
				try{
					if(conexion.hacerCambios(consulta)){
						JOptionPane.showMessageDialog(null,"\t\tSe realizó la eliminación de la minuta de base de datos con exito!  ");
					}else{
						JOptionPane.showMessageDialog(null,"\t\tOcurrió un problema y no se logró eliminar la minuta ");
					}
				}catch (Exception e){
					JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado al intentar eliminar la minuta :/ \nError:  " + e.getMessage());
				}

				ActualizarLista();
			}
	}
	
	//--------------------Acción del boton de Modificar acción--------------------\\
	@FXML
	private void TocarBotonModificar(ActionEvent event){
		
		Minuta minuta = new Minuta();
		
		minuta.setId(labelID.getText());
		minuta.setIdExpediente(labelExp.getText());
		minuta.setIdReunion(labelReu.getText());
		minuta.setContenido(textDescri.getText());
		
		
		//--------------------Se verifica si el ID del expediente existe en la Base de datos.
		consulta = "SELECT COUNT(*) AS existe FROM  \"EXPEDIENTE\" WHERE \"id_Expediente\" = ?";
		String valorBuscado= minuta.getIdExpediente() ;
		ResultSet resultadoConsulta = conexion.buscarValor(consulta,valorBuscado);
		boolean existenciaE = false;
		boolean existenciaR = false;
		LocalDate Fecha = null;
		String IDReunion = "";
		String id="";
		String textoNota = "";
		java.sql.Timestamp fechaIngreso = null;
		boolean estadoB = false;
		
		String estado = "Cerrado";
		
		try{
			if (resultadoConsulta.next()){
				int existe = resultadoConsulta.getInt("existe");
				existenciaE = existe > 0;
				if (existenciaE){
					consulta = "SELECT \"id_Expediente\",\"textoNota\", \"fechaIngreso\", \"estado\" FROM \"EXPEDIENTE\" WHERE \"id_Expediente\" = '" + minuta.getIdExpediente() +"';";
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

		if(existenciaE){
			//--------------------Verificamos que el ID de la reunion si exista
			valorBuscado = minuta.getIdReunion();
			consulta = "SELECT COUNT(*) AS existe FROM  \"REUNION\" WHERE \"id_Reunion\" = ?";
			resultadoConsulta = conexion.buscarValor(consulta,valorBuscado);
			try{
				if (resultadoConsulta.next()){
					int existe = resultadoConsulta.getInt("existe");
					existenciaR = existe > 0;
					resultadoConsulta=null;
					if (existenciaR){
						consulta = "SELECT * FROM \"REUNION\" WHERE \"id_Reunion\" = '" + minuta.getIdReunion() +"';";
						resultadoConsulta = conexion.hacerConsulta(consulta);
						resultadoConsulta.next();
						try{
							IDReunion = resultadoConsulta.getString(1);
							Fecha = resultadoConsulta.getDate(2).toLocalDate();
						}catch(Exception e){
							JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado al obtener los datos de la reunion :/ \nError:  " + e.getMessage());
						}
					}else{
						JOptionPane.showMessageDialog(null,"\t\tOcurrió un error, al parecer el ID de la reunion no se encuentra\n Verifique los datos y vuelva a seleccionar \"Nueva Minuta\"");
					}
				}else{
					existenciaR = false;
					JOptionPane.showMessageDialog(null,"\t\tOcurrió un error, al parecer el ID de la reunion no se encuentra\n Verifique los datos y vuelva a seleccionar \"Nueva Minuta\"");
				}
			}catch (Exception e){
				JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado al verificar el ID de la reunion :/ \nError:  " + e.getMessage());
				existenciaR = false;
			}
		}
			
		
		if (existenciaE && existenciaR){
			Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
			alerta.setTitle("Confirmación");
			alerta.setHeaderText(null);
			alerta.setContentText("¿Estás seguro que desea modificar esta minuta?\n Los datos son los siguientes: \n\tID = "+ minuta.getId() +"\n\tID del Expediente asociado = "+ minuta.getIdExpediente() +"\n\tID de la Reunion asociada = "+ minuta.getIdReunion() +"\n\tContenido = "+ minuta.getContenido() + "\n\n Datos del Expediente asociado:  \n\tID = "+ id + "\n\tTextoNota = " + textoNota + "\n\tFecha de Ingreso = " + fechaIngreso + "\n\tEstado = " + estado+"\n\n Datos del Expediente asociado:  \n\tID = "+IDReunion+"\n\tFecha  = "+Fecha);

			//Creación de los botones de aceptar y cancelar
			ButtonType botonAceptar = new ButtonType("Aceptar");
			ButtonType botonCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

			Optional<ButtonType> resultado = alerta.showAndWait();

			// Inicializar la lista de acciones
			listaMinutas = FXCollections.observableArrayList();
			if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
				String consulta = "UPDATE \"MINUTA\" SET \"id_Expediente\" = '"+ minuta.getIdExpediente() + "',\"id_Reunion\" = '" + minuta.getIdReunion() + "',\"contenido\" = '"+ minuta.getContenido() + "' WHERE \"id_Minuta\" = '"+ minuta.getId() +"';";

				try{
					if(conexion.hacerCambios(consulta)){
						JOptionPane.showMessageDialog(null,"\t\tSe realizaron los cambios para la Minuta con ID \""+ minuta.getId() +"\" en la base de datos con exito!  ");
					}else{
						JOptionPane.showMessageDialog(null,"\t\tOcurrió un problema y no se logró modificar la minuta :/");
					}
				}catch (Exception e){
					JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado al intentar modificar la minuta :/ \nError:  " + e.getMessage());
				}
			}
		}
		
		ActualizarLista();
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
		labelExp.setText("");
		textDescri.setText("");
		labelReu.setText("");
		
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
		
		tabla.getItems().clear();
		
		//Configuración de la tabla para mostrar el contenido
		columnaID.setCellValueFactory(new PropertyValueFactory<Minuta,String>("id")); 
		columnaIDExpediente.setCellValueFactory(new PropertyValueFactory<Minuta,String>("idExpediente"));
		columnaReunion.setCellValueFactory(new PropertyValueFactory<Minuta,String>("idReunion"));
		columnaDescripcion.setCellValueFactory(new PropertyValueFactory<Minuta,String>("contenido"));

		// Agregar las columnas a la tabla
		if (tabla.getColumns().isEmpty()) {
			tabla.getColumns().addAll(columnaID, columnaIDExpediente, columnaReunion, columnaDescripcion);
		}
		
		//Conección a la bd y mostrar los contenidos.	
		ConexionBD conexion = new ConexionBD();
		conexion.getConexion();
		
		String consulta = "SELECT * FROM \"MINUTA\"";
		
		try(ResultSet resultadoConsulta = conexion.hacerConsulta(consulta)){
			while (resultadoConsulta.next()){
						
				Minuta MinutaTabla = new Minuta();

				MinutaTabla.setId(resultadoConsulta.getString("id_Minuta"));
				MinutaTabla.setIdExpediente(resultadoConsulta.getString("id_Expediente"));
				MinutaTabla.setIdReunion(resultadoConsulta.getString("id_Reunion")); 
				MinutaTabla.setContenido(resultadoConsulta.getString("contenido"));

				listaMinutas.add(MinutaTabla);
				
			}
		}catch (Exception e){
			JOptionPane.showMessageDialog(null,"\t\tOcurrió un error inesperado al intentar cargar en la base de datos :/ \nError:  " + e.getMessage());
		}
		
		tabla.setItems(listaMinutas);
	}
	
	//--------------------Metodo de inicialización--------------------\\
	@Override
	public void initialize (URL url, ResourceBundle rb){
		
		BooleanBinding camposVacios = Bindings.createBooleanBinding(
		() -> labelID.getText().isEmpty() || labelExp.getText().isEmpty() || textDescri.getText().isEmpty() || labelReu.getText().isEmpty(),
		labelID.textProperty(), labelExp.textProperty(), textDescri.textProperty(), labelReu.textProperty()
		);
		
		BooleanBinding camposVaciosNueva = Bindings.createBooleanBinding(
		() -> !labelID.isEditable() || labelID.getText().isEmpty() || labelExp.getText().isEmpty() || textDescri.getText().isEmpty() || labelReu.getText().isEmpty(),
		labelID.textProperty(), labelExp.textProperty(), textDescri.textProperty(),  labelReu.textProperty(), labelID.editableProperty()
		);
		
		BooleanBinding camposLimpiar = Bindings.createBooleanBinding(
		() -> labelID.getText().isEmpty() && labelExp.getText().isEmpty() && textDescri.getText().isEmpty() && labelReu.getText().isEmpty(),
		labelID.textProperty(), labelExp.textProperty(), textDescri.textProperty(),  labelReu.textProperty()
		);
		
		
		labelID.editableProperty().addListener((observable, oldValue, newValue) -> {
			// Actualizar la propiedad del botón
			botonAleatorio.setDisable(!newValue);
		});
				
		BooleanBinding Busqueda = Bindings.createBooleanBinding(
		() -> labelID.getText().isEmpty() && labelExp.getText().isEmpty() && labelReu.getText().isEmpty(),
		labelID.textProperty(), labelExp.textProperty(), labelReu.textProperty()
		);
		
		BNuevaA.disableProperty().bind(camposVaciosNueva);
		BEliminarA.disableProperty().bind(camposVacios);
		BModificarA.disableProperty().bind(camposVacios);
		BLimpiar.disableProperty().bind(camposLimpiar);
		BBuscar.disableProperty().bind(Busqueda.or(labelID.editableProperty().not()));
				
		// Configurar el manejo de cambios de selección en la tabla
		tabla.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Minuta>() {
			@Override
			public void changed(ObservableValue<? extends Minuta> observable, Minuta oldValue, Minuta nuevoValor) {
				if (nuevoValor != null) {
					// Actualizar los TextField y DatePicker con los valores seleccionados
					labelID.setText(nuevoValor.getId());
					labelExp.setText(nuevoValor.getIdExpediente());
					textDescri.setText(nuevoValor.getContenido());
					labelReu.setText(nuevoValor.getIdReunion());

					labelID.setEditable(false);
				}
			}
		});
		
		ActualizarLista();
		
	}
}
