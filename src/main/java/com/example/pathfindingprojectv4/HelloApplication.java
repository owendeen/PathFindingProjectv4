package com.example.pathfindingprojectv4;

//package com.example.setuptest;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


import java.io.IOException;

public class HelloApplication extends Application{
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 490); // 600, 400
        stage.setTitle("Path Finding Simulator v4");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {launch();}

}


