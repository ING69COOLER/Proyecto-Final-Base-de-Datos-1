SPOOL sql\resultado_dml.txt
SET SERVEROUTPUT ON
SET ECHO ON
SET DEFINE OFF
SET PAGESIZE 200
WHENEVER SQLERROR CONTINUE

INSERT INTO PAISANFITRION (nombre) VALUES ('Mexico');
INSERT INTO PAISANFITRION (nombre) VALUES ('USA');
INSERT INTO PAISANFITRION (nombre) VALUES ('Canada');

INSERT INTO CIUDAD (nombre, id_pais_anfitrion) VALUES ('Ciudad de Mexico', 1);
INSERT INTO CIUDAD (nombre, id_pais_anfitrion) VALUES ('Guadalajara', 1);
INSERT INTO CIUDAD (nombre, id_pais_anfitrion) VALUES ('Monterrey', 1);
INSERT INTO CIUDAD (nombre, id_pais_anfitrion) VALUES ('New York', 2);
INSERT INTO CIUDAD (nombre, id_pais_anfitrion) VALUES ('Los Angeles', 2);
INSERT INTO CIUDAD (nombre, id_pais_anfitrion) VALUES ('Dallas', 2);
INSERT INTO CIUDAD (nombre, id_pais_anfitrion) VALUES ('Miami', 2);
INSERT INTO CIUDAD (nombre, id_pais_anfitrion) VALUES ('Toronto', 3);
INSERT INTO CIUDAD (nombre, id_pais_anfitrion) VALUES ('Vancouver', 3);

INSERT INTO ESTADIO (nombre, capacidad, id_ciudad) VALUES ('Estadio Azteca', 87500, (SELECT id_ciudad FROM CIUDAD WHERE nombre='Ciudad de Mexico'));
INSERT INTO ESTADIO (nombre, capacidad, id_ciudad) VALUES ('Estadio Akron', 49850, (SELECT id_ciudad FROM CIUDAD WHERE nombre='Guadalajara'));
INSERT INTO ESTADIO (nombre, capacidad, id_ciudad) VALUES ('Estadio BBVA', 53500, (SELECT id_ciudad FROM CIUDAD WHERE nombre='Monterrey'));
INSERT INTO ESTADIO (nombre, capacidad, id_ciudad) VALUES ('MetLife Stadium', 82500, (SELECT id_ciudad FROM CIUDAD WHERE nombre='New York'));
INSERT INTO ESTADIO (nombre, capacidad, id_ciudad) VALUES ('SoFi Stadium', 70240, (SELECT id_ciudad FROM CIUDAD WHERE nombre='Los Angeles'));
INSERT INTO ESTADIO (nombre, capacidad, id_ciudad) VALUES ('ATT Stadium', 80000, (SELECT id_ciudad FROM CIUDAD WHERE nombre='Dallas'));
INSERT INTO ESTADIO (nombre, capacidad, id_ciudad) VALUES ('Hard Rock Stadium', 64767, (SELECT id_ciudad FROM CIUDAD WHERE nombre='Miami'));
INSERT INTO ESTADIO (nombre, capacidad, id_ciudad) VALUES ('BMO Field', 30000, (SELECT id_ciudad FROM CIUDAD WHERE nombre='Toronto'));
INSERT INTO ESTADIO (nombre, capacidad, id_ciudad) VALUES ('BC Place', 54500, (SELECT id_ciudad FROM CIUDAD WHERE nombre='Vancouver'));

INSERT INTO CONFEDERACION (nombre) VALUES ('UEFA');
INSERT INTO CONFEDERACION (nombre) VALUES ('CONMEBOL');
INSERT INTO CONFEDERACION (nombre) VALUES ('CONCACAF');
INSERT INTO CONFEDERACION (nombre) VALUES ('CAF');
INSERT INTO CONFEDERACION (nombre) VALUES ('AFC');
INSERT INTO CONFEDERACION (nombre) VALUES ('OFC');

