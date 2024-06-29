package com.a.b.c.d.pangolin.tool.controller.encode;

import com.a.b.c.d.pangolin.tool.util.AlertUtil;
import com.a.b.c.d.pangolin.tool.util.GridPaneUtil;
import com.a.b.c.d.pangolin.util.ExceptionUtil;
import com.a.b.c.d.pangolin.util.StringUtil;
import com.a.b.c.d.pangolin.util.encode.XZUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.ResourceBundle;

public class XZController implements Initializable {
    @FXML
    private GridPane gridPane;
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

        // gridPane宽高自适应
        GridPaneUtil.setAutoPercentWidth(this.gridPane);
        GridPaneUtil.setAutoPercentHeight(this.gridPane);
    }

    private String getCharsetName() {
        return StringUtil.defaultIfBlank(cmbCharsetName.getValue(), StandardCharsets.UTF_8.name());
    }

    @FXML
    public void btnEncodeOnAction(ActionEvent event) {
        String input = this.txtFrom.getText();
        if (StringUtil.isBlank(input)) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "明文内容为空");
            return;
        }

        String charset = this.getCharsetName();
        try {
            this.txtTo.setText(new String(Base64.getEncoder().encode(XZUtil.compress(input.getBytes(charset))), charset));
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtil.getStackTrace(e));
        }
    }

    @FXML
    public void btnDecodeOnAction(ActionEvent event) {
        String input = this.txtTo.getText();
        if (StringUtil.isBlank(input)) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "密文内容为空");
            return;
        }

        String charset = this.getCharsetName();
        try {
            this.txtFrom.setText(new String(XZUtil.decompress(Base64.getDecoder().decode(input.getBytes(charset))), charset));
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtil.getStackTrace(e));
        }
    }
}
