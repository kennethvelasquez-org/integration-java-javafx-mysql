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
import org.kennethvelasquez.system.model.Product;
import org.kennethvelasquez.system.model.dto.ProductDTO;

/**
 * Repositorio encargado de la persistencia y acceso a datos de los productos.
 * <p>
 * Pertenece a la <b>Capa de Acceso a Datos (Data Access Layer / Repository Pattern)</b>.
 * Su función principal es interactuar directamente con la base de datos MySQL mediante
 * JDBC y la invocación de procedimientos almacenados (Stored Procedures), aislando por
 * completo los detalles de persistencia y consultas SQL de la capa de servicio y controlador.
 * </p>
 *
 * <p>
 * Responsabilidades clave:
 * <ul>
 *   <li>Ejecutar los procedimientos almacenados de inserción, actualización, eliminación y consulta en MySQL.</li>
 *   <li>Mapear los atributos de la entidad de dominio {@link Product} a los parámetros correspondientes de JDBC.</li>
 *   <li>Mapear los resultados de la vista {@code view_read_product} a instancias de {@link ProductDTO}.</li>
 *   <li>Asegurar la liberación adecuada de recursos de base de datos (mediante {@code try-with-resources}).</li>
 * </ul>
 * </p>
 *
 * @author STEPHRYS
 * @version 1.0
 * @see org.kennethvelasquez.system.repository.ProductInterface
 * @see org.kennethvelasquez.system.config.ConexionDB
 * @see org.kennethvelasquez.system.model.Product
 * @see org.kennethvelasquez.system.model.dto.ProductDTO
 */
public class ProductRepository implements ProductInterface {

    /**
     * Instancia singleton que administra la conexión activa a la base de datos MySQL.
     */
    private ConexionDB conexionDB = ConexionDB.getInstanceConexionDB();

    /**
     * {@inheritDoc}
     * <p>
     * Invoca el procedimiento almacenado {@code sp_create_product(name, description, price, img_producto, id_category)}.
     * </p>
     */
    @Override
    public void create(Product product) throws SQLException, SQLIntegrityConstraintViolationException {
        String storedProcedure = "{call sp_create_product(?,?,?,?,?,?)}";
        try (CallableStatement callSP = conexionDB.getConnection().prepareCall(storedProcedure)) {
            callSP.setString(1, product.getName());
            callSP.setString(2, product.getDescription());
            callSP.setDouble(3, product.getPrice());
            callSP.setBytes(4, product.getImgProducto());
            callSP.setInt(5, product.getIdCategory());
            callSP.setString(6, product.getIdUser());
            callSP.execute();
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Invoca el procedimiento almacenado {@code sp_read_products}, el cual recupera las columnas
     * de {@code view_read_product}: ID, Nombre, Descripcion, Precio, Imagen, ID de categoria y Categoria.
     * </p>
     */
    @Override
    public List<ProductDTO> read(String idUser) throws SQLException, SQLIntegrityConstraintViolationException {
        List<ProductDTO> listProducts = new ArrayList<>();
        String storedProcedure = "{call sp_read_products(?)}";

        try (CallableStatement callSP = conexionDB.getConnection().prepareCall(storedProcedure)) {
            callSP.setString(1, idUser);
            try (ResultSet result = callSP.executeQuery()) {
                while (result.next()) {
                    ProductDTO product = new ProductDTO();
                    product.setIdProduct(result.getInt(1));        // Columna 1: p.id_product (ID)
                    product.setName(result.getString(2));          // Columna 2: p.name (Nombre)
                    product.setDescription(result.getString(3));   // Columna 3: p.description (Descripcion)
                    product.setPrice(result.getDouble(4));         // Columna 4: p.price (Precio)
                    product.setImgProducto(result.getBytes(5));    // Columna 5: p.img_producto (Imagen en bytes)
                    product.setIdCategory(result.getInt(6));       // Columna 6: p.id_category (ID de categoria)
                    product.setCategoryName(result.getString(7));  // Columna 7: c.name_category (Categoria en texto)

                    listProducts.add(product);
                }
            }
        }
        return listProducts;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Invoca el procedimiento almacenado {@code sp_search_product(?)} filtrando por ID único.
     * </p>
     */
    @Override
    public ProductDTO search(Product product) throws SQLException, SQLIntegrityConstraintViolationException {
        String storedProcedure = "{call sp_search_product(?,?)}";

        try (CallableStatement callSP = conexionDB.getConnection().prepareCall(storedProcedure)) {
            callSP.setInt(1, product.getIdProduct());
            callSP.setString(2, product.getIdUser());

            try (ResultSet result = callSP.executeQuery()) {
                if (result.next()) {
                    ProductDTO productDTO = new ProductDTO();
                    productDTO.setIdProduct(result.getInt(1));
                    productDTO.setName(result.getString(2));
                    productDTO.setDescription(result.getString(3));
                    productDTO.setPrice(result.getDouble(4));
                    productDTO.setImgProducto(result.getBytes(5));
                    productDTO.setIdCategory(result.getInt(6));
                    productDTO.setCategoryName(result.getString(7));
                    return productDTO;
                }
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Invoca el procedimiento almacenado {@code sp_update_product(id, name, description, price, img_producto, id_category)}.
     * </p>
     */
    @Override
    public void update(Product product) throws SQLException, SQLIntegrityConstraintViolationException {
        String storedProcedure = "{call sp_update_product(?,?,?,?,?,?,?)}";
        try (CallableStatement callSP = conexionDB.getConnection().prepareCall(storedProcedure)) {
            callSP.setInt(1, product.getIdProduct());
            callSP.setString(2, product.getName());
            callSP.setString(3, product.getDescription());
            callSP.setDouble(4, product.getPrice());
            callSP.setBytes(5, product.getImgProducto());
            callSP.setInt(6, product.getIdCategory());
            callSP.setString(7, product.getIdUser());
            callSP.execute();
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Invoca el procedimiento almacenado {@code sp_delete_product(?)} para eliminar el registro físico.
     * </p>
     */
    @Override
    public void delete(Product product) throws SQLException, SQLIntegrityConstraintViolationException {
        String storedProcedure = "{call sp_delete_product(?,?)}";
        try (CallableStatement callSP = conexionDB.getConnection().prepareCall(storedProcedure)) {
            callSP.setInt(1, product.getIdProduct());
            callSP.setString(2, product.getIdUser());
            callSP.execute();
        }
    }
}
