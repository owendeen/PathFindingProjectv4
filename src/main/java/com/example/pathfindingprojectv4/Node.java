package com.example.pathfindingprojectv4;

import javafx.scene.shape.Rectangle;

/**
 *  I am not sure if we need this class - owen
 */
public class Node {
    private Rectangle rectangle;
    private double x;
    private double y;
    public Node(Rectangle rectangle, int x, int y){
        this.rectangle = rectangle;
        this.x = rectangle.getX();
        this.y = rectangle.getY();

    }

    public double getX() {
        return this.x;
    }

    public double getY(){
        return this.y;
    }



    @Override
    public String toString(){
        return String.format("%d%d%n", x, y);
    }
}

