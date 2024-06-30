package com.a.b.c.d.pangolin.tool.controller.json;

import com.a.b.c.d.pangolin.tool.util.AlertUtil;
import com.a.b.c.d.pangolin.util.ExceptionUtil;
import com.a.b.c.d.pangolin.util.FastJsonUtil;
import com.a.b.c.d.pangolin.util.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class JsonFormatController implements Initializable {
    @FXML
    private TextArea txtFrom;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void btnCheckOnAction(ActionEvent event) {
        String input = this.txtFrom.getText();
        if (StringUtil.isBlank(input)) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "输入内容为空");
            return;
        }

        try {
            new ObjectMapper().readTree(input);
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "成功", "校验成功");
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtil.getStackTrace(e));
        }
    }

    @FXML
    public void btnFormatPrettyOnAction(ActionEvent event) {
        String input = this.txtFrom.getText();
        if (StringUtil.isBlank(input)) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "输入内容为空");
            return;
        }

        try {
            this.txtFrom.setText(FastJsonUtil.formatPretty(input));
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtil.getStackTrace(e));
        }
    }

    @FXML
    public void btnFormatCompressOnAction(ActionEvent event) {
        String input = this.txtFrom.getText();
        if (StringUtil.isBlank(input)) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "输入内容为空");
            return;
        }

        try {
            this.txtFrom.setText(FastJsonUtil.formatCompress(input));
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtil.getStackTrace(e));
        }
    }

    @FXML
    public void btnFormatAddEscapeOnAction(ActionEvent event) {
        String input = this.txtFrom.getText();
        if (StringUtil.isBlank(input)) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "输入内容为空");
            return;
        }

        try {
            this.txtFrom.setText(FastJsonUtil.formatAddEscape(input));
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtil.getStackTrace(e));
        }
    }

    @FXML
    public void btnFormatRemoveEscapeOnAction(ActionEvent event) {
        String input = this.txtFrom.getText();
        if (StringUtil.isBlank(input)) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "输入内容为空");
            return;
        }

        try {
            this.txtFrom.setText(FastJsonUtil.formatRemoveEscape(input));
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtil.getStackTrace(e));
        }
    }
}
