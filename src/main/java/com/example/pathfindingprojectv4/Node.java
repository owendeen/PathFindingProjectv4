package com.example.pathfindingprojectv4;

import javafx.scene.shape.Rectangle;

/**
 *  I am not sure if we need this class - owen
 */
public class Node {
    private Rectangle rectangle;

    public boolean visited = false;
    private double x;
    private double y;

    private double h;
    public Node(Rectangle rectangle, int x, int y, int h){
        this.rectangle = rectangle;
        this.x = rectangle.getX();
        this.y = rectangle.getY();
        this.h = h;

    }

    public boolean isVisited(){return (visited);}

    public double getX() {
        return this.x;
    }

    public double getY(){
        return this.y;
    }

    public void setH(double h){this.h = h;}

    public Rectangle getRectangle(){
        return rectangle;
    }



    @Override
    public String toString(){
        return String.format("%d%d%n", x, y);
    }
}

