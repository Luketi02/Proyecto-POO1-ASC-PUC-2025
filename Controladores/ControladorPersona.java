/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lucas.proyecto.Controladores;

import com.lucas.proyecto.Clases.JPAUtil;
import com.lucas.proyecto.Clases.Persona;
import com.lucas.proyecto.Clases.CambiarPantalla;
import java.net.URL;
import java.util.ResourceBundle;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.time.LocalDate;
import javax.swing.JOptionPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import jakarta.persistence.*;

/**
 *
 * @author Fernando
 */
public class ControladorPersona implements Initializable{
    
    @FXML
    private TextField labelDNI, labelApellido, labelNombre, labelCorreo;
    
    @FXML
    private Button BNuevaP, BEliminarP, BModificarP, BActualizarL, BLimpiar, BBuscar;

    @FXML
    private TableView<Persona> tablaPersona;
	
    @FXML
    private TableColumn<Persona, String> columnaDNI, columnaNom, columnaAp, columnaCorreo;
    
    private ObservableList<Persona> listaPersonas = FXCollections.observableArrayList();

    
    /*--- ACCION DEL BOTON LIMPIAR CAMPOS ---*/
    @FXML
    private void TocarBLimpiar(ActionEvent event){
		
	limpiarCampos();
	
	labelDNI.setEditable(true);
		
    }
    
    private void limpiarCampos() {
        labelDNI.clear();
        labelNombre.clear();
        labelApellido.clear();
        labelCorreo.clear();
    }
    
    private boolean validarDatos() {
        if (!labelDNI.getText().matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "DNI debe ser numérico");
            return false;
        }
        if (!labelCorreo.getText().matches(".+@.+\\..+")) {
            JOptionPane.showMessageDialog(null, "Correo inválido");
            return false;
        }
        return true;
    }
    
    //--------------------Acción del boton de nueva persona--------------------\\
    @FXML
    private void TocarBotonNuevaPersona(ActionEvent event){
        if (labelDNI.getText().isEmpty() || labelNombre.getText().isEmpty() || labelApellido.getText().isEmpty() || labelCorreo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe completar todos los campos");
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            Persona existente = em.find(Persona.class, labelDNI.getText());
            if (existente != null) {
                JOptionPane.showMessageDialog(null, "Ya existe una persona con ese DNI");
                return;
            }

            Persona nueva = new Persona();
            nueva.setDni(labelDNI.getText());
            nueva.setNombre(labelNombre.getText());
            nueva.setApellido(labelApellido.getText());
            nueva.setCorreo(labelCorreo.getText());

            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                em.persist(nueva);
                tx.commit();
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                throw e;
            }

            JOptionPane.showMessageDialog(null, "Persona agregada correctamente");
            ActualizarLista();
            limpiarCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al agregar persona: " + e.getMessage());
        } finally {
            em.close();
        }
    }
    
    @FXML
    private void TocarMenuExp(ActionEvent event){
        CambiarPantalla.cambiarVentana(event, "Gestion_de_Expedientes.fxml", "Gestión de Expedientes");
    }
    
    @FXML
    private void TocarMenuReuniones(ActionEvent event){
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
        
    //--------------------Acción del boton de busqueda--------------------\\
    @FXML
    private void TocarBBuscar(ActionEvent event) {
        String dni = labelDNI.getText();
        if (dni.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese un DNI");
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            Persona persona = em.find(Persona.class, dni);
            if (persona == null) {
                JOptionPane.showMessageDialog(null, "No se encontró la persona");
                return;
            }
            listaPersonas.clear();
            listaPersonas.add(persona);
            tablaPersona.setItems(listaPersonas);
        } finally {
            em.close();
        }
    }

    //--------------------Acción del boton Eliminar Persona--------------------\\
    @FXML
    private void TocarBotonEliminar(ActionEvent event) {
        Persona seleccionada = tablaPersona.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            JOptionPane.showMessageDialog(null, "Seleccione una persona");
            return;
        }

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setContentText("¿Eliminar a " + seleccionada.getDni() + "?");
        if (alerta.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Persona p = em.find(Persona.class, seleccionada.getDni());
            if (p != null) {
                em.remove(p);
            }
            tx.commit();
            JOptionPane.showMessageDialog(null, "Persona eliminada correctamente.");
            ActualizarLista(); // Actualiza la tabla
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            JOptionPane.showMessageDialog(null, "Error al eliminar: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    
    
    //--------------------Acción del boton de Modificar persona--------------------\\
    @FXML
    private void TocarBotonModificar(ActionEvent event) {
        Persona seleccionada = tablaPersona.getSelectionModel().getSelectedItem();
        if (seleccionada == null || !validarDatos()) {
            JOptionPane.showMessageDialog(null, "Seleccione una persona y valide los datos");
            return;
        }

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setContentText("Está seguro de modificar a la persona con DNI: " + seleccionada.getDni() + "?");
        if (alerta.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Persona p = em.merge(seleccionada); // Adjunta la entidad al contexto de persistencia
            p.setNombre(labelNombre.getText());
            p.setApellido(labelApellido.getText());
            p.setCorreo(labelCorreo.getText());
            em.getTransaction().commit();
            ActualizarLista();
            limpiarCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al modificar: " + e.getMessage());
        } finally {
            em.close();
        }
    }        
        
    //--------------------Metodo publico para actualizar la lista con los datos recientes de la base de datos--------------------\\
    public void ActualizarLista(){
	//Vaciamos la lista por completo
	
	tablaPersona.getItems().clear();
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Persona> query = em.createQuery("SELECT p FROM Persona p", Persona.class);
            List<Persona> personas = query.getResultList();
            listaPersonas.setAll(personas);
            tablaPersona.setItems(listaPersonas);
        } finally {
            em.close();
        }
    }
        
    @FXML
    private void TocarBotonActualizar(ActionEvent event){
		
	ActualizarLista();
		
    }

        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 1. Bindings reutilizables
        BooleanBinding camposVacios = Bindings.createBooleanBinding(
            () -> labelDNI.getText().isEmpty() || labelNombre.getText().isEmpty(),
            labelDNI.textProperty(), labelNombre.textProperty()
        );

        BooleanBinding camposLimpiar = Bindings.createBooleanBinding(
            () -> labelDNI.getText().isEmpty() && labelNombre.getText().isEmpty(),
            labelDNI.textProperty(), labelNombre.textProperty()
        );

        // 2. Deshabilitar botones
        BNuevaP.disableProperty().bind(camposVacios);
        BEliminarP.disableProperty().bind(tablaPersona.getSelectionModel().selectedItemProperty().isNull());
        BModificarP.disableProperty().bind(tablaPersona.getSelectionModel().selectedItemProperty().isNull());
        BLimpiar.disableProperty().bind(camposLimpiar);
        BBuscar.disableProperty().bind(labelDNI.textProperty().isEmpty());

        // 3. Listener de selección (mejorado)
        tablaPersona.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                labelDNI.setText(newVal.getDni());
                labelNombre.setText(newVal.getNombre());
                labelApellido.setText(newVal.getApellido());
                labelCorreo.setText(newVal.getCorreo());
                labelDNI.setEditable(false);
            } else {
                limpiarCampos();
                labelDNI.setEditable(true);
            }
        });

        // 4. Configurar columnas
        columnaDNI.setCellValueFactory(new PropertyValueFactory<>("dni"));
        columnaNom.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaAp.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        columnaCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));

        // 5. Cargar datos con manejo de errores
        try {
            ActualizarLista();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                null, 
                "Error al cargar datos iniciales: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );        }
    }
}
    