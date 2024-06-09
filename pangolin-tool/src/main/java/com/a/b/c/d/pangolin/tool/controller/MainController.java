package com.a.b.c.d.pangolin.tool.controller;

import com.a.b.c.d.pangolin.tool.util.MenuConfigUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private MenuBar menuBar;

    @FXML
    private TabPane tabPane;

    @FXML
    private TextArea txtMsg;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MenuConfigUtil.build(menuBar, tabPane, txtMsg).initMenu(MainController.class.getResourceAsStream("/config/menu.json"));
    }
}