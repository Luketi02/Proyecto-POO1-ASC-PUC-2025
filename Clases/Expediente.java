package com.lucas.proyecto.Clases;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/*
 * @author Lukitas :)
 */
@Entity
@Table(name = "EXPEDIENTE")
public class Expediente implements Serializable {

////////////////////////////////////////////////////
// ------------ Atributos ------------ //
////////////////////////////////////////////////////

    @Id
    @Column(name = "id_Expediente", nullable = false, length = 50)
    private String id_Expediente;

    @Column(name = "textoNota", length = 50)
    private String textoNota;

    @Column(name = "fechaIngreso")
    private LocalDateTime fechaIngreso = LocalDateTime.now();

    @Column(name = "estado", nullable = false)
    private boolean estado;

    // Única lista necesaria - maneja TODAS las relaciones con personas
    @OneToMany(mappedBy = "expediente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PersonaExpediente> personasExpediente = new ArrayList<>();

////////////////////////////////////////////////////
// -------- Constructores --------- //
////////////////////////////////////////////////////

    public Expediente(String id_Expediente, String textoNota, boolean estado) {
        this.id_Expediente = id_Expediente;
        this.textoNota = textoNota;
        this.estado = estado;
    }

    public Expediente(){
        // Constructor por defecto
    }

////////////////////////////////////////////////////
// ----------- Get y Set ------------ //
////////////////////////////////////////////////////

    public String getId() {
        return id_Expediente;
    }

    public void setId(String id_Expediente) {
        this.id_Expediente = id_Expediente;
    }

    public String getTextoNota() {
        return textoNota;
    }

    public void setTextoNota(String textoNota) {
        this.textoNota = textoNota;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public List<PersonaExpediente> getPersonasExpediente() {
        return personasExpediente;
    }

    public void setPersonasExpediente(List<PersonaExpediente> personasExpediente) {
        this.personasExpediente = personasExpediente;
    }

////////////////////////////////////////////////////
// ------- Métodos de utilidad --------//
////////////////////////////////////////////////////

    // Agregar persona con su rol
    public void agregarPersona(Persona persona, boolean esIniciante) {
        PersonaExpediente personaExpediente = new PersonaExpediente(persona, this, esIniciante);
        personasExpediente.add(personaExpediente);
        persona.getExpedientes().add(personaExpediente);
    }

    // Eliminar persona
    public void eliminarPersona(Persona persona) {
        personasExpediente.removeIf(pe -> pe.getPersona().equals(persona));
        persona.getExpedientes().removeIf(pe -> pe.getExpediente().equals(this));
    }

    // Obtener iniciantes (cargo = true)
    @Transient
    public List<Persona> getIniciantes() {
        List<Persona> iniciantes = new ArrayList<>();
        for (PersonaExpediente pe : personasExpediente) {
            if (Boolean.TRUE.equals(pe.getCargo())) {
                iniciantes.add(pe.getPersona());
            }
        }
        return iniciantes;
    }

    // Obtener involucrados (cargo = false)
    @Transient
    public List<Persona> getInvolucrados() {
        List<Persona> involucrados = new ArrayList<>();
        for (PersonaExpediente pe : personasExpediente) {
            if (Boolean.FALSE.equals(pe.getCargo())) {
                involucrados.add(pe.getPersona());
            }
        }
        return involucrados;
    }

    // Verificar si una persona es iniciante
    @Transient
    public boolean esIniciante(Persona persona) {
        for (PersonaExpediente pe : personasExpediente) {
            if (pe.getPersona().equals(persona) && Boolean.TRUE.equals(pe.getCargo())) {
                return true;
            }
        }
        return false;
    }

    // Verificar si una persona es involucrado
    @Transient
    public boolean esInvolucrado(Persona persona) {
        for (PersonaExpediente pe : personasExpediente) {
            if (pe.getPersona().equals(persona) && Boolean.FALSE.equals(pe.getCargo())) {
                return true;
            }
        }
        return false;
    }

    // Cambiar rol de una persona
    public void cambiarRol(Persona persona, boolean nuevoEsIniciante) {
        for (PersonaExpediente pe : personasExpediente) {
            if (pe.getPersona().equals(persona)) {
                pe.setCargo(nuevoEsIniciante);
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "Expediente{" +
                "id_Expediente='" + id_Expediente + '\'' +
                ", textoNota='" + textoNota + '\'' +
                ", fechaIngreso=" + fechaIngreso +
                ", estado=" + estado +
                '}';
    }
}