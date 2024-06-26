package com.a.b.c.d.pangolin.tool.controller.json;

import com.a.b.c.d.pangolin.tool.MainApplication;
import com.a.b.c.d.pangolin.tool.util.AlertUtil;
import com.a.b.c.d.pangolin.util.ExceptionUtil;
import com.a.b.c.d.pangolin.util.StringUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.TypeReference;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.stage.FileChooser;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class JsonConvertCsvController implements Initializable {
    @FXML
    private TextArea txtFrom;
    @FXML
    private TextField txtDelimiter;
    @FXML
    private TableView<Map<String, String>> tbContent;


    private static final String INDEX_COL_NAME = "_$ix$_";

    private List<Map<String, String>> mapList = null;

    private List<String> colList = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.txtDelimiter.setText(StringEscapeUtils.escapeJava("\t"));
    }

    private char getDelimiter() {
        char delimiter = '\t';
        if (StringUtils.isNotBlank(this.txtDelimiter.getText())) {
            String txt = StringEscapeUtils.unescapeJava(this.txtDelimiter.getText().trim());
            if (Objects.nonNull(txt)) {
                char[] chars = txt.toCharArray();
                if (chars.length > 0) {
                    delimiter = chars[0];
                }
            }
        }
        return delimiter;
    }

    private List<String> getColumnList(List<Map<String, String>> mapList) {
        List<String> rtnList = new ArrayList<String>();
        if (CollectionUtils.isEmpty(mapList)) {
            return rtnList;
        }

        // 第一列为主
        Map<String, String> header = mapList.getFirst();
        for (Map.Entry<String, String> entry : header.entrySet()) {
            if (INDEX_COL_NAME.equalsIgnoreCase(entry.getKey())) {
                continue;
            }
            rtnList.add(entry.getKey());
        }

        // 其他数据
        rtnList.addAll(
                mapList.stream()
                        .filter(MapUtils::isNotEmpty)
                        .skip(1)
                        .flatMap(it -> it.keySet().stream())
                        .filter(StringUtils::isNotBlank)
                        .filter(it -> !INDEX_COL_NAME.equalsIgnoreCase(it))
                        .distinct()
                        .toList()
        );

        return rtnList.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }

    @FXML
    public void btnCheckOnAction(ActionEvent event) {
        String input = this.txtFrom.getText();
        if (StringUtil.isBlank(input)) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "输入内容为空");
            return;
        }

        try {
            if (JSON.isValidObject(input)) {
                Map<String, String> map = JSON.parseObject(input, new TypeReference<LinkedHashMap<String, String>>() {
                });
                if (Objects.nonNull(map)) {
                    mapList = List.of(map);
                }
            } else if (JSON.isValidArray(input)) {
                mapList = JSON.parseObject(input, new TypeReference<List<LinkedHashMap<String, String>>>() {
                });
            } else {
                throw new JSONException("JSON内容非法");
            }

            if (CollectionUtils.isEmpty(mapList)) {
                AlertUtil.showAlert(Alert.AlertType.ERROR, "失败", "JSON格式错误");
                return;
            }

            // 序号
            int size = CollectionUtils.size(mapList);
            for (int i = 0; i < size; i++) {
                mapList.get(i).put(INDEX_COL_NAME, String.valueOf(i + 1));
            }

            // 列设置
            tbContent.getColumns().clear();

            // 序号列
            TableColumn<Map<String, String>, String> indexColumn = new TableColumn<Map<String, String>, String>("序号");
            indexColumn.setPrefWidth(50);
            indexColumn.setCellValueFactory(it -> new SimpleStringProperty(it.getValue().get(INDEX_COL_NAME)));
            indexColumn.setStyle("-fx-alignment: CENTER;");
            tbContent.getColumns().add(indexColumn);

            colList = this.getColumnList(mapList);
            for (String colName : colList) {
                if (INDEX_COL_NAME.equalsIgnoreCase(colName)) {
                    continue;
                }
                TableColumn<Map<String, String>, String> column = new TableColumn<Map<String, String>, String>(colName);
                column.setCellValueFactory(new MapValueFactory(colName));
                tbContent.getColumns().add(column);
            }
            tbContent.setItems(FXCollections.observableList(mapList));
            tbContent.refresh();
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtil.getStackTrace(e));
        }
    }

    @FXML
    public void btnExportOnAction(ActionEvent event) {
        if (CollectionUtils.isEmpty(this.mapList)) {
            this.btnCheckOnAction(null);
        }

        if (CollectionUtils.isEmpty(this.mapList)) {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "提示", "导出内容为空");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择导出文件");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV文件（*.csv）", "*.csv"));
        File file = fileChooser.showOpenDialog(MainApplication.PRIMARY_STAGE);
        if (Objects.isNull(file)) {
            return;
        }

        // 分隔符
        char delimiter = this.getDelimiter();
        CSVFormat format = CSVFormat.DEFAULT.builder().setDelimiter(delimiter).setSkipHeaderRecord(true).build();
        String[] headers = this.colList.toArray(String[]::new);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8));
             CSVPrinter printer = new CSVPrinter(writer, format)) {
            //表头
            printer.printRecord(headers);

            // 数据
            for (Map<String, String> map : mapList) {
                printer.printRecord(
                        Arrays.stream(headers).map(it -> {
                            return MapUtils.getString(map, it);
                        }).collect(Collectors.toList())
                );
            }
            printer.flush();

            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "成功", "导出成功");
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtil.getStackTrace(e));
        }
    }
}
