-- ============================================================
-- TallerBMW — Script completo de base de datos
-- Ejecutar en MySQL Workbench o consola MySQL
-- ============================================================

CREATE DATABASE IF NOT EXISTS taller_bmw;
USE taller_bmw;

-- -----------------------------------------------
-- Tabla: usuario
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS usuario (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    nombre   VARCHAR(100) NOT NULL,
    usuario  VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    rol      ENUM('ADMIN','RECEPCIONISTA','MECANICO') NOT NULL,
    activo   BOOLEAN NOT NULL DEFAULT true
);

-- -----------------------------------------------
-- Tabla: cliente
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS cliente (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL,
    telefono        VARCHAR(20)  NOT NULL,
    correo          VARCHAR(100),
    direccion       VARCHAR(255),
    fecha_registro  DATE         NOT NULL,
    activo          BOOLEAN NOT NULL DEFAULT true
);

-- -----------------------------------------------
-- Tabla: vehiculo
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS vehiculo (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    placa       VARCHAR(20)  NOT NULL,
    marca       VARCHAR(50)  NOT NULL,
    modelo      VARCHAR(50)  NOT NULL,
    anio        INT          NOT NULL,
    color       VARCHAR(30),
    vin         VARCHAR(50),
    id_cliente  INT          NOT NULL,
    activo      BOOLEAN NOT NULL DEFAULT true,
    CONSTRAINT fk_vehiculo_cliente FOREIGN KEY (id_cliente) REFERENCES cliente(id)
);

-- -----------------------------------------------
-- Tabla: orden_servicio
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS orden_servicio (
    id                INT AUTO_INCREMENT PRIMARY KEY,
    fecha_entrada     DATETIME     NOT NULL,
    fecha_salida      DATETIME,
    descripcion       TEXT         NOT NULL,
    estado            ENUM('PENDIENTE','EN_PROCESO','COMPLETADA','CANCELADA') NOT NULL DEFAULT 'PENDIENTE',
    costo_total       DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    id_vehiculo       INT          NOT NULL,
    id_mecanico       INT,
    id_recepcionista  INT          NOT NULL,
    CONSTRAINT fk_orden_vehiculo      FOREIGN KEY (id_vehiculo)      REFERENCES vehiculo(id),
    CONSTRAINT fk_orden_mecanico      FOREIGN KEY (id_mecanico)      REFERENCES usuario(id),
    CONSTRAINT fk_orden_recepcionista FOREIGN KEY (id_recepcionista) REFERENCES usuario(id)
);

-- -----------------------------------------------
-- Tabla: inventario
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS inventario (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    nombre          VARCHAR(100)   NOT NULL,
    descripcion     VARCHAR(255),
    cantidad        INT            NOT NULL DEFAULT 0,
    precio_unitario DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    stock_minimo    INT            NOT NULL DEFAULT 5,
    activo          BOOLEAN NOT NULL DEFAULT true,
    CONSTRAINT uq_inventario_nombre UNIQUE (nombre)
);

-- ============================================================
-- DATOS DE EJEMPLO
-- ============================================================

-- Usuarios (admin / admin123 | recep / recep123 | mec / mec123)
INSERT INTO usuario (nombre, usuario, password, rol) VALUES
('Administrador',        'admin', 'admin123',  'ADMIN'),
('Carlos Recepcion',     'recep', 'recep123',  'RECEPCIONISTA'),
('Mario Mecanico',       'mec',   'mec123',    'MECANICO'),
('Sofia Mecanico',       'mec2',  'mec123',    'MECANICO');

-- Clientes
INSERT INTO cliente (nombre, telefono, correo, direccion, fecha_registro) VALUES
('Juan Perez',       '88881111', 'juan@email.com',   'San Jose Centro',       CURDATE()),
('Maria Gomez',      '88882222', 'maria@email.com',  'Heredia, San Francisco', CURDATE()),
('Roberto Vargas',   '88883333', NULL,               'Cartago, Paraiso',       CURDATE()),
('Ana Jimenez',      '88884444', 'ana@email.com',    'Alajuela Centro',        CURDATE());

-- Vehiculos
INSERT INTO vehiculo (placa, marca, modelo, anio, color, vin, id_cliente) VALUES
('ABC123', 'BMW', 'Serie 3 320i',  2020, 'Blanco',  'WBA8E9C50GK123456', 1),
('DEF456', 'BMW', 'Serie 5 530i',  2019, 'Negro',   'WBA53AF01NCJ12345', 2),
('GHI789', 'BMW', 'X5 xDrive40i', 2021, 'Gris',    'WBAKJ8C56KCJ45678', 3),
('JKL012', 'BMW', 'Serie 1 118i',  2022, 'Azul',    'WBA1R9102KVJ78901', 4);

-- Ordenes de servicio
INSERT INTO orden_servicio (fecha_entrada, descripcion, estado, costo_total, id_vehiculo, id_mecanico, id_recepcionista) VALUES
(NOW() - INTERVAL 5 DAY,  'Cambio de aceite y filtros completo',            'COMPLETADA', 45000.00, 1, 3, 2),
(NOW() - INTERVAL 3 DAY,  'Revision de frenos y cambio pastillas delanteras','EN_PROCESO',  0.00,   2, 3, 2),
(NOW() - INTERVAL 1 DAY,  'Alineacion y balanceo 4 ruedas',                 'PENDIENTE',    0.00,   3, 4, 2),
(NOW(),                   'Diagnostico electronico general',                 'PENDIENTE',    0.00,   4, 3, 2);

-- Completar la primera orden (fecha_salida)
UPDATE orden_servicio SET fecha_salida = NOW() - INTERVAL 4 DAY WHERE id = 1;

-- Inventario
INSERT INTO inventario (nombre, descripcion, cantidad, precio_unitario, stock_minimo) VALUES
('Filtro de aceite BMW N52',        'Filtro original motor N52 2.5L',         12, 8500.00,  5),
('Pastillas de freno delanteras',   'Kit pastillas eje delantero E90/E92',      6, 22000.00, 4),
('Aceite Motor 5W-30 (litro)',      'Aceite sintetico Castrol Edge 5W-30',     30, 4200.00, 10),
('Filtro de aire N20/N26',          'Filtro de panel motor N20/N26',            4, 9800.00,  5),
('Bujias NGK Laser Iridium x4',    'Set bujias BMW Serie 3 2.0T',              2, 35000.00, 3),
('Kit correa distribucion',         'Kit completo con tensor y bomba de agua',  1, 85000.00, 2),
('Liquido de frenos DOT4 500ml',    'Ate SL.6 DOT4',                           15, 6500.00,  5),
('Filtro de cabina carbon',         'Filtro carbon activado habitaculo',         3, 7200.00,  5);
