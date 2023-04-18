package com.example.pathfindingprojectv4;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
/**
 *Node class used for Dijkstras
 * Allows for the characteristics need to compare rectangles.
 */
public class Node implements Comparable<Node> {
    private Rectangle rectangle;

    public Node parentnode = null;

    public boolean visited = false;
    public boolean solved = false;

    private double x;
    private double y;

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

    public void setVisited(boolean visited){this.visited=visited;}


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
    @Override
    public int compareTo(Node o){
        return Double.compare(h, o.h);
    }
}

