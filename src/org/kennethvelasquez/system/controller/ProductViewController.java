/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.kennethvelasquez.system.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.kennethvelasquez.system.model.ApplicationStatus;
import org.kennethvelasquez.system.model.Category;
import org.kennethvelasquez.system.model.dto.ProductDTO;
import org.kennethvelasquez.system.service.CategoryService;
import org.kennethvelasquez.system.service.CategoryStatus;
import org.kennethvelasquez.system.service.ProductService;
import org.kennethvelasquez.system.service.ProductStatus;
import org.kennethvelasquez.system.utils.AlertInformation;
import org.kennethvelasquez.system.utils.ImageTool;
import org.kennethvelasquez.system.utils.Validations;
import org.kennethvelasquez.system.utils.ViewFactory;

/**
 * Controlador FXML para la gestión y visualización de Productos ({@link ProductDTO}).
 * <p>
 * Se comunica directamente con {@link ProductService} para delegar validaciones de negocio,
 * procesamiento de imágenes y persistencia en base de datos.
 * </p>
 *
 * @author STEPHRYS
 */
public class ProductViewController implements Initializable {

    @FXML
    private Button btnCancel;
    @FXML
    private Button btnCreate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnRead;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnUploadImage;

    @FXML
    private TableView<ProductDTO> tblProducts;
    @FXML
    private TableColumn<ProductDTO, Integer> colIdProduct;
    @FXML
    private TableColumn<ProductDTO, ImageView> colImgProduct;
    @FXML
    private TableColumn<ProductDTO, String> colName;
    @FXML
    private TableColumn<ProductDTO, String> colDescription;
    @FXML
    private TableColumn<ProductDTO, Double> colPrice;
    @FXML
    private TableColumn<ProductDTO, String> colCategory;

    @FXML
    private TextField txtIdProduct;
    @FXML
    private TextField txtName;
    @FXML
    private TextArea txaDescription;
    @FXML
    private TextField txtPrice;
    @FXML
    private ComboBox<Category> cmbCategory;

    @FXML
    private ImageView imvPreview;

    /**
     * Factoría de vistas para la navegación entre escenas.
     */
    private ViewFactory viewFacto = new ViewFactory();

    /**
     * Capa de servicio encargada de la lógica de negocio y validación de productos e imágenes.
     */
    private ProductService productService = new ProductService();

    /**
     * Capa de servicio encargada de la lógica de consulta de categorías.
     */
    private CategoryService categoryService = new CategoryService();

    private ObservableList<ProductDTO> observableListProducts = FXCollections.observableArrayList();
    private ObservableList<Category> observableListCategories = FXCollections.observableArrayList();

    private ApplicationStatus userViewStatus = ApplicationStatus.NONE;
    private AlertInformation alertInfo = new AlertInformation();
    private Validations validate = new Validations();

    private ProductDTO productSelect = null;
    private byte[] imageBytesSelected = null;

