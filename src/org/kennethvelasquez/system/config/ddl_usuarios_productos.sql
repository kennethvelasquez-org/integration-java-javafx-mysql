drop database if exists db_usuarios_productos;
create database db_usuarios_productos;
use db_usuarios_productos;

create table Rol(
    name varchar(70) not null check(length(name)<=70),
    description varchar(100) not null check(length(description)<=100),
    id_rol int not null auto_increment,
    constraint pk_rol primary key (id_rol)
) auto_increment=100;

create Table User(
    name varchar(70) not null check(length(name)<=70),
    last_name varchar(70) not null check(length(last_name)<=70),
    email varchar(70) not null check(length(email)<=70) unique,
    user varchar(70) not null check(length(user)<=70) unique,
    password varchar(70) not null check(length(password)<=70), # Es de 70 porque el hash de bcrypt es de 60 caracteres, y le puse 10 mas por si acaso
    id_rol int,
    type_encrypt int not null,
    user_status boolean default true,
    constraint fk_user_rol
        foreign key (id_rol) references Rol(id_rol)
        on delete set null 
        on update set null,
    id_user varchar(36) not null,
    constraint pk_user primary key (id_user)
);

create table Category (
    name_category varchar(100) not null check(length(name_category)<= 100),
    description varchar(200) not null check(length(description)<=200),
    id_category int not null auto_increment,
    constraint pk_category primary key (id_category)
);

create table Product(
    name varchar(100) not null check (char_length(name) <= 100),
    description varchar(200) not null check (char_length(description) <= 200),
    price decimal(10,2) not null default 0.00,
    img_producto mediumblob,
    id_user varchar(36),
    id_category int,
    constraint fk_product_category 
        foreign key(id_category) references Category(id_category)
        on delete set null
        on update cascade,
    constraint fk_product_user
        foreign key (id_user) references User(id_user)
        on delete set null 
        on update cascade,
    id_product int not null auto_increment,
    constraint pk_product primary key (id_product)
);

create table History_Change(
    id_history int auto_increment,
    id_user varchar(70),
    date_time datetime default current_timestamp,
    id_producto int,
    accion varchar(20),
    constraint pk_History_Change primary key (id_history)
);

#----------------STORED PROCEDURES DE ROLES----------------------
Delimiter $$
    create procedure sp_create_rol(
            in name_p varchar(70),
            in description_p varchar(100)
        )
    begin
        insert into Rol(name, description) values(name_p, description_p);
    end$$
Delimiter ;

Delimiter //
	create procedure sp_read_rol()
    begin
		select id_rol as ID,
			name as Nombre,
            description as "Descripción"
            from rol;
    end//
Delimiter ;

Delimiter //
	create procedure sp_search_rol(in id_rol_p int)
    begin
		select id_rol as ID,
			name as Nombre,
            description as "Descripción"
			from Rol
				where id_rol = id_rol_p;
    end//
Delimiter ;

Delimiter //
	create procedure sp_update_rol(in id_rol_p int, in name_p varchar(70),in description_p varchar(100))
    begin
		update Rol set
			name = name_p,
            description = description_p
		where id_rol = id_rol_p;
    end//
Delimiter ;

Delimiter //
	create procedure sp_delete_rol(in id_rol_p int)
    begin
		delete from Rol 
			where id_rol = id_rol_p;
    end//
Delimiter ;

#----------------STORED PROCEDURES DE USUARIOS----------------------
Delimiter $$
    create procedure sp_create_user(
        in name_p varchar(70),
        in last_name_p varchar(70),
        in email_p varchar(70),
        in user_p varchar(70),
        in password_p varchar(70),
        in id_rol_p int,
        in type_encrypt_p int,
        in user_status_p boolean
    )
    begin
		insert into user(name, last_name, email, user, password, id_rol, type_encrypt,user_status, id_user)
			values(name_p,last_name_p,email_p,user_p,
					#validacion de type_encript = md5
					case 
						when type_encrypt_p = 2 then md5(password_p)
						else password_p
					end, 
                    id_rol_p,
                    type_encrypt_p, user_status_p,uuid());
    end$$
Delimiter ;