INSERT INTO EQUIPO (nombre, ranking_fifa, id_confederacion, valor_mercado) VALUES ('Brasil', 5, (SELECT id_confederacion FROM CONFEDERACION WHERE nombre='CONMEBOL'), 1200000000);
INSERT INTO EQUIPO (nombre, ranking_fifa, id_confederacion, valor_mercado) VALUES ('Argentina', 1, (SELECT id_confederacion FROM CONFEDERACION WHERE nombre='CONMEBOL'), 980000000);
INSERT INTO EQUIPO (nombre, ranking_fifa, id_confederacion, valor_mercado) VALUES ('Francia', 2, (SELECT id_confederacion FROM CONFEDERACION WHERE nombre='UEFA'), 1500000000);
INSERT INTO EQUIPO (nombre, ranking_fifa, id_confederacion, valor_mercado) VALUES ('Inglaterra', 3, (SELECT id_confederacion FROM CONFEDERACION WHERE nombre='UEFA'), 1800000000);
INSERT INTO EQUIPO (nombre, ranking_fifa, id_confederacion, valor_mercado) VALUES ('Mexico', 15, (SELECT id_confederacion FROM CONFEDERACION WHERE nombre='CONCACAF'), 350000000);
INSERT INTO EQUIPO (nombre, ranking_fifa, id_confederacion, valor_mercado) VALUES ('USA', 13, (SELECT id_confederacion FROM CONFEDERACION WHERE nombre='CONCACAF'), 420000000);
INSERT INTO EQUIPO (nombre, ranking_fifa, id_confederacion, valor_mercado) VALUES ('Canada', 41, (SELECT id_confederacion FROM CONFEDERACION WHERE nombre='CONCACAF'), 280000000);
INSERT INTO EQUIPO (nombre, ranking_fifa, id_confederacion, valor_mercado) VALUES ('Marruecos', 14, (SELECT id_confederacion FROM CONFEDERACION WHERE nombre='CAF'), 320000000);
INSERT INTO EQUIPO (nombre, ranking_fifa, id_confederacion, valor_mercado) VALUES ('Japon', 17, (SELECT id_confederacion FROM CONFEDERACION WHERE nombre='AFC'), 210000000);
INSERT INTO EQUIPO (nombre, ranking_fifa, id_confederacion, valor_mercado) VALUES ('Espana', 7, (SELECT id_confederacion FROM CONFEDERACION WHERE nombre='UEFA'), 1100000000);
INSERT INTO EQUIPO (nombre, ranking_fifa, id_confederacion, valor_mercado) VALUES ('Portugal', 6, (SELECT id_confederacion FROM CONFEDERACION WHERE nombre='UEFA'), 980000000);
INSERT INTO EQUIPO (nombre, ranking_fifa, id_confederacion, valor_mercado) VALUES ('Alemania', 16, (SELECT id_confederacion FROM CONFEDERACION WHERE nombre='UEFA'), 870000000);

INSERT INTO DIRECTORTECNICO (nombre, nacionalidad, fecha_nacimiento, id_equipo) VALUES ('Dorival Junior', 'Brasilena', DATE '1961-06-03', (SELECT id_equipo FROM EQUIPO WHERE nombre='Brasil'));
INSERT INTO DIRECTORTECNICO (nombre, nacionalidad, fecha_nacimiento, id_equipo) VALUES ('Lionel Scaloni', 'Argentina', DATE '1978-05-16', (SELECT id_equipo FROM EQUIPO WHERE nombre='Argentina'));
INSERT INTO DIRECTORTECNICO (nombre, nacionalidad, fecha_nacimiento, id_equipo) VALUES ('Didier Deschamps', 'Francesa', DATE '1968-10-15', (SELECT id_equipo FROM EQUIPO WHERE nombre='Francia'));
INSERT INTO DIRECTORTECNICO (nombre, nacionalidad, fecha_nacimiento, id_equipo) VALUES ('Gareth Southgate', 'Inglesa', DATE '1970-09-03', (SELECT id_equipo FROM EQUIPO WHERE nombre='Inglaterra'));
INSERT INTO DIRECTORTECNICO (nombre, nacionalidad, fecha_nacimiento, id_equipo) VALUES ('Javier Aguirre', 'Mexicana', DATE '1958-12-01', (SELECT id_equipo FROM EQUIPO WHERE nombre='Mexico'));
INSERT INTO DIRECTORTECNICO (nombre, nacionalidad, fecha_nacimiento, id_equipo) VALUES ('Gregg Berhalter', 'Estadounidense', DATE '1973-08-01', (SELECT id_equipo FROM EQUIPO WHERE nombre='USA'));

