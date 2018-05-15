package model.project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class Category implements Serializable {
    public final static int INVALID_INDEX = -1;

    private String id;
    private String name;
    private int order;
    private ArrayList<model.project.Task> tasks;

    /** Constructor amb un sol paràmetre id.
     *
     * @param id Id amb el qual volem construir la categoria.
     */
    public Category(String id) {
        this.id = UUID.randomUUID().toString();
        this.name = id.toString();
        order = INVALID_INDEX;
        tasks = new ArrayList<>();
    }

    /** Constructor amb paràmetres.
     *
     * @param id Id de la categoria.
     * @param name Nom de la categoria.
     * @param order Posició de la categoria.
     * @param tasks Tasques de la categoria.
     */
    public Category(String id, String name, int order, ArrayList<Task> tasks) {
        this.id = id;
        this.name = name;
        this.order = order;
        this.tasks = tasks;
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

    /** Getter de la quantitat de tasques de la categoria.
     */
    public int getTasksSize() {
        return tasks.size();
    }

    /** Getter de les tasques de la categoria.
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /** Setter de les tasques de la categoria.
     *
     * @param tasks: Tasques a settejar.
     */
    public void setTasks(ArrayList<Task> tasks) {
        if(tasks != null) {
            this.tasks = tasks;
        }
    }

    public int getTaskIndex(Task task) {
        if(tasks.contains(task)) {
            return tasks.indexOf(task);
        } else {
            return INVALID_INDEX;
        }
    }


    public Task getTask(int taskIndex) {
        if(taskIndex < tasks.size()) {
            return tasks.get(taskIndex);
        } else {
            return null;
        }
    }


    public void setTask(Task task) {
        if(tasks.contains(task)) {
            tasks.remove(task);
            tasks.add(task.getOrder(), task);
        } else {
            tasks.add(task.getOrder(), task);
        }
    }

    public void addTask(Task task) {
        if(task != null && !tasks.contains(task)) {
            tasks.add(task);
        }
    }

    public void deleteTask(Task task) {
        if (tasks.contains(task)) {
            tasks.remove(task);
        }
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