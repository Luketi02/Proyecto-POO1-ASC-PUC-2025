package com.lucas.proyecto.Clases;

import java.io.Serializable;
import java.util.Objects;

public class MinutaId implements Serializable {
    private String id_Minuta;
    private String reunion; // corresponde al id_Reunion
    private String expediente; // corresponde al id_Expediente

    // Constructores
    public MinutaId() {
    }

    public MinutaId(String id_Minuta, String id_Reunion, String id_Expediente) {
        this.id_Minuta = id_Minuta;
        this.reunion = id_Reunion;
        this.expediente = id_Expediente;
    }

    // Getters y Setters
    public String getId_Minuta() {
        return id_Minuta;
    }

    public void setId_Minuta(String id_Minuta) {
        this.id_Minuta = id_Minuta;
    }

    public String getReunion() {
        return reunion;
    }

    public void setReunion(String reunion) {
        this.reunion = reunion;
    }

    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MinutaId minutaId = (MinutaId) o;
        return Objects.equals(id_Minuta, minutaId.id_Minuta) &&
               Objects.equals(reunion, minutaId.reunion) &&
               Objects.equals(expediente, minutaId.expediente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_Minuta, reunion, expediente);
    }
}