    /**
     * Inicializa el controlador de la vista de productos.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTableColumns();
        loadComboBox();

        // Callback Reactivo del modelo de selección en la tabla de productos
        tblProducts.getSelectionModel().selectedItemProperty().addListener((obs, oldProduct, newProduct) -> {
            this.productSelect = newProduct;
            if (userViewStatus == ApplicationStatus.NONE && newProduct != null) {
                viewProduct();
                showBasicFields();
                btnUpdate.setDisable(false);
                btnDelete.setDisable(false);
                btnCancel.setDisable(false);
                btnCreate.setDisable(true);
                btnRead.setDisable(true);
            }
        });

        controlOptionsCRUD();
    }
    
    /**
     * Enlaza las columnas del TableView con los atributos de ProductDTO.
     */
    private void initTableColumns() {
        colIdProduct.setCellValueFactory(new PropertyValueFactory<>("idProduct"));
        colImgProduct.setCellValueFactory(new PropertyValueFactory<>("imageView"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
    }

    private void loadComboBox() {
        CategoryStatus status = categoryService.readCategories();
        switch (status) {
            case READ_SUCCESS -> {
                observableListCategories = FXCollections.observableArrayList(categoryService.getCategoriesList());
                cmbCategory.setItems(observableListCategories);
            }
            case EMPTY_LIST -> {
                alertInfo.viewAlert("LISTAR CATEGORÍAS", "No hay categorías para listar",
                        "No existen categorías actualmente registradas.",
                        "WARN");
            }
            case ERROR_READ_CATEGORIES -> {
                alertInfo.viewAlert("ERROR LISTAR CATEGORÍAS", "Error al listar categorías",
                        "Ocurrió un error al momento de listar las categorías.\n" + categoryService.getMessageError(),
                        "ERR");
            }
        }
    }

    /**
     * Control de estados para la barra de botones CRUD.
     */
    private void controlOptionsCRUD() {
        switch (userViewStatus) {
            case NONE -> {
                btnCreate.setDisable(false);
                btnUpdate.setDisable(true);
                btnRead.setDisable(false);
                btnDelete.setDisable(true);
                btnCancel.setDisable(true);
            }
            case CREATE -> {
                btnCreate.setDisable(false);
                btnRead.setDisable(true);
                btnUpdate.setDisable(true);
                btnDelete.setDisable(true);
                btnCancel.setDisable(false);
            }
            case SAVE -> {
                btnCreate.setDisable(true);
                btnRead.setDisable(true);
                btnUpdate.setDisable(false);
                btnDelete.setDisable(true);
                btnCancel.setDisable(false);
            }
            case DELETE -> {
                btnCreate.setDisable(true);
                btnRead.setDisable(true);
                btnUpdate.setDisable(true);
                btnDelete.setDisable(false);
                btnCancel.setDisable(false);
            }
            case SEARCH -> {
                btnCreate.setDisable(true);
                btnUpdate.setDisable(true);
                btnRead.setDisable(true);
                btnDelete.setDisable(true);
                btnCancel.setDisable(false);
            }
        }
    }

    @FXML
    private void onDashboard(MouseEvent event) {
        viewFacto.dashboardView();
    }

    /**
     * Acción para cargar y previsualizar una imagen delegando la validación técnica al Servicio.
     */
    @FXML
    private void onUploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen del Producto");
        fileChooser.getExtensionFilters().add(ImageTool.getImageExtensionFilter());

        Stage stage = (Stage) btnUploadImage.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            // El servicio valida formato, existencia y peso límite de 10 MB
            ProductStatus status = productService.processProductImage(selectedFile);

            switch (status) {
                case IMAGE_SUCCESS -> {
                    imageBytesSelected = productService.getTemporalImageBytes();
                    Image img = ImageTool.bytesToImage(imageBytesSelected);
                    imvPreview.setImage(img);
                    imvPreview.setVisible(true);
                    imvPreview.setManaged(true);
                }
                case IMAGE_INVALID_FORMAT ->
                    alertInfo.viewAlert("FORMATO NO VÁLIDO", "Tipo de imagen no soportado",
                            "Solo se permiten imágenes con extensiones: PNG, JPG, JPEG, GIF o BMP.",
                            "WARN");
                case IMAGE_SIZE_EXCEEDED ->
                    alertInfo.viewAlert("ARCHIVO MUY PESADO", "Tamaño de imagen excedido",
                            "La imagen no debe superar el límite de 10 MB.",
                            "WARN");
                case IMAGE_READ_ERROR ->
                    alertInfo.viewAlert("ERROR DE LECTURA", "No se pudo leer la imagen",
                            "Ocurrió un error al leer el archivo de imagen:\n" + productService.getMessageError(),
                            "ERR");
            }
        }
    }

    /**
     * Muestra la información del producto seleccionado en los campos y el previsualizador.
     */
    private void viewProduct() {
        if (productSelect == null) {
            return;
        }
        txtIdProduct.setText(String.valueOf(productSelect.getIdProduct()) );
        txtName.setText(productSelect.getName());
        txaDescription.setText(productSelect.getDescription());
        txtPrice.setText(String.valueOf(productSelect.getPrice()));

        // Seleccionar categoría correspondiente en el ComboBox
        if (productSelect.getIdCategory() != null) {
            cmbCategory.setValue(categoryService.searchCategoryById(productSelect.getIdCategory()));
        } else {
            cmbCategory.getSelectionModel().clearSelection();
        }

        // Cargar imagen en el previsualizador si existe
        if (productSelect.getImgProduct() != null && productSelect.getImgProduct().length > 0) {
            Image img = ImageTool.bytesToImage(productSelect.getImgProduct());
            imvPreview.setImage(img);
            imvPreview.setVisible(true);
            imvPreview.setManaged(true);
        } else {
            imvPreview.setImage(null);
            imvPreview.setVisible(false);
            imvPreview.setManaged(false);
        }
    }

