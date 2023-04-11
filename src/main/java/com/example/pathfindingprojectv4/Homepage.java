package com.example.pathfindingprojectv4;

//package com.example.setuptest;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.ResourceBundle;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.event.EventHandler;

public class Homepage implements Initializable {

    private double paneWidth;
    private double paneHeight;

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
                "Dijkstra"
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
            //case "Blank" -> Color.WHITE;
            default -> null;
        };
    }


    @FXML
    void paneClicked(MouseEvent event) {
        drawRectangle(event);
    }






    @FXML
    private TextField gridSizeInput;



    private void drawRectangle(MouseEvent event) {
        Color color = getColor(optionSelect.getValue());
        double mouseX = event.getX();
        double mouseY = event.getY();
        int x = (int) mouseX / 30; // num of col
        int y = (int) (mouseY-40) / 30; // num of row

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
                rectangle.setFill(Color.color(0.9,0.9,0.9)); // better light gray
                gamePane.getChildren().add(rectangle);

                gamePane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        paneClicked(event);
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
        if(pathOption.equals("Random Walk")){
            ArrayList<Rectangle> path = performRandomWalk();
            Iterator<Rectangle> nodeIterator = path.iterator();
            nodeIterator.next();
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(75), ev -> {
                nodeIterator.next().setFill(Color.GRAY); // iterator is recatangle

            }));
            timeline.setCycleCount(path.size() - 1);
            timeline.playFromStart();

            //timeline.setOnFinished(e -> findPathTraversal());


        }
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

}









