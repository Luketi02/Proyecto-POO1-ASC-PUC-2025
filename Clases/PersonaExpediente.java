package com.lucas.proyecto.Clases;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PERSONA_EXPEDIENTE")
public class PersonaExpediente implements Serializable {

    @EmbeddedId
    private PersonaExpedienteId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("dni")
    @JoinColumn(name = "DNI", nullable = false)
    private Persona persona;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idExpediente")
    @JoinColumn(name = "id_Expediente", nullable = false)
    private Expediente expediente;

    @Column(name = "cargo", nullable = false)
    private Boolean cargo;

    public PersonaExpediente() {
    }

    public PersonaExpediente(Persona persona, Expediente expediente, Boolean cargo) {
        this.persona = persona;
        this.expediente = expediente;
        this.cargo = cargo;
        this.id = new PersonaExpedienteId(persona.getDni(), expediente.getId());
    }

    public PersonaExpedienteId getId() {
        return id;
    }

    public void setId(PersonaExpedienteId id) {
        this.id = id;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Expediente getExpediente() {
        return expediente;
    }

    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }

    public Boolean getCargo() {
        return cargo;
    }

    public void setCargo(Boolean cargo) {
        this.cargo = cargo;
    }

    @Override
    public String toString() {
        return "PersonaExpediente{" +
                "id=" + id +
                ", cargo=" + cargo +
                '}';
    }
}
