package com.lucas.proyecto.Clases;

import java.io.Serializable;
import java.util.Objects;

public class AsistenciaId implements Serializable {
    private String persona; // corresponde al dni
    private String reunion; // corresponde al id_Reunion

    // Constructores
    public AsistenciaId() {
    }

    public AsistenciaId(String personaDni, String reunionId) {
        this.persona = personaDni;
        this.reunion = reunionId;
    }

    // Getters y Setters
    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public String getReunion() {
        return reunion;
    }

    public void setReunion(String reunion) {
        this.reunion = reunion;
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AsistenciaId that = (AsistenciaId) o;
        return Objects.equals(persona, that.persona) &&
               Objects.equals(reunion, that.reunion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(persona, reunion);
    }
}
