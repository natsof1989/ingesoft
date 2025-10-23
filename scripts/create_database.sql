-- Database schema for Gestor de Accesos
CREATE DATABASE IF NOT EXISTS ingesoft;
USE ingesoft;

-- Tabla de empresas
CREATE TABLE IF NOT EXISTS empresa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de sucursales
CREATE TABLE IF NOT EXISTS sucursal (
    id INT AUTO_INCREMENT PRIMARY KEY,
    idEmpresa INT NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    ubicacion VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idEmpresa) REFERENCES empresa(id) ON DELETE CASCADE
);

-- Tabla de notas (credenciales de acceso)
CREATE TABLE IF NOT EXISTS notas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    idSucursal INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    anydesk VARCHAR(50),
    password VARCHAR(255),
    login BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idSucursal) REFERENCES sucursal(id) ON DELETE CASCADE
);

-- Datos de ejemplo
INSERT INTO empresa (name) VALUES 
    ('CRL'),
    ('Dos C Representaciones'),
    ('Nuevo'),
    ('Feldman'),
    ('Frimar');

INSERT INTO sucursal (idEmpresa, nombre, ubicacion) VALUES
    (1, 'CLR', NULL),
    (2, 'Dos C Representaciones', NULL),
    (3, 'El Germano', NULL),
    (4, 'Feldmann', NULL),
    (5, 'Frimar', NULL);

INSERT INTO notas (idSucursal, name, anydesk, password, login) VALUES
    (1, 'CLR', '489 469 287', 'password123', FALSE),
    (2, 'Dos C', '287 462 136', 'pass456', FALSE),
    (3, 'El Germano', '1 075 687 518', 'germano789', FALSE),
    (4, 'Feldmann', '915 010 755', 'feld2024', FALSE),
    (5, 'Frimar', '935137047', '', TRUE);
