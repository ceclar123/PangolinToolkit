package com.a.b.c.d.pangolin.tool.util;

import com.a.b.c.d.pangolin.tool.MainApplication;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Screen;
import javafx.stage.Window;

import java.util.Objects;

public class AlertUtil {
    private AlertUtil() {
    }

    public static void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(title);
        alert.setContentText(content);
        if (Objects.nonNull(MainApplication.PRIMARY_STAGE)) {
            alert.initOwner(MainApplication.PRIMARY_STAGE);
            setCenterIntOwner(alert, alert.getOwner());
        }
        alert.show();
    }

    public static void showAlert(Alert.AlertType alertType, String title, String content, String detail) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(title);
        alert.setContentText(content);

        TextArea textArea = new TextArea(detail);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        alert.getDialogPane().setExpandableContent(textArea);
        if (Objects.nonNull(MainApplication.PRIMARY_STAGE)) {
            alert.initOwner(MainApplication.PRIMARY_STAGE);
            setCenterIntOwner(alert, alert.getOwner());
        }
        alert.show();
    }

    private static void setCenterIntOwner(Alert alert, Window owner) {
        alert.heightProperty().addListener(it -> {
            double centerX = owner.getX() + owner.getWidth() / 2;
            double centerY = owner.getY() + owner.getHeight() / 2;

            double alertWidth = alert.getWidth();
            double alertHeight = alert.getHeight();
            double alertScreenX = centerX - alert.getWidth() / 2;
            double alertScreenY = centerY - alert.getHeight() / 2;

            // 获取屏幕的宽度和高度
            double width = Screen.getPrimary().getVisualBounds().getWidth();
            double height = Screen.getPrimary().getVisualBounds().getHeight();

            if (alertScreenX + alertWidth > width)
                alertScreenX = (int) (width - alertWidth);
            else if (alertScreenX < 0) {
                alertScreenX = 0;
            }

            if (alertScreenY + alertHeight > height) {
                alertScreenY = (int) (height - alertHeight);
            } else if (alertScreenY < 0) {
                alertScreenY = 0;
            }

            alert.setX(alertScreenX);
            alert.setY(alertScreenY);
        });
    }
}
