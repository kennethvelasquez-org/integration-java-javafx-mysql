package org.kennethvelasquez.system.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.kennethvelasquez.system.utils.AlertInformation;

public class ConexionDB{
    private static ConexionDB instanceConexionDB;
    private Connection instanceConnection;
    
    private ConexionDB(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            instanceConnection = DriverManager.getConnection(
      "jdbc:mysql://"+ Enviroment.LOCATION_SERVICE +"/"+ Enviroment.DATABASE,
                Enviroment.USER, 
                Enviroment.PASSWORD);
            
        } catch (ClassNotFoundException classNotFound) {
            AlertInformation alertInfo = new AlertInformation("ERROR DRIVER JDBC",
                    "Error al buscar la clase Driver",
                    "Se genero un error a la hora de buscar la clase Driver en com.mysql.cj.jdbc.Driver "
                            +classNotFound.getMessage(), 
                    "ERR");
            System.out.println("Error Constructor ConexionDB");
            classNotFound.printStackTrace();
            alertInfo.viewAlert();
        } catch (NullPointerException objetctNull) {
            AlertInformation alertInfo = new AlertInformation("ERROR OBJETO NULL",
                    "El objeto de conexion es nulo",
                    "Se genero un error al crear el objeto para conexion jdbc "
                            +objetctNull.getMessage(), 
                    "ERR");
            System.out.println("Error Null ConexionDB");
            objetctNull.printStackTrace();
            alertInfo.viewAlert();
        } catch (SQLException errorSQL) {
            AlertInformation alertInfo = new AlertInformation("ERROR CONEXION DATABASE",
                    "Error al conectar a la base de datos",
                    "Se genero un error al momento de establecer conexion a la base de datos "
                            +errorSQL.getMessage(), 
                    "ERR");
            System.out.println("Error Constructor ConexionDB");
            errorSQL.printStackTrace();
            alertInfo.viewAlert();
        }
    }
    
    
    public static ConexionDB getInstanceConexionDB(){
        if( instanceConexionDB == null )
            instanceConexionDB = new ConexionDB();
        return instanceConexionDB;
    }
    
    public Connection getConnection(){
        return instanceConnection;
    }
}