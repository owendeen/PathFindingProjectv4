package com.example.pathfindingprojectv4;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 *  I am not sure if we need this class - owen
 */
public class Node {
    private Rectangle rectangle;

    public boolean solved = false;
    public boolean visited = false;
    private double x;
    private double y;
    public Node parentnode = null;
    private int gCost;
    private int hCost;
    private int fCost;
    private double h;
    public Node(Rectangle rectangle, int x, int y, double h){
        this.rectangle = rectangle;
        this.x = rectangle.getX() / 30;
        this.y = (rectangle.getY() - 40)/ 30;
    }
    public boolean isVisited(){return (visited);}
    public double getX() {
        return this.x;
    }
    public double getY(){
        return this.y;
    }
    public void setH(double h){this.h = h;}
    public double getH(){return this.h;}
    public Rectangle getRectangle(){
        return rectangle;
    }
    public Paint getFill() {
        return rectangle.getFill();
    }
}

