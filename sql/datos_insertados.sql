INSERT INTO usuario (nombre, mail)
              VALUES('ivan', 'ivan@EMAIL.COM');
INSERT INTO usuario (nombre, mail)
              VALUES('pedro', 'pedro@EMAIL.COM');

INSERT INTO cuenta (tipo, fecha_apertura, balance, ownerId)
              VALUES('PERSONAL', NOW(), 1000.00,(SELECT id  FROM usuario where nombre = 'ivan'));

INSERT INTO cuenta (tipo, fecha_apertura, balance, ownerId)
              VALUES('COMPANY', NOW(), 2000.00,(SELECT id  FROM usuario where nombre = 'pedro'));