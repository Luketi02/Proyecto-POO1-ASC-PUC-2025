package com.lucas.proyecto.Clases;

import java.time.LocalDate;

/*
 * @author Lukitas :)
 */
public class Accion {

////////////////////////////////////////////////////
// ------------ Atributos ------------ //
////////////////////////////////////////////////////

	private String id;
	private LocalDate fecha;
	private String idExpediente;
	private String Descripcion;

////////////////////////////////////////////////////
// -------- Constructores --------- //
////////////////////////////////////////////////////

	public Accion(String id,  LocalDate fecha,String Descripcion, String expediente) {
		this.id = id;
		this.idExpediente = expediente;
		this.Descripcion = Descripcion;
		this.fecha = fecha;
	}
	
	public Accion(){
		
	}

////////////////////////////////////////////////////
// ----------- Get y Set ------------ //
///////////////////////////////////////////////////
    /// @return /

// ----------- Gets ------------ //

	public String getId() {
		return id;
	}

	public String getDescripcion() {
		return Descripcion;
	}

	public LocalDate getFecha() {
		return fecha;
	}
	
	public String getIdExpediente(){
		return idExpediente;
	}

// ----------- Sets ------------ //

	public void setId(String id) {
		this.id = id;
	}

	public void setDescripcion(String Descripcion) {
		this.Descripcion = Descripcion;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	
	public void setIdExpediente(String expediente){
		this.idExpediente = expediente;
	}

////////////////////////////////////////////////////
// ----------- To String ------------ //
///////////////////////////////////////////////////
    /// @return /

	@Override
	public String toString() {
		return "Accion{" + "id=" + id + ", fecha=" + fecha + ", Descripcion=" + Descripcion +  ", Descripcion=" + idExpediente + '}';
	}
  
////////////////////////////////////////////////////
// ------- Demás Metodos -------- //
////////////////////////////////////////////////////









}