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

public class Homepage implements Initializable{


    public ComboBox optionSelect;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        ObservableList<String> options = FXCollections.observableArrayList(
                "Start",
                "Goal",
                "Wall"
        );

        optionSelect.setItems(options);
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