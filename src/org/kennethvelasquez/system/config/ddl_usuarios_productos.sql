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
    constraint fk_user_rol
        foreign key (id_rol) references Rol(id_rol)
        on delete set null on update set null,
    id_user varchar(36) not null,
    constraint pk_user primary key (id_user)
);


create table Product(
    name varchar(70) not null check(length(name)<=70),
    description varchar(100) not null check(length(description)<=100),
    price decimal(10,2) not null default 0.00,
    id_user varchar(36),
    constraint fk_product_user
        foreign key (id_user) references User(id_user)
        on delete set null on update cascade,
    id_product varchar(36) not null,
    constraint pk_product primary key (id_product)
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


#----------------STORED PROCEDURES DE USUARIOS----------------------

Delimiter $$
    create procedure sp_create_user_unprotected(
            in name_p varchar(70),
            in last_name_p varchar(70),
            in email_p varchar(70),
            in user_p varchar(70),
            in password_p varchar(70),
            in id_rol_p int,
            in type_encrypt_p auto_increment
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
            in email_p varchar(70),
            in user_p varchar(70),
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
            in email_p varchar(70),
            in user_p varchar(70),
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
            in data_user varchar(70)
        )
    begin
        select name as Nombres,
               last_name as Apellidos,
               email as Correo,
               user as Usuario,
               password as Clave,
               id_rol as Rol,
               type_encrypt as Encript
            from User 
                where email = data_user or user = data_user;
    end$$
Delimiter ;

Delimiter $$ 
    create procedure sp_login_user_unprotected(
            in data_user varchar(70),
            in password_p varchar(70)
        )
    begin
        
        select name as Nombres,
               last_name as Apellidos,
               email as Correo,
               user as Usuario,
               password as Clave,
               id_rol as Rol
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
        
        select name as Nombres,
               last_name as Apellidos,
               email as Correo,
               user as Usuario,
               password as Clave,
               id_rol as Rol
            from User 
                /* Hacemos hash directamente en la consulta, para que el usuario no tenga que enviar el hash, sino que envie la contraseña en texto plano, y el SP haga el hash y compare con el hash guardado en la base de datos */
                where (email = data_user or user = data_user) and password = md5(password_hash);
    end$$
Delimiter ;

/* EL SP DE LOGIN CON BCRYPT SE OMITE PORQUE NO SE PUEDE HACER HASH CON BCRYPT EN MYSQL.
    Asi ya en java se hace el hash con bcrypt, y se envia al SP para que lo compare con el hash guardado en la base de datos de algun usuario que se busque con el sp_read_user_by_email_or_user.
 */