    @FXML
    private void onCancel(ActionEvent event) {
        switch (userViewStatus) {
            case CREATE -> btnCreate.setText("AGREGAR");
            case SAVE -> btnUpdate.setText("EDITAR");
            case SEARCH -> btnSearch.setText("BUSCAR");
        }
        userViewStatus = ApplicationStatus.NONE;
        productService.clearTemporalImage();
        clearAllFields();
        hideAllFields();
        controlOptionsCRUD();
    }

    @FXML
    private void onCreate(ActionEvent event) {
        switch (userViewStatus) {
            case NONE -> {
                userViewStatus = ApplicationStatus.CREATE;
                controlOptionsCRUD();
                btnCreate.setText("CREAR");
                clearAllFields();
                showAllFields();
            }
            case CREATE -> {
                if (isValidFields()) {
                    String name = txtName.getText().trim();
                    String description = txaDescription.getText().trim();
                    Double price = Double.valueOf(txtPrice.getText().trim());
                    Category categorySelect = cmbCategory.getSelectionModel().getSelectedItem();
                    Integer idCategory = categorySelect != null ? categorySelect.getIdCategory() : null;

                    ProductStatus status = productService.createProduct(name, description, price, imageBytesSelected, idCategory);
                    switch (status) {
                        case PRODUCT_CREATED -> {
                            alertInfo.viewAlert("CREAR PRODUCTO", "¡Producto creado exitosamente!",
                                    "El producto se ha guardado en la base de datos.",
                                    "INFO");
                            userViewStatus = ApplicationStatus.NONE;
                            controlOptionsCRUD();
                            btnCreate.setText("AGREGAR");
                            clearAllFields();
                            hideAllFields();
                            onRead(null);
                        }
                        case INVALID_DATA ->
                            alertInfo.viewAlert("DATOS INCOMPLETOS", "Campos obligatorios requeridos",
                                    "Existen campos vacíos o requeridos sin completar.",
                                    "WARN");
                        case INVALID_NAME_LENGTH ->
                            alertInfo.viewAlert("LONGITUD EXCEDIDA", "Nombre muy largo",
                                    "El nombre del producto no puede exceder los 100 caracteres.",
                                    "WARN");
                        case INVALID_DESCRIPTION_LENGTH ->
                            alertInfo.viewAlert("LONGITUD EXCEDIDA", "Descripción muy larga",
                                    "La descripción del producto no puede exceder los 200 caracteres.",
                                    "WARN");
                        case INVALID_PRICE ->
                            alertInfo.viewAlert("PRECIO INVÁLIDO", "Precio incorrecto",
                                    "El precio debe ser un valor positivo mayor a 0.00.",
                                    "WARN");
                        case CATEGORY_REQUIRED ->
                            alertInfo.viewAlert("CATEGORÍA REQUERIDA", "Categoría no válida",
                                    "Debe seleccionar una categoría existente para el producto.",
                                    "WARN");
                        case ERROR_PRODUCT_CREATE ->
                            alertInfo.viewAlert("ERROR AL CREAR", "Fallo en base de datos",
                                    "Ocurrió un error al guardar el producto:\n" + productService.getMessageError(),
                                    "ERR");
                        default ->
                            alertInfo.viewAlert("ERROR", "No se pudo registrar el producto",
                                    "Respuesta inesperada: " + status,
                                    "ERR");
                    }
                }
            }
        }
    }

