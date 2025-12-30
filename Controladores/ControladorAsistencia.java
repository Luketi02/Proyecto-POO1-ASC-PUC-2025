package com.lucas.proyecto.Controladores;

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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.lucas.proyecto.Clases.JPAUtil;
import com.lucas.proyecto.Clases.Persona;
import com.lucas.proyecto.Clases.Reunion;
import com.lucas.proyecto.Clases.Asistencia;
import com.lucas.proyecto.Clases.CambiarPantalla;
import java.time.LocalDateTime;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

/**
 *
 * @author Lukitas :)
 */

public class ControladorAsistencia implements Initializable {
	
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
	
	@FXML
	private Button BB;
	
	@FXML
	private Button BCA;
	
	@FXML
	private Button BCF;
	
	@FXML
	private TextField labelID;
	
	@FXML
	private TableView<Persona> tabla;
	
	@FXML
	private TableColumn<Persona, String> columnaDNI;
    
	@FXML
	private TableColumn<Persona, String> columnaNombre;

	@FXML
	private TableColumn<Persona, String> columnaApellido;
	
	@FXML
	private TableColumn<Persona, String> columnaCorreo;
	
	@FXML
	private TableColumn<Persona, Boolean> columnaAsistencia;
	
	private ObservableList<Persona> listaPersonas = FXCollections.observableArrayList();
	private Persona personaG = new Persona();
	private Reunion reunion = new Reunion();
	
