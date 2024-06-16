package com.a.b.c.d.pangolin.tool.controller.crypto;

import com.a.b.c.d.pangolin.tool.util.AlertUtil;
import com.a.b.c.d.pangolin.tool.util.GridPaneUtil;
import com.a.b.c.d.pangolin.util.ExceptionUtil;
import com.a.b.c.d.pangolin.util.StringUtil;
import com.a.b.c.d.pangolin.util.crypto.RsaUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.Arrays;
import java.util.Base64;
import java.util.ResourceBundle;

public class RsaController implements Initializable {
    @FXML
    private TextArea txtFrom;
    @FXML
    private TextArea txtTo;
    @FXML
    private ComboBox<String> cmbCharsetName;
    @FXML
    private ComboBox<Integer> cmbKeyLen;
    @FXML
    private ComboBox<String> cmbSignType;
    @FXML
    private TextArea txtPubKey;
    @FXML
    private TextArea txtPriKey;
    @FXML
    private TextField txtRandom;
    @FXML
    private GridPane gridPane;
    @FXML
    private GridPane gridPaneContent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbCharsetName.getItems().addAll(StandardCharsets.UTF_8.name(), "GB2312", StandardCharsets.UTF_16.name(), StandardCharsets.ISO_8859_1.name());
        cmbCharsetName.getSelectionModel().selectFirst();

        cmbKeyLen.getItems().addAll(Arrays.stream(RsaUtil.KeyLenEnum.values()).map(RsaUtil.KeyLenEnum::getLen).toList());
        cmbKeyLen.getSelectionModel().selectFirst();

        cmbSignType.getItems().addAll(Arrays.stream(RsaUtil.RsaSignTypeEnum.values()).map(RsaUtil.RsaSignTypeEnum::getCode).toList());
        cmbSignType.getSelectionModel().selectFirst();

        // gridPane宽高自适应
        GridPaneUtil.setAutoPercentWidth(this.gridPane);
        GridPaneUtil.setAutoPercentHeight(this.gridPane);