Delimiter $$
    create procedure sp_create_user_unprotected(
            in name_p varchar(70),
            in last_name_p varchar(70),
            in user_p varchar(70),
            in email_p varchar(70),
            in password_p varchar(70),
            in id_rol_p int,
            in type_encrypt_p int
        )
    begin
        insert into User(name, last_name, email, user, password, id_rol, id_user,type_encrypt) 
            values(name_p, last_name_p, email_p, user_p,
                password_p,
                id_rol_p, uuid(),type_encrypt_p);
    end$$
Delimiter ;

Delimiter $$
    create procedure sp_create_user_hashed(
            in name_p varchar(70),
            in last_name_p varchar(70),
            in user_p varchar(70),
            in email_p varchar(70),
            in password_p varchar(70),
            in id_rol_p int,
            in type_encrypt_p int
        )
    begin
        insert into User(name, last_name, email, user, password, id_rol, id_user, type_encrypt) 
            values(name_p, last_name_p, email_p, user_p,
                md5(password_p), # Este es el hash de la contraseña con md5
                id_rol_p, uuid(),type_encrypt_p);
    end$$
Delimiter ;


/* EL SIGUIENTE SP, ES PARECIDO AL PRIMERO, 
    PERO ESTE RECIBE EL HASH DE BYCRYPT EN JAVA, Y NO HACE EL HASH DENTRO DEL SP, 
    YA QUE EL HASH SE HACE EN EL BACKEND, Y SE ENVIA AL SP YA HASHEADO
    En java toca hacer un metodo para hashear la contraseña con bcrypt, y luego enviar ese hash al SP para que lo guarde en la base de datos.
*/
Delimiter $$
    create procedure sp_create_user_bcrypt(
            in name_p varchar(70),
            in last_name_p varchar(70),
            in user_p varchar(70),
            in email_p varchar(70),
            in password_hash varchar(70), # Este es el hash de la contraseña con bcrypt
            in id_rol_p int,
            in type_encrypt_p int
        )
    begin
        insert into User(name, last_name, email, user, password, id_rol, id_user, type_encrypt) 
            values(name_p, last_name_p, email_p, user_p,
                password_hash, # Aqui se recibe el hash de bcrypt ya hecho en java, y se guarda tal cual en la base de datos
                id_rol_p, uuid(),type_encrypt_p);
    end$$
Delimiter ;


Delimiter $$
    create procedure sp_read_user_by_email_or_user(
            in user_p varchar(70),
            in email_p varchar(70)
        )
    begin
        select name as Nombres,
               last_name as Apellidos,
               email as Correo,
               user as Usuario,
               password as Clave,
               id_rol as Rol,
               type_encrypt as Encript,
               id_user as ID,
               user_status as Estado
            from User 
                where email = email_p or user = user_p;
    end$$
Delimiter ;

Delimiter $$ 
    create procedure sp_login_user_unprotected(
            in data_user varchar(70),
            in password_p varchar(70)
        )
    begin
        
        select id_user as ID,
			   name as Nombres,
               last_name as Apellidos,
               email as Correo,
               user as Usuario,
               password as Clave,
               id_rol as Rol,
               user_status as Estado
            from User 
                where (email = data_user or user = data_user) and password = password_p;
    end$$
Delimiter ;

Delimiter $$ 
    create procedure sp_login_user_hashed(
            in data_user varchar(70),
            in password_hash varchar(70)
        )
    begin
        
        select id_user as ID,
			   name as Nombres,
               last_name as Apellidos,
               email as Correo,
               user as Usuario,
               password as Clave,
               id_rol as Rol,
               user_status as Estado
            from User 
                /* Hacemos hash directamente en la consulta, para que el usuario no tenga que enviar el hash, sino que envie la contraseña en texto plano, y el SP haga el hash y compare con el hash guardado en la base de datos */
                where (email = data_user or user = data_user) and password = md5(password_hash);
    end$$
Delimiter ;

/* EL SP DE LOGIN CON BCRYPT SE OMITE PORQUE NO SE PUEDE HACER HASH CON BCRYPT EN MYSQL.
    Asi ya en java se hace el hash con bcrypt, y se envia al SP para que lo compare con el hash guardado en la base de datos de algun usuario que se busque con el sp_read_user_by_email_or_user.
 */
 
#------------------------------ READ USUARIOS -----------
create view view_read_users as
	select u.id_user as "ID Usuario",
		   u.name as Nombres,
		   u.last_name as Apellidos,
		   u.email as Correo,
           u.user as Usuario,
           u.type_encrypt as Cifrado,
		   r.name as Rol,
		   u.user_status as Estado
		from User u
			inner join Rol r
				on r.id_rol = u.id_rol;
