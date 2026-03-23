-- ================================================================
-- SCRIPT DE CONFIGURACION INICIAL - Oracle 21c Express Edition
-- Ejecutar conectado como:
--   Usuario : sys   (o system)
--   Password: admin           <-- la que pusiste en la instalacion
--   Rol     : SYSDBA
--   Host    : localhost | Port: 1521 | SID: XE
-- ================================================================

-- PASO 1: Cambiar al Pluggable Database XEPDB1
ALTER SESSION SET CONTAINER = XEPDB1;

-- PASO 2: Crear el usuario MUNDIAL2026 dentro de XEPDB1
CREATE USER MUNDIAL2026 IDENTIFIED BY "Mundial2026"
  DEFAULT TABLESPACE USERS
  TEMPORARY TABLESPACE TEMP
  QUOTA UNLIMITED ON USERS;

-- PASO 3: Otorgar permisos necesarios
GRANT CONNECT TO MUNDIAL2026;
GRANT RESOURCE TO MUNDIAL2026;
GRANT CREATE SESSION TO MUNDIAL2026;
GRANT CREATE TABLE TO MUNDIAL2026;
GRANT CREATE SEQUENCE TO MUNDIAL2026;
GRANT CREATE VIEW TO MUNDIAL2026;
GRANT CREATE PROCEDURE TO MUNDIAL2026;
GRANT UNLIMITED TABLESPACE TO MUNDIAL2026;

-- Verificar que el usuario fue creado
SELECT USERNAME, ACCOUNT_STATUS, DEFAULT_TABLESPACE
FROM DBA_USERS
WHERE USERNAME = 'MUNDIAL2026';

-- ================================================================
-- MENSAJE FINAL
-- ================================================================
BEGIN
  DBMS_OUTPUT.PUT_LINE('Usuario MUNDIAL2026 creado en XEPDB1.');
  DBMS_OUTPUT.PUT_LINE('Ahora puedes ejecutar ddl_mundial2026.sql y dml_mundial2026.sql');
  DBMS_OUTPUT.PUT_LINE('conectado como MUNDIAL2026.');
END;
/
