/**
 * ProjectDBManager és el gestor que s'encarrega de fer totes les modificacions relacionades amb els projectes.
 *
 * @author  Albert Ferrando
 * @version 1.0
 */
package db.DataManagers;

import com.mysql.jdbc.Statement;
import db.DataBaseManager;
import model.project.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProjectDBManager {
    private Statement s;

    /**
     * Aquesta funció s'encarrega de retornar tots els projectes dels quals l'usuari que li passem sigui membre.
     * A més, els projectes contindran els membres de cada projecte retornat.
     *
     * @param userName Nom d'usuari de l'usuari del qual volem retornar els projectes en que és membre.
     */
    public ArrayList<Project> getProjectsOwner(String userName) {
        ArrayList<Project> projects = new ArrayList<>();
        ResultSet rs;
        try {
            s =(Statement) DataBaseManager.getInstance().getConnection().createStatement();
            //Seleccionem tots els projectes en que l'usuari sigui propietari.
            rs = s.executeQuery ("SELECT * FROM Projecte as p" +
                    " WHERE p.nom_propietari = '" + userName + "';");
            while(rs.next()) {
                if(rs.getString("id_projecte") != null) {
                    projects.add(new Project(rs.getString("id_projecte"), rs.getString("nom_projecte"),
                            rs.getString("color"), true));
                }
            }

            //Per cada projecte trobat:
            for (Project p: projects) {
                p.setMembersName(DataBaseManager.getInstance().getMemberDBManager().getMembers(p.getId()));
            }
        } catch (SQLException ex) {
            System.out.println("Problema al Recuperar les dades --> " + ex.getSQLState());
        }
        return projects;
    }

    /**
     * Aquesta funció s'encarrega de retornar tots els projectes dels quals l'usuari que li passem sigui propietari.
     * A més, els projectes contindran els membres de cada projecte retornat.
     *
     * @param userName Nom d'usuari de l'usuari del qual volem retornar els projectes en que és propietari.
     */
    public ArrayList<Project> getProjectsMember(String userName) {
        ArrayList<Project> projects = new ArrayList<>();
        ResultSet rs;

        try {
            s =(Statement) DataBaseManager.getInstance().getConnection().createStatement();
            //Seleccionem tots els projectes on l'usuari es membre.
            rs = s.executeQuery ("SELECT * FROM Projecte as p JOIN Membre as m ON p.id_projecte = m.id_projecte JOIN" +
                    " Usuari as u ON u.nom_usuari = m.nom_usuari WHERE u.nom_usuari = '" + userName + "' OR u.correu = '" + userName + "';");
            while(rs.next()) {
                if(rs.getString("id_projecte") != null) {
                    projects.add(new Project(rs.getString("id_projecte"), rs.getString("nom_projecte"),
                            rs.getString("color"), false));
                }
            }

            for (Project p: projects) {
                p.setMembersName(DataBaseManager.getInstance().getMemberDBManager().getMembers(p.getId()));
            }
        } catch (SQLException ex) {
            System.out.println("Problema al Recuperar les dades --> " + ex.getSQLState());
        }
        return projects;
    }

    /**
     * Aquesta funció s'encarrega de retornar el projecte que li demanem amb tots els elements interns d'aquest mateix.
     *
     * @param id_projecte Id del projecte que volem que se'ns retorni.
     */
    public Project getProject(String id_projecte) {
        Project project = new Project();
        ResultSet rs;
        try {
            s =(Statement) DataBaseManager.getInstance().getConnection().createStatement();
            rs = s.executeQuery ("SELECT * FROM Projecte WHERE id_projecte = '" + id_projecte + "';");
            rs.next();
            if(rs.getString("id_projecte") != null) {
                project.setId(rs.getString("id_projecte"));
                project.setName(rs.getString("nom_projecte"));
                project.setColorFromCode(rs.getString("color"));
                project.setOwnerName(rs.getString("nom_propietari"));
                BufferedImage myPicture;
                myPicture = ImageIO.read(new File(System.getProperty("user.dir") +
                        System.getProperty("file.separator") + "backgrounds" + System.getProperty("file.separator")
                + id_projecte));
                project.setBackground(myPicture);
            }
            project.setCategories(DataBaseManager.getInstance().getCategoryDBManager().getCategories(id_projecte));
        } catch (SQLException ex) {
            System.out.println("Problema al Recuperar les dades --> " + ex.getSQLState());
        } catch (IOException e) {
            BufferedImage myPicture = null;
            try {
                myPicture = ImageIO.read(new File(System.getProperty("user.dir") +
                        System.getProperty("file.separator") + "backgrounds" + System.getProperty("file.separator")
                        + "default.jpg"));
                project.setBackground(myPicture);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return project;
    }

    /**
     * Aquesta funció s'encarrega d'afegir un projecte en cas que no es trobi a la base de dades i, en cas contrari,
     * s'encarrega d'editar els elements del propi projecte. Aquesta funció no afegeix el propietari del projecte,
     * per a fer-ho s'ha d'usar la funció específica.
     *
     * @param projecte Projecte que volem afegir o editar.
     */
    public void addProject(Project projecte) {
        try {
            String query = "{CALL Organizer.AddProject(?,?,?)}";
            java.sql.CallableStatement stmt = DataBaseManager.getInstance().getConnection().prepareCall(query);
            stmt.setString(1, projecte.getName());
            stmt.setString(2, projecte.getHexColor());
            stmt.setString(3, projecte.getId());
            stmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Problema al Recuperar les dades --> " + e.getSQLState());
        }
    }

    /**
     * Aquesta funció s'encarrega d'esborrar un projecte i tots els elements que aquest conté.
     *
     * @param id_projecte Id del projecte que volem eliminar.
     */
    public void deleteProject(String id_projecte) {
        String query = "{CALL Organizer.deleteProject(?)}";
        java.sql.CallableStatement stmt;
        try {
            stmt = DataBaseManager.getInstance().getConnection().prepareCall(query);
            stmt.setString(1, id_projecte);
            stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Aquesta funció s'encarrega d'assignar a un projecte un propietari.
     *
     * @param id Id del projecte al qual volem afegir un propietari.
     * @param username Nom d'usuari del propietari.
     */
    public void addProjectOwner(String id, String username) {
        String query = "{CALL Organizer.addProjectOwner(?,?)}";
        java.sql.CallableStatement stmt;
        try {
            stmt = DataBaseManager.getInstance().getConnection().prepareCall(query);
            stmt.setString(1, id);
            stmt.setString(2, username);
            stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
