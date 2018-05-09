DROP DATABASE IF EXISTS Organizer;
CREATE DATABASE Organizer;
USE Organizer;

SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS Usuari CASCADE;
DROP TABLE IF EXISTS Projecte CASCADE;
DROP TABLE IF EXISTS Propietari CASCADE;
DROP TABLE IF EXISTS Membre CASCADE;
DROP TABLE IF EXISTS Columna CASCADE;
DROP TABLE IF EXISTS Tasca CASCADE;
DROP TABLE IF EXISTS Etiqueta CASCADE;
DROP TABLE IF EXISTS Encarregat CASCADE;
SET FOREIGN_KEY_CHECKS=1;

CREATE TABLE Usuari(
	nom_usuari VARCHAR(255),
    correu VARCHAR(255),
    contrasenya VARCHAR(255),
    PRIMARY KEY (nom_usuari)
);

CREATE TABLE Projecte(
	id_projecte VARCHAR(255),
    nom_projecte VARCHAR(255),
    color VARCHAR(255),
    background VARCHAR(255),
    PRIMARY KEY (id_projecte)
);

CREATE TABLE Propietari(
	nom_propietari VARCHAR(255),
    id_projecte VARCHAR(255),
    PRIMARY KEY (nom_propietari, id_projecte),
    FOREIGN KEY (nom_propietari) REFERENCES Usuari(nom_usuari),
    FOREIGN KEY (id_projecte) REFERENCES Projecte(id_projecte)
);

CREATE TABLE Membre(
	nom_usuari VARCHAR(255),
    id_projecte VARCHAR(255),
    PRIMARY KEY (nom_usuari, id_projecte),
    FOREIGN KEY (nom_usuari) REFERENCES Usuari(nom_usuari),
    FOREIGN KEY (id_projecte) REFERENCES Projecte(id_projecte)
);

CREATE TABLE Columna(
	id_projecte VARCHAR(255),
    id_columna VARCHAR(255),
    nom_columna VARCHAR(255),
    posicio INT,
    PRIMARY KEY (id_columna),
    FOREIGN KEY (id_projecte) REFERENCES Projecte(id_projecte)
);

CREATE TABLE Tasca(
	id_columna VARCHAR(255),
    id_tasca VARCHAR(255),
    nom_tasca VARCHAR(255),
    descripcio VARCHAR(255),
    posicio INT,
    data_done DATE DEFAULT NULL,
    PRIMARY KEY (id_tasca),
    FOREIGN KEY (id_columna) REFERENCES Columna(id_columna)
);

CREATE TABLE Etiqueta(
    id_tasca VARCHAR(255),
    id_etiqueta VARCHAR(255),
    nom_etiqueta VARCHAR(255),
    color VARCHAR(255),
    PRIMARY KEY (id_etiqueta),
    FOREIGN KEY (id_tasca) REFERENCES Tasca(id_tasca)
);

CREATE TABLE Encarregat(
    id_tasca VARCHAR(255),
    id_encarregat VARCHAR(255),
    nom_encarregat VARCHAR(255),
    color VARCHAR(255),
    PRIMARY KEY (id_encarregat),
    FOREIGN KEY (id_tasca) REFERENCES Tasca(id_tasca)
);

INSERT INTO Usuari(nom_usuari, correu, contrasenya) VALUES('Willy', 'si', 'shit');

INSERT INTO Usuari(nom_usuari, correu, contrasenya) VALUES('Lactosito', 'bomilk@bomilk.com', 'aylmao');

INSERT INTO Usuari(nom_usuari, correu, contrasenya) VALUES('BernatPudent', 'friki', 'lsC');

INSERT INTO Usuari(nom_usuari, correu, contrasenya) VALUES('Posterman', 'sida', 'sh');

INSERT INTO Usuari(nom_usuari, correu, contrasenya) VALUES('LilAlbert', 'gang', 'do');


INSERT INTO Projecte(id_projecte, nom_projecte, color, background) VALUES('afga', 'p1', 'df', 'gds');


INSERT INTO Propietari(nom_propietari, id_projecte) VALUES('Willy', 'p1');


INSERT INTO Membre(nom_usuari, id_projecte) VALUES('Lactosito', 'p1');
INSERT INTO Membre(nom_usuari, id_projecte) VALUES ('Posterman','p1');


INSERT INTO Columna(id_projecte, id_columna, nom_columna, posicio) VALUES ('p1', 'c1', 'nomc1', 1); 


