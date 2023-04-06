package com.example.pathfindingprojectv4;

public class Node {
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;
    private boolean diagUR;
    private boolean diagUL;
    private boolean diagDR;
    private boolean diagDL;
    private boolean visited;
    private boolean solved;
    public Node(boolean up, boolean down, boolean left, boolean right, boolean diagUR, boolean diagUL, boolean diagDR, boolean diagDL, boolean solved, boolean visited){
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.diagUR = diagUR;
        this.diagUL = diagUL;
        this.diagDR = diagDR;
        this.diagDL = diagDL;
        this.solved = solved;
        this.visited = visited;
    }
}
