package com.a.b.c.d.pangolin.tool.controller.encode;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.ResourceBundle;

public class Base64Controller implements Initializable {
    @FXML
    private TextArea txtFrom;
    @FXML
    private TextArea txtTo;
    @FXML
    private ComboBox<String> cmbCharsetName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbCharsetName.getItems().addAll(StandardCharsets.UTF_8.name(), "GB2312", StandardCharsets.UTF_16.name(), StandardCharsets.ISO_8859_1.name());
        cmbCharsetName.getSelectionModel().select(0);
    }

    private String getCharsetName() {
        return StringUtils.defaultIfBlank(cmbCharsetName.getValue(), StandardCharsets.UTF_8.name());
    }

    @FXML
    public void btnBaseEncodeOnAction(ActionEvent event) {
        String input = this.txtFrom.getText();
        if (StringUtils.isBlank(input)) {
            this.showAlert(Alert.AlertType.INFORMATION, "提示", "输入内容为空");
            return;
        }

        String charset = this.getCharsetName();
        try {
            this.txtTo.setText(new String(Base64.getEncoder().encode(input.getBytes(charset)), charset));
        } catch (Exception e) {
            this.showError(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtils.getStackTrace(e));
        }
    }

    @FXML
    public void btnBaseDecodeOnAction(ActionEvent event) {
        String input = this.txtFrom.getText();
        if (StringUtils.isBlank(input)) {
            this.showAlert(Alert.AlertType.INFORMATION, "提示", "输入内容为空");
            return;
        }

        String charset = this.getCharsetName();
        try {
            this.txtTo.setText(new String(Base64.getDecoder().decode(input.getBytes(charset)), charset));
        } catch (Exception e) {
            this.showError(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtils.getStackTrace(e));
        }
    }

    @FXML
    public void btnUrlEncodeOnAction(ActionEvent event) {
        String input = this.txtFrom.getText();
        if (StringUtils.isBlank(input)) {
            this.showAlert(Alert.AlertType.INFORMATION, "提示", "输入内容为空");
            return;
        }

        String charset = this.getCharsetName();
        try {
            this.txtTo.setText(new String(Base64.getUrlEncoder().encode(input.getBytes(charset)), charset));
        } catch (Exception e) {
            this.showError(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtils.getStackTrace(e));
        }
    }

    @FXML
    public void btnUrlDecodeOnAction(ActionEvent event) {
        String input = this.txtFrom.getText();
        if (StringUtils.isBlank(input)) {
            this.showAlert(Alert.AlertType.INFORMATION, "提示", "输入内容为空");
            return;
        }

        String charset = this.getCharsetName();
        try {
            this.txtTo.setText(new String(Base64.getUrlDecoder().decode(input.getBytes(charset)), charset));
        } catch (Exception e) {
            this.showError(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtils.getStackTrace(e));
        }
    }

    @FXML
    public void btnMimeEncodeOnAction(ActionEvent event) {
        String input = this.txtFrom.getText();
        if (StringUtils.isBlank(input)) {
            this.showAlert(Alert.AlertType.INFORMATION, "提示", "输入内容为空");
            return;
        }

        String charset = this.getCharsetName();
        try {
            this.txtTo.setText(new String(Base64.getMimeEncoder().encode(input.getBytes(charset)), charset));
        } catch (Exception e) {
            this.showError(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtils.getStackTrace(e));
        }
    }

    @FXML
    public void btnMimeDecodeOnAction(ActionEvent event) {
        String input = this.txtFrom.getText();
        if (StringUtils.isBlank(input)) {
            this.showAlert(Alert.AlertType.INFORMATION, "提示", "输入内容为空");
            return;
        }

        String charset = this.getCharsetName();
        try {
            this.txtTo.setText(new String(Base64.getMimeDecoder().decode(input.getBytes(charset)), charset));
        } catch (Exception e) {
            this.showError(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtils.getStackTrace(e));
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.show();
    }

    private void showError(Alert.AlertType alertType, String title, String content, String detail) {
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