delimiter $$
	create procedure sp_read_users()
		begin
			select * from view_read_users;
        end$$
delimiter ;

/* 
Procedimiento de Búsqueda Global y Parcial (sp_search_user_by_data)
Permite buscar usuarios ingresando cualquier fragmento de texto (incompleto o completo).
 Evalúa coincidencias en Nombres, Apellidos, Nombre Completo, Usuario, Correo y UUID.
*/

create or replace view view_users_details as
	select 
		u.id_user as ID,
		u.name as Nombres,
		u.last_name as Apellidos,
		u.user as Usuario,
		u.email as Correo,
		r.name as Rol,
		u.id_rol as IdRol,
		u.type_encrypt as Encriptado,
		u.user_status as Estado
		from User u
			left join Rol r on u.id_rol = r.id_rol;

Delimiter $$
    create procedure sp_search_user_by_data(
        in id_user_p varchar(36),
        in name_p varchar(70),
        in last_name_p varchar(70),
        in email_p varchar(70),
        in user_p varchar(70),
        in id_rol_p varchar(70),
        in name_rol varchar(70),
        in type_encrypt_p varchar(70),
        in user_status_p varchar(70)
    )
    begin
        select 
            ID, Nombres, Apellidos,
            Usuario, Correo, Rol,
            IdRol, Encriptado, Estado
			from view_users_details
				where (id_user_p != '' and ID like concat('%', id_user_p, '%'))
				   or (name_p != '' and Nombres like concat('%', name_p, '%'))
				   or (last_name_p != '' and Apellidos like concat('%', last_name_p, '%'))
				   or (user_p != '' and Usuario like concat('%', user_p, '%'))
				   or (email_p != '' and Correo like concat('%', email_p, '%'))
				   or (id_rol_p != '' and IdRol like concat('%', id_rol_p, '%'))
				   or (name_rol != '' and Rol like concat('%', name_rol, '%'))
				   or (type_encrypt_p != '' and Encriptado like concat('%', type_encrypt_p, '%'))
				   or (user_status_p != '' and Estado like concat('%', user_status_p, '%'))
				order by Nombres asc;
    end$$
Delimiter ;

#------------------------------ UPDATE USUARIOS -----------
Delimiter $$
    create procedure sp_update_user(
        in id_user_p varchar(36),
        in name_p varchar(70),
        in last_name_p varchar(70),
        in email_p varchar(70),
        in user_p varchar(70),
        in password_p varchar(70),
        in id_rol_p int,
        in type_encrypt_p int,
        in user_status_p boolean
    )
    begin
        update User
            set name = name_p,
                last_name = last_name_p,
                email = email_p,
                user = user_p,
                id_rol = id_rol_p,
                /*La siguiente seccion es para los administradores con el uso de una validacion mas avanzada usando 
                sentencias de control switch en SQL
					-- Si password_p viene nulo o vacío, conserva la que ya tenía ('password').
					-- Si viene texto nuevo y type_encrypt es 2, le aplica MD5.
					-- En cualquier otro caso nuevo, guarda el nuevo password_p.
                */
                password = case 
								when password_p is null or trim(password_p) = '' then password 
								when type_encrypt_p = 2 then md5(password_p)
								else password_p
						   end,
                type_encrypt = case 
								when type_encrypt is null or trim(type_encrypt) = '' then type_encrypt 
								else type_encrypt_p
						   end,
                user_status = user_status_p
            where id_user = id_user_p;
    end$$
Delimiter ;

#------------------------------ DELETE USUARIOS -----------
/*
	 Procedimiento de Eliminación Lógica / Soft Delete (sp_soft_delete_user)
	No destruye el registro de la base de datos (evita problemas de integridad referencial con compras, logs o auditoría), únicamente pasa user_status a false (0):
*/
Delimiter $$
    create procedure sp_delete_user(
        in id_user_p varchar(36)
    )
    begin
        update User
            set user_status = false
            where id_user = id_user_p;
    end$$
Delimiter ;

# ----------------------------------- C R U D S DE CATEGORIA  -----------------------  
# ----------------------- create de categoria --------------
delimiter $$
	create procedure sp_create_category(
			in p_name_category varchar(100),
			in p_description varchar(200)
		)
	begin
		insert into Category (name_category, description)
			values (p_name_category, p_description);
	end $$
