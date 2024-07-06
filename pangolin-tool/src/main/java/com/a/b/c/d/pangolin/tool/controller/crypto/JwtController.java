package com.a.b.c.d.pangolin.tool.controller.crypto;

import com.a.b.c.d.pangolin.tool.util.AlertUtil;
import com.a.b.c.d.pangolin.tool.util.GridPaneUtil;
import com.a.b.c.d.pangolin.util.ExceptionUtil;
import com.a.b.c.d.pangolin.util.JacksonUtil;
import com.a.b.c.d.pangolin.util.bean.JwtKeyDTO;
import com.a.b.c.d.pangolin.util.crypto.JwtUtil;
import com.a.b.c.d.pangolin.util.jwt.JwtFactoryUtil;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class JwtController implements Initializable {
    @FXML
    private GridPane gridPane;
    @FXML
    private GridPane gridClaims;
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
    private TextArea txtToken;
    @FXML
    private TextField txtJwtID;
    @FXML
    private TextField txtIssuer;
    @FXML
    private TextField txtIssueTime;
    @FXML
    private TextField txtNotBeforeTime;
    @FXML
    private TextField txtExpirationTime;
    @FXML
    private TextField txtAudience;
    @FXML
    private TextArea txtSubject;

    private JwtKeyDTO jwkKey;

    private static final long TWO_HOUR_MS = TimeUnit.MILLISECONDS.convert(2, TimeUnit.HOURS);


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
                jwkKey = null;
            }
        });
        this.cmbSignAlgorithm.getSelectionModel().selectFirst();
        this.txtJwtID.setText(UUID.randomUUID().toString());
        // 格式化输入
        this.txtIssueTime.setTextFormatter(new TextFormatter<Long>(new StringConverter<Long>() {
            @Override
            public String toString(Long object) {
                return String.valueOf(object);
            }

            @Override
            public Long fromString(String string) {
                return NumberUtils.toLong(string, System.currentTimeMillis());
            }
        }, System.currentTimeMillis()));
        this.txtNotBeforeTime.setTextFormatter(new TextFormatter<Long>(new StringConverter<Long>() {
            @Override
            public String toString(Long object) {
                return String.valueOf(object);
            }

            @Override
            public Long fromString(String string) {
                return NumberUtils.toLong(string, System.currentTimeMillis());
            }
        }, System.currentTimeMillis()));
        this.txtExpirationTime.setTextFormatter(new TextFormatter<Long>(new StringConverter<Long>() {
            @Override
            public String toString(Long object) {
                return String.valueOf(object);
            }

            @Override
            public Long fromString(String string) {
                return NumberUtils.toLong(string, System.currentTimeMillis() + TWO_HOUR_MS);
            }
        }, System.currentTimeMillis() + TWO_HOUR_MS));


        // gridPane宽高自适应
        GridPaneUtil.setAutoPercentWidth(this.gridPane);
        GridPaneUtil.setAutoPercentHeight(this.gridPane);

        // gridPaneContent宽高自适应
        GridPaneUtil.setAutoPercentWidth(this.gridPaneContent);

        // gridClaims
        GridPaneUtil.setAutoPercentWidth(this.gridClaims);
    }

    public void btnGenKeyOnAction(ActionEvent event) {
        try {
            JWSAlgorithm algorithm = JWSAlgorithm.parse(this.cmbSignAlgorithm.getValue());
            jwkKey = JwtFactoryUtil.genKey(algorithm);
            this.txtSecret.setText(jwkKey.getSecret());
            if (Objects.nonNull(jwkKey.getJwkKey())) {
                this.txtPubKey.setText(new String(Base64.getEncoder().encode(jwkKey.getJwkKey().toPublicKey().getEncoded())));
                this.txtPriKey.setText(new String(Base64.getEncoder().encode(jwkKey.getJwkKey().toPrivateKey().getEncoded())));
            }
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtil.getStackTrace(e));
        }
    }

    public void btnEncryptOnAction(ActionEvent event) {
        if (Objects.isNull(this.jwkKey)) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "密钥内容为空");
            return;
        }
        if (StringUtils.isBlank(this.txtIssueTime.getText())) {
            this.txtIssueTime.requestFocus();
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "issueTime内容为空");
            return;
        }
        if (StringUtils.isBlank(this.txtNotBeforeTime.getText())) {
            this.txtNotBeforeTime.requestFocus();
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "notBeforeTime内容为空");
            return;
        }
        if (StringUtils.isBlank(this.txtExpirationTime.getText())) {
            this.txtExpirationTime.requestFocus();
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "expirationTime内容为空");
            return;
        }
        if (StringUtils.isBlank(this.txtSubject.getText())) {
            this.txtSubject.requestFocus();
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "subject内容为空");
            return;
        }

        try {
            JWSAlgorithm algorithm = JWSAlgorithm.parse(this.cmbSignAlgorithm.getValue());
            JWSSigner signer = JwtFactoryUtil.buildSigner(algorithm, this.jwkKey);
            JWSHeader header = JwtUtil.buildHeader(algorithm);

            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .jwtID(this.txtJwtID.getText())
                    .issuer(this.txtIssuer.getText())
                    .issueTime(new Date(NumberUtils.toLong(this.txtIssueTime.getText(), 0L)))
                    .notBeforeTime(new Date(NumberUtils.toLong(this.txtNotBeforeTime.getText(), 0L)))
                    .expirationTime(new Date(NumberUtils.toLong(this.txtExpirationTime.getText(), 0L)))
                    .subject(this.txtSubject.getText())
                    .audience(this.txtAudience.getText())
                    .build();

            String token = JwtFactoryUtil.serialize(algorithm, signer, header, claims);
            this.txtToken.setText(token);
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtil.getStackTrace(e));
        }
    }

    public void btnDecryptOnAction(ActionEvent event) {
        if (StringUtils.isBlank(this.txtToken.getText())) {
            this.txtToken.requestFocus();
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "Token内容为空");
            return;
        }

        try {
            JWSAlgorithm algorithm = JWSAlgorithm.parse(this.cmbSignAlgorithm.getValue());
            JWTClaimsSet claims = JwtFactoryUtil.deserialize(algorithm, this.txtToken.getText());
            AlertUtil.showAlert(Alert.AlertType.ERROR, "结果", JacksonUtil.formatPretty(claims.toPayload().toString()));
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtil.getStackTrace(e));
        }
    }

    public void btnVerifyOnAction(ActionEvent event) {
        if (Objects.isNull(this.jwkKey)) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "密钥内容为空");
            return;
        }
        if (StringUtils.isBlank(this.txtToken.getText())) {
            this.txtToken.requestFocus();
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "Token内容为空");
            return;
        }

        try {
            JWSAlgorithm algorithm = JWSAlgorithm.parse(this.cmbSignAlgorithm.getValue());
            JWSVerifier verifier = JwtFactoryUtil.buildVerifier(algorithm, this.jwkKey);

            boolean suc = JwtFactoryUtil.verify(algorithm, this.txtToken.getText(), verifier);
            AlertUtil.showAlert(Alert.AlertType.ERROR, "提示", String.valueOf(suc));
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtil.getStackTrace(e));
        }
    }
}
