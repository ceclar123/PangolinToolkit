package com.a.b.c.d.pangolin.tool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainApplication extends Application {
    public static Stage PRIMARY_STAGE = null;

    @Override
    public void start(Stage stage) throws IOException {
        PRIMARY_STAGE = stage;
        Application.setUserAgentStylesheet(STYLESHEET_MODENA);
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
        stage.setWidth(width);
        stage.setHeight(height);
        stage.getIcons().addAll(this.getIconList());

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private List<Image> getIconList() {
        return Arrays.asList(16, 32, 48, 64, 128, 256).stream()
                .map(it -> {
                    return new Image(MainApplication.class.getResourceAsStream("/img/pangolin_" + it + ".png"));
                }).collect(Collectors.toList());
    }
}