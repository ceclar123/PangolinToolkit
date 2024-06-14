package com.a.b.c.d.pangolin.tool.controller.crypto;

import com.a.b.c.d.pangolin.tool.util.AlertUtil;
import com.a.b.c.d.pangolin.util.ExceptionUtil;
import com.a.b.c.d.pangolin.util.StringUtil;
import com.a.b.c.d.pangolin.util.crypto.AesUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.math.NumberUtils;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class AesController implements Initializable {
    @FXML
    private TextArea txtFrom;
    @FXML
    private TextArea txtTo;
    @FXML
    private ComboBox<String> cmbMode;
    @FXML
    private ComboBox<String> cmbKeyLen;
    @FXML
    private ComboBox<Integer> cmbIvLen;
    @FXML
    private TextField txtKey;
    @FXML
    private TextField txtIv;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbKeyLen.getItems().addAll(Arrays.stream(AesUtil.KeyLenEnum.values()).map(AesUtil.KeyLenEnum::getLen).map(String::valueOf).toList());
        cmbKeyLen.getSelectionModel().select(0);

        cmbIvLen.getItems().addAll(AesUtil.IV_LENGTH);
        cmbIvLen.getSelectionModel().select(0);

        cmbMode.getItems().addAll(AesUtil.AES_CBC, AesUtil.AES_CFB, AesUtil.AES_ECB);
        cmbMode.getSelectionModel().select(0);
    }


    @FXML
    public void btnGenKeyOnAction(ActionEvent event) {
        this.txtKey.setText(AesUtil.getString(NumberUtils.toInt(cmbKeyLen.getValue(), 0)));
    }

    @FXML
    public void btnGenIvOnAction(ActionEvent event) {
        this.txtIv.setText(AesUtil.getString(AesUtil.IV_LENGTH));
    }

    @FXML
    public void btnEncryptOnAction(ActionEvent event) {
        String input = this.txtFrom.getText();
        if (StringUtil.isBlank(input)) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "输入内容为空");
            return;
        }

        try {
            //this.txtTo.setText(new String(Base64.getEncoder().encode(SnappyUtil.compress(input.getBytes(charset))), charset));
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtil.getStackTrace(e));
        }
    }

    @FXML
    public void btnDecryptOnAction(ActionEvent event) {
        String input = this.txtFrom.getText();
        if (StringUtil.isBlank(input)) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "输入内容为空");
            return;
        }

        try {
            //this.txtTo.setText(new String(SnappyUtil.decompress(Base64.getDecoder().decode(input.getBytes(charset))), charset));
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtil.getStackTrace(e));
        }
    }
}
