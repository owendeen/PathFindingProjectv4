/**
 * @authors Owen Deen, Zack Lewis, Zack Glew, Carson Spitler, Grayson Frazee
 * @date 4/18/23
 * @course CSC-331
 * @instructor Shauna White
 * @project Pathfinding Algorithm Visualization
 * @purpose Creates a GUI that allows the visualization of multiple pathfinding algorithms including:
 *          - Random Walk
 *          - Dijkstra
 *          - A*
 *          - Dijkstra (Allowing Diagonals)
 *          - Greedy Best First (Shows traversal)
 *          Allows user to place a Start node, an End node, and Walls that act as barriers.
 *          Clear: Clears the whole grid
 *          Reset: Resets the path, timer, and squares traveled. Keeping start, end, and wall nodes.
 *          Find Path: Finds a path using the algorithm selected from the dropdown menu.
 */



package com.example.pathfindingprojectv4;

//package com.example.setuptest;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.event.EventHandler;

public class Homepage implements Initializable {

    private double paneWidth;
    private double paneHeight;

    Node start;
    Node end;

    ArrayList<Node> nodes = new ArrayList<>();
    ArrayList<Node> pathTaken = new ArrayList<>();

    Rectangle[][] rectangles = new Rectangle[15][30]; // array is [row][col]

    public AnchorPane gamePane;

    //public ComboBox optionSelect;

    @FXML
    private ComboBox<String> optionSelect;

    @FXML ComboBox<String> optionPath;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<String> options = FXCollections.observableArrayList(
                "Start",
                "End",
                "Wall"
        );

        optionSelect.setItems(options);

        ObservableList<String> paths = FXCollections.observableArrayList(
                "Random Walk",
                "A Star",
                "Dijkstra",
                "Trivial Path (Diagonals)",
                "Greedy Best First"
        );

        optionSelect.setItems(options);
        optionPath.setItems(paths);