INSERT INTO Tasca(id_columna, id_tasca, nom_tasca, descripcio, posicio, data_done) VALUES ('c1', 't1', 'nomt1', 'mdmnvba<jfjd' , 1, 27/10/1998);
INSERT INTO Tasca(id_columna, id_tasca, nom_tasca, descripcio, posicio, data_done) VALUES ('c1', 't2', 'nomt2', 'ba<jfjd' , 2, null);
INSERT INTO Tasca(id_columna, id_tasca, nom_tasca, descripcio, posicio, data_done) VALUES ('c1', 't3', 'nomt3', 'ggfbsmdmnvba<jfjd' , 3, null);
INSERT INTO Tasca(id_columna, id_tasca, nom_tasca, descripcio, posicio, data_done) VALUES ('c1', 't4', 'nomt4', 'mdmnvgdasfnba<jfjd' , 4, 27/10/1998);
INSERT INTO Tasca(id_columna, id_tasca, nom_tasca, descripcio, posicio, data_done) VALUES ('c2', 't5', 'nomt5', 'mdmnvba<jfjd' , 5, null);
INSERT INTO Tasca(id_columna, id_tasca, nom_tasca, descripcio, posicio, data_done) VALUES ('c1', 't6', 'nomt6', 'mdmnvba<jfjd' , 6, 27/10/1998);
INSERT INTO Tasca(id_columna, id_tasca, nom_tasca, descripcio, posicio, data_done) VALUES ('c1', 't7', 'nomt7', 'mdmnvba<jfjd' , 7, 27/10/1998);
INSERT INTO Tasca(id_columna, id_tasca, nom_tasca, descripcio, posicio, data_done) VALUES ('c1', 't8', 'nomt8', 'mdmnvba<jfjd' , 8, 27/10/1998);
INSERT INTO Tasca(id_columna, id_tasca, nom_tasca, descripcio, posicio, data_done) VALUES ('c1', 't9', 'nomt9', 'mdmnvba<jfjd' , 9, 27/10/1998);
INSERT INTO Tasca(id_columna, id_tasca, nom_tasca, descripcio, posicio, data_done) VALUES ('c1', 't10', 'nomt10', 'mdmnvba<jfjd' , 10, 27/10/1998);
INSERT INTO Tasca(id_columna, id_tasca, nom_tasca, descripcio, posicio, data_done) VALUES ('c1', 't11', 'nomt11', 'mdmnvba<jfjd' , 11, 27/10/1998);
INSERT INTO Tasca(id_columna, id_tasca, nom_tasca, descripcio, posicio, data_done) VALUES ('c1', 't12', 'nomt12', 'mdmnvba<jfjd' , 12,27/10/1998);
INSERT INTO Tasca(id_columna, id_tasca, nom_tasca, descripcio, posicio, data_done) VALUES ('c1', 't13', 'nomt13', 'mdmnvba<jfjd' , 13, 27/10/1998);
INSERT INTO Tasca(id_columna, id_tasca, nom_tasca, descripcio, posicio, data_done) VALUES ('c1', 't14', 'nomt14', 'mdmnvba<jfjd' , 14, 27/10/1998);
INSERT INTO Tasca(id_columna, id_tasca, nom_tasca, descripcio, posicio, data_done) VALUES ('c1', 't15', 'nomt15', 'mdmnvba<jfjd' , 15, 27/10/1998);
INSERT INTO Tasca(id_columna, id_tasca, nom_tasca, descripcio, posicio, data_done) VALUES ('c1', 't16', 'nomt16', 'mdmnvba<jfjd' , 16, 27/10/1998);
INSERT INTO Tasca(id_columna, id_tasca, nom_tasca, descripcio, posicio, data_done) VALUES ('c1', 't17', 'nomt17', 'mdmnvba<jfjd' , 17, 27/10/1998);
INSERT INTO Tasca(id_columna, id_tasca, nom_tasca, descripcio, posicio, data_done) VALUES ('c1', 't18', 'nomt18', 'mdmnvba<jfjd' , 18, 27/10/1998);
INSERT INTO Tasca(id_columna, id_tasca, nom_tasca, descripcio, posicio, data_done) VALUES ('c1', 't19', 'nomt19', 'mdmnvba<jfjd' , 19, 27/10/1998);
INSERT INTO Tasca(id_columna, id_tasca, nom_tasca, descripcio, posicio, data_done) VALUES ('c1', 't20', 'nomt20', 'mdmnvba<jfjd' , 20, 27/10/1998);
INSERT INTO Tasca(id_columna, id_tasca, nom_tasca, descripcio, posicio, data_done) VALUES ('c1', 't21', 'nomt21', 'mdmnvba<jfjd' , 21, 27/10/1998);
INSERT INTO Tasca(id_columna, id_tasca, nom_tasca, descripcio, posicio, data_done) VALUES ('c1', 't22', 'nomt22', 'mdmnvba<jfjd' , 22, 27/10/1998);


INSERT INTO Etiqueta(id_tasca, id_etiqueta , nom_etiqueta, color) VALUES ('t1', 'e1', 'nome1', 'de');


INSERT INTO Encarregat(id_tasca, id_encarregat, nom_encarregat, color) VALUES ('t1', '123', 'Lactosito', 'k');



