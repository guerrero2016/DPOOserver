package model.project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Task implements Serializable{

    private String id;
    private String name;
    private int order;
    private String description;
    private ArrayList<Tag> tags;
    private ArrayList<User> usuaris;

    public Task(String id, String name, int order, String description, ArrayList<Tag> tags, ArrayList<User> usuaris) {
        this.id = id;
        this.name = name;
        this.order = order;
        this.description = description;
        this.usuaris = usuaris;
        this.tags = tags;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getOrder() {
        return order;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public ArrayList<User> getUsuaris() {
        return usuaris;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return order == task.order &&
                Objects.equals(name, task.name) &&
                Objects.equals(description, task.description) &&
                Objects.equals(tags, task.tags) &&
                Objects.equals(usuaris, task.usuaris);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, order, description, tags, usuaris);
    }
}