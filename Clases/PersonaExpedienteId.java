package com.lucas.proyecto.Clases;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PersonaExpedienteId implements Serializable {

    private String dni;
    private String idExpediente;

    public PersonaExpedienteId() {
    }

    public PersonaExpedienteId(String dni, String idExpediente) {
        this.dni = dni;
        this.idExpediente = idExpediente;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getIdExpediente() {
        return idExpediente;
    }

    public void setIdExpediente(String idExpediente) {
        this.idExpediente = idExpediente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonaExpedienteId)) return false;
        PersonaExpedienteId that = (PersonaExpedienteId) o;
        return Objects.equals(dni, that.dni) &&
               Objects.equals(idExpediente, that.idExpediente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dni, idExpediente);
    }
}