delimiter ;
# ----------------------- read de categoria --------------
delimiter $$
	create procedure sp_read_category()
	begin
		select 
				id_category as ID,
				name_category as Nombre,
				description as Descripcion
			from Category;
	end $$
delimiter ;
# ----------------------- search de categoria --------------
delimiter $$
	create procedure sp_search_category(
			in p_id_category int
		)
	begin
		select 
			id_category as ID,
			name_category as Nombre,
			description as Descripcion
		from Category
			where id_category = p_id_category;
	end $$
delimiter ;
# ----------------------- update de categoria --------------
delimiter $$
	create procedure sp_update_category(
			in p_id_category int,
			in p_name_category varchar(100),
			in p_description varchar(200)
		)
	begin
		update Category
			set 
				name_category = p_name_category,
				description = p_description
			where id_category = p_id_category;
	end $$
delimiter ;
# ----------------------- delete de categoria --------------
delimiter $$
	create procedure sp_delete_category(
			in p_id_category int
		)
	begin
		delete from Category
			where id_category = p_id_category;
	end $$
delimiter ;

# ----------------------------------- C R U D S DE PRODUCTO  -----------------------  
# --------------------- Crear Producto ---------------------
delimiter $$
	create procedure sp_create_product(
			in name_p varchar(100),
			in description_p varchar(200),
			in price_p decimal(10, 2),
			in img_producto_p mediumblob,
			in id_category_p int,
			in id_user_p varchar(70)
		)
	begin
		insert into Product (name, description, price, img_producto, id_category, id_user)
			values (name_p, description_p, price_p, img_producto_p, id_category_p, id_user_p);
	end $$
delimiter ;

# ------------------------ Listar Productos -----------------
# VISTA
create view view_read_product as
select 
        p.id_product as ID,
        p.name as Nombre,
        p.description as Descripcion,
        p.price as Precio,
        p.img_producto as Imagen,
        p.id_category as "ID de categoria",
        c.name_category as Categoria
    from Product p
        inner join Category c
           on p.id_category = c.id_category;
# ---------------  SP DE LISTAR Productos--------------
delimiter $$
	create procedure sp_read_products(
			in id_user_p varchar(70)
		)
	begin
		insert into History_Change (id_user, id_producto, accion)
			values (id_user_p, null, 'READ');
		select * from view_read_product;
	end $$
delimiter ;

# -------------------  BUSCAR PRODUCTO
delimiter $$
	create procedure sp_search_product(
			in id_product_p int,
			in id_user_p varchar(70)
		)
	begin
		insert into History_Change (id_user, id_producto, accion)
			values (id_user_p, id_product_p, 'SEARCH');
		select * from view_read_product
			where ID = id_product_p;
	end $$
delimiter ;

#------------ Actualizar Producto
delimiter $$
	create procedure sp_update_product(
		in id_product_p int,
		in name_p varchar(100),
		in description_p varchar(200),
		in price_p decimal(10, 2),
		in img_producto_p mediumblob,
		in id_category_p int,
		in id_user_p varchar(70)
	)
	begin
		update Product
			set 
				name = name_p,
				description = description_p,
				price = price_p,
				img_producto = ifnull(img_producto_p, img_producto),
				id_category = id_category_p,
				id_user = id_user_p
			where id_product = id_product_p;
	end $$
delimiter ;

# -------------------- Eliminar Producto
delimiter $$
	create procedure sp_delete_product(
		in id_product_p int,
		in id_user_p varchar(70)
	)
	begin
		insert into History_Change (id_user, id_producto, accion)
			values (id_user_p, id_product_p, 'DELETE');
		delete from Product
			where id_product = id_product_p;
	end $$
delimiter ;

#------------------------ TRIGGERS DE PRODUCTO ---------------
# Trigger tras crear producto (AFTER INSERT)
delimiter $$
	create trigger tr_product_after_insert
            after insert on Product
            for each row
	begin
		insert into History_Change (id_user, id_producto, accion)
			values (NEW.id_user, NEW.id_product, 'CREATE');
	end $$
delimiter ;

# Trigger tras actualizar producto (AFTER UPDATE)
delimiter $$
	create trigger tr_product_after_update
            after update on Product
            for each row
	begin
		insert into History_Change (id_user, id_producto, accion)
			values (NEW.id_user, NEW.id_product, 'UPDATE');
	end $$
delimiter ;