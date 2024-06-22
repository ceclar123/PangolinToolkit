package com.a.b.c.d.pangolin.tool.controller.json;

import com.a.b.c.d.pangolin.tool.util.AlertUtil;
import com.a.b.c.d.pangolin.util.ExceptionUtil;
import com.a.b.c.d.pangolin.util.FreemarkerUtil;
import com.a.b.c.d.pangolin.util.StringUtil;
import com.a.b.c.d.pangolin.util.bean.EntityTypeDTO;
import com.a.b.c.d.pangolin.util.bean.FreemarkerEntityParamDTO;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ConvertEntityController implements Initializable {
    @FXML
    private TextArea txtFrom;
    @FXML
    private TextArea txtTo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void btnJavaEntityOnAction(ActionEvent event) {
        String input = this.txtFrom.getText();
        if (StringUtil.isBlank(input)) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "输入内容为空");
            return;
        }

        try {
            Map<String, Object> map = JSON.parseObject(input, new TypeReference<LinkedHashMap<String, Object>>() {
            });
            List<EntityTypeDTO> typeList = FreemarkerUtil.buildJavaEntityTypeList(map);
            FreemarkerEntityParamDTO paramDTO = new FreemarkerEntityParamDTO();
            paramDTO.setParamList(typeList);

            this.txtTo.setText(FreemarkerUtil.toEntityString(paramDTO, FreemarkerUtil.TPL_JAVA_ENTITY));
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtil.getStackTrace(e));
        }
    }

    @FXML
    public void btnCSharpEntityOnAction(ActionEvent event) {
        String input = this.txtFrom.getText();
        if (StringUtil.isBlank(input)) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "输入内容为空");
            return;
        }

        try {
            Map<String, Object> map = JSON.parseObject(input, new TypeReference<LinkedHashMap<String, Object>>() {
            });
            List<EntityTypeDTO> typeList = FreemarkerUtil.buildCSharpEntityTypeList(map);
            FreemarkerEntityParamDTO paramDTO = new FreemarkerEntityParamDTO();
            paramDTO.setParamList(typeList);

            this.txtTo.setText(FreemarkerUtil.toEntityString(paramDTO, FreemarkerUtil.TPL_CSHARP_ENTITY));
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtil.getStackTrace(e));
        }
    }
}
