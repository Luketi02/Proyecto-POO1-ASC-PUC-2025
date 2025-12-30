package com.lucas.proyecto.Controladores;

import com.lucas.proyecto.Clases.CambiarPantalla;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.lucas.proyecto.Clases.Expediente;
import com.lucas.proyecto.Clases.PersonaExpediente;
import com.lucas.proyecto.Clases.Persona;
import com.lucas.proyecto.Clases.JPAUtil;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.util.Callback;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import jakarta.persistence.*;

/**
 *
 * @author Fernando B)
 */
public class ControladorExpediente implements Initializable {

    // Inicializacion de componentes
    @FXML
    private TextField labelID;
    
    @FXML
    private Button BAleatorio;
    
    @FXML
    private Button BNuevoE;
    
    @FXML
    private Button BEliminarE;
    
    @FXML
    private Button BModificarE;
    
    @FXML
    private Button BActualizarL;
    
    @FXML
    private Button BLimpiar;
    
    @FXML
    private Button BBuscar;
    
    @FXML
    private Button BAgregar;
    
    @FXML
    private Button BEliminar;
    
    @FXML
    private TextField labelAgregar;
    
    @FXML
    private TextArea textNota;
    
    @FXML
    private CheckBox CheckBEstado;
    
    @FXML
    private CheckBox CheckBIniciante;

    @FXML
    private TableView<Expediente> tablaExpedientes;
    
    @FXML
    private TableColumn<Expediente, String> columnaID;
    
    @FXML
    private TableColumn<Expediente, String> columnaFecha;

    @FXML
    private TableColumn<Expediente, String> columnaNota;
    
    @FXML
    private TableColumn<Expediente, String> columnaEstado;
    
    @FXML
    private TableView<Persona> tablaInvIn;
    
    @FXML
    private TableColumn<Persona, String> columnaDNI;
    
    @FXML
    private TableColumn<Persona, String> columnaApellido;

    @FXML
    private TableColumn<Persona, String> columnaNombre;
    
    @FXML
    private TableColumn<Persona, String> columnaCargo;
    
    // Inicializamos variables y lista de acciones
    private ObservableList<Expediente> listaExpedientes = FXCollections.observableArrayList();
    private ObservableList<Persona> listaPersonas = FXCollections.observableArrayList();   
    
    // Lista temporal para personas a agregar al expediente
    private List<PersonaExpediente> personasTemporales = new ArrayList<>();
    
    //--------------------Acción del boton de ID aleatorio--------------------\\    
    @FXML
    private void TocarBotonAleatorio(ActionEvent event){
        
        boolean existencia = false;
        String randomID = "";
        EntityManager em = JPAUtil.getEntityManager();
        
        do{
            UUID id = UUID.randomUUID();
            randomID = id.toString().replaceAll("-", "");
            randomID = randomID.substring(0, 10);
            
            // Verificar existencia usando JPA
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(e) FROM Expediente e WHERE e.id_Expediente = :id", Long.class);
            query.setParameter("id", randomID);
            Long count = query.getSingleResult();
            existencia = count > 0;
            
        }while(existencia);
        
        em.close();
        labelID.setText(randomID);
    }
    
