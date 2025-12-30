package com.lucas.proyecto.Clases;

import jakarta.persistence.*;
import java.util.Objects;

/*
 * @author Lukitas :)
 */
@Entity
@Table(name = "MINUTA")
@IdClass(MinutaId.class)
public class Minuta {

////////////////////////////////////////////////////
// ------------ Atributos ------------ //
////////////////////////////////////////////////////

    @Id
    @Column(name = "id_Minuta")
    private String id_Minuta;
    
    @Id
    @ManyToOne
    @JoinColumn(name = "id_Reunion", referencedColumnName = "id_Reunion")
    private Reunion reunion;
    
    @Id
    @ManyToOne
    @JoinColumn(name = "id_Expediente", referencedColumnName = "id_Expediente")
    private Expediente expediente;
    
    @Column(name = "contenido", length = 50)
    private String contenido;
    

////////////////////////////////////////////////////
// -------- Constructores --------- //
////////////////////////////////////////////////////

    public Minuta(String id_Minuta, Reunion reunion, Expediente expediente, String contenido) {
        this.id_Minuta = id_Minuta;
        this.reunion = reunion;
        this.expediente = expediente;
        this.contenido = contenido;
    }
    
    public Minuta() {
        //JEJE CONSTRUCTOR VACIO JEJE
    }

////////////////////////////////////////////////////
// ----------- Get y Set ------------ //
////////////////////////////////////////////////////

// ----------- Gets ------------ //

    public String getId() {
        return id_Minuta;
    }

    public Reunion getReunion() {
        return reunion;
    }

    public Expediente getExpediente() {
        return expediente;
    }

    public String getContenido() {
        return contenido;
    }
    
    // Métodos auxiliares para compatibilidad
    public String getIdExpediente() {
        return expediente != null ? expediente.getId() : null;
    }
    
    public String getIdReunion() {
        return reunion != null ? reunion.getId() : null;
    }

// ----------- Sets ------------ //

    public void setId(String id_Minuta) {
        this.id_Minuta = id_Minuta;
    }

    public void setReunion(Reunion reunion) {
        this.reunion = reunion;
    }

    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    
    // Métodos auxiliares para compatibilidad
    public void setIdExpediente(String idExpediente) {
        // Este método es para compatibilidad, pero es mejor usar setExpediente() con un objeto Expediente
        if (this.expediente == null) {
            this.expediente = new Expediente();
        }
        this.expediente.setId(idExpediente);
    }
    
    public void setIdReunion(String idReunion) {
        // Este método es para compatibilidad, pero es mejor usar setReunion() con un objeto Reunion
        if (this.reunion == null) {
            this.reunion = new Reunion();
        }
        this.reunion.setId(idReunion);
    }

////////////////////////////////////////////////////
// ----------- To String ------------ //
////////////////////////////////////////////////////

    @Override
    public String toString() {
        return "Minuta{" +
                "id_Minuta='" + id_Minuta + '\'' +
                ", reunion=" + (reunion != null ? reunion.getId() : "null") +
                ", expediente=" + (expediente != null ? expediente.getId() : "null") +
                ", contenido='" + contenido + '\'' +
                '}';
    }

////////////////////////////////////////////////////
// ------- Demás Metodos -------- //
////////////////////////////////////////////////////

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Minuta minuta = (Minuta) o;
        return Objects.equals(id_Minuta, minuta.id_Minuta) &&
               Objects.equals(reunion != null ? reunion.getId() : null, 
                             minuta.reunion != null ? minuta.reunion.getId() : null) &&
               Objects.equals(expediente != null ? expediente.getId() : null, 
                             minuta.expediente != null ? minuta.expediente.getId() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_Minuta, 
                          reunion != null ? reunion.getId() : null, 
                          expediente != null ? expediente.getId() : null);
    }
}