# IngeSoft - Sistema de Gestión Empresarial

Sistema de gestión empresarial desarrollado con JavaFX y MySQL.

## Estructura del Proyecto

\`\`\`
ingesoft/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/mycompany/ingesoft/
│   │   │       ├── App.java                    # Clase principal de la aplicación
│   │   │       ├── controllers/                # Controladores de las vistas
│   │   │       │   ├── PrimaryController.java
│   │   │       │   ├── SecondaryController.java
│   │   │       │   ├── NuevaEmpresaController.java
│   │   │       │   ├── NuevaNotaController.java
│   │   │       │   └── NuevaSucursalController.java
│   │   │       ├── dao/                        # Data Access Objects
│   │   │       │   ├── ClaseDAO.java
│   │   │       │   └── Conexion.java
│   │   │       ├── models/                     # Modelos de datos
│   │   │       │   ├── Empresa.java
│   │   │       │   ├── Nota.java
│   │   │       │   └── Sucursal.java
│   │   │       └── module-info.java
│   │   └── resources/
│   │       └── com/mycompany/ingesoft/
│   │           ├── fxml/                       # Archivos FXML de las vistas
│   │           │   ├── primary.fxml
│   │           │   ├── secondary.fxml
│   │           │   ├── nuevaEmpresa.fxml
│   │           │   ├── nuevaNota.fxml
│   │           │   └── nuevaSucursal.fxml
│   │           ├── css/                        # Hojas de estilo
│   │           │   └── styles.css
│   │           └── images/                     # Recursos gráficos
│   │               └── ingesoft-logo.png
│   └── test/                                   # Tests unitarios (futuro)
├── scripts/                                    # Scripts SQL
│   ├── create_database.sql
│   └── create_database_v2.sql
├── target/                                     # Archivos compilados (ignorado en git)
├── pom.xml                                     # Configuración de Maven
├── nbactions.xml                               # Acciones de NetBeans
└── README.md
\`\`\`

## Requisitos

- Java 17 o superior
- Maven 3.6+
- MySQL 8.0+
- JavaFX 17

## Configuración

1. Crear la base de datos ejecutando los scripts en `scripts/`
2. Configurar la conexión a la base de datos en `src/main/java/com/mycompany/ingesoft/dao/Conexion.java`

## Ejecución

\`\`\`bash
# Compilar el proyecto
mvn clean compile

# Ejecutar la aplicación
mvn javafx:run

# Generar JAR ejecutable
mvn clean package
\`\`\`

## Tecnologías

- **JavaFX 17**: Framework para interfaces gráficas
- **Maven**: Gestión de dependencias y construcción
- **MySQL**: Base de datos relacional
- **JDBC**: Conectividad con base de datos
