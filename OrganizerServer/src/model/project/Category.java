package model.project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

/**
 * Classe que representa una categoria del projecte
 */
public class Category implements Serializable {
    public final static int INVALID_INDEX = -1;
    public final static int serialVersionUID = 1234;

    private String id;
    private String name;
    private int order = -1;
    private ArrayList<model.project.Task> tasks;


    public Category(String name) {
        this.name = name.toString();
        order = INVALID_INDEX;
        tasks = new ArrayList<>();
    }

    public Category(String name, int order, ArrayList<Task> tasks) {
        this.name = name;
        this.order = order;
        this.tasks = tasks;
    }

    public void setId (String id) {
        this.id = id;
    }

    /** Getter de l'id de la categoria.
     */
    public String getId() {
        return id;
    }

    /** Getter del nom de la categoria.
     */
    public String getName() {
        return name;
    }

    /** Setter del nom de la categoria.
     *
     * @param name Nom a settejar.
     */
    public void setName(String name) {
        this.name = name;
    }

    /** Getter de la posició de la categoria.
     */
    public int getOrder() {
        return order;
    }

    /** Setter de la posició de la categoria.
     *
     * @param order Posició a settejar.
     */
    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Category category = (Category) o;

        return order == category.order &&
                Objects.equals(id, category.id) &&
                Objects.equals(name, category.name) &&
                Objects.equals(tasks, category.tasks);

    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, order, tasks);
    }

}