package model.project;

import java.awt.*;

public class Project {

    private String name;
    private Color color;

    public Project(Color color, String name) {
        this.color = color;
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
}
