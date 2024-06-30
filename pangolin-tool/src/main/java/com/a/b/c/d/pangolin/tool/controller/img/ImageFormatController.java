package com.a.b.c.d.pangolin.tool.controller.img;

import com.a.b.c.d.pangolin.tool.MainApplication;
import com.a.b.c.d.pangolin.tool.util.AlertUtil;
import com.a.b.c.d.pangolin.util.ExceptionUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ImageFormatController implements Initializable {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private ImageView imgFrom;

    private File fileFrom;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void btnSelectInputOnAction(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择图片");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("所有图片文件", "*.png", "*.jpg", "*.jpeg", "*.bmp"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG图片（*.png）", "*.png"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG图片（*.jpg）", "*.jpg"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG图片（*.jpeg）", "*.jpeg"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("BMP图片（*.bmp）", "*.bmp"));
        File file = fileChooser.showOpenDialog(MainApplication.PRIMARY_STAGE);
        if (Objects.isNull(file)) {
            return;
        }

        try {
            fileFrom = file;
            imgFrom.setImage(new Image(file.toURI().toString()));
            imgFrom.setLayoutX((scrollPane.getWidth() - imgFrom.getImage().getWidth()) / 2);
            imgFrom.setLayoutY((scrollPane.getHeight() - imgFrom.getImage().getHeight()) / 2);
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtil.getStackTrace(e));
        }
    }

    public void btnExportPngOnAction(ActionEvent event) {
        this.export("png");
    }

    public void btnExportJpgOnAction(ActionEvent event) {
        this.export("jpg");
    }

    public void btnExportBmpOnAction(ActionEvent event) {
        this.export("bmp");
    }

    private void export(String fileType) {
        if (Objects.isNull(fileFrom)) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "提示", "请选择原始图片");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择图片");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(fileType.toUpperCase() + "图片（*." + fileType + "）", "*." + fileType));
        File toFile = fileChooser.showOpenDialog(MainApplication.PRIMARY_STAGE);
        if (Objects.isNull(toFile)) {
            return;
        }

        try {
            Mat src = Imgcodecs.imread(fileFrom.getAbsolutePath());
            boolean suc = Imgcodecs.imwrite(toFile.getAbsolutePath(), src);
            AlertUtil.showAlert(Alert.AlertType.ERROR, "提示", suc ? "导出成功" : "导出失败");
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "错误", e.getMessage(), ExceptionUtil.getStackTrace(e));
        }
    }
}
