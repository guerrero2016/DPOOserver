package model;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import model.project.*;
import model.user.Top10NeededData;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DataBaseManager {
    private static DataBaseManager ourInstance = new DataBaseManager();
    private static Connection connection;
    private static Statement s;

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

    //Funció validada
    public static ArrayList<Project> getProjectsInfo(String userName) {
        ArrayList<Project> projects = new ArrayList<>();
        ResultSet rs = null;

        try {
            s =(Statement) connection.createStatement();
            //Seleccionem tots els projectes en que l'usuari sigui propietari.
            rs = s.executeQuery ("SELECT * FROM Projecte as p JOIN Propietari as o ON p.id_projecte = o.id_projecte JOIN" +
                    " Usuari as u ON u.nom_usuari = o.nom_propietari WHERE u.nom_usuari = '" + userName + "' OR u.correu = '" + userName + "';");
            while(rs.next()) {
                if(rs.getString("id_projecte") != null) {
                    projects.add(new Project(rs.getString("id_projecte"), rs.getString("nom_projecte"),
                            rs.getString("color"), rs.getString("background"), true));
                }
            }

            s =(Statement) connection.createStatement();
            //Seleccionem tots els projectes on l'usuari es membre.
            rs = s.executeQuery ("SELECT * FROM Projecte as p JOIN Membre as m ON p.id_projecte = m.id_projecte JOIN" +
                    " Usuari as u ON u.nom_usuari = m.nom_usuari WHERE u.nom_usuari = '" + userName + "' OR u.correu = '" + userName + "';");
            while(rs.next()) {
                if(rs.getString("id_projecte") != null) {
                    projects.add(new Project(rs.getString("id_projecte"), rs.getString("nom_projecte"),
                            rs.getString("color"), rs.getString("background"), false));
                }
            }

            //Per cada projecte trobat:
            for (Project p: projects) {
                p.setMembersName(DataBaseManager.getMembers(p.getId(), userName));
            }
        } catch (SQLException ex) {
            System.out.println("Problema al Recuperar les dades --> " + ex.getSQLState());
        }
        return projects;
    }

    //Funció validada
    private static ArrayList<String> getMembers(String id_projecte, String userName) {
        ArrayList<String> members = new ArrayList<>();
        ResultSet rs;

        try {
            s =(Statement) connection.createStatement();
            rs = s.executeQuery ("SELECT * FROM Membre WHERE id_projecte = '" + id_projecte + "' AND nom_usuari != '" + userName + "';");
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

    //Funció validada
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

    //Funció validada
    public static Project getProject(String id_projecte) {
        Project project = new Project();
        ResultSet rs;
        try {
            s =(Statement) connection.createStatement();
            rs = s.executeQuery ("SELECT * FROM Projecte WHERE id_projecte = '" + id_projecte + "';");
            rs.next();
            if(rs.getString("id_projecte") != null) {
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

    //Funció validada
    private static ArrayList<Category> getCategories(String id_projecte) {
        ArrayList<Category> categories = new ArrayList<>();
        ResultSet rs;
        try {
            s =(Statement) connection.createStatement();
            rs = s.executeQuery ("SELECT * FROM Columna WHERE id_projecte = '" + id_projecte + "';");
            while(rs.next()) {
                if (rs.getString("id_columna") != null) {
                    categories.add(new Category(rs.getString("id_columna"), rs.getString("nom_columna"), rs.getInt("posicio"),
                            DataBaseManager.getTasks(id_projecte, rs.getString("id_columna"))));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Problema al Recuperar les dades --> " + ex.getSQLState());
        }
        return categories;
    }

    //Funció validada
    private static ArrayList<Task> getTasks(String id_projecte, String id_columna) {
        ArrayList<Task> tasks = new ArrayList<>();
        ResultSet rs;

        try {
            s =(Statement) connection.createStatement();
            rs = s.executeQuery ("SELECT * FROM Tasca WHERE id_projecte = '" + id_projecte + "' AND id_columna = '" + id_columna + "';");
            while(rs.next()) {
                if (rs.getString("id_tasca") != null) {
                    tasks.add(new Task(rs.getString("id_columna"), rs.getString("id_tasca"), rs.getString("nom_tasca"), rs.getInt("posicio"),
                            rs.getString("descripcio"), DataBaseManager.getTags(id_projecte, id_columna, rs.getString("id_tasca")),
                            DataBaseManager.getEncarregats(id_projecte, id_columna, rs.getString("id_tasca"))));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Problema al Recuperar les dades --> " + ex.getSQLState());
        }
        return tasks;
    }

    //Funció validada
    private static ArrayList<Tag> getTags(String id_projecte, String id_columna, String id_tasca) {
        ArrayList<Tag> tags = new ArrayList<>();
        ResultSet rs;

        try {
            s =(Statement) connection.createStatement();
            rs = s.executeQuery ("SELECT * FROM Etiqueta WHERE id_projecte = '" + id_projecte + "' AND id_columna = '" + id_columna +
                    "' AND id_tasca = '" + id_tasca + "';");
            while(rs.next()) {
                if (rs.getString("id_etiqueta") != null) {
                    tags.add(new Tag(rs.getString("id_columna"), rs.getString("id_tasca"), rs.getString("id_etiqueta"),
                            rs.getString("nom_etiqueta"), rs.getString("color")));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Problema al Recuperar les dades --> " + ex.getSQLState());
        }
        return tags;
    }

    //Funció validada
    private static ArrayList<Encarregat> getEncarregats(String id_projecte, String id_columna, String id_tasca) {
        ArrayList<Encarregat> encarregats = new ArrayList<>();
        ResultSet rs;
        try {
            s =(Statement) connection.createStatement();
            rs = s.executeQuery ("SELECT * FROM Etiqueta WHERE id_projecte = '" + id_projecte + "' AND id_columna = '" + id_columna +
                    "' AND id_tasca = '" + id_tasca + "';");
            while(rs.next()) {
                if (rs.getString("id_encarregat") != null) {
                    encarregats.add(new Encarregat(rs.getString("id_columna"), rs.getString("id_tasca"), rs.getString("id_encarregat"),
                            rs.getString("nom_encarregat"), rs.getString("color")));                }
            }
        } catch (SQLException ex) {
            System.out.println("Problema al Recuperar les dades --> " + ex.getSQLState());
        }
        return encarregats;
    }

    //Funció validada
    public static void addProject(Project projecte) {
        try {
            String query = "{CALL Organizer.AddProject(?,?,?,?)}";
            java.sql.CallableStatement stmt = connection.prepareCall(query);
            stmt.setString(1, projecte.getName());
            stmt.setString(2, projecte.getColor());
            stmt.setString(3, projecte.getId());
            stmt.setString(4, projecte.getBackground());
            stmt.executeQuery();

            for(Category c: projecte.getCategories()) {
                DataBaseManager.addCategory(c, projecte.getId());
                for(Task t: c.getTasks()) {
                    DataBaseManager.addTask(t, projecte.getId());
                    for(Tag tag: t.getTags()) {
                        DataBaseManager.addTag(tag, projecte.getId());
                    }
                    for(Encarregat e: t.getEncarregats()) {
                        DataBaseManager.addEncarregat(e, projecte.getId());
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Problema al Recuperar les dades --> " + e.getSQLState());
        }
    }

    //Funció validada
    public static void addEncarregat(Encarregat e, String id_projecte) {
        String query = "{CALL Organizer.AddEncarregat(?,?,?,?,?,?)}";
        java.sql.CallableStatement stmt = null;
        try {
            stmt = connection.prepareCall(query);
            stmt.setString(1, id_projecte);
            stmt.setString(2, e.getId_category());
            stmt.setString(3, e.getId_task());
            stmt.setString(4, e.getId());
            stmt.setString(5, e.getName());
            stmt.setString(6, e.getColor());
            stmt.executeQuery();
        } catch (SQLException ex) {
            System.out.println("Problema al Recuperar les dades --> " + ex.getSQLState());
        }
    }

    //Funció validada
    public static void addTag(Tag tag, String id_projecte) {
        String query = "{CALL Organizer.AddTag(?,?,?,?,?,?)}";
        java.sql.CallableStatement stmt = null;
        try {
            stmt = connection.prepareCall(query);
            stmt.setString(1, id_projecte);
            stmt.setString(2, tag.getId_category());
            stmt.setString(3, tag.getId_task());
            stmt.setString(4, tag.getId());
            stmt.setString(5, tag.getName());
            stmt.setString(6, tag.getColor());
            stmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Problema al Recuperar les dades --> " + e.getSQLState());
        }
    }

    //Funció validada
    public static void addTask(Task t, String id_projecte) {
        String query = "{CALL Organizer.AddTask(?,?,?,?,?,?)}";
        java.sql.CallableStatement stmt = null;
        try {
            stmt = connection.prepareCall(query);
            stmt.setString(1, id_projecte);
            stmt.setString(2, t.getId_category());
            stmt.setString(3, t.getId());
            stmt.setString(4, t.getName());
            stmt.setString(5, t.getDescription());
            stmt.setInt(6, t.getOrder());
            stmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Problema al Recuperar les dades --> " + e.getSQLState());
        }
    }

    //Funció validada
    public static void addCategory(Category c, String id_projecte) {
        String query = "{CALL Organizer.AddCategory(?,?,?,?)}";
        java.sql.CallableStatement stmt = null;
        try {
            stmt = connection.prepareCall(query);
            stmt.setString(1, id_projecte);
            stmt.setString(2, c.getId());
            stmt.setString(3, c.getName());
            stmt.setInt(4, c.getOrder());
            stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Funció validada
    public static void deleteProject(String id_projecte) {
        deleteAllTags(id_projecte);
        deleteAllEncarregats(id_projecte);
        deleteAllTasks(id_projecte);
        deleteAllCategories(id_projecte);
        deleteAllProject(id_projecte);
    }

    //Funció validada
    private static void deleteAllProject(String id_projecte) {
        try {
            s =(Statement) connection.createStatement();
            s.executeUpdate ("DELETE FROM Projecte WHERE id_projecte = '" + id_projecte + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }

    //Funció validada
    private static void deleteAllCategories(String id_projecte) {
        try {
            s =(Statement) connection.createStatement();
            s.executeUpdate ("DELETE FROM Columna WHERE id_projecte = '" + id_projecte + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }

    //Funció validada
    private static void deleteAllTasks(String id_projecte) {
        try {
            s =(Statement) connection.createStatement();
            s.executeUpdate ("DELETE FROM Tasca WHERE id_projecte = '" + id_projecte + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }

    //Funció validada
    private static void deleteAllEncarregats(String id_projecte) {
        try {
            s =(Statement) connection.createStatement();
            s.executeUpdate ("DELETE FROM Encarregat WHERE id_projecte = '" + id_projecte + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }

    //Funció validada
    private static void deleteAllTags(String id_projecte) {
        try {
            s =(Statement) connection.createStatement();
            s.executeUpdate ("DELETE FROM Etiqueta WHERE id_projecte = '" + id_projecte + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }

    //Funció validada
    public static void deleteCategory(String id_projecte, String id_columna) {
        deleteEncarregats(id_projecte, id_columna);
        deleteTags(id_projecte, id_columna);
        deleteTasks(id_projecte, id_columna);
        deleteAllCategory(id_projecte, id_columna);
    }

    //Funció validada
    private static void deleteAllCategory(String id_projecte, String id_columna) {
        try {
            s =(Statement) connection.createStatement();
            s.executeUpdate ("DELETE FROM Columna WHERE id_projecte = '" + id_projecte + "' AND" +
                    " id_columna = '" + id_columna + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }

    //Funció validada
    private static void deleteTasks(String id_projecte, String id_columna) {
        try {
            s.executeUpdate ("DELETE FROM Tasca WHERE id_projecte = '" + id_projecte + "' AND" +
                    " id_columna = '" + id_columna + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }

    //Funció validada
    private static void deleteTags(String id_projecte, String id_columna) {
        try {
            s =(Statement) connection.createStatement();
            s.executeUpdate ("DELETE FROM Etiqueta WHERE id_projecte = '" + id_projecte + "' AND" +
                    " id_columna = '" + id_columna + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }

    //Funció validada
    private static void deleteEncarregats(String id_projecte, String id_columna) {
        try {
            s =(Statement) connection.createStatement();
            s.executeUpdate ("DELETE FROM Encarregat WHERE id_projecte = '" + id_projecte + "' AND" +
                    " id_columna = '" + id_columna + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }

    //Funció validada
    public static void deleteTag(String id_projecte, String id_columna, String id_tasca, String id_etiqueta) {
        try {
            s =(Statement) connection.createStatement();
            s.executeUpdate ("DELETE FROM Etiqueta WHERE id_projecte = '" + id_projecte + "' AND" +
                    " id_columna = '" + id_columna + "' AND id_tasca = '" + id_tasca + "' AND" +
                    " id_etiqueta = '" + id_etiqueta + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }

    //Funció validada
    public static void deleteEncarregat(String id_projecte, String id_columna, String id_tasca, String id_encarregat) {
        try {
            s =(Statement) connection.createStatement();
            s.executeUpdate ("DELETE FROM Encarregat WHERE id_projecte = '" + id_projecte + "' AND" +
                    " id_columna = '" + id_columna + "' AND id_tasca = '" + id_tasca + "' AND" +
                    " id_encarregat = '" + id_encarregat + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }

    //Funció validada
    public static void deleteTask(String id_projecte, String id_columna, String id_tasca) {
        deleteTags(id_projecte, id_columna, id_tasca);
        deleteEncarregats(id_projecte, id_columna, id_tasca);
        deleteAllTask(id_projecte, id_columna, id_tasca);
    }

    //Funció validada
    private static void deleteAllTask(String id_projecte, String id_columna, String id_tasca) {
        try {
            s =(Statement) connection.createStatement();
            s.executeUpdate ("DELETE FROM Tasca WHERE id_projecte = '" + id_projecte + "' AND" +
                    " id_columna = '" + id_columna + "' AND id_tasca = '" + id_tasca + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }

    //Funció validada
    private static void deleteEncarregats(String id_projecte, String id_columna, String id_tasca) {
        try {
            s =(Statement) connection.createStatement();
            s.executeUpdate ("DELETE FROM Encarregat WHERE id_projecte = '" + id_projecte + "' AND" +
                    " id_columna = '" + id_columna + "' AND id_tasca = '" + id_tasca + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }

    //Funció validada
    private static void deleteTags(String id_projecte, String id_columna, String id_tasca) {
        try {
            s =(Statement) connection.createStatement();
            s.executeUpdate ("DELETE FROM Etiqueta WHERE id_projecte = '" + id_projecte + "' AND" +
                    " id_columna = '" + id_columna + "' AND id_tasca = '" + id_tasca + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }

    public static void swapTask(String id_projecte, Task task1, Task task2) {
        String query = "{CALL Organizer.SwapTask(?,?,?,?,?,?)}";
        java.sql.CallableStatement stmt = null;
        try {
            stmt = connection.prepareCall(query);
            stmt.setString(1, id_projecte);
            stmt.setString(2, task1.getId_category());
            stmt.setString(3, task1.getId());
            stmt.setString(4, task2.getId());
            stmt.setInt(5, task1.getOrder());
            stmt.setInt(6, task2.getOrder());
            stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void swapCategory(String id_projecte, Category category1, Category category2) {
        String query = "{CALL Organizer.SwapCategory(?,?,?,?,?,?)}";
        java.sql.CallableStatement stmt = null;
        try {
            stmt = connection.prepareCall(query);
            stmt.setString(1, id_projecte);
            stmt.setString(2, category1.getId());
            stmt.setString(3, category2.getId());
            stmt.setInt(4, category1.getOrder());
            stmt.setInt(5, category2.getOrder());
            stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addUser(String id_projecte, String username) {
        try {
            s =(Statement) connection.createStatement();
            s.executeUpdate ("INSERT INTO Membre(nom_usuari, id_projecte) VALUES ('" + id_projecte + "', '"
            + username + "');");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }

    public static void deleteUser(String id_projecte, String username) {
        try {
            s =(Statement) connection.createStatement();
            s.executeUpdate ("DELETE FROM Membre WHERE id_projecte = '" + id_projecte + "'AND nom_usuari = '"
                    + username + "';");
        } catch (SQLException ex) {
            System.out.println("Problema al esborrar les dades --> " + ex.getSQLState());
        }
    }

    //Funcio demanar dades de l'evolució de l'usuari des de la data passada.
    //TODO:Implementar-la
    //El Date es el de sql, no java.util
    public static Date[] requestUserEvolution(String username, Date minDate){
        return null;
    }

    //Funcio demanar el top10 d'usuaris amb tasques pendents
    public static Top10NeededData[] requestTop10(){
        return null;
    }
}