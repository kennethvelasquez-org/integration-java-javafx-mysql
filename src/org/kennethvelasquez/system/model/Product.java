/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kennethvelasquez.system.model;

/**
 * Entidad de dominio que representa la tabla Product en la base de datos.
 * <p>
 * Contiene los atributos requeridos tanto para la creación como para la persistencia
 * en MySQL: id_product, name, description, price, img_producto, id_user e id_category.
 * </p>
 *
 * @author STEPHRYS
 */
public class Product {

    private Integer idProduct;
    private String name;
    private String description;
    private Double price;
    private byte[] imgProduct;
    private String idUser;
    private Integer idCategory;

    public Product() {
    }

    /**
     * Constructor para la creación de un nuevo producto (sin ID, ya que es auto_increment en la base de datos).
     *
     * @param name        Nombre del producto
     * @param description Descripción del producto
     * @param price       Precio del producto
     * @param imgProduct Imagen en formato binario (mediumblob)
     * @param idCategory  ID de la categoría a la que pertenece
     */
    public Product(String name, String description, Double price, byte[] imgProduct, Integer idCategory) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgProduct = imgProduct;
        this.idCategory = idCategory;
    }

    /**
     * Constructor para la creación con referencia al usuario que lo registra.
     *
     * @param name        Nombre del producto
     * @param description Descripción del producto
     * @param price       Precio del producto
     * @param imgProduct Imagen en formato binario (mediumblob)
     * @param idUser      UUID del usuario que registra el producto
     * @param idCategory  ID de la categoría a la que pertenece
     */
    public Product(String name, String description, Double price, byte[] imgProduct, String idUser, Integer idCategory) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgProduct = imgProduct;
        this.idUser = idUser;
        this.idCategory = idCategory;
    }

    /**
     * Constructor completo con ID auto_increment generado.
     *
     * @param idProduct   Identificador único del producto
     * @param name        Nombre del producto
     * @param description Descripción del producto
     * @param price       Precio del producto
     * @param imgProduct Imagen en formato binario (mediumblob)
     * @param idCategory  ID de la categoría a la que pertenece
     */
    public Product(Integer idProduct, String name, String description, Double price, byte[] imgProduct, Integer idCategory) {
        this.idProduct = idProduct;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgProduct = imgProduct;
        this.idCategory = idCategory;
    }

    /**
     * Constructor completo con ID auto_increment y usuario.
     *
     * @param idProduct   Identificador único del producto
     * @param name        Nombre del producto
     * @param description Descripción del producto
     * @param price       Precio del producto
     * @param imgProduct Imagen en formato binario (mediumblob)
     * @param idUser      UUID del usuario que registra el producto
     * @param idCategory  ID de la categoría a la que pertenece
     */
    public Product(Integer idProduct, String name, String description, Double price, byte[] imgProduct, String idUser, Integer idCategory) {
        this.idProduct = idProduct;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgProduct = imgProduct;
        this.idUser = idUser;
        this.idCategory = idCategory;
    }

    /**
     * Constructor ligero para operaciones que requieren únicamente el identificador
     * del producto y el usuario ejecutor (como búsquedas o eliminaciones con auditoría).
     *
     * @param idProduct Identificador único del producto
     * @param idUser    UUID del usuario que ejecuta la acción
     */
    public Product(Integer idProduct, String idUser) {
        this.idProduct = idProduct;
        this.idUser = idUser;
    }

    public Integer getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Integer idProduct) {
        this.idProduct = idProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public byte[] getImgProduct() {
        return imgProduct;
    }

    public void setImgProduct(byte[] imgProduct) {
        this.imgProduct = imgProduct;
    }

    public byte[] getImgProducto() {
        return imgProduct;
    }

    public void setImgProducto(byte[] imgProducto) {
        this.imgProduct = imgProducto;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public Integer getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Integer idCategory) {
        this.idCategory = idCategory;
    }

    @Override
    public String toString() {
        return "Product{" +
                "idProduct=" + idProduct +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", idCategory=" + idCategory +
                '}';
    }
}