        // gridPaneContent宽高自适应
        GridPaneUtil.setAutoPercentWidth(this.gridPaneContent);
    }

    private Charset getSelectedCharset() {
        return Charset.forName(StringUtil.defaultIfBlank(cmbCharsetName.getValue(), StandardCharsets.UTF_8.name()));
    }

    private RsaUtil.RsaSignTypeEnum getSelectedSignType() {
        return ObjectUtils.defaultIfNull(RsaUtil.RsaSignTypeEnum.getItemByCode(cmbSignType.getValue()), RsaUtil.RsaSignTypeEnum.RSA_SIGNATURE_ALGORITHM);
    }

    @FXML
    public void btnGenKeyOnAction(ActionEvent event) {
        try {
            byte[] random = StringUtils.isNotBlank(this.txtRandom.getText()) ? this.txtRandom.getText().getBytes(this.getSelectedCharset()) : new byte[0];
            KeyPair keyPair = RsaUtil.genKeyPair(this.cmbKeyLen.getValue(), random);

            this.txtPubKey.setText(new String(Base64.getEncoder().encode(keyPair.getPublic().getEncoded()), this.getSelectedCharset()));
            this.txtPriKey.setText(new String(Base64.getEncoder().encode(keyPair.getPrivate().getEncoded()), this.getSelectedCharset()));
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtil.getStackTrace(e));
        }
    }

    @FXML
    public void btnPubEncryptOnAction(ActionEvent event) {
        String fromText = this.txtFrom.getText();
        if (StringUtil.isBlank(fromText)) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "明文内容为空");
            return;
        }
        if (StringUtil.isBlank(this.txtPubKey.getText())) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "公钥内容为空");
            return;
        }

        try {
            byte[] input = fromText.getBytes(this.getSelectedCharset());
            byte[] pubKey = Base64.getDecoder().decode(this.txtPubKey.getText().getBytes(this.getSelectedCharset()));
            byte[] output = RsaUtil.encryptByPubKey(input, pubKey);

            this.txtTo.setText(new String(Base64.getEncoder().encode(output), this.getSelectedCharset()));
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtil.getStackTrace(e));
        }
    }

    @FXML
    public void btnPriDecryptOnAction(ActionEvent event) {
        String toText = this.txtTo.getText();
        if (StringUtil.isBlank(toText)) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "密文内容为空");
            return;
        }
        if (StringUtil.isBlank(this.txtPriKey.getText())) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "私钥内容为空");
            return;
        }

        try {
            byte[] input = Base64.getDecoder().decode(toText.getBytes(this.getSelectedCharset()));
            byte[] priKey = Base64.getDecoder().decode(this.txtPriKey.getText().getBytes(this.getSelectedCharset()));
            byte[] output = RsaUtil.decryptByPriKey(input, priKey);

            this.txtFrom.setText(new String(output, this.getSelectedCharset()));
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtil.getStackTrace(e));
        }
    }


    @FXML
    public void btnPriEncryptOnAction(ActionEvent event) {
        String fromText = this.txtFrom.getText();
        if (StringUtil.isBlank(fromText)) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "明文内容为空");
            return;
        }
        if (StringUtil.isBlank(this.txtPriKey.getText())) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "私钥内容为空");
            return;
        }

        try {
            byte[] input = fromText.getBytes(this.getSelectedCharset());
            byte[] priKey = Base64.getDecoder().decode(this.txtPriKey.getText().getBytes(this.getSelectedCharset()));
            byte[] output = RsaUtil.encryptByPriKey(input, priKey);

            this.txtTo.setText(new String(Base64.getEncoder().encode(output), this.getSelectedCharset()));
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtil.getStackTrace(e));
        }
    }

    @FXML
    public void btnPubDecryptOnAction(ActionEvent event) {
        String toText = this.txtTo.getText();
        if (StringUtil.isBlank(toText)) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "密文内容为空");
            return;
        }
        if (StringUtil.isBlank(this.txtPubKey.getText())) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "公钥内容为空");
            return;
        }

        try {
            byte[] input = Base64.getDecoder().decode(toText.getBytes(this.getSelectedCharset()));
            byte[] pubKey = Base64.getDecoder().decode(this.txtPubKey.getText().getBytes(this.getSelectedCharset()));
            byte[] output = RsaUtil.decryptByPubKey(input, pubKey);

            this.txtFrom.setText(new String(output, this.getSelectedCharset()));
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtil.getStackTrace(e));
        }
    }

    @FXML
    public void btnPriSinOnAction(ActionEvent event) {
        String fromText = this.txtFrom.getText();
        if (StringUtil.isBlank(fromText)) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "明文内容为空");
            return;
        }
        if (StringUtil.isBlank(this.txtPriKey.getText())) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "私钥内容为空");
            return;
        }

        try {
            byte[] input = fromText.getBytes(this.getSelectedCharset());
            byte[] priKey = Base64.getDecoder().decode(this.txtPriKey.getText().getBytes(this.getSelectedCharset()));
            byte[] output = RsaUtil.sign(input, priKey, this.getSelectedSignType());

            this.txtTo.setText(new String(Base64.getEncoder().encode(output), this.getSelectedCharset()));
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtil.getStackTrace(e));
        }
    }

    @FXML
    public void btnPubVerifyOnAction(ActionEvent event) {
        String fromText = this.txtFrom.getText();
        if (StringUtil.isBlank(fromText)) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "明文内容为空");
            return;
        }
        String toText = this.txtTo.getText();
        if (StringUtil.isBlank(toText)) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "密文内容为空");
            return;
        }
        if (StringUtil.isBlank(this.txtPubKey.getText())) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "公钥内容为空");
            return;
        }

        try {
            byte[] input = fromText.getBytes(this.getSelectedCharset());
            byte[] sign = Base64.getDecoder().decode(toText.getBytes(this.getSelectedCharset()));

            byte[] pubKey = Base64.getDecoder().decode(this.txtPubKey.getText().getBytes(this.getSelectedCharset()));
            boolean result = RsaUtil.verify(input, sign, pubKey, this.getSelectedSignType());

            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "结果", String.valueOf(result));
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtil.getStackTrace(e));
        }
    }
}
