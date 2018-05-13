package db.DataManagers;

import com.mysql.jdbc.Statement;
import db.DataBaseManager;
import model.project.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProjectDBManager {
    private Statement s;

    public ArrayList<Project> getProjectsOwner(String userName) {
        ArrayList<Project> projects = new ArrayList<>();
        ResultSet rs;

        try {
            s =(Statement) DataBaseManager.getConnection().createStatement();
            //Seleccionem tots els projectes en que l'usuari sigui propietari.
            rs = s.executeQuery ("SELECT * FROM Projecte as p JOIN Propietari as o ON p.id_projecte = o.id_projecte JOIN" +
                    " Usuari as u ON u.nom_usuari = o.nom_propietari WHERE u.nom_usuari = '" + userName + "' OR u.correu = '" + userName + "';");
            while(rs.next()) {
                if(rs.getString("id_projecte") != null) {
                    projects.add(new Project(rs.getString("id_projecte"), rs.getString("nom_projecte"),
                            rs.getString("color"), rs.getString("background"), true));
                }
            }

            //Per cada projecte trobat:
            for (Project p: projects) {
                p.setMembersName(DataBaseManager.getMemberDBManager().getMembers(p.getId()));
            }
        } catch (SQLException ex) {
            System.out.println("Problema al Recuperar les dades --> " + ex.getSQLState());
        }
        return projects;
    }

    public ArrayList<Project> getProjectsMember(String userName) {
        ArrayList<Project> projects = new ArrayList<>();
        ResultSet rs;

        try {
            s =(Statement) DataBaseManager.getConnection().createStatement();
            //Seleccionem tots els projectes on l'usuari es membre.
            rs = s.executeQuery ("SELECT * FROM Projecte as p JOIN Membre as m ON p.id_projecte = m.id_projecte JOIN" +
                    " Usuari as u ON u.nom_usuari = m.nom_usuari WHERE u.nom_usuari = '" + userName + "' OR u.correu = '" + userName + "';");
            while(rs.next()) {
                if(rs.getString("id_projecte") != null) {
                    projects.add(new Project(rs.getString("id_projecte"), rs.getString("nom_projecte"),
                            rs.getString("color"), rs.getString("background"), false));
                }
            }

            for (Project p: projects) {
                p.setMembersName(DataBaseManager.getMemberDBManager().getMembers(p.getId()));
            }
        } catch (SQLException ex) {
            System.out.println("Problema al Recuperar les dades --> " + ex.getSQLState());
        }
        return projects;
    }

    public Project getProject(String id_projecte) {
        Project project = new Project();
        ResultSet rs;
        try {
            s =(Statement) DataBaseManager.getConnection().createStatement();
            rs = s.executeQuery ("SELECT * FROM Projecte WHERE id_projecte = '" + id_projecte + "';");
            rs.next();
            if(rs.getString("id_projecte") != null) {
                project.setName(rs.getString("nom_projecte"));
                project.setColor(rs.getString("color"));
                project.setBackground(rs.getString("background"));
            }
            project.setCategories(DataBaseManager.getCategoryDBManager().getCategories(id_projecte));
        } catch (SQLException ex) {
            System.out.println("Problema al Recuperar les dades --> " + ex.getSQLState());
        }
        return project;
    }

    //Funció validada
    public void addProject(Project projecte) {
        try {
            String query = "{CALL Organizer.AddProject(?,?,?,?)}";
            java.sql.CallableStatement stmt = DataBaseManager.getConnection().prepareCall(query);
            stmt.setString(1, projecte.getName());
            stmt.setString(2, projecte.getColor());
            stmt.setString(3, projecte.getId());
            stmt.setString(4, projecte.getBackground());
            stmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Problema al Recuperar les dades --> " + e.getSQLState());
        }
    }

    public void deleteProject(String id_projecte) {
        String query = "{CALL Organizer.deleteProject(?)}";
        java.sql.CallableStatement stmt;
        try {
            stmt = DataBaseManager.getConnection().prepareCall(query);
            stmt.setString(1, id_projecte);
            stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Funció validada
    public void addProjectOwner(String id, String username) {
        String query = "{CALL Organizer.addProjectOwner(?,?)}";
        java.sql.CallableStatement stmt;
        try {
            stmt = DataBaseManager.getConnection().prepareCall(query);
            stmt.setString(1, id);
            stmt.setString(2, username);
            stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
