package model.project;

import model.user.UserLogIn;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Project implements Serializable{

    private String id;
    private String name;
    private Color color;
    private ArrayList<Category> categories;
    private ArrayList<String> membersName;
    private Image background;   //s'haurà de canviar PROFE

    public Project(String name, Color color, ArrayList<Category> categories, ArrayList<String> membersName, Image background) {
        this.name = name;
        this.color = color;
        this.categories = categories;
        this.membersName = membersName;
        this.background = background;
    }

    public String getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public ArrayList<String> getMembersName() {
        return membersName;
    }

    public Image getBackground() {
        return background;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public void setMembersName(ArrayList<String> membersName) {
        this.membersName = membersName;
    }

    public void setBackground(Image background) {
        this.background = background;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(id, project.id) &&
                Objects.equals(name, project.name) &&
                Objects.equals(color, project.color) &&
                Objects.equals(categories, project.categories) &&
                Objects.equals(membersName, project.membersName) &&
                Objects.equals(background, project.background);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, categories, membersName, background);
    }
}