	//Acción del boton Buscar
	@FXML
	public void TocarBB(ActionEvent event){
		listaPersonas.clear();
		String idReunion = labelID.getText();
		
		if (idReunion.isEmpty()) {
			JOptionPane.showMessageDialog(null, "\t\tDebe ingresar un ID de reunión", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		EntityManager em = JPAUtil.getEntityManager();
		
		try {
			// Buscar la reunión
			reunion = em.find(Reunion.class, idReunion);
			
			if (reunion == null) {
				JOptionPane.showMessageDialog(null, "\t\tNo se encontró la reunión con ID: " + idReunion, "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			// Verificar fecha (máximo 7 días desde la reunión)
			boolean verifFecha = reunion.getFecha().isAfter(LocalDateTime.now().minusWeeks(1));
			
			if (!verifFecha) {
				JOptionPane.showMessageDialog(null, "\t\tLa reunión ya finalizó. \nSolo se puede ajustar la asistencia de una reunión con máximo 7 días de realizada", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			// Obtener asistencias de la reunión
			TypedQuery<Asistencia> query = em.createQuery(
				"SELECT a FROM Asistencia a WHERE a.reunion.id = :idReunion", Asistencia.class);
			query.setParameter("idReunion", idReunion);
			
			List<Asistencia> asistencias = query.getResultList();
			
			for (Asistencia asistencia : asistencias) {
				Persona persona = asistencia.getPersona();
				persona.setAsistencia(asistencia.getPresencia());
				listaPersonas.add(persona);
			}
			
			tabla.setItems(listaPersonas);
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "\t\tOcurrió un error\n\t" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} finally {
			em.close();
		}
	}

	//Acción del boton colocar asistencia
	@FXML
	public void TocarBCA(ActionEvent event){
		if (personaG == null || personaG.getDni() == null || reunion == null || reunion.getId() == null) {
			JOptionPane.showMessageDialog(null, "\t\tDebe seleccionar una persona primero", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction transaction = null;
		
		try {
			transaction = em.getTransaction();
			transaction.begin();
			
			// Buscar la asistencia específica
			TypedQuery<Asistencia> query = em.createQuery(
				"SELECT a FROM Asistencia a WHERE a.reunion.id = :idReunion AND a.persona.dni = :dni", Asistencia.class);
			query.setParameter("idReunion", reunion.getId());
			query.setParameter("dni", personaG.getDni());
			
			Asistencia asistencia = query.getSingleResult();
			
			if (asistencia != null) {
				asistencia.setPresencia(true);
				em.merge(asistencia);
				transaction.commit();
				
				// Actualizar la tabla
				actualizarTablaAsistencias();
			}
			
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			JOptionPane.showMessageDialog(null, "\t\tOcurrió un error\n\t" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} finally {
			em.close();
		}
	}
	
	//Acción del boton colocar falta
	@FXML
	public void TocarBCF(ActionEvent event){
		if (personaG == null || personaG.getDni() == null || reunion == null || reunion.getId() == null) {
			JOptionPane.showMessageDialog(null, "\t\tDebe seleccionar una persona primero", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction transaction = null;
		
		try {
			transaction = em.getTransaction();
			transaction.begin();
			
			// Buscar la asistencia específica
			TypedQuery<Asistencia> query = em.createQuery(
				"SELECT a FROM Asistencia a WHERE a.reunion.id = :idReunion AND a.persona.dni = :dni", Asistencia.class);
			query.setParameter("idReunion", reunion.getId());
			query.setParameter("dni", personaG.getDni());
			
			Asistencia asistencia = query.getSingleResult();
			
			if (asistencia != null) {
				asistencia.setPresencia(false);
				em.merge(asistencia);
				transaction.commit();
				
				// Actualizar la tabla
				actualizarTablaAsistencias();
			}
			
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			JOptionPane.showMessageDialog(null, "\t\tOcurrió un error\n\t" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} finally {
			em.close();
		}
	}
	
	// Método para actualizar la tabla de asistencias
	private void actualizarTablaAsistencias() {
		EntityManager em = JPAUtil.getEntityManager();
		
		try {
			listaPersonas.clear();
			
			TypedQuery<Asistencia> query = em.createQuery(
				"SELECT a FROM Asistencia a WHERE a.reunion.id = :idReunion", Asistencia.class);
			query.setParameter("idReunion", reunion.getId());
			
			List<Asistencia> asistencias = query.getResultList();
			
			for (Asistencia asistencia : asistencias) {
				Persona persona = asistencia.getPersona();
				persona.setAsistencia(asistencia.getPresencia());
				listaPersonas.add(persona);
			}
			
			tabla.setItems(listaPersonas);
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "\t\tOcurrió un error al actualizar la tabla\n\t" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} finally {
			em.close();
		}
	}
	
        @FXML
        private void TocarMenuExp(ActionEvent event){
            CambiarPantalla.cambiarVentana(event, "Gestion_de_Expedientes.fxml", "Gestión de Expedientes");
        }
        
        @FXML
        private void TocarMenuReu(ActionEvent event){
            CambiarPantalla.cambiarVentana(event, "/Gestion_de_Reuniones.fxml", "Gestión de Reuniones");
        }

        @FXML
        private void TocarMenuAcciones(ActionEvent event){
            CambiarPantalla.cambiarVentana(event, "/Gestion_de_Acciones.fxml", "Gestión de Acciones");
        }    

        @FXML
        private void TocarMenuMinu(ActionEvent event){
            CambiarPantalla.cambiarVentana(event, "/Gestion_de_Minutas.fxml", "Gestión de Minutas");
        }
        
        @FXML
        private void TocarMenuPer(ActionEvent event){
            CambiarPantalla.cambiarVentana(event, "/Gestion_de_Personas.fxml", "Gestión de Personas");
        }
	

	
	//Metodo de inicialización
	@Override
	public void initialize (URL url, ResourceBundle rb){
		
		columnaDNI.setCellValueFactory(new PropertyValueFactory<Persona,String>("dni"));
		columnaNombre.setCellValueFactory(new PropertyValueFactory<Persona,String>("nombre"));
		columnaApellido.setCellValueFactory(new PropertyValueFactory<Persona,String>("apellido"));
		columnaCorreo.setCellValueFactory(new PropertyValueFactory<Persona,String>("correo"));
		columnaAsistencia.setCellValueFactory(new PropertyValueFactory<>("asistencia"));
		
		columnaAsistencia.setCellFactory(column -> new TableCell<Persona, Boolean>() {
			@Override
			protected void updateItem(Boolean item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
				} else {
					setText(item ? "Presente" : "Ausente");
				}
			}
		});
	
		// Deshabilitar los botones al iniciar la pantalla
		BCA.setDisable(true);
		BCF.setDisable(true);

		// Agregar un listener para habilitar/deshabilitar los botones cuando se selecciona una fila
		tabla.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			boolean isSelected = (newSelection != null);
			BCA.setDisable(!isSelected);
			BCF.setDisable(!isSelected);
			
			if (newSelection != null) {
				personaG = newSelection;
			}
		});
		
		// BooleanBinding para verificar si labelID está vacío
		BooleanBinding labelIDVacio = Bindings.createBooleanBinding(() ->
				labelID.getText().isEmpty(),
				labelID.textProperty());

		// Asociar la propiedad disable del botón BB con la propiedad vacía de labelID
		BB.disableProperty().bind(labelIDVacio);
	}
}