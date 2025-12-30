package com.lucas.proyecto.Clases;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import jakarta.persistence.*;
import java.util.Objects;

/*
 * @author GhOsTFeN & Lukitas :)
 */
@Entity
@Table(name = "PERSONA")
public class Persona implements Serializable {

////////////////////////////////////////////////////
// ------------ Atributos ------------ //
////////////////////////////////////////////////////

    @Id
    @Column(name = "dni")
    private String dni;

    @Column(name = "apellido", nullable = false)
    private String apellido;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "correo", nullable = false)
    private String correo;
    
    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PersonaExpediente> expedientes = new ArrayList<>();

    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Asistencia> asistencias = new ArrayList<>();

    // Campo transitorio para mostrar asistencia en la tabla (no se persiste en BD)
    @Transient
    private Boolean asistencia;

////////////////////////////////////////////////////
// -------- Constructores --------- //
////////////////////////////////////////////////////

    public Persona(String dni, String apellido, String nombre, String correo) {
        this.dni = dni;
        this.apellido = apellido;
        this.nombre = nombre;
        this.correo = correo;
    }
    
    public Persona(String dni, String apellido, String nombre, String correo, Boolean asistencia) {
        this.dni = dni;
        this.apellido = apellido;
        this.nombre = nombre;
        this.correo = correo;
        this.asistencia = asistencia;
    }
    
    public Persona(){
        
    }

////////////////////////////////////////////////////
// ----------- Get y Set ------------ //
////////////////////////////////////////////////////

// ----------- Gets ------------ //

    public String getDni() {
        return dni;
    }

    public String getApellido() {
        return apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }
    
    public List<PersonaExpediente> getExpedientes() {
        return expedientes;
    }

    public List<Asistencia> getAsistencias() {
        return asistencias;
    }

    public Boolean getAsistencia() {
        return asistencia;
    }

// ----------- Sets ------------ //

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    public void setExpedientes(List<PersonaExpediente> expedientes) {
        this.expedientes = expedientes;
    }

    public void setAsistencias(List<Asistencia> asistencias) {
        this.asistencias = asistencias;
    }

    public void setAsistencia(Boolean asistencia) {
        this.asistencia = asistencia;
    }

////////////////////////////////////////////////////
// ----------- To String ------------ //
////////////////////////////////////////////////////

    @Override
    public String toString() {
        return String.format(
            "Persona[dni=%s, apellido=%s, nombre=%s, correo=%s, asistencia=%s]", 
            dni, apellido, nombre, correo, asistencia
        );
    }

////////////////////////////////////////////////////
// ------- Demás Metodos -------- //
////////////////////////////////////////////////////

    // Métodos de utilidad para relaciones
    public void agregarExpediente(PersonaExpediente personaExpediente) {
        expedientes.add(personaExpediente);
        personaExpediente.setPersona(this);
    }

    public void removerExpediente(PersonaExpediente personaExpediente) {
        expedientes.remove(personaExpediente);
        personaExpediente.setPersona(null);
    }

    public void agregarAsistencia(Asistencia asistencia) {
        asistencias.add(asistencia);
        asistencia.setPersona(this);
    }

    public void removerAsistencia(Asistencia asistencia) {
        asistencias.remove(asistencia);
        asistencia.setPersona(null);
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Persona persona = (Persona) o;
        return Objects.equals(dni, persona.dni);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dni);
    }
}