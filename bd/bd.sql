use tienda_virtual;
select * from productos; 
	
show tables;

CREATE TABLE IF NOT EXISTS rol (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255),
    usuario VARCHAR(255),
    contraseña VARCHAR(255)
);		

INSERT INTO usuarios (cedula, nombre,apellidos,email,numero_celular,direccion,rol,usuario,contraseña)
VALUES ('3216549870', 'santiago','zapata','santiagozapataladino@gmail.com','1234567890','Calle Principal 123','admin','szapata','|SyU8m9jnj');

INSERT INTO compas (id_cliente,fecha,total)
VALUES ('3216549870', 'santiago','zapata');

DELETE FROM productos WHERE id_producto = 9;

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE usuarios;
ALTER TABLE usuarios AUTO_INCREMENT = 1;
SET FOREIGN_KEY_CHECKS = 1