INSERT INTO JUGADOR (nombre, apellido, fecha_nacimiento, posicion, peso, estatura, valor_mercado, id_equipo) VALUES ('Vinicius', 'Junior', DATE '2000-07-12', 'Delantero', 73, 1.76, 200000000, (SELECT id_equipo FROM EQUIPO WHERE nombre='Brasil'));
INSERT INTO JUGADOR (nombre, apellido, fecha_nacimiento, posicion, peso, estatura, valor_mercado, id_equipo) VALUES ('Rodrygo', 'Goes', DATE '2001-01-09', 'Delantero', 64, 1.74, 90000000, (SELECT id_equipo FROM EQUIPO WHERE nombre='Brasil'));
INSERT INTO JUGADOR (nombre, apellido, fecha_nacimiento, posicion, peso, estatura, valor_mercado, id_equipo) VALUES ('Lionel', 'Messi', DATE '1987-06-24', 'Delantero', 72, 1.70, 35000000, (SELECT id_equipo FROM EQUIPO WHERE nombre='Argentina'));
INSERT INTO JUGADOR (nombre, apellido, fecha_nacimiento, posicion, peso, estatura, valor_mercado, id_equipo) VALUES ('Julian', 'Alvarez', DATE '2000-01-31', 'Delantero', 70, 1.70, 90000000, (SELECT id_equipo FROM EQUIPO WHERE nombre='Argentina'));
INSERT INTO JUGADOR (nombre, apellido, fecha_nacimiento, posicion, peso, estatura, valor_mercado, id_equipo) VALUES ('Kylian', 'Mbappe', DATE '1998-12-20', 'Delantero', 73, 1.78, 180000000, (SELECT id_equipo FROM EQUIPO WHERE nombre='Francia'));
INSERT INTO JUGADOR (nombre, apellido, fecha_nacimiento, posicion, peso, estatura, valor_mercado, id_equipo) VALUES ('Jude', 'Bellingham', DATE '2003-06-29', 'Mediocampista', 75, 1.86, 180000000, (SELECT id_equipo FROM EQUIPO WHERE nombre='Inglaterra'));
INSERT INTO JUGADOR (nombre, apellido, fecha_nacimiento, posicion, peso, estatura, valor_mercado, id_equipo) VALUES ('Harry', 'Kane', DATE '1993-07-28', 'Delantero', 86, 1.88, 100000000, (SELECT id_equipo FROM EQUIPO WHERE nombre='Inglaterra'));
INSERT INTO JUGADOR (nombre, apellido, fecha_nacimiento, posicion, peso, estatura, valor_mercado, id_equipo) VALUES ('Hirving', 'Lozano', DATE '1995-07-30', 'Delantero', 70, 1.74, 20000000, (SELECT id_equipo FROM EQUIPO WHERE nombre='Mexico'));
INSERT INTO JUGADOR (nombre, apellido, fecha_nacimiento, posicion, peso, estatura, valor_mercado, id_equipo) VALUES ('Christian', 'Pulisic', DATE '1998-09-18', 'Mediocampista', 70, 1.80, 40000000, (SELECT id_equipo FROM EQUIPO WHERE nombre='USA'));
INSERT INTO JUGADOR (nombre, apellido, fecha_nacimiento, posicion, peso, estatura, valor_mercado, id_equipo) VALUES ('Pedri', 'Gonzalez', DATE '2002-11-25', 'Mediocampista', 60, 1.74, 120000000, (SELECT id_equipo FROM EQUIPO WHERE nombre='Espana'));
INSERT INTO JUGADOR (nombre, apellido, fecha_nacimiento, posicion, peso, estatura, valor_mercado, id_equipo) VALUES ('Lamine', 'Yamal', DATE '2007-07-13', 'Delantero', 57, 1.76, 180000000, (SELECT id_equipo FROM EQUIPO WHERE nombre='Espana'));
INSERT INTO JUGADOR (nombre, apellido, fecha_nacimiento, posicion, peso, estatura, valor_mercado, id_equipo) VALUES ('Cristiano', 'Ronaldo', DATE '1985-02-05', 'Delantero', 83, 1.87, 15000000, (SELECT id_equipo FROM EQUIPO WHERE nombre='Portugal'));

