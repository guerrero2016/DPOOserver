package model.project;

public class User {
    private String name;
    private String color;
    private String id_category;
    private String id_task;
    private String id;

<<<<<<< HEAD:OrganizerServer/src/model/project/User.java
    public User(String name) {
=======
    public MemberInCharge(String id_category, String id_task, String id, String name, String color) {
        this.id_category = id_category;
        this.id_task = id_task;
        this.id = id;
>>>>>>> bb9d9daa305d9905943c63ab5d667c0cac961a12:OrganizerServer/src/model/project/MemberInCharge.java
        this.name = name;
        this.color = color;
    }

    public String getId_category() {
        return id_category;
    }

    public String getId_task() {
        return id_task;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
