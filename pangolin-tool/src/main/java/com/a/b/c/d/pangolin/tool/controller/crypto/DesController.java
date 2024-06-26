package com.a.b.c.d.pangolin.tool.controller.crypto;

import com.a.b.c.d.pangolin.tool.util.AlertUtil;
import com.a.b.c.d.pangolin.tool.util.GridPaneUtil;
import com.a.b.c.d.pangolin.util.ExceptionUtil;
import com.a.b.c.d.pangolin.util.StringUtil;
import com.a.b.c.d.pangolin.util.crypto.DesUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DesController implements Initializable {
    @FXML
    private TextArea txtFrom;
    @FXML
    private TextArea txtTo;
    @FXML
    private ComboBox<String> cmbCharsetName;
    @FXML
    private ComboBox cmbMode;
    @FXML
    private ComboBox<String> cmbKeyLen;
    @FXML
    private ComboBox<Integer> cmbIvLen;
    @FXML
    private TextField txtKey;
    @FXML
    private TextField txtIv;
    @FXML
    private GridPane gridPane;
    @FXML
    private GridPane gridPaneContent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbIvLen.getItems().addAll(DesUtil.IV_LENGTH_8);
        cmbIvLen.getSelectionModel().selectFirst();

        cmbCharsetName.getItems().addAll(StandardCharsets.UTF_8.name(), "GB2312", StandardCharsets.UTF_16.name(), StandardCharsets.ISO_8859_1.name());
        cmbCharsetName.getSelectionModel().selectFirst();

        cmbKeyLen.getItems().addAll(Arrays.stream(DesUtil.KeyLenEnum.values()).map(DesUtil.KeyLenEnum::getLen).map(String::valueOf).toList());
        cmbKeyLen.getSelectionModel().selectFirst();

        List<Object> cmbList = new ArrayList<Object>();
        cmbList.addAll(DesUtil.SYS_MODE_LIST_CBC);
        cmbList.add(new Separator());
        cmbList.addAll(DesUtil.SYS_MODE_LIST_CFB);
        cmbList.add(new Separator());
        cmbList.addAll(DesUtil.SYS_MODE_LIST_ECB);
        cmbList.add(new Separator());
        cmbList.addAll(DesUtil.SYS_MODE_LIST_OFB);
        cmbList.add(new Separator());
        cmbList.addAll(DesUtil.SYS_MODE_LIST_PCBC);
        cmbList.add(new Separator());
        cmbList.addAll(DesUtil.SYS_MODE_LIST_CTR);
        cmbMode.getItems().addAll(cmbList);
        cmbMode.getSelectionModel().selectFirst();
        // ECB模式不需要向量
        cmbMode.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                txtIv.setText(null);
                boolean isECBMode = StringUtils.isNotBlank(newValue) && DesUtil.SYS_MODE_LIST_ECB.contains(newValue);
                if (isECBMode) {
                    txtIv.setVisible(false);
                } else {
                    txtIv.setVisible(true);
                }
            }
        });

        // gridPane宽高自适应
        GridPaneUtil.setAutoPercentWidth(this.gridPane);
        GridPaneUtil.setAutoPercentHeight(this.gridPane);

        // gridPaneContent宽高自适应
        GridPaneUtil.setAutoPercentWidth(this.gridPaneContent);
    }

    private Charset getSelectedCharset() {
        return Charset.forName(StringUtil.defaultIfBlank(cmbCharsetName.getValue(), StandardCharsets.UTF_8.name()));
    }

    @FXML
    public void btnGenKeyOnAction(ActionEvent event) {
        this.txtKey.setText(StringUtil.getRandomString(NumberUtils.toInt(cmbKeyLen.getValue(), 0)));
    }

    @FXML
    public void btnGenIvOnAction(ActionEvent event) {
        this.txtIv.setText(StringUtil.getRandomString(cmbIvLen.getValue()));
    }

    @FXML
    public void btnEncryptOnAction(ActionEvent event) {
        String fromText = this.txtFrom.getText();
        if (StringUtil.isBlank(fromText)) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "明文内容为空");
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

        try {
            byte[] input = fromText.getBytes(this.getSelectedCharset());
            byte[] key = this.txtKey.getText().getBytes(this.getSelectedCharset());
            byte[] iv = this.txtIv.isVisible() ? this.txtIv.getText().getBytes(this.getSelectedCharset()) : new byte[0];

            byte[] output = DesUtil.encrypt(cmbMode.getValue().toString(), input, key, iv);
            this.txtTo.setText(new String(Base64.getEncoder().encode(output), this.getSelectedCharset()));
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtil.getStackTrace(e));
        }
    }

    @FXML
    public void btnDecryptOnAction(ActionEvent event) {
        String toText = this.txtTo.getText();
        if (StringUtil.isBlank(toText)) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "密文内容为空");
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

        try {
            byte[] input = Base64.getDecoder().decode(toText.getBytes(this.getSelectedCharset()));
            byte[] key = this.txtKey.getText().getBytes(this.getSelectedCharset());
            byte[] iv = this.txtIv.isVisible() ? this.txtIv.getText().getBytes(this.getSelectedCharset()) : new byte[0];

            byte[] output = DesUtil.decrypt(cmbMode.getValue().toString(), input, key, iv);
            this.txtFrom.setText(new String(output, this.getSelectedCharset()));
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtil.getStackTrace(e));
        }
    }
}