INSERT INTO GRUPO (nombre_grupo) VALUES ('A');
INSERT INTO GRUPO (nombre_grupo) VALUES ('B');
INSERT INTO GRUPO (nombre_grupo) VALUES ('C');
INSERT INTO GRUPO (nombre_grupo) VALUES ('D');
INSERT INTO GRUPO (nombre_grupo) VALUES ('E');
INSERT INTO GRUPO (nombre_grupo) VALUES ('F');
INSERT INTO GRUPO (nombre_grupo) VALUES ('G');
INSERT INTO GRUPO (nombre_grupo) VALUES ('H');
INSERT INTO GRUPO (nombre_grupo) VALUES ('I');
INSERT INTO GRUPO (nombre_grupo) VALUES ('J');
INSERT INTO GRUPO (nombre_grupo) VALUES ('K');
INSERT INTO GRUPO (nombre_grupo) VALUES ('L');

INSERT INTO EQUIPO_GRUPO (id_equipo, id_grupo) VALUES ((SELECT id_equipo FROM EQUIPO WHERE nombre='Brasil'), (SELECT id_grupo FROM GRUPO WHERE nombre_grupo='A'));
INSERT INTO EQUIPO_GRUPO (id_equipo, id_grupo) VALUES ((SELECT id_equipo FROM EQUIPO WHERE nombre='Japon'), (SELECT id_grupo FROM GRUPO WHERE nombre_grupo='A'));
INSERT INTO EQUIPO_GRUPO (id_equipo, id_grupo) VALUES ((SELECT id_equipo FROM EQUIPO WHERE nombre='Argentina'), (SELECT id_grupo FROM GRUPO WHERE nombre_grupo='B'));
INSERT INTO EQUIPO_GRUPO (id_equipo, id_grupo) VALUES ((SELECT id_equipo FROM EQUIPO WHERE nombre='Marruecos'), (SELECT id_grupo FROM GRUPO WHERE nombre_grupo='B'));
INSERT INTO EQUIPO_GRUPO (id_equipo, id_grupo) VALUES ((SELECT id_equipo FROM EQUIPO WHERE nombre='Francia'), (SELECT id_grupo FROM GRUPO WHERE nombre_grupo='C'));
INSERT INTO EQUIPO_GRUPO (id_equipo, id_grupo) VALUES ((SELECT id_equipo FROM EQUIPO WHERE nombre='Mexico'), (SELECT id_grupo FROM GRUPO WHERE nombre_grupo='C'));
INSERT INTO EQUIPO_GRUPO (id_equipo, id_grupo) VALUES ((SELECT id_equipo FROM EQUIPO WHERE nombre='Inglaterra'), (SELECT id_grupo FROM GRUPO WHERE nombre_grupo='D'));
INSERT INTO EQUIPO_GRUPO (id_equipo, id_grupo) VALUES ((SELECT id_equipo FROM EQUIPO WHERE nombre='USA'), (SELECT id_grupo FROM GRUPO WHERE nombre_grupo='D'));
INSERT INTO EQUIPO_GRUPO (id_equipo, id_grupo) VALUES ((SELECT id_equipo FROM EQUIPO WHERE nombre='Espana'), (SELECT id_grupo FROM GRUPO WHERE nombre_grupo='E'));
INSERT INTO EQUIPO_GRUPO (id_equipo, id_grupo) VALUES ((SELECT id_equipo FROM EQUIPO WHERE nombre='Canada'), (SELECT id_grupo FROM GRUPO WHERE nombre_grupo='E'));
INSERT INTO EQUIPO_GRUPO (id_equipo, id_grupo) VALUES ((SELECT id_equipo FROM EQUIPO WHERE nombre='Portugal'), (SELECT id_grupo FROM GRUPO WHERE nombre_grupo='F'));
INSERT INTO EQUIPO_GRUPO (id_equipo, id_grupo) VALUES ((SELECT id_equipo FROM EQUIPO WHERE nombre='Alemania'), (SELECT id_grupo FROM GRUPO WHERE nombre_grupo='F'));

