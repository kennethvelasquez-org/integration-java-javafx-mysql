use db_usuarios_productos;

#----------------DATOS INICIALES DE ROLES----------------------
call sp_create_rol('admin', 'Administrador del sistema');
call sp_create_rol('user', 'Usuario estandar del sistema');
call sp_create_rol('employee', 'Vendedor de productos');

#----------------USUARIOS DE PRUEBA-----------------------------
# Usuario con contraseña almacenada sin hash.
call sp_create_user_unprotected(
	'Admin',
	'Principal',
	'admin@example.com',
	'admin',
	'admin123',
	100
);

# Usuario con contraseña almacenada usando MD5 dentro del procedimiento.
call sp_create_user_hashed(
	'User',
	'Standard',
	'user@example.com',
	'user',
	'user123',
	101
);

#----------------SIMULACIONES DE LOGIN--------------------------
# Login sin hash: se envia la contraseña plana y se compara directamente.
call sp_login_user_unprotected('admin', 'admin123');

# Login con MD5: se envia la contraseña plana y el procedimiento aplica MD5.
call sp_login_user_hashed('user@example.com', 'user123');

# El login con BCrypt se omite: la verificación debe hacerse en Java con BCrypt.
