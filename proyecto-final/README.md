# TallerBMW — Instrucciones de instalacion

## Requisitos
- Java 17+
- Maven 3.8+
- MySQL 8.0
- IntelliJ IDEA (Community o Ultimate)

## Credenciales por defecto de la app
| Usuario | Contrasena | Rol          |
|---------|------------|--------------|
| admin   | admin123   | ADMIN        |
| recep   | recep123   | RECEPCIONISTA|
| mec     | mec123     | MECANICO     |

---

## Pasos de instalacion

### 1. Base de datos
Abrir MySQL Workbench y ejecutar:
```
TallerBMW/sql/taller_bmw.sql
```
Crea la base de datos, todas las tablas y los datos de ejemplo.

### 2. Instalar la libreria bmw-components
```bash
cd bmw-components
mvn install
```
Esto instala el JAR en el repositorio Maven local (~/.m2).

### 3. Compilar y ejecutar TallerBMW
```bash
cd TallerBMW
mvn compile
mvn exec:java -Dexec.mainClass="view.LoginView"
```
O desde IntelliJ: abrir como proyecto Maven, ejecutar `view.LoginView`.

### 4. Credencial de MySQL
Si tu MySQL usa usuario/contrasena diferente a `root / Derek123`,
editar la linea correspondiente en:
```
TallerBMW/src/main/java/util/DBConnection.java
```

---

## Estructura del proyecto

```
proyecto-final/
  bmw-components/          <- Libreria reutilizable (instalar primero)
    src/main/java/com/bmwcomponents/
      db/DBConnection.java         Conexion configurable
      dao/GenericDAO.java          DAO abstracto generico
      ui/UIFactory.java            Fabrica de componentes Swing
      ui/GenericTablePanel.java    Tabla reutilizable con tema oscuro

  TallerBMW/               <- Aplicacion principal
    sql/taller_bmw.sql     <- Script completo BD
    src/main/java/
      util/DBConnection.java       Puente a bmw-components
      model/                       5 modelos: Usuario, Cliente, Vehiculo, OrdenServicio, Inventario
      dao/                         5 DAOs todos extendiendo GenericDAO<T>
      controller/                  5 controllers con validaciones
      view/                        7 vistas: Login, Main, Usuario, Cliente, Vehiculo, OrdenServicio, Inventario
```

## Modulos del sistema

| Modulo           | Roles con acceso              | Funciones                                      |
|-----------------|-------------------------------|------------------------------------------------|
| Clientes         | Todos                         | CRUD, busqueda, soft-delete                    |
| Vehiculos        | Todos                         | CRUD, filtro por cliente, soft-delete          |
| Ordenes Servicio | Todos                         | Crear, cambiar estado, actualizar costo, filtrar|
| Inventario       | Admin y Mecanico              | CRUD, entrada/salida stock, alerta bajo stock  |
| Usuarios         | Solo Admin                    | CRUD, cambio de rol, soft-delete               |