INSERT INTO PARTIDO (id_eq_local, id_eq_visit, fecha_hora, id_estadio, id_grupo)
  VALUES ((SELECT id_equipo FROM EQUIPO WHERE nombre='Brasil'), (SELECT id_equipo FROM EQUIPO WHERE nombre='Japon'), TIMESTAMP '2026-06-12 15:00:00', (SELECT id_estadio FROM ESTADIO WHERE nombre='MetLife Stadium'), (SELECT id_grupo FROM GRUPO WHERE nombre_grupo='A'));
INSERT INTO PARTIDO (id_eq_local, id_eq_visit, fecha_hora, id_estadio, id_grupo)
  VALUES ((SELECT id_equipo FROM EQUIPO WHERE nombre='Argentina'), (SELECT id_equipo FROM EQUIPO WHERE nombre='Marruecos'), TIMESTAMP '2026-06-13 18:00:00', (SELECT id_estadio FROM ESTADIO WHERE nombre='ATT Stadium'), (SELECT id_grupo FROM GRUPO WHERE nombre_grupo='B'));
INSERT INTO PARTIDO (id_eq_local, id_eq_visit, fecha_hora, id_estadio, id_grupo)
  VALUES ((SELECT id_equipo FROM EQUIPO WHERE nombre='Francia'), (SELECT id_equipo FROM EQUIPO WHERE nombre='Mexico'), TIMESTAMP '2026-06-14 21:00:00', (SELECT id_estadio FROM ESTADIO WHERE nombre='Estadio Azteca'), (SELECT id_grupo FROM GRUPO WHERE nombre_grupo='C'));
INSERT INTO PARTIDO (id_eq_local, id_eq_visit, fecha_hora, id_estadio, id_grupo)
  VALUES ((SELECT id_equipo FROM EQUIPO WHERE nombre='Inglaterra'), (SELECT id_equipo FROM EQUIPO WHERE nombre='USA'), TIMESTAMP '2026-06-15 15:00:00', (SELECT id_estadio FROM ESTADIO WHERE nombre='MetLife Stadium'), (SELECT id_grupo FROM GRUPO WHERE nombre_grupo='D'));
INSERT INTO PARTIDO (id_eq_local, id_eq_visit, fecha_hora, id_estadio, id_grupo)
  VALUES ((SELECT id_equipo FROM EQUIPO WHERE nombre='Espana'), (SELECT id_equipo FROM EQUIPO WHERE nombre='Canada'), TIMESTAMP '2026-06-16 18:00:00', (SELECT id_estadio FROM ESTADIO WHERE nombre='BMO Field'), (SELECT id_grupo FROM GRUPO WHERE nombre_grupo='E'));

COMMIT;

SELECT 'PAISANFITRION=' || COUNT(*) FROM PAISANFITRION;
SELECT 'EQUIPO='        || COUNT(*) FROM EQUIPO;
SELECT 'JUGADOR='       || COUNT(*) FROM JUGADOR;
SELECT 'ESTADIO='       || COUNT(*) FROM ESTADIO;
SELECT 'PARTIDO='       || COUNT(*) FROM PARTIDO;
SELECT 'GRUPO='         || COUNT(*) FROM GRUPO;
SELECT 'EQUIPO_GRUPO='  || COUNT(*) FROM EQUIPO_GRUPO;

SPOOL OFF
EXIT
