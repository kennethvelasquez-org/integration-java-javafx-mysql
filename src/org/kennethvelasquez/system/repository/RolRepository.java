/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kennethvelasquez.system.repository;

import java.sql.CallableStatement;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.kennethvelasquez.system.config.ConexionDB;
import org.kennethvelasquez.system.model.Rol;

/**
 *
 * @author STEPHRYS
 */
public class RolRepository implements RolInterface{

    private ConexionDB conexionDB = ConexionDB.getInstanceConexionDB();
    
    @Override
    public void create(Rol rol) throws SQLException, SQLIntegrityConstraintViolationException {
    
    }

    @Override
    public List<Rol> read() throws SQLException, SQLIntegrityConstraintViolationException {
        List<Rol> roles = new ArrayList<>();
        String storedProcedure = "{call sp_read_rol()}";
        try (CallableStatement callSP = conexionDB.getConnection().prepareCall(storedProcedure);
             ResultSet rs = callSP.executeQuery()) {
            while (rs.next()) {
                Rol rol = new Rol();
                rol.setIdRol(rs.getInt(1));
                rol.setName(rs.getString(2));
                rol.setDescription(rs.getString(3));
                roles.add(rol);
            }
        }
        return roles;
    }

    @Override
    public Rol search(int idRol) throws SQLException, SQLIntegrityConstraintViolationException {
        return null;
    }

    @Override
    public void update(Rol rol) throws SQLException, SQLIntegrityConstraintViolationException {
        
    }

    @Override
    public void delete(int idRol) throws SQLException, SQLIntegrityConstraintViolationException {
        
    }
    
}
