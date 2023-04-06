package com.example.pathfindingprojectv4;

public class Node {
    private int x;
    private int y;
    public Node(int x, int y){
        this.x = x;
        this.y = y;
    }
    @Override
    public String toString(){
        return String.format("%d%d%n", x, y);
    }
}

