package com.example.pathfindingprojectv4;

//package com.example.setuptest;
import java.net.URL;
import java.util.*;


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

    @FXML
    ComboBox<String> optionPath;

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
                "Dijkstra"
        );

        optionSelect.setItems(options);
        optionPath.setItems(paths);

        makeGrid();


    }

    public Color getColor(String option) {
        return switch (option) {
            case "Wall" -> Color.BLACK;
            case "End" -> Color.RED;
            case "Start" -> Color.BLUE;
            //case "Blank" -> Color.WHITE;
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

        Rectangle rectangle = rectangles[y][x];
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
                rectangle.setFill(Color.color(0.9, 0.9, 0.9)); // better light gray
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

    }

    public Rectangle findStartNode() {
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 30; col++) {
                Rectangle value = rectangles[row][col];
                if (value.getFill().equals(Color.BLUE)) {
                    return value;
                }
            }
        }
        return null;
    }

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
    public void findPath(ActionEvent event) {
        String pathOption = optionPath.getValue();
        if (pathOption.equals("Random Walk")) {
            // 2 iterators one to mark the current rectangle and one to make the path gray
            ArrayList<Rectangle> path = performRandomWalk();
            Animation(path, Color.PURPLE);
        }
        else if (pathOption.equals("A Star")){
            ArrayList<Rectangle> path = performAStar();
            Animation(path, Color.LIGHTGREEN);
        }
    }

    public void Animation(ArrayList<Rectangle> path, Color color){
        Iterator<Rectangle> nodeIterator = path.iterator();
        nodeIterator.next();
        nodeIterator.next().setFill(color);
        nodeIterator.next();
        Iterator<Rectangle> nodeIteratorprevious = path.iterator();
        nodeIteratorprevious.next();
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(75), ev -> {
            try {
                nodeIteratorprevious.next().setFill(Color.GREY);
                nodeIterator.next().setFill(color); // iterator is rectangle

            } catch (NoSuchElementException e) {
            }

        }));
        timeline.setCycleCount(path.size() - 1);
        timeline.playFromStart();
    }


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

    public ArrayList<Rectangle> performDijkstra() {
        // find start and end nodes
        Rectangle startRectangle = findStartNode();
        Rectangle endRectangle = findEndNode();
        Node[][] nodes = makeNodeArray();
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                if (nodes[row][col].getRectangle() != startRectangle) {
                    nodes[row][col].setH(Double.POSITIVE_INFINITY);
                }
            }
        }

        // create path
        ArrayList<Rectangle> path = new ArrayList<>(); // path to go
        path.add(startRectangle);


        return null;
    }

    public Node[][] makeNodeArray() {

        Node[][] nodeList = new Node[15][30];

        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 30; col++) {
                nodeList[row][col] = new Node(rectangles[row][col], row, col, 0);
            }
        }
        return nodeList;
    }

    //============================================================================================
    public void makeNodes() {
        Rectangle[][] list = rectangles;
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 30; col++) {
                Node node = null;
                if (rectangles[row][col].getFill().equals(Color.BLACK)) {
                    node = new Node(rectangles[row][col], col, row, -1);
                } else if (rectangles[row][col].getFill().equals(Color.BLUE)) {
                    node = new Node(rectangles[row][col], col, row, calcHeuristic(rectangles[row][col], end.getRectangle()));
                    start = node;
                } else if (rectangles[row][col].getFill().equals(Color.RED)) {
                    node = new Node(rectangles[row][col], col, row, 0);
                    end = node;
                } else if (!rectangles[row][col].getFill().equals(Color.BLACK) || !rectangles[row][col].getFill().equals(Color.RED) || !rectangles[row][col].getFill().equals(Color.BLUE)) {
                    node = new Node(rectangles[row][col], col, row, calcHeuristic(rectangles[row][col], end.getRectangle()));
                }
                nodes.add(node);

            }
        }
    }

    public void performGreedyBest() {


    }


    public ArrayList<Node> Neighbors(Node current, ArrayList<Node> nodeList) {
        ArrayList<Node> neighbors = new ArrayList<>();

        for (Node node : nodeList) {
            if (node.isVisited()) {
                continue;
            }

        }
        return null;
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
        fScore.put(startNode, calcHeuristic(startNode, endNode));

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
                    fScore.put(neighbor, tentativeGScore + calcHeuristic(neighbor, endNode));
                    if (!openSet.contains(new AStarNode(neighbor, 0))) {
                        openSet.add(new AStarNode(neighbor, fScore.get(neighbor)));
                    }
                }
            }
        }

        return new ArrayList<>();
    }

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

    private int calcHeuristic(Rectangle node, Rectangle goal) {
        // L1 Norm
        int xDiff = Math.abs((int) node.getX() / 30 - (int) goal.getX() / 30);
        int yDiff = Math.abs(((int) node.getY() - 40) / 30 - ((int) goal.getY() - 40) / 30);
        return xDiff + yDiff;
        // push comment
    }

}