        makeGrid();


    }

    public Color getColor(String option){
        return switch (option) {
            case "Wall" -> Color.BLACK;
            case "End" -> Color.RED;
            case "Start" -> Color.BLUE;
            default -> null;
        };
    }


    @FXML
    void paneClicked(MouseEvent event) {
        drawRectangle(event);
    }

    @FXML
    void paneDragged(MouseEvent event) {
        drawRectangle(event);
    }

    @FXML
    private TextField gridSizeInput;


    private void drawRectangle(MouseEvent event) {
        Color color = getColor(optionSelect.getValue());

        double mouseX = event.getX();
        double mouseY = event.getY();
        int x = (int) mouseX / 30; // num of col
        int y = (int) (mouseY - 40) / 30; // num of row

        //Clears color of node back to white when node is clicked again
        Rectangle rectangle = rectangles[y][x];
        if (rectangle.getFill().equals(Color.RED) || rectangle.getFill().equals(Color.GRAY) || rectangle.getFill().equals(Color.BLUE)){
            color = Color.color(0.9, 0.9, 0.9);
        }
        rectangle.setFill(color);

    }

    private void makeGrid() {
        paneWidth = gamePane.getPrefWidth();
        paneHeight = gamePane.getPrefHeight() - 40; // subtract 40 because of header height


        int indcROW = -1;
        // dependent on the row and col 15 x 30 and then each is 30 x 30
        for (int row = 40; row < 490; row += 30) {
            indcROW += 1;
            int indcCOL = -1;
            for (int col = 0; col < 900; col += 30) {
                indcCOL += 1;
                Rectangle rectangle = new Rectangle();
                rectangle.setHeight(30);
                rectangle.setWidth(30);
                rectangle.setX(col);
                rectangle.setY(row);



                rectangles[indcROW][indcCOL] = rectangle;



                /* This probably is not needed
                Node node = new Node(rectangle, col, row);
                nodes[indcROW][indcCOL] = node;
                */



                rectangle.setStroke(Color.BLACK);
                rectangle.setFill(Color.color(0.9,0.9,0.9)); // better light gray
                gamePane.getChildren().add(rectangle);

                gamePane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        paneClicked(event);
                    }
                });

                gamePane.setOnMouseDragged(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        paneDragged(event);
                    }
                });


            }
        }
    }


    @FXML
    void actionSelect(ActionEvent event) {

    }

    @FXML
    void clearWindow(ActionEvent event) {

        makeGrid(); // Clear button now works -zack g
        timer.setText("0.0s");

    }

    @FXML
    private TextField timer;

    @FXML
    private TextField counter;

    public static class startTime{
        // Timer stuff -grayson
        private final long time;
        public startTime(long time){
            this.time = time;
        }
    }


    /**
     * Loops through each rectangle in the grid to find the start node
     * @return rectangle that is the start node
     */
    public Rectangle findStartNode() {
        for(int row = 0; row < 15;  row++){
            for(int col = 0; col < 30; col++){
                Rectangle value = rectangles[row][col];
                if(value.getFill().equals(Color.BLUE)){
                    return value;
                }
            }
        }
        return null;
    }

    /**
     * Loops through each rectangle in the grid to find the end node
     * @return rectangle that is the end node
     */
    public Rectangle findEndNode() {
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 30; col++) {
                Rectangle value = rectangles[row][col];
                if (value.getFill().equals(Color.RED)) {
                    return value;
                }
            }
        }
        return null;
    }
    private boolean isWall(Rectangle cell) {
        return cell.getFill().equals(Color.BLACK);
    }
    @FXML
    public void findPath(ActionEvent event) throws InterruptedException {
        String pathOption = optionPath.getValue();
        if(pathOption.equals("Random Walk")){
            // 2 iterators one to mark the current rectangle and one to make the path gray
            ArrayList<Rectangle> path = performRandomWalk();
            Animation(path, Color.PURPLE);
            int size = path.size();
            counter.setText(Integer.toString(size));

            //timeline.setOnFinished(e -> findPathTraversal());
        }
        else if (pathOption.equals("A Star")){
            ArrayList<Rectangle> path = performAStar();
            Animation(path, Color.LIGHTGREEN);
//            int size = path.size();
//            counter.setText(Integer.toString(size));
        }
        else if (pathOption.equals("Dijkstra")){
            ArrayList<Rectangle> path = performDijkstra();
            Animation(path, Color.DEEPSKYBLUE);
//            int size = path.size();
//            counter.setText(Integer.toString(size));
//            for(Rectangle item: path) {
//                item.setFill(Color.DEEPSKYBLUE);
//            }
        }
        else if (pathOption.equals("Trivial Path (Diagonals)")) {
            ArrayList<Rectangle> path = performDiagonals();
            Animation(path, Color.MAGENTA);
            int size = path.size();
            counter.setText(Integer.toString(size));

        } else if (pathOption.equals("Greedy Best First")) {
            ArrayList<Rectangle> path = performGBS();
            Animation(path, Color.CORAL);
        }
    }

    /**
     * Allows for the animation of the path drawing.
     *
     * @param path ArrayList of rectangles that contains the path that the algorithm calculated..
     * @param color Color of the path.
     */
    public void Animation(ArrayList<Rectangle> path, Color color){

        startTime st = new startTime(System.currentTimeMillis());
        AtomicInteger count = new AtomicInteger(0);

        Iterator<Rectangle> nodeIterator = path.iterator();
        nodeIterator.next();
        nodeIterator.next().setFill(color);
        nodeIterator.next();
        Iterator<Rectangle> nodeIteratorprevious = path.iterator();
        nodeIteratorprevious.next();
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(75), ev -> {
            try {

                int processedRectangles = count.incrementAndGet();
                counter.setText(String.valueOf(processedRectangles) + " squares");

                nodeIteratorprevious.next().setFill(Color.GREY);

                nodeIterator.next().setFill(color); // iterator is rectangle

                // Timer updates
                double elapsedTime = System.currentTimeMillis() - st.time;
                double elapsedSeconds = elapsedTime / 1000;
                timer.setText(elapsedSeconds + "s");



            }catch (NoSuchElementException e){}

        }));
        timeline.setCycleCount(path.size() - 1);
        timeline.playFromStart();

    }


    @FXML
    public void Reset(ActionEvent event){
        counter.setText("");
        timer.setText("");
        for(int row = 0; row < 15;  row++){
            for(int col = 0; col < 30; col++){
                Rectangle value = rectangles[row][col];
                if(value.getFill().equals(Color.GRAY)){
                    value.setFill(Color.color(0.9,0.9,0.9));
                }}}
    }


    /**
     * Random Walk:
     * Looks at each neighbor of the current node and adds the neighbors into an ArrayList. Then chooses a random
     * node from the Array of neighbors and sets it as the current node until in the end node is found.
     * @return ArrayList containing the path taken to reach the end node.
     */

    public ArrayList<Rectangle> performRandomWalk() {
        // find start and end nodes
        Rectangle startNode = findStartNode();
        Rectangle endNode = findEndNode();

        // create path
        ArrayList<Rectangle> path = new ArrayList<>(); // path to go
        path.add(startNode);
        Rectangle currentNode = startNode;
        while (!currentNode.equals(endNode)) {
            // get neighbors of current node
            ArrayList<Rectangle> neighbors = new ArrayList<>();
            int currentRow = (int) (currentNode.getY() - 40) / 30;
            int currentCol = (int) currentNode.getX() / 30;
            if ((currentRow > 0) && !isWall(rectangles[currentRow-1][currentCol])) {
                neighbors.add(rectangles[currentRow-1][currentCol]); // up
            }
            if ((currentRow < 14) && !isWall(rectangles[currentRow+1][currentCol])) {
                neighbors.add(rectangles[currentRow+1][currentCol]); // down
            }
            if ((currentCol > 0) && !isWall(rectangles[currentRow][currentCol-1])) {
                neighbors.add(rectangles[currentRow][currentCol-1]); // left
            }
            if ((currentCol < 29) && !isWall(rectangles[currentRow][currentCol+1])) {
                neighbors.add(rectangles[currentRow][currentCol+1]); // right
            }

            // choose a random neighbor and add it to path
            Random rand = new Random();
            Rectangle nextNode = neighbors.get(rand.nextInt(neighbors.size()));
            if (nextNode != startNode && nextNode != endNode) {
                path.add(nextNode);
                //nextNode.setFill(Color.GRAY);
            }

            currentNode = nextNode;
        }
        return path;
    }

    /**
     *Dijksras:
     *  Finds the shortest path from the start to end node. Picks an unvisited node and calculates the distance to it
     *  through each of its neighbors and updates the neighbors distance if smaller
     * @return ArrayList containing the path taken.
     */

    public ArrayList<Rectangle> performDijkstra() {
        // find start and end nodes
        Rectangle startRectangle = findStartNode();
        Rectangle finalRectangle = findEndNode();
        Node[][] nodes = makeNodeArray();
        Node[] startnode = new Node[1];
        for (int row = 0; row < 15; row++){
            for (int col = 0; col < 30; col++){
                if (nodes[row][col].getRectangle() != startRectangle){
                    if (nodes[row][col].getFill() != Color.BLACK) {
                        nodes[row][col].setH(Double.POSITIVE_INFINITY); //set h value of each available node to infinity
                    }
                    else{
                        nodes[row][col].setH(Double.NEGATIVE_INFINITY);
                    }
                }
                else{
                    startnode[0] = nodes[row][col];
                }
            }
        }
        Node[][] finalnodes = dijkstrahelper(startnode, nodes);
        Node finalnode = finalnodes[(int)(finalRectangle.getY()-40)/30][(int)(finalRectangle.getX()/30)];
        Node currentNode = finalnode;
        Node[] finalpath = new Node[0];
        while (currentNode.parentnode != null) {
            Node[] temp = new Node[finalpath.length + 1];
            for (int item = 0; item < finalpath.length; item++){
                temp[item] = finalpath[item];
            }
            finalpath = temp;
            currentNode = currentNode.parentnode;
            finalpath[temp.length - 1] = currentNode;
        }


        // create path
        ArrayList<Rectangle> path = new ArrayList<>(); // path to go

        for(int item = finalpath.length - 1;  item >= 0; item--) {
            Rectangle currentRectangle = finalpath[item].getRectangle();
            path.add(currentRectangle);
        }


        return path;
    }

    public Node[][] dijkstrahelper(Node[] working_nodes, Node[][] nodes) {
        Node[] newWorking = new Node[0];
        boolean final_found = false;
        for(int item = 0; item < working_nodes.length; item++) {
            working_nodes[item].solved = true;
            if (working_nodes[item].getFill() != Color.RED) {
                if (working_nodes[item].getH() >= 0) {
                    for (double row = working_nodes[item].getY() - 1; row <= working_nodes[item].getY() + 1; row++) {
                        for (double col = working_nodes[item].getX() - 1; col <= working_nodes[item].getX() + 1; col++) {
                            if (row >= 0 && row < 15 && col >= 0 && col < 30) {
                                Node currentnode = nodes[(int) row][(int) col];
//                                if (Objects.equals(currentnode.getFill(), Color.color(0.9, 0.9, 0.9))) {
//                                    currentnode.getRectangle().setFill(Color.GRAY);
//                                }
                                if (currentnode.getH() != Double.NEGATIVE_INFINITY) {
                                    double minH = currentnode.getH();
                                    if (currentnode != working_nodes[item] && !currentnode.solved) {
                                        if (row == working_nodes[item].getY() || col == working_nodes[item].getX()) {
                                            if (working_nodes[item].getH() + 10 <= minH) {
                                                currentnode.parentnode = working_nodes[item];
                                                currentnode.setH(working_nodes[item].getH() + 10);
                                                currentnode.visited = true;
                                            }
                                        }
//                                        else {
//                                            if (working_nodes[item].getH() + 14 < minH) {
//                                                currentnode.parentnode = working_nodes[item];
//                                                currentnode.setH(working_nodes[item].getH() + 14);
//                                                currentnode.visited = true;
//                                            }
//                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else {
                final_found = true;
                break;
            }
        }
        double minh = Double.POSITIVE_INFINITY;
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 30; col++) {
                Node currentnode = nodes[row][col];
                if (currentnode.getH() > 0 && currentnode.getH() < minh && !currentnode.solved) {
                    minh = currentnode.getH();
                }
            }
        }

        int counter = 0;
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 30; col++) {
                Node currentnode = nodes[row][col];
                if (currentnode.getH() == minh && !currentnode.solved) {
                    counter +=1;
                }
            }
        }
        Node[] temp = new Node[newWorking.length + counter];
        for(int i = 0; i < newWorking.length; i++){
            temp[i] = newWorking[i];
        }
        int counter1 = newWorking.length;
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 30; col++) {
                Node currentnode = nodes[row][col];
                if (currentnode.getH() == minh && !currentnode.solved) {
                    temp[counter1] = currentnode;
                    counter1++;
                }
            }
        }
        newWorking = temp;
        if (final_found) {
            return nodes;
        }
        else{
            return dijkstrahelper(newWorking, nodes);
        }
    }

    /**
     * Dijkstras allowing for diagonals.
     * @return ArrayList containing the path taken.
     */
    public ArrayList<Rectangle> performDiagonals() {
        // find start and end nodes
        Rectangle startRectangle = findStartNode();
        Rectangle finalRectangle = findEndNode();
        Node[][] nodes = makeNodeArray();
        Node[] startnode = new Node[1];
        for (int row = 0; row < 15; row++){
            for (int col = 0; col < 30; col++){
                if (nodes[row][col].getRectangle() != startRectangle){
                    if (nodes[row][col].getFill() != Color.BLACK) {
                        nodes[row][col].setH(Double.POSITIVE_INFINITY);
                    }
                    else{
                        nodes[row][col].setH(Double.NEGATIVE_INFINITY);
                    }
                }
                else{
                    startnode[0] = nodes[row][col];
                }
            }
        }
        Node[][] finalnodes = dijkstrahelper1(startnode, nodes);
        Node finalnode = finalnodes[(int)(finalRectangle.getY()-40)/30][(int)(finalRectangle.getX()/30)];
        Node currentNode = finalnode;
        Node[] finalpath = new Node[0];
        while (currentNode.parentnode != null) {
            Node[] temp = new Node[finalpath.length + 1];
            for (int item = 0; item < finalpath.length; item++){
                temp[item] = finalpath[item];
            }
            finalpath = temp;
            currentNode = currentNode.parentnode;
            finalpath[temp.length - 1] = currentNode;
        }


        // create path
        ArrayList<Rectangle> path = new ArrayList<>(); // path to go

        for(int item = finalpath.length - 1;  item >= 0; item--) {
            Rectangle currentRectangle = finalpath[item].getRectangle();
            path.add(currentRectangle);
        }


        return path;
    }

    public Node[][] dijkstrahelper1(Node[] working_nodes, Node[][] nodes) {
        Node[] newWorking = new Node[0];
        boolean final_found = false;
        for(int item = 0; item < working_nodes.length; item++) {
            working_nodes[item].solved = true;
            if (working_nodes[item].getFill() != Color.RED) {
                if (working_nodes[item].getH() >= 0) {
                    for (double row = working_nodes[item].getY() - 1; row <= working_nodes[item].getY() + 1; row++) {
                        for (double col = working_nodes[item].getX() - 1; col <= working_nodes[item].getX() + 1; col++) {
                            if (row >= 0 && row < 15 && col >= 0 && col < 30) {
                                Node currentnode = nodes[(int) row][(int) col];
//                                if (Objects.equals(currentnode.getFill(), Color.color(0.9, 0.9, 0.9))) {
//                                    currentnode.getRectangle().setFill(Color.GRAY);
//                                }
                                if (currentnode.getH() != Double.NEGATIVE_INFINITY) {
                                    double minH = currentnode.getH();
                                    if (currentnode != working_nodes[item] && !currentnode.solved) {
                                        if (row == working_nodes[item].getY() || col == working_nodes[item].getX()) {
                                            if (working_nodes[item].getH() + 10 <= minH) {
                                                currentnode.parentnode = working_nodes[item];
                                                currentnode.setH(working_nodes[item].getH() + 10);
                                                currentnode.visited = true;
                                            }
                                        }
                                        else {
                                            if (working_nodes[item].getH() + 14 < minH) {
                                                currentnode.parentnode = working_nodes[item];
                                                currentnode.setH(working_nodes[item].getH() + 14);
                                                currentnode.visited = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else {
                final_found = true;
                break;
            }
        }
        double minh = Double.POSITIVE_INFINITY;
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 30; col++) {
                Node currentnode = nodes[row][col];
                if (currentnode.getH() > 0 && currentnode.getH() < minh && !currentnode.solved) {
                    minh = currentnode.getH();
                }
            }
        }

        int counter = 0;
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 30; col++) {
                Node currentnode = nodes[row][col];
                if (currentnode.getH() == minh && !currentnode.solved) {
                    counter +=1;
                }
            }
        }
        Node[] temp = new Node[newWorking.length + counter];
        for(int i = 0; i < newWorking.length; i++){
            temp[i] = newWorking[i];
        }
        int counter1 = newWorking.length;
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 30; col++) {
                Node currentnode = nodes[row][col];
                if (currentnode.getH() == minh && !currentnode.solved) {
                    temp[counter1] = currentnode;
                    counter1++;
                }
            }
        }
        newWorking = temp;
        if (final_found) {
            return nodes;
        }
        else{
            return dijkstrahelper1(newWorking, nodes);
        }
    }
    public Node[][] makeNodeArray(){

        Node[][] nodeList = new Node[15][30];

        for( int row = 0; row < 15; row++){
            for(int col =0; col < 30; col++ ){
                nodeList[row][col] = new Node(rectangles[row][col], row, col, 0);
            }
        }
        return nodeList;
    }

//============================================================================================


    /**
     * Greedy Best First:
     * Implements Pseudocode from this Wikipedia page - <a href="https://en.wikipedia.org/wiki/Best-first_search">...</a>
     * Chooses a neighbor of the current node with the most promising heuristic score (how close it is to the end)
     * and adds it to the queue.
     * @return ArrayList containing nodes used to find the path.
     */
    public ArrayList<Rectangle> performGBS(){
        Rectangle start = findStartNode();
        Rectangle end = findEndNode();

        ArrayList<Rectangle> visited = new ArrayList<>();

        ArrayList<Rectangle> pathTaken = new ArrayList<>();

        PriorityQueue<bfsNode> queue = new PriorityQueue<>();

        queue.add(new bfsNode(start, bfsHeuristic(start, end)));

        while(!queue.isEmpty()){
            bfsNode current = queue.poll();
            Rectangle currentNode = current.getNode();
            visited.add(currentNode); //adding the current node to the visited arraylist
            if(currentNode.equals(end)){
                //pathTaken.add(end);
                return pathTaken;
            }
            pathTaken.add(currentNode); // updating path

            //checking all the neighbors
            ArrayList<Rectangle> neighbors = new ArrayList<>();
            int currentRow = (int) (currentNode.getY() - 40) / 30;
            int currentCol = (int) currentNode.getX() / 30;
            if ((currentRow > 0) && !isWall(rectangles[currentRow - 1][currentCol])) {
                neighbors.add(rectangles[currentRow - 1][currentCol]); // up
            }
            if ((currentRow < 14) && !isWall(rectangles[currentRow + 1][currentCol])) {
                neighbors.add(rectangles[currentRow + 1][currentCol]); // down
            }
            if ((currentCol > 0) && !isWall(rectangles[currentRow][currentCol - 1])) {
                neighbors.add(rectangles[currentRow][currentCol - 1]); // left
            }
            if ((currentCol < 29) && !isWall(rectangles[currentRow][currentCol + 1])) {
                neighbors.add(rectangles[currentRow][currentCol + 1]); // right
            }
            for(Rectangle neighbor : neighbors){
                if(visited.contains(neighbor)){
                    continue;
                }
                queue.add(new bfsNode(neighbor, bfsHeuristic(neighbor, end)));//if the neighbor is not in the
                                                                        //visited array add them to the queue
            }

        }

        return new ArrayList<>();
    }

    public int bfsHeuristic(Rectangle current, Rectangle endNode){
        double x1 = current.getX() / 30;
        double x2 = endNode.getX() / 30;
        double y1 = (current.getY() - 40) / 30;
        double y2 = (endNode.getY() - 40) / 30;

        int xDiff = (int) Math.abs(x2 - x1);
        int yDiff = (int) Math.abs(y2 - y1);

        return xDiff + yDiff;
    }

    /**
     * Node class used for the Greedy Best First algorithm
     * allows for the characteristics needed to compare nodes.
     */
    class bfsNode implements Comparable<bfsNode>{

        public final Rectangle node;
        public final int hScore;

        public boolean visited;

        bfsNode(Rectangle node, int hScore){
            this.node = node;
            this.hScore = hScore;
        }

        public Rectangle getNode(){
            return this.node;
        }

        public boolean isVisited(){return visited;}
        public void setVisited(boolean visited){this.visited = visited;}

        public int gethScore(){
            return this.hScore;
        }

        @Override
        public int compareTo(bfsNode other){return Integer.compare(this.hScore, other.gethScore());}
    }

//=========================================================================================
    /**
     * Implements pseudocode from this Wikipedia page - <a href="https://en.wikipedia.org/wiki/A">...</a>*_search_algorithm
     * A* is a very famous algorithm. The goal is to min( F(n) = G(n) + H(n) ), where H estimates the cheapest path
     * from n to goal, and G is the cost of the path from the start to n.
     */
    public ArrayList<Rectangle> performAStar() {

        // get start an end nodes
        Rectangle startNode = findStartNode();
        Rectangle endNode = findEndNode();

        // nodes not discovered and not visited
        PriorityQueue<AStarNode> openSet = new PriorityQueue<>(); // Queue such that min F is first

        // nodes already visited
        HashSet<Rectangle> closedSet = new HashSet<>(); // elements are keys, values irrelevant

        // path from start to current node
        HashMap<Rectangle, Rectangle> cameFrom = new HashMap<>(); // includes parent nodes

        // start ->  current
        HashMap<Rectangle, Integer> gScore = new HashMap<>();

        // argmin (F = G + H)
        HashMap<Rectangle, Integer> fScore = new HashMap<>();


        gScore.put(startNode, 0); // (key, value)
        fScore.put(startNode, AStarHeuristic(startNode, endNode));

        openSet.add(new AStarNode(startNode, fScore.get(startNode)));


        while (!openSet.isEmpty()) {

            AStarNode current = openSet.poll(); // retrieves head of queue and pops it

            Rectangle currentNode = current.getNode();

            if (currentNode.equals(endNode)) {
                ArrayList<Rectangle> path = new ArrayList<>();
                path.add(endNode);
                Rectangle node = endNode;
                while (!node.equals(startNode)) {
                    node = cameFrom.get(node);
                    path.add(node);
                }
                Collections.reverse(path);
                path.remove(path.size() - 1);
                return path;
            }
            closedSet.add(currentNode);
            ArrayList<Rectangle> neighbors = new ArrayList<>();
            int currentRow = (int) (currentNode.getY() - 40) / 30;
            int currentCol = (int) currentNode.getX() / 30;
            if ((currentRow > 0) && !isWall(rectangles[currentRow - 1][currentCol])) {
                neighbors.add(rectangles[currentRow - 1][currentCol]); // up
            }
            if ((currentRow < 14) && !isWall(rectangles[currentRow + 1][currentCol])) {
                neighbors.add(rectangles[currentRow + 1][currentCol]); // down
            }
            if ((currentCol > 0) && !isWall(rectangles[currentRow][currentCol - 1])) {
                neighbors.add(rectangles[currentRow][currentCol - 1]); // left
            }
            if ((currentCol < 29) && !isWall(rectangles[currentRow][currentCol + 1])) {
                neighbors.add(rectangles[currentRow][currentCol + 1]); // right
            }

            for (Rectangle neighbor : neighbors) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }
                int tentativeGScore = gScore.get(currentNode) + 1;
                if (!openSet.contains(new AStarNode(neighbor, 0)) || tentativeGScore < gScore.get(neighbor)) {
                    cameFrom.put(neighbor, currentNode);
                    gScore.put(neighbor, tentativeGScore);
                    fScore.put(neighbor, tentativeGScore + AStarHeuristic(neighbor, endNode));
                    if (!openSet.contains(new AStarNode(neighbor, 0))) {
                        openSet.add(new AStarNode(neighbor, fScore.get(neighbor)));
                    }
                }
            }
        }
        return new ArrayList<>();
    }

    /**
     *Node class for the A* Algorithm
     * allows for the characteristics needed to perform the A* algorithm.
     */
    class AStarNode implements Comparable<AStarNode> {
    private final Rectangle node;
    private final int fScore;

    public AStarNode(Rectangle node, int fScore) {
        this.node = node;
        this.fScore = fScore;
    }


    public Rectangle getNode() {
        return node;
    }

    public int getFScore() {
        return fScore;
    }

    @Override
    public int compareTo(AStarNode other) {
        return Integer.compare(this.fScore, other.getFScore());
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof AStarNode)) {
            return false;
        }
        AStarNode otherNode = (AStarNode) other;
        return this.node.equals(otherNode.getNode());
    }

    @Override
    public int hashCode() {
        return node.hashCode();
    }

}
    private int AStarHeuristic(Rectangle node, Rectangle goal) {
        // L1 Norm
        double x1 = node.getX() / 30;
        double x2 = goal.getX() / 30;
        double y1 = (node.getY() - 40) / 30;
        double y2 = (goal.getY() - 40) / 30;

        int xDiff = (int) Math.abs(x2 - x1);
        int yDiff = (int) Math.abs(y2 - y1);

        return xDiff + yDiff;
    }

}








