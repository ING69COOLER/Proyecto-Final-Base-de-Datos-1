-- Eliminar duplicados manteniendo un unico registro original (criterio MIN ROWID)

-- 1. Tablas finales (Sin hijos)
DELETE FROM BITACORA WHERE ROWID NOT IN (SELECT MIN(ROWID) FROM BITACORA GROUP BY id_usuario, fecha_hora_ingreso);
DELETE FROM PARTIDO WHERE ROWID NOT IN (SELECT MIN(ROWID) FROM PARTIDO GROUP BY id_eq_local, id_eq_visit, fecha_hora, id_estadio, id_grupo);
DELETE FROM EQUIPO_GRUPO WHERE ROWID NOT IN (SELECT MIN(ROWID) FROM EQUIPO_GRUPO GROUP BY id_equipo, id_grupo);
DELETE FROM JUGADOR WHERE ROWID NOT IN (SELECT MIN(ROWID) FROM JUGADOR GROUP BY nombre, apellido, id_equipo);
DELETE FROM DIRECTORTECNICO WHERE ROWID NOT IN (SELECT MIN(ROWID) FROM DIRECTORTECNICO GROUP BY nombre, id_equipo);

-- 2. Tablas intermedias
BEGIN
  FOR d IN (SELECT nombre_grupo AS nombre, MIN(id_grupo) min_id FROM GRUPO GROUP BY nombre_grupo HAVING COUNT(*) > 1) LOOP
    UPDATE EQUIPO_GRUPO SET id_grupo = d.min_id WHERE id_grupo IN (SELECT id_grupo FROM GRUPO WHERE nombre_grupo = d.nombre AND id_grupo != d.min_id);
    UPDATE PARTIDO SET id_grupo = d.min_id WHERE id_grupo IN (SELECT id_grupo FROM GRUPO WHERE nombre_grupo = d.nombre AND id_grupo != d.min_id);
  END LOOP;
END;
/
DELETE FROM GRUPO WHERE ROWID NOT IN (SELECT MIN(ROWID) FROM GRUPO GROUP BY nombre_grupo);

BEGIN
  FOR d IN (SELECT nombre, MIN(id_estadio) min_id FROM ESTADIO GROUP BY nombre HAVING COUNT(*) > 1) LOOP
    UPDATE PARTIDO SET id_estadio = d.min_id WHERE id_estadio IN (SELECT id_estadio FROM ESTADIO WHERE nombre = d.nombre AND id_estadio != d.min_id);
  END LOOP;
END;
/
DELETE FROM ESTADIO WHERE ROWID NOT IN (SELECT MIN(ROWID) FROM ESTADIO GROUP BY nombre);

BEGIN
  FOR d IN (SELECT nombre, MIN(id_equipo) min_id FROM EQUIPO GROUP BY nombre HAVING COUNT(*) > 1) LOOP
    UPDATE PARTIDO SET id_eq_local = d.min_id WHERE id_eq_local IN (SELECT id_equipo FROM EQUIPO WHERE nombre = d.nombre AND id_equipo != d.min_id);
    UPDATE PARTIDO SET id_eq_visit = d.min_id WHERE id_eq_visit IN (SELECT id_equipo FROM EQUIPO WHERE nombre = d.nombre AND id_equipo != d.min_id);
    UPDATE EQUIPO_GRUPO SET id_equipo = d.min_id WHERE id_equipo IN (SELECT id_equipo FROM EQUIPO WHERE nombre = d.nombre AND id_equipo != d.min_id);
    UPDATE JUGADOR SET id_equipo = d.min_id WHERE id_equipo IN (SELECT id_equipo FROM EQUIPO WHERE nombre = d.nombre AND id_equipo != d.min_id);
    UPDATE DIRECTORTECNICO SET id_equipo = d.min_id WHERE id_equipo IN (SELECT id_equipo FROM EQUIPO WHERE nombre = d.nombre AND id_equipo != d.min_id);
  END LOOP;
END;
/
DELETE FROM EQUIPO WHERE ROWID NOT IN (SELECT MIN(ROWID) FROM EQUIPO GROUP BY nombre);

BEGIN
  FOR d IN (SELECT nombre, MIN(id_ciudad) min_id FROM CIUDAD GROUP BY nombre HAVING COUNT(*) > 1) LOOP
    UPDATE ESTADIO SET id_ciudad = d.min_id WHERE id_ciudad IN (SELECT id_ciudad FROM CIUDAD WHERE nombre = d.nombre AND id_ciudad != d.min_id);
  END LOOP;
END;
/
DELETE FROM CIUDAD WHERE ROWID NOT IN (SELECT MIN(ROWID) FROM CIUDAD GROUP BY nombre);

BEGIN
  FOR d IN (SELECT nombre, MIN(id_confederacion) min_id FROM CONFEDERACION GROUP BY nombre HAVING COUNT(*) > 1) LOOP
    UPDATE EQUIPO SET id_confederacion = d.min_id WHERE id_confederacion IN (SELECT id_confederacion FROM CONFEDERACION WHERE nombre = d.nombre AND id_confederacion != d.min_id);
  END LOOP;
END;
/
DELETE FROM CONFEDERACION WHERE ROWID NOT IN (SELECT MIN(ROWID) FROM CONFEDERACION GROUP BY nombre);

BEGIN
  FOR d IN (SELECT nombre, MIN(id_pais_anfitrion) min_id FROM PAISANFITRION GROUP BY nombre HAVING COUNT(*) > 1) LOOP
    UPDATE CIUDAD SET id_pais_anfitrion = d.min_id WHERE id_pais_anfitrion IN (SELECT id_pais_anfitrion FROM PAISANFITRION WHERE nombre = d.nombre AND id_pais_anfitrion != d.min_id);
  END LOOP;
END;
/
DELETE FROM PAISANFITRION WHERE ROWID NOT IN (SELECT MIN(ROWID) FROM PAISANFITRION GROUP BY nombre);

COMMIT;
EXIT;
