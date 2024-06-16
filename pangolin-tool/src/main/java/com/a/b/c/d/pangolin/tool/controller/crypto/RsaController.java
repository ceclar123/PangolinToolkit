package com.a.b.c.d.pangolin.tool.controller.crypto;

import com.a.b.c.d.pangolin.tool.util.AlertUtil;
import com.a.b.c.d.pangolin.util.ExceptionUtil;
import com.a.b.c.d.pangolin.util.StringUtil;
import com.a.b.c.d.pangolin.util.crypto.RsaUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
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
    private TextArea txtPubKey;
    @FXML
    private TextArea txtPriKey;
    @FXML
    private TextField txtRandom;
    @FXML
    private SplitPane spKey;
    @FXML
    private SplitPane spContent;
    @FXML
    private GridPane gridPane;

    private static final int BTN_CONTROL_BASE_WIDTH = 60;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbCharsetName.getItems().addAll(StandardCharsets.UTF_8.name(), "GB2312", StandardCharsets.UTF_16.name(), StandardCharsets.ISO_8859_1.name());
        cmbCharsetName.getSelectionModel().selectFirst();

        cmbKeyLen.getItems().addAll(Arrays.stream(RsaUtil.KeyLenEnum.values()).map(RsaUtil.KeyLenEnum::getLen).toList());
        cmbKeyLen.getSelectionModel().selectFirst();
//        gridPane.heightProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                spContent.setPrefHeight(gridPane.getRowConstraints().getLast().getPrefHeight());
//            }
//        });

//        spContent.widthProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                double width = spContent.getWidth();
//                double s1 = ((width - BTN_CONTROL_BASE_WIDTH) / 2) / width;
//                double s2 = ((width - BTN_CONTROL_BASE_WIDTH) / 2 + BTN_CONTROL_BASE_WIDTH) / width;
//                spContent.setDividerPositions(s1, s2, 1);
//            }
//        });

        this.txtPubKey.prefWidthProperty().bind(this.spKey.widthProperty().divide(2));
        this.txtPriKey.prefWidthProperty().bind(this.spKey.widthProperty().divide(2));

//        this.txtFrom.prefWidthProperty().bind(this.spContent.widthProperty().subtract(BTN_CONTROL_BASE_WIDTH).divide(2));
//        this.txtTo.prefWidthProperty().bind(this.spContent.widthProperty().subtract(BTN_CONTROL_BASE_WIDTH).divide(2));
    }

    private Charset getSelectedCharset() {
        return Charset.forName(StringUtil.defaultIfBlank(cmbCharsetName.getValue(), StandardCharsets.UTF_8.name()));
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
    public void btnEncryptOnAction(ActionEvent event) {
        String fromText = this.txtFrom.getText();
        if (StringUtil.isBlank(fromText)) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "输入内容为空");
            return;
        }
        if (StringUtil.isBlank(this.txtPubKey.getText())) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "密钥内容为空");
            return;
        }

        byte[] input = fromText.getBytes(this.getSelectedCharset());
        byte[] pubKey = this.txtPubKey.getText().getBytes(this.getSelectedCharset());


        try {
            byte[] output = RsaUtil.encrypt(input, pubKey);
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
        if (StringUtil.isBlank(this.txtPriKey.getText())) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "密钥内容为空");
            return;
        }

        byte[] input = Base64.getDecoder().decode(fromText.getBytes(this.getSelectedCharset()));
        byte[] priKey = this.txtPriKey.getText().getBytes(this.getSelectedCharset());

        try {
            byte[] output = RsaUtil.decrypt(input, priKey);
            this.txtTo.setText(new String(output, this.getSelectedCharset()));
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtil.getStackTrace(e));
        }
    }
}