    @FXML
    private void onUpdate(ActionEvent event) {
        switch (userViewStatus) {
            case NONE -> {
                if (productSelect != null) {
                    userViewStatus = ApplicationStatus.SAVE;
                    controlOptionsCRUD();
                    btnUpdate.setText("GUARDAR");
                    showAllFields();
                    imageBytesSelected = null;
                } else {
                    alertInfo.viewAlert("EDITAR PRODUCTO", "No se seleccionó producto",
                            "Debe seleccionar un producto en la tabla para editar.",
                            "WARN");
                }
            }
            case SAVE -> {
                if (isValidFields()) {
                    String name = txtName.getText().trim();
                    String description = txaDescription.getText().trim();
                    Double price = Double.valueOf(txtPrice.getText().trim());
                    Category categorySelect = cmbCategory.getSelectionModel().getSelectedItem();
                    Integer idCategory =  categorySelect.getIdCategory() ;

                    ProductStatus status = productService.updateProduct(
                            productSelect.getIdProduct(),
                            name,
                            description,
                            price,
                            imageBytesSelected, // Si es null, MySQL preserva la imagen previa
                            idCategory
                    );

                    switch (status) {
                        case PRODUCT_UPDATED -> {
                            alertInfo.viewAlert("ACTUALIZACIÓN EXITOSA", "Producto actualizado",
                                    "Los datos del producto han sido modificados exitosamente.",
                                    "INFO");
                            btnUpdate.setText("EDITAR");
                            userViewStatus = ApplicationStatus.NONE;
                            controlOptionsCRUD();
                            clearAllFields();
                            hideAllFields();
                            onRead(null);
                        }
                        case INVALID_DATA ->
                            alertInfo.viewAlert("DATOS INCOMPLETOS", "Campos obligatorios requeridos",
                                    "Existen campos vacíos o requeridos sin completar.",
                                    "WARN");
                        case INVALID_NAME_LENGTH ->
                            alertInfo.viewAlert("LONGITUD EXCEDIDA", "Nombre muy largo",
                                    "El nombre del producto no puede exceder los 100 caracteres.",
                                    "WARN");
                        case INVALID_DESCRIPTION_LENGTH ->
                            alertInfo.viewAlert("LONGITUD EXCEDIDA", "Descripción muy larga",
                                    "La descripción del producto no puede exceder los 200 caracteres.",
                                    "WARN");
                        case INVALID_PRICE ->
                            alertInfo.viewAlert("PRECIO INVÁLIDO", "Precio incorrecto",
                                    "El precio debe ser un valor positivo mayor a 0.00.",
                                    "WARN");
                        case CATEGORY_REQUIRED ->
                            alertInfo.viewAlert("CATEGORÍA REQUERIDA", "Categoría no válida",
                                    "Debe seleccionar una categoría existente para el producto.",
                                    "WARN");
                        case ERROR_PRODUCT_UPDATE ->
                            alertInfo.viewAlert("ERROR AL ACTUALIZAR", "Fallo en base de datos",
                                    "Ocurrió un error al modificar el producto:\n" + productService.getMessageError(),
                                    "ERR");
                        default ->
                            alertInfo.viewAlert("ERROR", "No se pudo actualizar el producto",
                                    "Respuesta inesperada: " + status,
                                    "ERR");
                    }
                }
            }
        }
    }
    
    private void deleteProducto(ProductDTO productSelect){
        alertInfo.viewAlert("ELIMINAR PRODUCTO", "¿Estás seguro de eliminar?",
                        "¿Desea eliminar el producto: " + productSelect.getName() + "?",
                        "CONFIRM");

        if (alertInfo.isConfirmed()) {
            ProductStatus status = productService.deleteProduct(productSelect.getIdProduct());
            switch (status) {
                case PRODUCT_DELETED -> {
                    alertInfo.viewAlert("ELIMINAR PRODUCTO", "Producto eliminado",
                            "El producto ha sido eliminado correctamente.",
                            "INFO");
                    userViewStatus = ApplicationStatus.NONE;
                    onRead(null);
                }
                case PRODUCT_HAS_DEPENDENCIES ->
                    alertInfo.viewAlert("NO SE PUEDE ELIMINAR", "Registro con dependencias",
                            "El producto está vinculado a otras transacciones y no puede eliminarse.",
                            "WARN");
                case ERROR_PRODUCT_DELETE ->
                    alertInfo.viewAlert("ERROR AL ELIMINAR", "Fallo en base de datos",
                            "Ocurrió un error al eliminar el producto:\n" + productService.getMessageError(),
                            "ERR");
                case PRODUCT_NOT_FOUND->{
                    alertInfo.viewAlert(
                        "PRODUCTO NO ENCONTRADO",
                        "Registro inexistente",
                        "El producto no existe o ya fue eliminado del sistema.",
                        "WARN"
                    );
                }
                case ERROR_SEARCH_PRODUCT->{
                    alertInfo.viewAlert(
                        "ERROR DE COMPROBACIÓN",
                        "Error al verificar producto",
                        "Ocurrió un error al intentar verificar la existencia del producto:\n" 
                                + productService.getMessageError(),
                        "ERR"
                    );
                }
            }
        }else{
            alertInfo.viewAlert("ELIMINAR PRODUCTO",
                "No se ha eliminado el producto",
                "El producto seleccionado no se ha eliminado",
                "INFO");
            userViewStatus = ApplicationStatus.NONE;
        }
        controlOptionsCRUD();
        clearAllFields();
        hideAllFields();
    }

