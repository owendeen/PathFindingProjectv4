package com.example.pathfindingprojectv4;

//package com.example.setuptest;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import javafx.event.ActionEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Homepage implements Initializable{

    private double paneWidth;
    private double paneHeight;

    private int xside_length = 30;
    private int yside_length = 36;

    public AnchorPane gamePane;

    public ComboBox optionSelect;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        ObservableList<String> options = FXCollections.observableArrayList(
                "Start",
                "Goal",
                "Wall"
        );

        optionSelect.setItems(options);
        makeGrid();
    }

    private void makeGrid(){

        paneWidth = gamePane.getPrefWidth();
        paneHeight = gamePane.getPrefHeight() - 40;

//        for(int rows = 0; rows <= xside_length; xside_length += 30){
//            for(int cols = 0; cols <= yside_length; cols++){
//
//                Rectangle rectangle = new Rectangle()
//            }
//        }

        for (int y = 40; y<= 400; y += 36) {

            for (int x = 0; x<= 600; x += 30) {

                Rectangle test = new Rectangle();
                test.setHeight(36);
                test.setWidth(30);
                test.setX(x);
                test.setY(y);

                test.setStroke(Color.BLACK);
                test.setFill(Color.LIGHTGRAY);
                gamePane.getChildren().add(test);

            }
        }



    }

    @FXML
    void actionSelect(ActionEvent event) {

    }

    @FXML
    void clearWindow(ActionEvent event) {

    }

    @FXML
    void findPath(ActionEvent event) {

    }



}



//import javafx.fxml.FXML;
//import javafx.scene.control.Label;
//
//public class HelloController {
//    @FXML
//    private Label welcomeText;
//
//    @FXML
//    protected void onHelloButtonClick() {
//        welcomeText.setText("Welcome to JavaFX Application!");
//    }
//}