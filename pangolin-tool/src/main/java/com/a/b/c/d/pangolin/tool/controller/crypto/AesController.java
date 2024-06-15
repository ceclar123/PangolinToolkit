package com.a.b.c.d.pangolin.tool.controller.crypto;

import com.a.b.c.d.pangolin.tool.util.AlertUtil;
import com.a.b.c.d.pangolin.util.ExceptionUtil;
import com.a.b.c.d.pangolin.util.StringUtil;
import com.a.b.c.d.pangolin.util.crypto.AesUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.math.NumberUtils;

import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.ResourceBundle;

public class AesController implements Initializable {
    @FXML
    private TextArea txtFrom;
    @FXML
    private TextArea txtTo;
    @FXML
    private ComboBox<String> cmbCharsetName;
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
        cmbCharsetName.getItems().addAll(StandardCharsets.UTF_8.name(), "GB2312", StandardCharsets.UTF_16.name(), StandardCharsets.ISO_8859_1.name());
        cmbCharsetName.getSelectionModel().select(0);

        cmbKeyLen.getItems().addAll(Arrays.stream(AesUtil.KeyLenEnum.values()).map(AesUtil.KeyLenEnum::getLen).map(String::valueOf).toList());
        cmbKeyLen.getSelectionModel().select(0);

        cmbIvLen.getItems().addAll(AesUtil.IV_LENGTH);
        cmbIvLen.getSelectionModel().select(0);

        cmbMode.getItems().addAll(AesUtil.AES_CBC, AesUtil.AES_CFB, AesUtil.AES_ECB);
        cmbMode.getSelectionModel().select(0);
        // ECB模式不需要向量
        cmbMode.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                txtIv.setVisible(!AesUtil.AES_ECB.equals(newValue));
                txtIv.setText(null);
            }
        });
    }

    private Charset getSelectedCharset() {
        return Charset.forName(StringUtil.defaultIfBlank(cmbCharsetName.getValue(), StandardCharsets.UTF_8.name()));
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
        String fromText = this.txtFrom.getText();
        if (StringUtil.isBlank(fromText)) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "输入内容为空");
            return;
        }
        if (StringUtil.isBlank(this.txtKey.getText())) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "密钥内容为空");
            return;
        }
        if (this.txtIv.isVisible() && StringUtil.isBlank(this.txtIv.getText())) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "向量内容为空");
            return;
        }

        byte[] input = fromText.getBytes(this.getSelectedCharset());
        byte[] key = this.txtKey.getText().getBytes(this.getSelectedCharset());
        byte[] iv = this.txtIv.isVisible() ? this.txtIv.getText().getBytes(this.getSelectedCharset()) : null;

        try {
            byte[] output = AesUtil.encrypt(cmbMode.getValue(), input, key, iv);
            this.txtTo.setText(new String(Base64.getEncoder().encode(output), this.getSelectedCharset()));
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtil.getStackTrace(e));
        }
    }

    @FXML
    public void btnDecryptOnAction(ActionEvent event) {
        String fromText = this.txtFrom.getText();
        if (StringUtil.isBlank(fromText)) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "输入内容为空");
            return;
        }
        if (StringUtil.isBlank(this.txtKey.getText())) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "密钥内容为空");
            return;
        }
        if (this.txtIv.isVisible() && StringUtil.isBlank(this.txtIv.getText())) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "向量内容为空");
            return;
        }

        byte[] input = Base64.getDecoder().decode(fromText.getBytes(this.getSelectedCharset()));
        byte[] key = this.txtKey.getText().getBytes(this.getSelectedCharset());
        byte[] iv = this.txtIv.isVisible() ? this.txtIv.getText().getBytes(this.getSelectedCharset()) : null;

        try {
            byte[] output = AesUtil.decrypt(cmbMode.getValue(), input, key, iv);
            this.txtTo.setText(new String(output, this.getSelectedCharset()));
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtil.getStackTrace(e));
        }
    }
}