    @FXML
    private void onDelete(ActionEvent event) {
        if (userViewStatus == ApplicationStatus.NONE) {
            productSelect = tblProducts.getSelectionModel().getSelectedItem();
            if (productSelect != null) {
                userViewStatus = ApplicationStatus.DELETE;
                controlOptionsCRUD();
                viewProduct();
                showBasicFields();
                deleteProducto(productSelect);
            } else {
                alertInfo.viewAlert("ELIMINAR PRODUCTO", "No se seleccionó producto",
                        "Tiene que seleccionar un producto en la tabla para eliminar.",
                        "WARN");
            }
        }
    }
    
    private void loadTableProducts(){
        observableListProducts = FXCollections.observableArrayList(productService.getProductsList());
        tblProducts.setItems(observableListProducts);
    }

    @FXML
    private void onRead(ActionEvent event) {
        if (userViewStatus == ApplicationStatus.NONE) {
            ProductStatus status = productService.readProducts();
            switch (status) {
                case READ_SUCCESS -> {
                    loadTableProducts();
                }
                case EMPTY_LIST ->
                    alertInfo.viewAlert("LISTAR PRODUCTOS", "No existen productos",
                            "No hay productos registrados en la base de datos.",
                            "WARN");
                case ERROR_READ_PRODUCTS ->
                    alertInfo.viewAlert("ERROR AL LISTAR", "Fallo al consultar productos",
                            "Ocurrió un error al consultar productos:\n" + productService.getMessageError(),
                            "ERR");
            }
        }
    }

    @FXML
    private void onSearch(ActionEvent event) {
        switch (userViewStatus) {
            case NONE -> {
                userViewStatus = ApplicationStatus.SEARCH;
                controlOptionsCRUD();
                clearAllFields();
                hideAllFields();
                btnSearch.setText("VALIDAR");
                txtIdProduct.setEditable(true);
                txtIdProduct.setDisable(false);
                tblProducts.getItems().clear();
            }
            case SEARCH -> {
                String idProducto = txtIdProduct.getText().trim();
                
                if( !validate.isValidInteger(idProducto) &&
                    !validate.isValidPositiveInteger(idProducto)){
                        alertInfo.viewAlert("ID DE PRODUCTO", 
                                "El ID de producto inválido.",
                                "El ID que ha ingresado no es un numero entero\nIngrese un valor numérico entero.",
                                "WARN");
                    return ;
                }
                
                ProductStatus status = productService.searchProduct(
                                        Integer.parseInt(idProducto)
                                    );
                
                switch (status) {
                    case PRODUCT_FOUND->{
                        userViewStatus= ApplicationStatus.NONE;
                        controlOptionsCRUD();
                        loadTableProducts();
                        btnSearch.setText("BUSCAR");
                        hideAllFields();
                    }
                    case EMPTY_LIST->{
                        alertInfo.viewAlert("LISTAR PRODUCTOS", "No existen productos a mostrar", 
                        "No existen productos para mostrar.",
                        "WARN");
                    }               
                    case ERROR_SEARCH_PRODUCT->{
                        alertInfo.viewAlert("ERROR LISTAR PRODUCTOS", "Error al listar productos", 
                        "Ocurrió un error al momento de listar datos de los usuarios.\n"
                                +productService.getMessageError(),
                        "ERR");
                    } 
                    case PRODUCT_NOT_FOUND->{
                        alertInfo.viewAlert(
                            "PRODUCTO NO ENCONTRADO",
                            "Registro inexistente",
                            "El producto no existe o ya fue eliminado del sistema.",
                            "WARN"
                        );
                    }
                }
                clearAllFields();
            }
        }
    }

