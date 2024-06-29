package com.a.b.c.d.pangolin.tool.controller.crypto;

import com.a.b.c.d.pangolin.tool.util.AlertUtil;
import com.a.b.c.d.pangolin.tool.util.GridPaneUtil;
import com.a.b.c.d.pangolin.util.ExceptionUtil;
import com.a.b.c.d.pangolin.util.StringUtil;
import com.a.b.c.d.pangolin.util.crypto.RsaUtil;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.crypto.MACSigner;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.security.KeyPair;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import java.util.ResourceBundle;

public class JwtController implements Initializable {
    @FXML
    private GridPane gridPane;
    @FXML
    private GridPane gridPaneContent;
    @FXML
    private ComboBox<String> cmbSignAlgorithm;
    @FXML
    private TitledPane tpOneKey;
    @FXML
    private SplitPane spTwoKey;
    @FXML
    private TextArea txtPubKey;
    @FXML
    private TextArea txtPriKey;
    @FXML
    private TextArea txtSecret;
    @FXML
    private TextArea txtPayload;
    @FXML
    private TextArea txtToken;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.cmbSignAlgorithm.getItems().addAll(
                Arrays.stream(JWSAlgorithm.class.getDeclaredFields())
                        .filter(Objects::nonNull)
                        .filter(it -> Modifier.isPublic(it.getModifiers()) && Modifier.isStatic(it.getModifiers()) && Modifier.isFinal(it.getModifiers()) && it.getType() == JWSAlgorithm.class)
                        .map(Field::getName)
                        .toList()
        );
        this.cmbSignAlgorithm.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                JWSAlgorithm algorithm = StringUtils.isNotBlank(newValue) ? JWSAlgorithm.parse(newValue) : null;
                if (Objects.isNull(algorithm)) {
                    return;
                }
                boolean singleKey = JWSAlgorithm.Family.HMAC_SHA.contains(algorithm);
                spTwoKey.setVisible(!singleKey);
                tpOneKey.setVisible(singleKey);
                if (singleKey) {
                    txtSecret.setText(null);
                } else {
                    txtPubKey.setText(null);
                    txtPriKey.setText(null);
                }
            }
        });
        this.cmbSignAlgorithm.getSelectionModel().selectFirst();

        // gridPane宽高自适应
        GridPaneUtil.setAutoPercentWidth(this.gridPane);
        GridPaneUtil.setAutoPercentHeight(this.gridPane);

        // gridPaneContent宽高自适应
        GridPaneUtil.setAutoPercentWidth(this.gridPaneContent);
    }

    public void btnGenKeyOnAction(ActionEvent event) {
        try {
            JWSAlgorithm algorithm = JWSAlgorithm.parse(this.cmbSignAlgorithm.getValue());
            if (JWSAlgorithm.Family.HMAC_SHA.contains(algorithm)) {
                int len = MACSigner.getMinRequiredSecretLength(algorithm);
                this.txtSecret.setText(StringUtil.getRandomString(len));
            } else if (JWSAlgorithm.Family.RSA.contains(algorithm)) {
                int len = StringUtil.getNumber(algorithm.getName());
                KeyPair pair = RsaUtil.genKeyPair(len, null);
                this.txtPubKey.setText(new String(Base64.getEncoder().encode(pair.getPublic().getEncoded())));
                this.txtPriKey.setText(new String(Base64.getEncoder().encode(pair.getPrivate().getEncoded())));
            }
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtil.getStackTrace(e));
        }

    }

    public void btnEncryptOnAction(ActionEvent event) {
    }

    public void btnDecryptOnAction(ActionEvent event) {
    }

    public void btnVerifyOnAction(ActionEvent event) {
    }
}
