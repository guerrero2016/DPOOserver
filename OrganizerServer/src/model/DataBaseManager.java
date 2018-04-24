package model;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import model.project.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DataBaseManager {
    private static DataBaseManager ourInstance = new DataBaseManager();
    private static Connection connection;
    private static Statement s;

    public static DataBaseManager getInstance() {
        return ourInstance;
    }

    private DataBaseManager() {
    }

    public static void setConnection(Connection connection) {
        DataBaseManager.connection = connection;
        try {
            s = (Statement) connection.createStatement();
        } catch (SQLException e) {
            System.out.println("Problema al Inserir --> " + e.getSQLState());
        }
    }

    //Funció validada.
    public static int RegistrarUsuari(String nom_usuari, String correu, String contrasenya) {
            ResultSet rs;
            try {
                String query = "{CALL Organizer.RegistrarUsuari(?,?,?)}";
                java.sql.CallableStatement stmt = connection.prepareCall(query);
                stmt.setString(1, nom_usuari);
                stmt.setString(2, correu);
                stmt.setString(3, contrasenya);
                rs = stmt.executeQuery();
                rs.next();
                if(rs.getString(1).equals("NO1")) {
                    return 1;
                } else if(rs.getString(1).equals("NO2")) {
                    return 2;
                } else  {
                    return 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 1;
    }

    //Funció validada
    public static boolean IniciarSessio(String nom_correu, String password) {
        ResultSet rs;
        try {
            String query = "{CALL Organizer.IniciarSessio(?,?)}";
            java.sql.CallableStatement stmt = connection.prepareCall(query);
            stmt.setString(1, nom_correu);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            rs.next();
            if(rs.getString(1).equals("NO")) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<Project> getProjectsInfo(String userName) {
        ArrayList<Project> projects = new ArrayList<>();
        ResultSet rs = null;
        try {
            s =(Statement) connection.createStatement();
            rs = s.executeQuery ("SELECT * FROM Projecte as p JOIN Propietari as o ON p.id_projecte = o.id_projecte JOIN" +
                    " Usuari as u ON u.nom_usuari = o.nom_propietari WHERE u.nom_usuari = " + userName + " OR u.correu = " + userName + ";");
            while(rs.next()) {
                if(rs.getString("id_projecte") != null) {
                    projects.add(new Project(rs.getString("id_projecte"), rs.getString("nom_projecte"),
                            rs.getString("color"), rs.getString("background"), true));
                }
            }
            s =(Statement) connection.createStatement();
            rs = s.executeQuery ("SELECT * FROM Projecte as p JOIN Membre as m ON p.id_projecte = m.id_projecte JOIN" +
                    " Usuari as u ON u.nom_usuari = m.nom_usuari WHERE u.nom_usuari = " + userName + " OR u.correu = " + userName + ";");
            while(rs.next()) {
                if(rs.getString("id_projecte") != null) {
                    projects.add(new Project(rs.getString("id_projecte"), rs.getString("nom_projecte"),
                            rs.getString("color"), rs.getString("background"), false));
                }
            }

            for (Project p: projects) {
                p.setMembersName(DataBaseManager.getMembers(p.getId(), userName));
            }
        } catch (SQLException ex) {
            System.out.println("Problema al Recuperar les dades --> " + ex.getSQLState());
        }
        return projects;
    }

    private static ArrayList<String> getMembers(String id_projecte, String userName) {
        ArrayList<String> members = new ArrayList<>();
        ResultSet rs;
        try {
            s =(Statement) connection.createStatement();
            rs = s.executeQuery ("SELECT * FROM Membre WHERE id_projecte = " + id_projecte + " AND nom_usuari != '" + userName + "';");
            while(rs.next()) {
                if(rs.getString("nom_usuari") != null) {
                    members.add(rs.getString("nom_usuari"));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Problema al Recuperar les dades --> " + ex.getSQLState());
        }
        return members;
    }

    public static String getUser(String userName) {
        ResultSet rs = null;
        String query = "{CALL Organizer.GetUser(?)}";
        try {
            java.sql.CallableStatement stmt = connection.prepareCall(query);
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            rs.next();
            return rs.getString(1);
        } catch (SQLException e) {
            System.out.println("Problema al Recuperar les dades --> " + e.getSQLState());
        }
        return "";
    }

    public static Project getProject(String id_projecte) {
        Project project = new Project();
        ResultSet rs;
        try {
            s =(Statement) connection.createStatement();
            rs = s.executeQuery ("SELECT * FROM Projecte WHERE id_projecte = '" + id_projecte + "';");
            rs.next();
            if(rs.getString("nom_usuari") != null) {
                project.setName(rs.getString("nom_projecte"));
                project.setColor(rs.getString("color"));
                project.setBackground(rs.getString("background"));
            }
            project.setCategories(DataBaseManager.getCategories(id_projecte));
        } catch (SQLException ex) {
            System.out.println("Problema al Recuperar les dades --> " + ex.getSQLState());
        }
        return project;
    }

    private static ArrayList<Category> getCategories(String id_projecte) {
        ArrayList<Category> categories = new ArrayList<>();
        ResultSet rs;
        try {
            s =(Statement) connection.createStatement();
            rs = s.executeQuery ("SELECT * FROM Columna WHERE id_projecte = '" + id_projecte + "';");
            while(rs.next()) {
                if (rs.getString("nom_columna") != null) {
                    categories.add(new Category(rs.getString("nom_columna"), rs.getInt("posicio"),
                            DataBaseManager.getTasks(id_projecte, rs.getString("nom_columna"))));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Problema al Recuperar les dades --> " + ex.getSQLState());
        }
        return categories;
    }

    private static ArrayList<Task> getTasks(String id_projecte, String nom_columna) {
        ArrayList<Task> tasks = new ArrayList<>();
        ResultSet rs;
        try {
            s =(Statement) connection.createStatement();
            rs = s.executeQuery ("SELECT * FROM Tasca WHERE id_projecte = '" + id_projecte + "' AND nom_columna = '" + nom_columna + "';");
            while(rs.next()) {
                if (rs.getString("nom_columna") != null) {
                    tasks.add(new Task(rs.getString("nom_tasca"), rs.getInt("posicio"),
                            rs.getString("descripcio"), DataBaseManager.getTags(id_projecte, nom_columna, rs.getString("nom_tasca")),
                            DataBaseManager.getEncarregats(id_projecte, nom_columna, rs.getString("nom_tasca"))));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Problema al Recuperar les dades --> " + ex.getSQLState());
        }
        return tasks;
    }

    private static ArrayList<Tag> getTags(String id_projecte, String nom_columna, String nom_tasca) {
        ArrayList<Tag> tags = new ArrayList<>();
        ResultSet rs;
        try {
            s =(Statement) connection.createStatement();
            rs = s.executeQuery ("SELECT * FROM Etiqueta WHERE id_projecte = '" + id_projecte + "' AND nom_columna = '" + nom_columna + "" +
                    " AND nom_tasca = '" + nom_tasca + "';");
            while(rs.next()) {
                if (rs.getString("nom_columna") != null) {
                    tags.add(new Tag(rs.getString("nom_etiqueta"), rs.getString("color")));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Problema al Recuperar les dades --> " + ex.getSQLState());
        }
        return tags;
    }

    private static ArrayList<Encarregat> getEncarregats(String id_projecte, String nom_columna, String nom_tasca) {
        ArrayList<Encarregat> encarregats = new ArrayList<>();
        ResultSet rs;
        try {
            s =(Statement) connection.createStatement();
            rs = s.executeQuery ("SELECT * FROM Etiqueta WHERE id_projecte = '" + id_projecte + "' AND nom_columna = '" + nom_columna + "" +
                    " AND nom_tasca = '" + nom_tasca + "';");
            while(rs.next()) {
                if (rs.getString("nom_columna") != null) {
                    encarregats.add(new Encarregat(rs.getString("nom_encarregat"), rs.getString("color")));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Problema al Recuperar les dades --> " + ex.getSQLState());
        }
        return encarregats;
    }

    public static boolean addProject(Project projecte) {
        ResultSet rs;
        try {
            String query = "{CALL Organizer.AddProject(?,?,?,?)}";
            java.sql.CallableStatement stmt = connection.prepareCall(query);
            stmt.setString(1, projecte.getName());
            stmt.setString(2, projecte.getColor());
            stmt.setString(3, projecte.getId());
            stmt.setString(4, projecte.getBackground());
            rs = stmt.executeQuery();
            rs.next();
            if(rs.getString(1).equals("NO")) {
                return false;
            }
            for(Category c: projecte.getCategories()) {
                if(!DataBaseManager.addCategory(c, projecte.getId())) {
                    return false;
                }
                for(Task t: c.getTasks()) {
                    if(!DataBaseManager.addTask(t, projecte.getId(), c.getName())) {
                        return false;
                    }
                    for(Tag tag: t.getTags()) {
                        if(!DataBaseManager.addTag(tag, projecte.getId(), c.getName(), t.getName())) {
                            return false;
                        }
                    }
                    for(Encarregat e: t.getEncarregats()) {
                        if(!DataBaseManager.addEncarregat(e, projecte.getId(), c.getName(), t.getName())) {
                            return false;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Problema al Recuperar les dades --> " + e.getSQLState());
        }
        return true;
    }

    public static boolean addEncarregat(Encarregat e, String id_projecte, String nom_columna, String nom_tasca) {
        ResultSet rs;
        String query = "{CALL Organizer.AddEncarregat(?,?,?,?,?)}";
        java.sql.CallableStatement stmt = null;
        try {
            stmt = connection.prepareCall(query);
            stmt.setString(1, id_projecte);
            stmt.setString(2, nom_columna);
            stmt.setString(3, nom_tasca);
            stmt.setString(4, e.getName());
            stmt.setString(5, e.getColor());
            rs = stmt.executeQuery();
            rs.next();
            if(rs.getString(1).equals("NO")) {
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("Problema al Recuperar les dades --> " + ex.getSQLState());
        }
        return true;
    }

    public static boolean addTag(Tag tag, String id_projecte, String nom_columna, String nom_tasca) {
        ResultSet rs;
        String query = "{CALL Organizer.AddTag(?,?,?,?,?)}";
        java.sql.CallableStatement stmt = null;
        try {
            stmt = connection.prepareCall(query);
            stmt.setString(1, id_projecte);
            stmt.setString(2, nom_columna);
            stmt.setString(3, nom_tasca);
            stmt.setString(4, tag.getName());
            stmt.setString(5, tag.getColor());
            rs = stmt.executeQuery();
            rs.next();
            if(rs.getString(1).equals("NO")) {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Problema al Recuperar les dades --> " + e.getSQLState());
        }
        return true;
    }

    public static boolean addTask(Task t, String id_projecte, String nom_columna) {
        ResultSet rs;
        String query = "{CALL Organizer.AddTask(?,?,?,?,?)}";
        java.sql.CallableStatement stmt = null;
        try {
            stmt = connection.prepareCall(query);
            stmt.setString(1, id_projecte);
            stmt.setString(2, nom_columna);
            stmt.setString(3, t.getName());
            stmt.setString(4, t.getDescription());
            stmt.setInt(5, t.getOrder());
            rs = stmt.executeQuery();
            rs.next();
            if(rs.getString(1).equals("NO")) {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Problema al Recuperar les dades --> " + e.getSQLState());
        }
        return true;
    }

    public static boolean addCategory(Category c, String id_projecte) {
        ResultSet rs;
        String query = "{CALL Organizer.AddCategory(?,?,?)}";
        java.sql.CallableStatement stmt = null;
        try {
            stmt = connection.prepareCall(query);
            stmt.setString(1, c.getName());
            stmt.setString(2, id_projecte);
            stmt.setInt(3, c.getOrder());
            rs = stmt.executeQuery();
            rs.next();
            if(rs.getString(1).equals("NO")) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void deleteProject(String id_projecte) {
        deleteAllTags(id_projecte);
        deleteAllEncarregats(id_projecte);
        deleteAllTasks(id_projecte);
        deleteAllCategories(id_projecte);
        deleteAllProject(id_projecte);
    }

    private static void deleteAllProject(String id_projecte) {
        try {
            s =(Statement) connection.createStatement();
            s.executeQuery ("DELETE FROM Projecte WHERE id_projecte = '" + id_projecte + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }

    private static void deleteAllCategories(String id_projecte) {
        try {
            s =(Statement) connection.createStatement();
            s.executeQuery ("DELETE FROM Columna WHERE id_projecte = '" + id_projecte + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }

    private static void deleteAllTasks(String id_projecte) {
        try {
            s =(Statement) connection.createStatement();
            s.executeQuery ("DELETE FROM Tasca WHERE id_projecte = '" + id_projecte + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }

    private static void deleteAllEncarregats(String id_projecte) {
        try {
            s =(Statement) connection.createStatement();
            s.executeQuery ("DELETE FROM Encarregat WHERE id_projecte = '" + id_projecte + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }

    private static void deleteAllTags(String id_projecte) {
        try {
            s =(Statement) connection.createStatement();
            s.executeQuery ("DELETE FROM Etiqueta WHERE id_projecte = '" + id_projecte + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }

    public static void deleteCategory(String id_projecte, String id_categoria) {
        deleteEncarregats(id_projecte, id_categoria);
        deleteTags(id_projecte, id_categoria);
        deleteTasks(id_projecte, id_categoria);
        deleteAllCategory(id_projecte, id_categoria);
    }

    private static void deleteAllCategory(String id_projecte, String id_categoria) {
        try {
            s =(Statement) connection.createStatement();
            s.executeQuery ("DELETE FROM Columna WHERE id_projecte = '" + id_projecte + "' AND" +
                    " nom_columna = '" + id_categoria + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }

    private static void deleteTasks(String id_projecte, String id_categoria) {
        try {
            s.executeQuery ("DELETE FROM Tasca WHERE id_projecte = '" + id_projecte + "' AND" +
                    " nom_columna = '" + id_categoria + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }

    private static void deleteTags(String id_projecte, String id_categoria) {
        try {
            s =(Statement) connection.createStatement();
            s.executeQuery ("DELETE FROM Etiqueta WHERE id_projecte = '" + id_projecte + "' AND" +
                    " nom_columna = '" + id_categoria + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }

    private static void deleteEncarregats(String id_projecte, String id_categoria) {
        try {
            s =(Statement) connection.createStatement();
            s.executeQuery ("DELETE FROM Encarregat WHERE id_projecte = '" + id_projecte + "' AND" +
                    " nom_columna = '" + id_categoria + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }

    public static void deleteTag(String id_projecte, String nom_columna, String nom_tasca, String nom_tag) {
        try {
            s =(Statement) connection.createStatement();
            s.executeQuery ("DELETE FROM Etiqueta WHERE id_projecte = '" + id_projecte + "' AND" +
                    " nom_columna = '" + nom_columna + "' AND nom_tasca = '" + nom_tasca + "' AND" +
                    " nom_etiqueta = '" + nom_tag + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }

    public static void deleteEncarregat(String id_projecte, String nom_columna, String nom_tasca, String nom_encarregat) {
        try {
            s =(Statement) connection.createStatement();
            s.executeQuery ("DELETE FROM Encarregat WHERE id_projecte = '" + id_projecte + "' AND" +
                    " nom_columna = '" + nom_columna + "' AND nom_tasca = '" + nom_tasca + "' AND" +
                    " nom_etiqueta = '" + nom_encarregat + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }

    public static void deleteTask(String id_projecte, String nom_columna, String nom_tasca) {
        deleteTags(id_projecte, nom_columna, nom_tasca);
        deleteEncarregats(id_projecte, nom_columna, nom_tasca);
        deleteAllTask(id_projecte, nom_columna, nom_tasca);
    }

    private static void deleteAllTask(String id_projecte, String nom_columna, String nom_tasca) {
        try {
            s =(Statement) connection.createStatement();
            s.executeQuery ("DELETE FROM Tasca WHERE id_projecte = '" + id_projecte + "' AND" +
                    " nom_columna = '" + nom_columna + "' AND nom_tasca = '" + nom_tasca + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }

    private static void deleteEncarregats(String id_projecte, String nom_columna, String nom_tasca) {
        try {
            s =(Statement) connection.createStatement();
            s.executeQuery ("DELETE FROM Encarregat WHERE id_projecte = '" + id_projecte + "' AND" +
                    " nom_columna = '" + nom_columna + "' AND nom_tasca = '" + nom_tasca + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }

    private static void deleteTags(String id_projecte, String nom_columna, String nom_tasca) {
        try {
            s =(Statement) connection.createStatement();
            s.executeQuery ("DELETE FROM Etiqueta WHERE id_projecte = '" + id_projecte + "' AND" +
                    " nom_columna = '" + nom_columna + "' AND nom_tasca = '" + nom_tasca + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }
}