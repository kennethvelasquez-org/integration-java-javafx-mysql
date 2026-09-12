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

#----------------SIMULACIONES DE BUSQUEDA DE USUARIO--------------------------
call sp_read_user_by_email_or_user(null,"admin@example.com");
call sp_read_user_by_email_or_user("user",null);


#----------------SIMULACIONES DE LOGIN--------------------------
# Login sin hash: se envia la contraseña plana y se compara directamente.
call sp_login_user_unprotected('admin', 'admin123');

# Login con MD5: se envia la contraseña plana y el procedimiento aplica MD5.
call sp_login_user_hashed('user@example.com', 'user123');

# El login con BCrypt se omite: la verificación debe hacerse en Java con BCrypt.

#----------------CRUD DE USUARIOS--------------------------
call sp_read_users();



