-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 23-10-2025 a las 12:59:15
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `ingesoft`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empresa`
--

CREATE TABLE `empresa` (
  `id_empresa` int(11) NOT NULL,
  `descripcion` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `empresa`
--

INSERT INTO `empresa` (`id_empresa`, `descripcion`) VALUES
(1, 'Empresa Central'),
(2, 'Empresa la empanadota'),
(3, 'Empresa ejemplo');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `recurso`
--

CREATE TABLE `recurso` (
  `id_recurso` int(11) NOT NULL,
  `id_empresa` int(11) NOT NULL,
  `id_sucursal` int(11) DEFAULT NULL,
  `id_tipo_recurso` int(11) NOT NULL,
  `titulo` varchar(200) NOT NULL,
  `usuario` varchar(100) DEFAULT NULL,
  `contrasena` varchar(255) DEFAULT NULL,
  `ip` varchar(50) DEFAULT NULL,
  `nota` text DEFAULT NULL,
  `inicio_sesion` tinyint(1) DEFAULT 0,
  `anydesk` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `recurso`
--

INSERT INTO `recurso` (`id_recurso`, `id_empresa`, `id_sucursal`, `id_tipo_recurso`, `titulo`, `usuario`, `contrasena`, `ip`, `nota`, `inicio_sesion`, `anydesk`) VALUES
(1, 1, 1, 1, 'PC Administracion', 'admin', '1234', NULL, NULL, 1, '111-222-333'),
(2, 1, 2, 1, 'PC Soporte', 'soporte', 'pass', NULL, NULL, 1, '222-333-444'),
(3, 1, 1, 2, 'Servidor DB', 'root', 'root123', '192.168.1.100', 'Servidor de base de datos', 1, NULL),
(4, 2, 3, 2, 'Servidor Aplicaciones', 'appuser', 'app2025', '10.0.0.50', 'Servidor de apps', 1, NULL),
(7, 1, 1, 1, 'Acceso remoto oficina principal', 'usuario1', 'pass123', NULL, 'Acceso remoto mediante AnyDesk', 1, '123-456-789'),
(8, 2, 2, 1, 'Acceso remoto oficina secundaria', 'usuario2', 'pass456', NULL, 'Uso para soporte técnico', 1, '987-654-321'),
(9, 1, 1, 2, 'Impresora Laser', NULL, NULL, '192.168.1.50', 'Compartida por toda la oficina', 0, NULL),
(10, 3, 3, 2, 'Impresora Color', NULL, NULL, '192.168.20.20', 'Uso general', 0, NULL),
(11, 2, 2, 3, 'Servidor de archivos', 'admin', 'server123', '192.168.2.10', 'Servidor principal de documentos', 1, NULL),
(12, 1, 1, 3, 'Servidor de base de datos', 'dbuser', 'dbpass', '192.168.1.20', 'Base de datos interna', 1, NULL),
(13, 3, 3, 4, 'VPN Oficina Norte', 'vpnuser', 'vpnpass', '10.10.10.1', 'VPN para acceso remoto', 1, NULL),
(14, 2, 4, 4, 'VPN Oficina Sur', 'vpnuser2', 'vpnpass2', '10.10.20.1', 'VPN para soporte', 1, NULL),
(15, 2, NULL, 1, 'nuevo anydesk', NULL, 'la vaca lola', NULL, NULL, 0, '236725562');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sucursales`
--

CREATE TABLE `sucursales` (
  `id_sucursal` int(11) NOT NULL,
  `id_empresa` int(11) NOT NULL,
  `descripcion` varchar(200) NOT NULL,
  `direccion` text DEFAULT NULL,
  `telefono` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `sucursales`
--

INSERT INTO `sucursales` (`id_sucursal`, `id_empresa`, `descripcion`, `direccion`, `telefono`) VALUES
(1, 1, 'Oficina Principal', 'Av. Central 123', '1234-5678'),
(2, 1, 'Oficina Secundaria', 'Calle Secundaria 45', '1234-8765'),
(3, 2, 'Sucursal Norte', 'Calle Norte 10', '2345-6789'),
(4, 3, 'Sucursal Sur', 'Calle Sur 99', '3456-7890');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipo_recurso`
--

CREATE TABLE `tipo_recurso` (
  `id_tipo_recurso` int(11) NOT NULL,
  `descripcion` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tipo_recurso`
--

INSERT INTO `tipo_recurso` (`id_tipo_recurso`, `descripcion`) VALUES
(1, 'AnyDesk'),
(2, 'Servidor'),
(3, 'Impresora'),
(4, 'Software'),
(5, 'Licencia'),
(6, 'anydesk');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `empresa`
--
ALTER TABLE `empresa`
  ADD PRIMARY KEY (`id_empresa`);

--
-- Indices de la tabla `recurso`
--
ALTER TABLE `recurso`
  ADD PRIMARY KEY (`id_recurso`),
  ADD KEY `id_empresa` (`id_empresa`),
  ADD KEY `id_sucursal` (`id_sucursal`),
  ADD KEY `id_tipo_recurso` (`id_tipo_recurso`);

--
-- Indices de la tabla `sucursales`
--
ALTER TABLE `sucursales`
  ADD PRIMARY KEY (`id_sucursal`),
  ADD KEY `id_empresa` (`id_empresa`);

--
-- Indices de la tabla `tipo_recurso`
--
ALTER TABLE `tipo_recurso`
  ADD PRIMARY KEY (`id_tipo_recurso`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `empresa`
--
ALTER TABLE `empresa`
  MODIFY `id_empresa` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `recurso`
--
ALTER TABLE `recurso`
  MODIFY `id_recurso` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT de la tabla `sucursales`
--
ALTER TABLE `sucursales`
  MODIFY `id_sucursal` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `tipo_recurso`
--
ALTER TABLE `tipo_recurso`
  MODIFY `id_tipo_recurso` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `recurso`
--
ALTER TABLE `recurso`
  ADD CONSTRAINT `recurso_ibfk_1` FOREIGN KEY (`id_empresa`) REFERENCES `empresa` (`id_empresa`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `recurso_ibfk_2` FOREIGN KEY (`id_sucursal`) REFERENCES `sucursales` (`id_sucursal`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `recurso_ibfk_3` FOREIGN KEY (`id_tipo_recurso`) REFERENCES `tipo_recurso` (`id_tipo_recurso`) ON UPDATE CASCADE;

--
-- Filtros para la tabla `sucursales`
--
ALTER TABLE `sucursales`
  ADD CONSTRAINT `sucursales_ibfk_1` FOREIGN KEY (`id_empresa`) REFERENCES `empresa` (`id_empresa`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
