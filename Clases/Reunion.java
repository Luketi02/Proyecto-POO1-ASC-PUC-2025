package com.lucas.proyecto.Clases;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
 * @author Lukitas :)
 */
@Entity
@Table(name = "REUNION")
public class Reunion {

////////////////////////////////////////////////////
// ------------ Atributos ------------ //
////////////////////////////////////////////////////

    @Id
    @Column(name = "id_Reunion")
    private String id;
    
    @Column(name = "fecha")
    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "id_Expediente", referencedColumnName = "id_Expediente")
    private Expediente expediente;
    
    @OneToMany(mappedBy = "reunion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Asistencia> asistencias = new ArrayList<>();
    
    @OneToMany(mappedBy = "reunion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Minuta> minutas = new ArrayList<>();

////////////////////////////////////////////////////
// -------- Constructores --------- //
////////////////////////////////////////////////////

    public Reunion(String id, LocalDateTime fecha, Expediente expediente) {
        this.id = id;
        this.fecha = fecha;
        this.expediente = expediente;
    }
    
    public Reunion(){
        
    }
    
////////////////////////////////////////////////////
// ----------- Get y Set ------------ //
////////////////////////////////////////////////////

// ----------- Gets ------------ //

    public String getId() {
        return id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public Expediente getExpediente() {
        return expediente;
    }

    public List<Asistencia> getAsistencias() {
        return asistencias;
    }

    public List<Minuta> getMinutas() {
        return minutas;
    }

// ----------- Sets ------------ //
    
    public void setId(String id){
        this.id = id;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }

    public void setAsistencias(List<Asistencia> asistencias) {
        this.asistencias = asistencias;
    }

    public void setMinutas(List<Minuta> minutas) {
        this.minutas = minutas;
    }

////////////////////////////////////////////////////
// ----------- To String ------------ //
////////////////////////////////////////////////////
    
    @Override
    public String toString() {
        return "Reunion{" +
                "id='" + id + '\'' +
                ", fecha=" + fecha +
                ", expediente=" + (expediente != null ? expediente.getId() : "null") +
                '}';
    }

////////////////////////////////////////////////////
// ------- Demás Metodos -------- //
////////////////////////////////////////////////////
    
    // Métodos de utilidad para manejar asistencias
    public void agregarAsistencia(Asistencia asistencia) {
        asistencias.add(asistencia);
        asistencia.setReunion(this);
    }
    
    public void removerAsistencia(Asistencia asistencia) {
        asistencias.remove(asistencia);
        asistencia.setReunion(null);
    }
    
    // Métodos de utilidad para manejar minutas
    public void agregarMinuta(Minuta minuta) {
        minutas.add(minuta);
        minuta.setReunion(this);
    }
    
    public void removerMinuta(Minuta minuta) {
        minutas.remove(minuta);
        minuta.setReunion(null);
    }
    
    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reunion reunion = (Reunion) o;
        return Objects.equals(id, reunion.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}