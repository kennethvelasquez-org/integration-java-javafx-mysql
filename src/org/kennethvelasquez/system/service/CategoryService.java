/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kennethvelasquez.system.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.kennethvelasquez.system.model.Category;
import org.kennethvelasquez.system.repository.CategoryRepository;

/**
 * Servicio encargado de la lógica de negocio y gestión de datos para la entidad {@link Category}.
 * <p>
 * Se comunica con {@link CategoryRepository} para consultar categorías desde la base de datos
 * y suministrar la información a componentes gráficos como {@code ComboBox<Category>}
 * en {@link org.kennethvelasquez.system.controller.ProductViewController}.
 * </p>
 *
 * @author STEPHRYS
 * @version 1.0
 * @see CategoryRepository
 * @see CategoryStatus
 * @see Category
 */
public class CategoryService {

    /**
     * Repositorio para el acceso a datos de categorías.
     */
    private CategoryRepository categoryRepo = new CategoryRepository();

    /**
     * Mensaje de error para auditoría o propagación hacia el controlador.
     */
    private String messageError;

    /**
     * Lista en memoria de categorías leídas desde la base de datos.
     */
    private List<Category> categoriesList = new ArrayList<>();

    public CategoryService() {
    }

    /**
     * Obtiene todas las categorías registradas para suministrar al ComboBox.
     *
     * @return {@link CategoryStatus#READ_SUCCESS} si se recuperaron registros exitosamente,
     *         {@link CategoryStatus#EMPTY_LIST} si la tabla está vacía,
     *         o {@link CategoryStatus#ERROR_READ_CATEGORIES} si ocurrió un fallo de SQL.
     */
    public CategoryStatus readCategories() {
        try {
            this.categoriesList = categoryRepo.read();
            if (this.categoriesList == null || this.categoriesList.isEmpty()) {
                return CategoryStatus.EMPTY_LIST;
            }
            return CategoryStatus.READ_SUCCESS;
        } catch (SQLException e) {
            this.messageError = e.getMessage();
            return CategoryStatus.ERROR_READ_CATEGORIES;
        }
    }

    /**
     * Busca una categoría por su identificador único dentro de la lista en memoria.
     *
     * @param id Identificador numérico de la categoría.
     * @return La instancia {@link Category} correspondiente, o {@code null} si no existe.
     */
    public Category searchCategoryById(Integer id) {
        if (id == null) {
            return null;
        }
        for (Category category : categoriesList) {
            if (category.getIdCategory() != null && category.getIdCategory().equals(id)) {
                return category;
            }
        }
        return null;
    }

    /**
     * Busca una categoría por su nombre dentro de la lista en memoria.
     *
     * @param name Nombre de la categoría a buscar.
     * @return La instancia {@link Category} coincidente, o {@code null} si no se encuentra.
     */
    public Category searchCategoryByName(String name) {
        if (name == null) {
            return null;
        }
        for (Category category : categoriesList) {
            if (category.getNameCategory() != null && category.getNameCategory().equalsIgnoreCase(name.trim())) {
                return category;
            }
        }
        return null;
    }

    /**
     * Obtiene el índice numérico de la categoría buscada por nombre en la lista en memoria.
     *
     * @param name Nombre de la categoría.
     * @return Índice de la categoría en la lista, o {@code null} si no se encuentra.
     */
    public Integer searchIndexCategoryByName(String name) {
        if (name == null) {
            return null;
        }
        for (Category category : categoriesList) {
            if (category.getNameCategory() != null && category.getNameCategory().equalsIgnoreCase(name.trim())) {
                return categoriesList.indexOf(category);
            }
        }
        return null;
    }

    public List<Category> getCategoriesList() {
        return categoriesList;
    }

    public String getMessageError() {
        return messageError;
    }
}
