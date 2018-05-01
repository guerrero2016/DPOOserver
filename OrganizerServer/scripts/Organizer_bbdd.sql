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
    PRIMARY KEY (id_projecte, id_columna),
    FOREIGN KEY (id_projecte) REFERENCES Projecte(id_projecte)
);

CREATE TABLE Tasca(
	id_projecte VARCHAR(255),
	id_columna VARCHAR(255),
    id_tasca VARCHAR(255),
    nom_tasca VARCHAR(255),
    descripcio VARCHAR(255),
    posicio INT,
    PRIMARY KEY (id_projecte, id_columna, id_tasca),
    FOREIGN KEY (id_projecte, id_columna) REFERENCES Columna(id_projecte, id_columna)
);

CREATE TABLE Etiqueta(
	id_projecte VARCHAR(255),
	id_columna VARCHAR(255),
    id_tasca VARCHAR(255),
    id_etiqueta VARCHAR(255),
    nom_etiqueta VARCHAR(255),
    color VARCHAR(255),
    PRIMARY KEY (id_projecte, id_columna, id_tasca, id_etiqueta),
    FOREIGN KEY (id_projecte, id_columna, id_tasca) REFERENCES Tasca(id_projecte, id_columna, id_tasca)
);

CREATE TABLE Encarregat(
	id_projecte VARCHAR(255),
	id_columna VARCHAR(255),
    id_tasca VARCHAR(255),
    id_encarregat VARCHAR(255),
    nom_encarregat VARCHAR(255),
    color VARCHAR(255),
    PRIMARY KEY (id_projecte, id_columna, id_tasca, id_encarregat),
    FOREIGN KEY (id_projecte, id_columna, id_tasca) REFERENCES Tasca(id_projecte, id_columna, id_tasca)
);