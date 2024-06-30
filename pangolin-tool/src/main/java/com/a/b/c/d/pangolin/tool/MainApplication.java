package com.a.b.c.d.pangolin.tool;

import com.a.b.c.d.pangolin.tool.util.AlertUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

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

        try {
            System.load(MainApplication.class.getResource(getFilePath()).getFile());
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", "加载opencv报错");
        }
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

    private static String getFilePath() {
        String osName = StringUtils.lowerCase(System.getProperty("os.name"));
        if (osName.contains("win")) {
            return "/lib/opencv_java490.dll";
        } else if (osName.contains("linux")) {
            return "/lib/libopencv_java4.so";
        } else if (osName.contains("mac")) {
            return "/lib/opencv_java490.dylib";
        } else {
            throw new UnsupportedOperationException();
        }
    }
}