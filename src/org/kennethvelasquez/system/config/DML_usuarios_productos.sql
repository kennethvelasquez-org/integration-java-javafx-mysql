use db_usuarios_productos;

#----------------DATOS INICIALES DE ROLES----------------------
call sp_create_rol('admin', 'Administrador del sistema');
call sp_create_rol('user', 'Usuario estandar del sistema');
call sp_create_rol('employee', 'Vendedor de productos');
call sp_create_rol('test delete', 'Prueba de rol a eliminar');
call sp_read_rol();
call sp_search_rol(101);
call sp_update_rol(102, "Manager","Es una persona que restringe acceso");
call sp_delete_rol(103);


#----------------USUARIOS DE PRUEBA-----------------------------
call sp_create_user(
    'Kenneth',
    'Velasquez',
    'kvelasquez@example.com',
    'kvelasquez',
    'password123',
    100,
    1,
    true
);

# Usuario con contraseña almacenada sin hash.
call sp_create_user_unprotected(
	'Admin',
	'Principal',
	'admin',
	'admin@example.com',
	'admin123',
	100,
    1
);

# Usuario con contraseña almacenada usando MD5 dentro del procedimiento.
call sp_create_user_hashed(
	'User',
	'Standard',
	'user',
	'user@example.com',
	'user123',
	101,
    2
);

call sp_create_user_hashed(
	'Otro',
	'Otro',
	'Otro',
	'Otro@example.com',
	'Otro',
	101,
    2
);

#----------------SIMULACIONES DE BUSQUEDA DE USUARIO--------------------------
call sp_read_user_by_email_or_user(null,"admin@example.com");
call sp_read_user_by_email_or_user("Otro",null);


#----------------SIMULACIONES DE LOGIN--------------------------
# Login sin hash: se envia la contraseña plana y se compara directamente.
call sp_login_user_unprotected('admin', 'admin123');

# Login con MD5: se envia la contraseña plana y el procedimiento aplica MD5.
call sp_login_user_hashed('ffff', "Otro");

# El login con BCrypt se omite: la verificación debe hacerse en Java con BCrypt.

#----------------CRUD DE USUARIOS--------------------------
call sp_read_users();
select * from user;
-- 1. Prueba de búsqueda (trae a todos los que tengan 'ad' en= user )
call sp_search_user_by_data(null,null,null,null,null,null,null,null,null);
-- 3. Prueba de eliminación lógica (desactivar usuario por su UUID)
call sp_delete_user('2e83eb20-af0e-11f1-8a26-f8edfc2af70e');
-- 4. Prueba de actualización
call sp_update_user(
    '',
    'g',
    'g',
    'g@g',
    'ffff',
    null,
    102,
    3,
    true
);


# ----------------------- CREATE de cateogoria
CALL sp_create_category('Electrónica', 'Smartphones, laptops, tablets y accesorios tecnológicos.');
CALL sp_create_category('Ropa y Moda', 'Prendas de vestir para dama, caballero y niños.');
CALL sp_create_category('Hogar y Cocina', 'Electrodomésticos, muebles y artículos de decoración.');
CALL sp_create_category('Deportes y Fitness', 'Equipamiento deportivo, suplementos y ropa deportiva.');
CALL sp_create_category('Libros y Papelería', 'Literatura, libros de texto y artículos de oficina.');
CALL sp_create_category('Belleza y Cuidado Personal', 'Cosméticos, productos para el cuidado de la piel y perfumes.');
CALL sp_create_category('Categoría Temporal (Prueba)', 'Registro creado exclusivamente para probar Update, Find y Delete.');

# ------------------- LISTAR TODAS LAS CTEOGIRAS
-- Debe listar las 7 categorías creadas
CALL sp_read_category();
# ------------------- SEARCH BY ID DE UNA CATEGORIA
-- Buscamos el registro con ID = 7
CALL sp_search_category(7);
# --------------------- REALIZAR UPDATE DE UNA CATEOGIRA
-- Actualizamos los datos de la categoría con ID = 7
CALL sp_update_category(
    7, 
    'Categoría Modificada', 
    'Descripción actualizada exitosamente para pruebas.'
);

#------------------- delete de categoria
-- Eliminamos definitivamente la categoría con ID = 7
CALL sp_delete_category(7);

# -------------------------------------------------------------
# 1. INSERTAR 5 PRODUCTOS (sp_create_product)
# Obtenemos el id_user del usuario 'admin' para las pruebas de auditoría y producto
select id_user into @test_user_id from User where user = 'admin' limit 1;

# Nota: Para la imagen se utiliza un valor hexadecimal ficticio (0x89504e47)
call sp_create_product(
    'Laptop Gamer ASUS',
    'Laptop con procesador Intel Core i7, 16GB RAM y tarjeta RTX 4060',
    8999.99,
    0x89504e47,
    1,
    @test_user_id
);
call sp_create_product(
    'Teclado Mecanico RGB',
    'Teclado mecanico con switches blue y retroiluminacion personalizable',
    350.50,
    0x89504e47,
    1,
    @test_user_id
);
call sp_create_product(
    'Camisa Polo Casual',
    'Camisa de algodon corte slim fit color azul marino',
    125.00,
    0x89504e47,
    2,
    @test_user_id
);
call sp_create_product(
    'Cafetera Espresso Automatica',
    'Cafetera de presion de 15 bares con espumador de leche integrado',
    650.00,
    0x89504e47,
    3,
    @test_user_id
);
call sp_create_product(
    'Balon de Futbol Pro',
    'Balon profesional de alta resistencia tamano 5',
    180.00,
    0x89504e47,
    4,
    @test_user_id
);
# Listar todos los productos cargados (registra acción READ en History_Change)
call sp_read_products(@test_user_id);

# ---------------------- ACCIONES CON 1 PRODUCTO (ID = 5)
# BUSCAR el producto (registra acción SEARCH en History_Change)
call sp_search_product(4, @test_user_id);

# EDITAR el producto (el trigger tr_product_after_update registra UPDATE en History_Change)
call sp_update_product(
    5,
    'Balon de Futbol Pro Elite Edition',
    'Balon profesional termocellado tamano 5 edicion torneo',
    220.00,
    0x89504e47,
    4,
    @test_user_id
);

# ELIMINAR el producto (registra acción DELETE en History_Change)
call sp_delete_product(5, @test_user_id);

# Consultar historial de auditoría de cambios
select * from History_Change;

