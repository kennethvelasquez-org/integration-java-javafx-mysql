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
call sp_read_user_by_email_or_user("user",null);


#----------------SIMULACIONES DE LOGIN--------------------------
# Login sin hash: se envia la contraseña plana y se compara directamente.
call sp_login_user_unprotected('admin', 'admin123');

# Login con MD5: se envia la contraseña plana y el procedimiento aplica MD5.
call sp_login_user_hashed('ffff', "Otro");

# El login con BCrypt se omite: la verificación debe hacerse en Java con BCrypt.

#----------------CRUD DE USUARIOS--------------------------
call sp_read_users();
select * from user;
-- 1. Prueba de búsqueda parcial (trae a todos los que tengan 'ad' en nombre, user o correo)
call sp_search_user_by_data('ad');
-- 2. Prueba de búsqueda vacía (trae todos los usuarios)
call sp_search_user_by_data('');
-- 3. Prueba de eliminación lógica (desactivar usuario por su UUID)
call sp_delete_user('2e83eb20-af0e-11f1-8a26-f8edfc2af70e');
-- 4. Prueba de actualización
call sp_update_user(
    '13d4eac3-af1c-11f1-8a26-f8edfc2af70e',
    'as',
    'ds',
    'as@kinal.edu.gt',
    'ffff',
    null,
    100,
    2,
    true
);


