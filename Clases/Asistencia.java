package com.lucas.proyecto.Clases;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ASISTENCIA")
@IdClass(AsistenciaId.class)
public class Asistencia {

    @Id
    @ManyToOne
    @JoinColumn(name = "dni", referencedColumnName = "dni", nullable = false)
    private Persona persona;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_Reunion", referencedColumnName = "id_Reunion", nullable = false)
    private Reunion reunion;

    @Column(name = "presencia")
    private Boolean presencia;

    // Constructores
    public Asistencia() {
    }

    public Asistencia(Persona persona, Reunion reunion, Boolean presencia) {
        this.persona = persona;
        this.reunion = reunion;
        this.presencia = presencia;
    }

    // Getters y Setters
    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Reunion getReunion() {
        return reunion;
    }

    public void setReunion(Reunion reunion) {
        this.reunion = reunion;
    }

    public Boolean getPresencia() {
        return presencia;
    }

    public void setPresencia(Boolean presencia) {
        this.presencia = presencia;
    }

    // Métodos de utilidad
    public boolean isPresente() {
        return Boolean.TRUE.equals(presencia);
    }

    public boolean isAusente() {
        return Boolean.FALSE.equals(presencia);
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Asistencia that = (Asistencia) o;
        return Objects.equals(persona != null ? persona.getDni() : null, 
                             that.persona != null ? that.persona.getDni() : null) &&
               Objects.equals(reunion != null ? reunion.getId() : null, 
                             that.reunion != null ? that.reunion.getId() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(persona != null ? persona.getDni() : null, 
                          reunion != null ? reunion.getId() : null);
    }

    // toString 
    @Override
    public String toString() {
        return "Asistencia{" +
                "persona=" + (persona != null ? persona.getDni() : "null") +
                ", reunion=" + (reunion != null ? reunion.getId() : "null") +
                ", presencia=" + presencia +
                '}';
    }
}
