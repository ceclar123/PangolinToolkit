package com.a.b.c.d.pangolin.tool;

import com.a.b.c.d.pangolin.tool.util.AlertUtil;
import com.a.b.c.d.pangolin.util.ExceptionUtil;
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
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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


        // 加载opencv本地库
        String filePath = null;
        try {
            String fileName = getFileName();
            filePath = MainApplication.class.getResource("/lib/" + fileName).getPath();
            if (StringUtils.contains(filePath, "!BOOT-INF")) {
                filePath = getNewFilePath(filePath);
                System.load(filePath);
            } else {
                System.load(filePath);
            }
        } catch (Throwable e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", "加载opencv报错:" + filePath, ExceptionUtil.getStackTrace(e));
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

    private static String getFileName() {
        String osName = StringUtils.lowerCase(System.getProperty("os.name"));
        if (osName.contains("win")) {
            return "opencv_java490.dll";
        } else if (osName.contains("linux")) {
            return "libopencv_java4.so";
        } else if (osName.contains("mac")) {
            return "opencv_java490.dylib";
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * <pre>
     *      输入:nested:/d:/a/b/PangolinToolkit/pangolin-tool-final.jar/!BOOT-INF/classes/!lib/opencv_java490.dll
     *      输出:d:/a/b/PangolinToolkit/app/classes/lib/opencv_java490.dll
     *  </pre>
     *
     * @param path
     * @return
     */
    private static String getNewFilePath(String path) throws MalformedURLException {
        path = StringUtils.replace(path, "nested:/", "");
        String[] array = StringUtils.split(path, "/");
        int len = array.length;
        int index = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i].endsWith(".jar")) {
                index = i;
                break;
            }
        }

        path = Arrays.stream(array)
                .filter(Objects::nonNull)
                .limit(index - 1)
                .collect(Collectors.joining("/")) + "/app/classes/lib/" + array[len - 1];
        return path;
    }
}