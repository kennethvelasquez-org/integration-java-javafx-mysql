/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kennethvelasquez.system.repository;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import org.kennethvelasquez.system.config.ConexionDB;
import org.kennethvelasquez.system.model.Category;

/**
 * Repositorio encargado del acceso y consulta a datos de categorías en MySQL.
 * <p>
 * Implementa {@link CategoryInterface} consumiendo los procedimientos almacenados correspondientes.
 * </p>
 *
 * @author STEPHRYS
 * @version 1.0
 * @see CategoryInterface
 * @see org.kennethvelasquez.system.config.ConexionDB
 * @see org.kennethvelasquez.system.model.Category
 */
public class CategoryRepository implements CategoryInterface {

    /**
     * Singleton de conexión a la base de datos MySQL.
     */
    private ConexionDB conexionDB = ConexionDB.getInstanceConexionDB();

    /**
     * {@inheritDoc}
     * <p>
     * Invoca el procedimiento almacenado {@code sp_read_category()}, mapeando
     * las columnas {@code ID}, {@code Nombre} y {@code Descripcion} a instancias de {@link Category}.
     * </p>
     */
    @Override
    public List<Category> read() throws SQLException, SQLIntegrityConstraintViolationException {
        List<Category> categories = new ArrayList<>();
        String storedProcedure = "{call sp_read_category()}";

        try (CallableStatement callSP = conexionDB.getConnection().prepareCall(storedProcedure);
             ResultSet rs = callSP.executeQuery()) {

            while (rs.next()) {
                Category category = new Category();
                category.setIdCategory(rs.getInt(1));
                category.setNameCategory(rs.getString(2));
                category.setDescription(rs.getString(3));
                categories.add(category);
            }
        }
        return categories;
    }
}