    /**
     * Valida secuencialmente cada uno de los campos del formulario evaluando por false.
     * <p>
     * Ante el primer fallo de validación, despliega la alerta visual informativa correspondiente
     * y retorna {@code false}, impidiendo que los flujos de creación o edición continúen.
     * </p>
     *
     * @return {@code true} si todos los campos cumplen con las restricciones; {@code false} ante cualquier error.
     */
    public boolean isValidFields() {
        String name =txtName.getText().trim();
        String description = txaDescription.getText().trim();
        String priceText = txtPrice.getText().trim();

        // 1. Validar nombre vacío
        if (validate.isEmptyText(name)) {
            alertInfo.viewAlert("CAMPO OBLIGATORIO", "Nombre requerido",
                    "Debe ingresar el nombre del producto.", "WARN");
            return false;
        }

        // 2. Validar longitud del nombre (máximo 100 caracteres)
        if (!validate.isValidLengthText(name, 100)) {
            alertInfo.viewAlert("LONGITUD EXCEDIDA", "Nombre muy largo",
                    "El nombre del producto no puede exceder los 100 caracteres.", "WARN");
            return false;
        }

        // 3. Validar descripción vacía
        if (validate.isEmptyText(description)) {
            alertInfo.viewAlert("CAMPO OBLIGATORIO", "Descripción requerida",
                    "Debe ingresar una descripción para el producto.", "WARN");
            return false;
        }

        // 4. Validar longitud de la descripción (máximo 200 caracteres)
        if (!validate.isValidLengthText(description, 200)) {
            alertInfo.viewAlert("LONGITUD EXCEDIDA", "Descripción muy larga",
                    "La descripción no puede exceder los 200 caracteres.", "WARN");
            return false;
        }

        // 5. Validar precio vacío
        if (validate.isEmptyText(priceText)) {
            alertInfo.viewAlert("CAMPO OBLIGATORIO", "Precio requerido",
                    "Debe ingresar el precio del producto.", "WARN");
            return false;
        }

        // 6. Validar que sea un número decimal válido (delegado a Validations)
        if (!validate.isValidDecimal(priceText)) {
            alertInfo.viewAlert("FORMATO INVÁLIDO", "Precio incorrecto",
                    "El precio debe ser un número decimal válido (ej. 12.50).", "WARN");
            return false;
        }

        // 7. Validar que sea un decimal positivo mayor a cero (delegado a Validations)
        if (!validate.isValidPositiveDecimal(priceText)) {
            alertInfo.viewAlert("PRECIO INVÁLIDO", "Precio debe ser mayor a 0",
                    "El precio del producto debe ser un valor positivo mayor a 0.00.", "WARN");
            return false;
        }

        // 8. Validar categoría seleccionada
        if (cmbCategory.getSelectionModel().getSelectedItem() == null) {
            alertInfo.viewAlert("CATEGORÍA REQUERIDA", "Seleccione una categoría",
                    "Debe seleccionar una categoría para el producto.", "WARN");
            return false;
        }

        return true;
    }

    /**
     * MODO SOLO LECTURA (Al hacer clic en la tabla):
     * Muestra todos los campos pero bloquea la edición y el teclado.
     */
    private void showBasicFields() {
        txtIdProduct.setDisable(false);
        txtIdProduct.setEditable(false);
        txtName.setDisable(false);
        txtName.setEditable(false);
        txaDescription.setDisable(false);
        txaDescription.setEditable(false);
        txtPrice.setDisable(false);
        txtPrice.setEditable(false);
        cmbCategory.setDisable(false);
        cmbCategory.setFocusTraversable(false);
        cmbCategory.setMouseTransparent(true);
        btnUploadImage.setDisable(true);
    }

    /**
     * MODO EDICIÓN / AGREGAR:
     * Habilita la edición en campos y el botón de carga de imagen.
     */
    private void showAllFields() {
        txtIdProduct.setDisable(false);
        txtIdProduct.setEditable(false);
        txtName.setDisable(false);
        txtName.setEditable(true);
        txaDescription.setDisable(false);
        txaDescription.setEditable(true);
        txtPrice.setDisable(false);
        txtPrice.setEditable(true);
        cmbCategory.setDisable(false);
        btnUploadImage.setDisable(false);
        cmbCategory.setFocusTraversable(true);
        cmbCategory.setMouseTransparent(false);
    }

    /**
     * MODO INACTIVO / BLOQUEO TOTAL (Estado inicial o Cancelar).
     */
    private void hideAllFields() {
        txtIdProduct.setDisable(true);
        txtName.setDisable(true);
        txaDescription.setDisable(true);
        txtPrice.setDisable(true);
        cmbCategory.setDisable(true);
        btnUploadImage.setDisable(true);
    }

    /**
     * LIMPIEZA DE FORMULARIO Y LIBERACIÓN DE MEMORIA.
     * Oculta el previsualizador y remueve la referencia a los bytes y a la imagen.
     */
    private void clearAllFields() {
        txtIdProduct.clear();
        txtName.clear();
        txaDescription.clear();
        txtPrice.clear();
        cmbCategory.getSelectionModel().clearSelection();
        tblProducts.getSelectionModel().clearSelection();

        // Liberación de memoria y ocultamiento de previsualizador
        imageBytesSelected = null;
        productSelect = null;
        imvPreview.setImage(null);
        imvPreview.setVisible(false);
        imvPreview.setManaged(false);
    }
}
