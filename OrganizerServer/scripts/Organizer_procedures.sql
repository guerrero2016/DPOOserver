USE Organizer;
SET SQL_SAFE_UPDATES = 1;

DELIMITER $$
DROP PROCEDURE IF EXISTS Organizer.iniciarSessio $$
CREATE PROCEDURE Organizer.iniciarSessio (IN nom_correu VARCHAR(255), IN pass VARCHAR(255))
BEGIN
	IF nom_correu IN (SELECT nom_usuari FROM Usuari) THEN
		IF pass = (SELECT contrasenya FROM Usuari WHERE nom_usuari = nom_correu) THEN
			SELECT 'OK';
		ELSE
			SELECT 'NO';
        END IF;
	ELSEIF nom_correu IN (SELECT correu FROM Usuari) THEN
		IF pass = (SELECT contrasenya FROM Usuari WHERE correu = nom_correu) THEN
			SELECT 'OK';
		ELSE
			SELECT 'NO';
        END IF;
	ELSE
		SELECT 'NO';
    END IF;
END $$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS Organizer.RegistrarUsuari $$
CREATE PROCEDURE Organizer.RegistrarUsuari (IN nom VARCHAR(255), IN correu VARCHAR(255), IN pass VARCHAR(255))
BEGIN
	IF nom IN (SELECT nom_usuari FROM Usuari) THEN
		SELECT 'NO1';
	ELSEIF correu IN (SELECT u.correu FROM Usuari as u) THEN
		SELECT 'NO2';
	ELSE
		INSERT INTO Usuari(nom_usuari, correu, contrasenya) VALUES (nom, correu, pass);
        SELECT 'SI';
    END IF;
END $$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS Organizer.GetUser $$
CREATE PROCEDURE Organizer.GetUser (IN user_or_email VARCHAR(255))
BEGIN
	IF user_or_email IN (SELECT nom_usuari FROM Usuari) THEN
		SELECT user_or_email;
	ELSE
		SELECT nom_usuari FROM Usuari
        WHERE correu = user_or_email;
    END IF;
END $$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS Organizer.AddProject $$
CREATE PROCEDURE Organizer.AddProject (IN nom_in VARCHAR(255), IN color_in VARCHAR(255),
	IN id_in VARCHAR(255))
BEGIN
	IF id_in IN (SELECT id_projecte FROM Projecte) THEN
		UPDATE Projecte
			SET nom_projecte = nom_in, color = color_in
            WHERE id_projecte = id_in;
	ELSE
		INSERT INTO Projecte(id_projecte, nom_projecte, color) VALUES (id_in, nom_in, color_in);
    END IF;
END $$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS Organizer.AddCategory $$
CREATE PROCEDURE Organizer.AddCategory (IN id_p_in VARCHAR(255), IN id_in VARCHAR(255),
	IN nom_in VARCHAR(255), IN pos_in INT)
BEGIN
	IF (id_p_in, id_in) IN (SELECT id_projecte, id_columna FROM Columna) THEN
		UPDATE Columna
			SET nom_columna = nom_in, posicio = pos_in
            WHERE id_projecte = id_p_in AND id_columna = id_in;
	ELSE
		INSERT INTO Columna(id_projecte, id_columna, nom_columna, posicio)
        VALUES (id_p_in, id_in, nom_in, pos_in);
    END IF;
END $$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS Organizer.AddTask $$
CREATE PROCEDURE Organizer.AddTask (IN id_c_in VARCHAR(255), IN id_in VARCHAR(255),
	IN nom_in VARCHAR(255), IN des_in TEXT, IN pos_in INT)
BEGIN
	IF (id_c_in, id_in) IN (SELECT id_columna, id_tasca FROM Tasca) THEN
		UPDATE Tasca
			SET nom_tasca = nom_in, descripcio = des_in, posicio = pos_in
            WHERE id_columna = id_c_in AND id_tasca = id_in;
	ELSE
		INSERT INTO Tasca(id_columna, id_tasca, nom_tasca, descripcio, posicio)
        VALUES (id_c_in, id_in, nom_in, des_in, pos_in);
    END IF;
END $$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS Organizer.AddTag $$
CREATE PROCEDURE Organizer.AddTag (IN id_t_in VARCHAR(255), IN id_in VARCHAR(255),
	IN nom_in VARCHAR(255), IN color_in VARCHAR(255))
BEGIN
	IF (id_t_in, id_in) IN (SELECT id_tasca, id_etiqueta FROM Etiqueta) THEN
		UPDATE Etiqueta
			SET nom_etiqueta = nom_in, color = color_in
            WHERE id_tasca = id_t_in AND id_etiqueta = id_in;
	ELSE
		INSERT INTO Etiqueta(id_tasca, id_etiqueta, nom_etiqueta, color) VALUES (id_t_in, id_in, nom_in, color_in);
    END IF;
