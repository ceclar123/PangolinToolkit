package com.a.b.c.d.pangolin.tool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Application.setUserAgentStylesheet(STYLESHEET_CASPIAN);
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));

        // 主屏宽高
        Rectangle2D bound = Screen.getPrimary().getBounds();
        double width = bound.getWidth() / 2;
        double height = bound.getHeight() / 2;

        Scene scene = new Scene(fxmlLoader.load(), width, height);
        stage.setScene(scene);

        stage.setTitle("PangolinToolkit");
        stage.setMinWidth(width);
        stage.setMinHeight(height);
        stage.getIcons().add(new Image(MainApplication.class.getResourceAsStream("/img/pangolin.png")));

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}