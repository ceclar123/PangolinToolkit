package com.a.b.c.d.pangolin.tool.util;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

public class AlertUtil {
    private AlertUtil() {
    }

    public static void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(title);
        alert.setContentText(content);
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
        alert.show();
    }
}