END $$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS Organizer.AddEncarregat $$
CREATE PROCEDURE Organizer.AddEncarregat (IN nom_u_in VARCHAR(255), IN id_t_in VARCHAR(255))
BEGIN
	IF (id_t_in, nom_u_in) NOT IN (SELECT id_tasca, nom_usuari FROM Tasca_Usuari) THEN
		INSERT INTO Tasca_Usuari(id_tasca, nom_usuari) VALUES (id_t_in, nom_u_in);
    END IF;
END $$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS Organizer.SwapTask $$
CREATE PROCEDURE Organizer.SwapTask (IN id_t_in VARCHAR(255), IN pos_in INT)
BEGIN
	IF (id_t_in) IN (SELECT id_tasca FROM Tasca) THEN
		UPDATE Tasca
			SET posicio = pos_in
			WHERE id_tasca = id_t_in;
	END IF;
END $$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS Organizer.SwapCategory $$
CREATE PROCEDURE Organizer.SwapCategory (IN id_c1_in VARCHAR(255),
	IN id_c2_in VARCHAR(255), IN pos1_in INT, IN pos2_in INT)
BEGIN
	UPDATE Columna
		SET posicio = pos1_in
		WHERE id_columna = id_c2_in;
	UPDATE Columna
		SET posicio = pos2_in
		WHERE id_columna = id_c1_in;
END $$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS Organizer.requestUserEvolution $$
CREATE PROCEDURE Organizer.requestUserEvolution (IN user_in VARCHAR(255), IN date_in DATE)
BEGIN
	SELECT data_done FROM Tasca as t JOIN Tasca_Usuari as tu ON t.id_tasca = tu.id_tasca
    WHERE tu.nom_usuari = user_in AND data_done >= date_in;
END $$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS Organizer.requestTop10 $$
CREATE PROCEDURE Organizer.requestTop10 ()
BEGIN
	SELECT nom_usuari, COUNT(*) as tasques_per_fer
    FROM Tasca as t JOIN Tasca_Usuari as tu ON t.id_tasca = tu.id_tasca
    WHERE data_done IS null
    GROUP BY nom_usuari
    ORDER BY tasques_per_fer DESC LIMIT 10;
END $$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS Organizer.deleteProject $$
CREATE PROCEDURE Organizer.deleteProject (IN id_in VARCHAR(255))
BEGIN
	DELETE e, tu, t, c, p
    FROM Projecte as p LEFT JOIN Columna as c USING (id_projecte)
    LEFT JOIN Tasca as t USING (id_columna) LEFT JOIN Tasca_Usuari as tu USING (id_tasca)
    LEFT JOIN Etiqueta as e ON e.id_tasca = t.id_tasca
    WHERE p.id_projecte = id_in;
END $$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS Organizer.deleteCategory $$
CREATE PROCEDURE Organizer.deleteCategory (IN id_in VARCHAR(255))
BEGIN
	DELETE tu, e, t, c
    FROM Columna as c LEFT JOIN Tasca as t USING (id_columna) LEFT JOIN Tasca_Usuari as tu USING (id_tasca)
    LEFT JOIN Etiqueta as e ON e.id_tasca = t.id_tasca
    WHERE c.id_columna = id_in;
END $$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS Organizer.deleteTask $$
CREATE PROCEDURE Organizer.deleteTask (IN id_in VARCHAR(255))
BEGIN
	DELETE tu, e, t
    FROM Tasca as t LEFT JOIN Tasca_Usuari as tu USING (id_tasca)
    LEFT JOIN Etiqueta as e ON e.id_tasca = t.id_tasca
    WHERE t.id_tasca = id_in;
END $$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS Organizer.taskDone $$
CREATE PROCEDURE Organizer.taskDone (IN id_in VARCHAR(255))
BEGIN
	UPDATE Tasca
		SET data_done = current_date()
		WHERE id_tasca = id_in;
END $$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS Organizer.taskNotDone $$
CREATE PROCEDURE Organizer.taskNotDone (IN id_in VARCHAR(255))
BEGIN
	UPDATE Tasca
		SET data_done = null
		WHERE id_tasca = id_in;
END $$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS Organizer.addProjectOwner $$
CREATE PROCEDURE Organizer.addProjectOwner (IN id_in VARCHAR(255), IN nom_in VARCHAR(255))
BEGIN
	UPDATE Projecte
			SET nom_propietari = nom_in
            WHERE id_projecte = id_in;
END $$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS Organizer.addMember $$
CREATE PROCEDURE Organizer.addMember (IN id_in VARCHAR(255), IN nom_in VARCHAR(255))
BEGIN
	IF (id_in, nom_in) NOT IN (SELECT id_projecte, nom_usuari FROM Membre) THEN
		INSERT INTO Membre(id_projecte, nom_usuari) VALUES (id_in, nom_in);
    END IF;
END $$
DELIMITER ;