    //--------------------Acción del boton de agregar iniciante o involucrado--------------------\\
    @FXML
    private void TocarBotonAgregar(ActionEvent event) {
        String dni = labelAgregar.getText().trim();
        
        if (dni.isEmpty()) {
            JOptionPane.showMessageDialog(null, "\t\tNo ha ingresado ningún DNI");
            return;
        }
        
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            Persona persona = em.find(Persona.class, dni);
            
            if (persona == null) {
                JOptionPane.showMessageDialog(null, "\t\tNo se encontró a la persona con DNI " + dni);
                return;
            }
            
            // Verificar si la persona ya está en la lista temporal
            boolean existe = personasTemporales.stream()
                .anyMatch(pe -> pe.getPersona().getDni().equals(dni));
            
            if (existe) {
                JOptionPane.showMessageDialog(null, "\t\tEl DNI ya existe en la lista");
                return;
            }
            
            // Crear nueva relación temporal
            PersonaExpediente personaExpediente = new PersonaExpediente();
            personaExpediente.setPersona(persona);
            personaExpediente.setCargo(CheckBIniciante.isSelected());
            
            personasTemporales.add(personaExpediente);
            listaPersonas.add(persona);
            
            JOptionPane.showMessageDialog(null, "\t\tEl DNI se agregó correctamente a la lista");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "\t\tError al buscar persona: " + e.getMessage());
        } finally {
            em.close();
        }
        
        tablaInvIn.setItems(listaPersonas);
    }

    //--------------------Acción del boton de eliminar iniciante o involucrado--------------------\\
    @FXML
    private void TocarBotonEliminar(ActionEvent event) {
        String dni = labelAgregar.getText().trim();
        
        if (dni.isEmpty()) {
            JOptionPane.showMessageDialog(null, "\t\tNo ha ingresado ningún DNI");
            return;
        }
        
        // Buscar y eliminar de la lista temporal
        Iterator<PersonaExpediente> iterator = personasTemporales.iterator();
        boolean encontrado = false;
        
        while (iterator.hasNext()) {
            PersonaExpediente pe = iterator.next();
            if (pe.getPersona().getDni().equals(dni)) {
                iterator.remove();
                encontrado = true;
                break;
            }
        }
        
        if (encontrado) {
            // Eliminar también de la lista de visualización
            Iterator<Persona> personaIterator = listaPersonas.iterator();
            while (personaIterator.hasNext()) {
                Persona persona = personaIterator.next();
                if (persona.getDni().equals(dni)) {
                    personaIterator.remove();
                    break;
                }
            }
            
            JOptionPane.showMessageDialog(null, "\t\tEl DNI se eliminó correctamente de la lista");
        } else {
            JOptionPane.showMessageDialog(null, "\t\tEl DNI ingresado no se encuentra en la lista");
        }
        
        tablaInvIn.setItems(listaPersonas);
    }
    
    //--------------------Acción del boton de nuevo expediente--------------------\\
    @FXML
    private void TocarBotonNuevoExpediente(ActionEvent event){
        String idExpediente = labelID.getText().trim();
        String textoNota = textNota.getText().trim();
        
        if (idExpediente.isEmpty() || textoNota.isEmpty()) {
            JOptionPane.showMessageDialog(null, "\t\tDebe completar todos los campos obligatorios");
            return;
        }
        
        // Verificar que haya al menos un iniciante
        boolean tieneIniciante = personasTemporales.stream()
            .anyMatch(PersonaExpediente::getCargo);
        
        if (!tieneIniciante) {
            JOptionPane.showMessageDialog(null, "No hay ningún iniciante para asociar con el expediente");
            return;
        }
        
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            // Verificar si el ID ya existe
            Expediente existente = em.find(Expediente.class, idExpediente);
            if (existente != null) {
                JOptionPane.showMessageDialog(null, "\t\tEl ID del Expediente ya se encuentra en uso");
                return;
            }
            
            // Crear el expediente
            Expediente expediente = new Expediente();
            expediente.setId(idExpediente);
            expediente.setFechaIngreso(LocalDateTime.now());
            expediente.setTextoNota(textoNota);
            expediente.setEstado(CheckBEstado.isSelected());
            
            // Mostrar confirmación
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setTitle("Confirmación");
            alerta.setHeaderText(null);
            alerta.setContentText("¿Estás seguro que desea crear este nuevo expediente?\n" +
                "Los datos son los siguientes: \n\tID = " + expediente.getId() +
                "\n\tFecha = " + expediente.getFechaIngreso() +
                "\n\tNota = " + expediente.getTextoNota() +
                "\n\tEstado = " + expediente.isEstado());
            
            ButtonType botonAceptar = new ButtonType("Aceptar");
            ButtonType botonCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            alerta.getButtonTypes().setAll(botonAceptar, botonCancelar);

            Optional<ButtonType> resultado = alerta.showAndWait();
            
            if (resultado.isPresent() && resultado.get() == botonAceptar) {
                EntityTransaction transaction = em.getTransaction();
                
                try {
                    transaction.begin();
                    
                    // Guardar el expediente
                    em.persist(expediente);
                    
                    // Guardar las relaciones persona-expediente
                    for (PersonaExpediente pe : personasTemporales) {
                        pe.setExpediente(expediente);
                        em.persist(pe);
                    }
                    
                    transaction.commit();
                    
                    // Limpiar listas temporales
                    personasTemporales.clear();
                    listaPersonas.clear();
                    
                    JOptionPane.showMessageDialog(null, "\t\tSe realizó la carga del nuevo expediente con éxito!");
                    ActualizarLista();
                    
                } catch (Exception e) {
                    if (transaction.isActive()) {
                        transaction.rollback();
                    }
                    JOptionPane.showMessageDialog(null, "\t\tError al guardar el expediente: " + e.getMessage());
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "\t\tError: " + e.getMessage());
        } finally {
            em.close();
        }
    }
    
    @FXML
    private void TocarBotonModificarExpediente(ActionEvent event) {
        String idExpediente = labelID.getText().trim();
        
        if (idExpediente.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El ID del expediente no puede estar vacío.");
            return;
        }
        
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = null;
        
        try {
            Expediente expediente = em.find(Expediente.class, idExpediente);
            
            if (expediente == null) {
                JOptionPane.showMessageDialog(null, "No se encontró el expediente con ID: " + idExpediente);
                return;
            }
            
            // Actualizar datos del expediente
            expediente.setTextoNota(textNota.getText());
            expediente.setEstado(CheckBEstado.isSelected());
            
            // Mostrar confirmación
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setTitle("Confirmación");
            alerta.setHeaderText(null);
            alerta.setContentText("¿Estás seguro de que deseas modificar este expediente?\n" +
                "Los datos son los siguientes:\n" +
                "\tID: " + expediente.getId() + "\n" +
                "\tFecha: " + expediente.getFechaIngreso() + "\n" +
                "\tNota: " + expediente.getTextoNota() + "\n" +
                "\tEstado: " + expediente.isEstado());
            
            ButtonType botonAceptar = new ButtonType("Aceptar");
            ButtonType botonCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            alerta.getButtonTypes().setAll(botonAceptar, botonCancelar);

            Optional<ButtonType> resultado = alerta.showAndWait();
            
            if (resultado.isPresent() && resultado.get() == botonAceptar) {
                transaction = em.getTransaction();
                transaction.begin();
                
                // Actualizar expediente
                em.merge(expediente);
                
                // Si hay cambios en las personas, actualizar las relaciones
                if (!personasTemporales.isEmpty()) {
                    // Eliminar relaciones existentes
                    Query deleteQuery = em.createQuery(
                        "DELETE FROM PersonaExpediente pe WHERE pe.expediente.id_Expediente = :idExpediente");
                    deleteQuery.setParameter("idExpediente", idExpediente);
                    deleteQuery.executeUpdate();
                    
                    // Crear nuevas relaciones
                    for (PersonaExpediente pe : personasTemporales) {
                        pe.setExpediente(expediente);
                        em.persist(pe);
                    }
                }
                
                transaction.commit();
                
                // Limpiar listas temporales
                personasTemporales.clear();
                listaPersonas.clear();
                
                JOptionPane.showMessageDialog(null, "Se modificó el expediente correctamente.");
                ActualizarLista();
            }
            
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            JOptionPane.showMessageDialog(null, "Error al modificar el expediente: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    //--------------------Acción del boton Eliminar Expediente--------------------\\
    @FXML
    private void TocarBotonEliminarExpediente(ActionEvent event) {
        String idExpediente = labelID.getText().trim();
        
        if (idExpediente.isEmpty()) {
            JOptionPane.showMessageDialog(null, "\t\tNo se seleccionó ningún expediente para eliminar.");
            return;
        }
        
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            Expediente expediente = em.find(Expediente.class, idExpediente);
            
            if (expediente == null) {
                JOptionPane.showMessageDialog(null, "No se encontró el expediente con ID: " + idExpediente);
                return;
            }
            
            // Mostrar confirmación
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setTitle("Confirmación");
            alerta.setHeaderText(null);
            alerta.setContentText("¿Estás seguro que desea eliminar este expediente?\n" +
                "Los datos son los siguientes: \n\tID = " + expediente.getId() + "\n" +
                "\tFecha = " + expediente.getFechaIngreso() + "\n" +
                "\tNota = " + expediente.getTextoNota() + "\n" +
                "\tEstado = " + expediente.isEstado());
            
            ButtonType botonAceptar = new ButtonType("Aceptar");
            ButtonType botonCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            alerta.getButtonTypes().setAll(botonAceptar, botonCancelar);
            
            Optional<ButtonType> resultado = alerta.showAndWait();
            
            if (resultado.isPresent() && resultado.get() == botonAceptar) {
                EntityTransaction transaction = em.getTransaction();
                
                try {
                    transaction.begin();
                    
                    // Eliminar relaciones primero
                    Query deleteRelaciones = em.createQuery(
                        "DELETE FROM PersonaExpediente pe WHERE pe.expediente.id_Expediente = :idExpediente");
                    deleteRelaciones.setParameter("idExpediente", idExpediente);
                    deleteRelaciones.executeUpdate();
                    
                    // Eliminar el expediente
                    em.remove(em.merge(expediente));
                    
                    transaction.commit();
                    
                    JOptionPane.showMessageDialog(null, "\t\tSe eliminó el expediente correctamente!");
                    ActualizarLista();
                    TocarBLimpiar(null); // Limpiar campos
                    
                } catch (Exception e) {
                    if (transaction.isActive()) {
                        transaction.rollback();
                    }
                    JOptionPane.showMessageDialog(null, "\t\tError al eliminar el expediente: " + e.getMessage());
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "\t\tError: " + e.getMessage());
        } finally {
            em.close();
        }
    }
    
    //--------------------Acción del boton de busqueda--------------------\\
    @FXML
    private void TocarBBuscar(ActionEvent event){
        String idExpediente = labelID.getText().trim();
        
        if (idExpediente.isEmpty()) {
            // Si no hay ID, mostrar todos los expedientes
            ActualizarLista();
            return;
        }
        
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            Expediente expediente = em.find(Expediente.class, idExpediente);
            
            if (expediente != null) {
                // Cargar las relaciones de persona-expediente
                TypedQuery<PersonaExpediente> query = em.createQuery(
                    "SELECT pe FROM PersonaExpediente pe WHERE pe.expediente.id_Expediente = :idExpediente", 
                    PersonaExpediente.class);
                query.setParameter("idExpediente", idExpediente);
                
                List<PersonaExpediente> relaciones = query.getResultList();
                
                // Actualizar la lista de expedientes
                listaExpedientes.clear();
                listaExpedientes.add(expediente);
                
                // Actualizar la lista de personas
                listaPersonas.clear();
                for (PersonaExpediente pe : relaciones) {
                    listaPersonas.add(pe.getPersona());
                }
                
                // Actualizar la lista temporal para edición
                personasTemporales.clear();
                personasTemporales.addAll(relaciones);
                
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el expediente con ID: " + idExpediente);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "\t\tError al buscar: " + e.getMessage());
        } finally {
            em.close();
        }
        
        if (listaExpedientes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "\t\tNo se encontraron resultados");
        }
    }
    
    /*--- ACCION DEL BOTON LIMPIAR CAMPOS ---*/
    @FXML
    private void TocarBLimpiar(ActionEvent event){
        labelID.setText("");
        labelAgregar.setText("");
        textNota.setText("");
        CheckBEstado.setSelected(false);
        CheckBIniciante.setSelected(false);

        labelID.setEditable(true);
        
        // Limpiar listas temporales
        personasTemporales.clear();
        listaPersonas.clear();
        listaExpedientes.clear();
        
        tablaInvIn.setItems(listaPersonas);
        tablaExpedientes.setItems(listaExpedientes);
    }
    
    //--------------------Metodo publico para actualizar la lista con los datos recientes de la base de datos--------------------\\
    public void ActualizarLista(){
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            // Obtener todos los expedientes
            TypedQuery<Expediente> query = em.createQuery(
                "SELECT e FROM Expediente e ORDER BY e.id", Expediente.class);
            List<Expediente> expedientes = query.getResultList();
            
            // Actualizar la lista observable
            listaExpedientes.clear();
            listaExpedientes.addAll(expedientes);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "\t\tError al cargar expedientes: " + e.getMessage());
        } finally {
            em.close();
        }
        
        tablaExpedientes.setItems(listaExpedientes);
    }

    @FXML
    private void TocarBotonActualizar(ActionEvent event){
        ActualizarLista();
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
    
    @FXML
    private void TocarMenuPer(ActionEvent event){
        CambiarPantalla.cambiarVentana(event, "/Gestion_de_Personas.fxml", "Gestión de Personas");
    }
    
    @FXML
    private void TocarMenuAsistencia(ActionEvent event){
        CambiarPantalla.cambiarVentana(event, "/Asistencia.fxml", "Gestión de Asistencias");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configurar bindings de UI
        BooleanBinding camposVacios = Bindings.createBooleanBinding(
            () -> labelID.getText().isEmpty() || textNota.getText().isEmpty(), 
            labelID.textProperty(), textNota.textProperty()
        );
        
        BooleanBinding camposVaciosNueva = Bindings.createBooleanBinding(
            () -> !labelID.isEditable() || labelID.getText().isEmpty() || textNota.getText().isEmpty(), 
            labelID.textProperty(), textNota.textProperty(), labelID.editableProperty()
        );
        
        BooleanBinding camposLimpiar = Bindings.createBooleanBinding(
            () -> labelID.getText().isEmpty() && labelAgregar.getText().isEmpty() && 
                  textNota.getText().isEmpty() && !CheckBEstado.isSelected(),
            labelID.textProperty(), labelAgregar.textProperty(), 
            textNota.textProperty(), CheckBEstado.selectedProperty()
        );
        
        labelID.editableProperty().addListener((observable, oldValue, newValue) -> {
            BAleatorio.setDisable(!newValue);
        });
                
        BooleanBinding Busqueda = Bindings.createBooleanBinding(
            () -> labelID.getText().isEmpty(),
            labelID.textProperty()
        );
        
        // Configurar botones
        BNuevoE.disableProperty().bind(camposVaciosNueva);
        BEliminarE.disableProperty().bind(camposVacios);
        BModificarE.disableProperty().bind(camposVacios);
        BLimpiar.disableProperty().bind(camposLimpiar);
        BBuscar.disableProperty().bind(Busqueda.or(labelID.editableProperty().not()));
        
        // Configurar listeners de selección
        tablaInvIn.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, nuevoValor) -> {
            if (nuevoValor != null) {
                labelAgregar.setText(nuevoValor.getDni());
            }
        });
        
        tablaExpedientes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, nuevoValor) -> {
            if (nuevoValor != null) {
                EntityManager em = JPAUtil.getEntityManager();
                
                try {
                    // Cargar expediente con sus relaciones
                    Expediente expediente = em.find(Expediente.class, nuevoValor.getId());
                    
                    if (expediente != null) {
                        // Actualizar campos
                        labelID.setText(expediente.getId());
                        labelID.setEditable(false);
                        textNota.setText(expediente.getTextoNota());
                        CheckBEstado.setSelected(expediente.isEstado());
                        
                        // Cargar personas relacionadas
                        TypedQuery<PersonaExpediente> query = em.createQuery(
                            "SELECT pe FROM PersonaExpediente pe WHERE pe.expediente.id_Expediente = :idExpediente", 
                            PersonaExpediente.class);
                        query.setParameter("idExpediente", expediente.getId());
                        
                        List<PersonaExpediente> relaciones = query.getResultList();
                        
                        // Actualizar listas
                        personasTemporales.clear();
                        personasTemporales.addAll(relaciones);
                        
                        listaPersonas.clear();
                        for (PersonaExpediente pe : relaciones) {
                            listaPersonas.add(pe.getPersona());
                        }
                        
                        tablaInvIn.setItems(listaPersonas);
                    }
                    
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "\t\tError al cargar expediente: " + e.getMessage());
                } finally {
                    em.close();
                }
            }
        });

        // Configurar columnas de la tabla
        columnaID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fechaIngreso"));
        columnaNota.setCellValueFactory(new PropertyValueFactory<>("textoNota"));
        columnaEstado.setCellValueFactory(cellData -> {
            String estadoTexto = cellData.getValue().isEstado() ? "Abierto" : "Cerrado";
            return new SimpleStringProperty(estadoTexto);
        });
        
        columnaDNI.setCellValueFactory(new PropertyValueFactory<>("dni"));
        columnaApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaCargo.setCellValueFactory(cellData -> {
            Persona persona = cellData.getValue();

            // Buscar en relaciones temporales
            Optional<PersonaExpediente> relacion = personasTemporales.stream()
                .filter(pe -> pe.getPersona().getDni().equals(persona.getDni()))
                .findFirst();

            if (relacion.isPresent()) {
                String cargo = relacion.get().getCargo() ? "Iniciante" : "Involucrado";
                return new SimpleStringProperty(cargo);
            }

            return new SimpleStringProperty("N/A");
        });

        // Cargar datos iniciales
        ActualizarLista();
    }
}