package com.lucas.proyecto.Clases;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/*
 * @author Luketi :)
 */

public class ConexionBD {

	private final String Host = "localhost";
	private final String Puerto = "5432";
	private final String BD = "Integrador - POO1";
	private final String Usuario = "postgres";
	private final String Contra = "POSTGRES";
	
	public Connection getConexion(){
		Connection conexion = null;
		
		try{
			Class.forName("org.postgresql.Driver");
			String url = "jdbc:postgresql://"+Host+":"+Puerto+"/"+BD;
			conexion = DriverManager.getConnection(url,Usuario,Contra);
		}catch (Exception e){
			JOptionPane.showMessageDialog(null,"\t\tOcurrió un error con la base de datos y no se logró establecer la conexión \nError:  " + e.getMessage());
		}
		return conexion;
	}

	public ResultSet hacerConsulta(String consulta){
		PreparedStatement sentencia = null;
		Connection conexion = null;
		ResultSet resultado = null;
		try{
			Class.forName("org.postgresql.Driver");
			String url = "jdbc:postgresql://"+Host+":"+Puerto+"/"+BD;
			conexion = DriverManager.getConnection(url,Usuario,Contra);

			sentencia = conexion.prepareStatement(consulta);
			
			resultado = sentencia.executeQuery();
			
		}catch (Exception e){
			JOptionPane.showMessageDialog(null,"\t\tOcurrió un error con la base de datos y no se logró realizar la consulta \nError:  " + e.getMessage());
		}
		
		return resultado;
	} 
	
	public ResultSet buscarValor(String consulta, String valor){
		PreparedStatement sentencia = null;
		Connection conexion = null;
		ResultSet resultado = null;
		try{
			Class.forName("org.postgresql.Driver");
			String url = "jdbc:postgresql://"+Host+":"+Puerto+"/"+BD;
			conexion = DriverManager.getConnection(url,Usuario,Contra);

			sentencia = conexion.prepareStatement(consulta);
			sentencia.setString(1, valor);
			
			resultado = sentencia.executeQuery();
		}catch (Exception e){
			JOptionPane.showMessageDialog(null,"\t\tOcurrió un error con la base de datos y no se logró buscar el valor \nError:  " + e.getMessage());
		}
		
		return resultado;
	} 
	
	
	
	public boolean hacerCambios(String consulta){
		PreparedStatement sentencia = null;
		Connection conexion = null;
		boolean resultado = false;
		try{
			Class.forName("org.postgresql.Driver");
			String url = "jdbc:postgresql://"+Host+":"+Puerto+"/"+BD;
			conexion = DriverManager.getConnection(url,Usuario,Contra);

			sentencia = conexion.prepareStatement(consulta);
			
			resultado = sentencia.executeUpdate() > 0;
			
		}catch (Exception e){
			JOptionPane.showMessageDialog(null,"\t\tOcurrió un error con la base de datos y no se lograron hacer cambios \nError:  " + e.getMessage());
		}
		
		return resultado;
	} 
}