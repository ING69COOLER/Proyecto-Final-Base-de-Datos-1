# 🏆 Proyecto Final: Mundial de Fútbol 2026

Este proyecto es el sistema de gestión del Campeonato Mundial de Fútbol 2026 desarrollado en Java con base de datos Oracle 21c (JDBC). Cubre la gestión de las selecciones, jugadores, partidos, estadios, usuarios y la bitácora de auditoría.

---

## 🏗️ Arquitectura del Proyecto (Patrón MVC / DAO)

El código Java está dividido limpiamente en paquetes. Aquí tienes la explicación exacta de la función de cada carpeta y clase:

### 1. Paquete `db` (Base de Datos)
- **`ConexionOracle.java`**: Es una clase bajo el **patrón Singleton** que se encarga de probar y establecer la conexión con Oracle utilizando el driver JDBC (ojdbc11). Garantiza que solo exista una conexión abierta a la base de datos `XEPDB1` en todo el programa para ahorrar recursos.

### 2. Paquete `model` (Entidades o POJOs)
Son representaciones directas de las tablas de Oracle pasadas al mundo de los objetos Java. Cada objeto maneja todos sus atributos y métodos constructores/getters/setters elementales.
- **`Usuario.java`**: Representa a quienes inician sesión (`Admin`, `Tradicional` o `Esporádico`).
- **`Bitacora.java`**: Modela el historial de ingresos/salidas que se guarda al usar el sistema.
- **`Equipo.java`**: Estructura general de una selección nacional.
- **`Jugador.java`**: Información de los jugadores (incluye un método adicional `getEdad()` que calcula su edad real en base a su fecha de nacimiento).
- **`Partido.java`**: Detalle que une 2 equipos en un estadio.
- **`Confederacion.java`**: La asociación mundial (UEFA, CONMEBOL, etc.).

### 3. Paquete `dao` (Data Access Object)
Aquí se encapsula **toda la lógica de envío de Queries SQL**. La interfaz de usuario nunca toca el SQL, todo pasa a través del DAO correspondiente.
- **`GenericDAO.java`**: Interfaz genérica que obliga a los DAOs a tener los 5 métodos básicos del CRUD (`crear`, `obtenerPorId`, `obtenerTodos`, `actualizar`, `eliminar`).
- **`UsuarioDAO.java`**: Manejo de creación, edición y eliminación de usuarios. Adicionalmente, tiene el método para autenticar y validar credenciales.
- **`BitacoraDAO.java`**: Maneja el reporte 5 (búsqueda por rango de fechas) y tiene métodos específicos (`registrarIngreso` y `registrarSalida` por `idRegistro`).
- **`JugadorDAO.java`**: Además de las funciones clásicas CRUD, aquí implementamos los reportes avanzados:
  - `jugadorMasCaroPorConfederacion()` **[Reporte 1]**
  - `jugadoresMenoresDe20()` **[Reporte 4]**
  - `filtrarPorFisico()` **[Reporte 6]**
  - `valorTotalPorConfederacion()` **[Reporte 7]**
- **`PartidoDAO.java`**: Lógica de gestión de partidos y los siguientes reportes:
  - `obtenerPorEstadio()` **[Reporte 2]**
  - `partidosPorEquipoPorPais()` **[Reporte 8]**

### 4. Paquete `security` (Seguridad)
- **`SeguridadService.java`**: Módulo sumamente importante que encripta las contraseñas en **SHA-256 con un "Salt" aleatorio** interno para no depender de librerías de terceros (y cumplir el req. de la universidad). Además valida a qué cosas puede acceder un tipo de rol ("Admin", "Tradicional", "Esporadico").
- **`SesionActual.java`**: Clase estática que retiene globalmente quién es la persona que se conectó en ese momento y qué registro en la bitácora le corresponde a su sesión, para facilitar el borrado al botón "Cerrar sesión".

### 5. Paquete `gui` (Interfaz Gráfica)
Creadas nativamente con Java Swing.
- **`LoginForm.java`**: La ventana inicial de fondo oscuro de "Ingresar". Valida que no dejes campos en blanco, desencripta y verifica tu contraseña y arranca la bitácora.
- **`MainMenu.java`**: Menú dinámico que evalúa los roles. Si eres Admin verás el botón de "Usuarios", si eres esporádico te ocultará los módulos CRUD de modificación y solo verás "Reportes". Al cerrarse por la X, registrará la hora de salida de la auditoría.

### 6. Paquete `util` (Herramientas extra)
- **`InicializarAdmin.java`**: Script independiente por terminal que se usa una única vez por el programador (tú) para obligar a insertar al primer `admin` a la base de datos de tal forma que su password (`Admin123!`) quede correctamente encriptado.

Correr con el comando java -cp "out;lib\ojdbc11.jar" gui.LoginForm


