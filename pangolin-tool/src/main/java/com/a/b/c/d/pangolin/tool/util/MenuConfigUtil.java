package com.a.b.c.d.pangolin.tool.util;

import com.a.b.c.d.pangolin.tool.MainApplication;
import com.a.b.c.d.pangolin.util.CollectionUtils;
import com.a.b.c.d.pangolin.util.FunctionUtil;
import com.a.b.c.d.pangolin.util.Lists;
import com.a.b.c.d.pangolin.util.bean.MenuConfigDTO;
import com.alibaba.fastjson2.JSON;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class MenuConfigUtil {
    private static final String TYPE_MENU = Menu.class.getSimpleName();
    private static final String TYPE_MENU_ITEM = MenuItem.class.getSimpleName();
    private static final String TYPE_SEPARATOR_MENU_ITEM = SeparatorMenuItem.class.getSimpleName();

    private static final String PREFIX_MENU_ID = "menu_";
    private static final String PREFIX_TAB_ID = "tab_";

    private MenuBar menuBar;
    private TabPane tabPane;
    private TextArea txtMsg;
    private Class baseClass;
    private List<MenuConfigDTO> list;

    private MenuConfigUtil() {
    }

    public static MenuConfigUtil build(MenuBar menuBar, TabPane tabPane, TextArea txtMsg, Class baseClass) {
        MenuConfigUtil util = new MenuConfigUtil();
        util.menuBar = menuBar;
        util.tabPane = tabPane;
        util.txtMsg = txtMsg;
        util.baseClass = baseClass;
        return util;
    }

    public void initMenu(InputStream is) {
        String json = null;
        try {
            json = IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (Exception e) {
            this.txtMsg.appendText(ExceptionUtils.getStackTrace(e));
        }
        this.list = JSON.parseArray(json, MenuConfigDTO.class);

        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        for (MenuConfigDTO item : list) {
            Menu menu = parseMenu(item);
            if (Objects.nonNull(menu)) {
                menuBar.getMenus().add(menu);
            }
        }
    }


    private String convertMenuId2TabId(String menuId) {
        return StringUtils.replace(menuId, PREFIX_MENU_ID, PREFIX_TAB_ID);
    }

    private Menu parseMenu(MenuConfigDTO item) {
        if (Objects.isNull(item)) {
            return null;
        }

        if (TYPE_MENU.equalsIgnoreCase(item.getType())) {
            Menu menu = new Menu(item.getName());
            menu.setId(PREFIX_MENU_ID + item.getId());
            // 递归
            menu.getItems().setAll(parseMenuItem(item.getChildren()));
            return menu;
        }
        return null;
    }

    private Collection<MenuItem> parseMenuItem(Collection<MenuConfigDTO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Lists.newArrayList();
        }

        return FunctionUtil.toList(list, this::parseMenuItem);
    }

    private MenuItem parseMenuItem(MenuConfigDTO item) {
        if (Objects.isNull(item)) {
            return null;
        }

        if (TYPE_MENU.equalsIgnoreCase(item.getType())) {
            Menu menu = new Menu(item.getName());
            menu.setId(PREFIX_MENU_ID + item.getId());
            menu.getItems().setAll(parseMenuItem(item.getChildren()));
            return menu;
        } else if (TYPE_MENU_ITEM.equalsIgnoreCase(item.getType())) {
            MenuItem menu = new MenuItem(item.getName());
            menu.setId(PREFIX_MENU_ID + item.getId());
            menu.setOnAction((event) -> {
                Object source = event.getSource();
                if (source instanceof MenuItem) {
                    this.addTab((MenuItem) source);
                }
            });
            return menu;
        }

        return null;
    }

    private void addTab(MenuItem source) {
        String tabId = convertMenuId2TabId(source.getId());
        Tab findOne = tabPane.getTabs().stream()
                .filter(Objects::nonNull)
                .filter(it -> Objects.equals(tabId, it.getId()))
                .findFirst()
                .orElse(null);
        if (Objects.nonNull(findOne)) {
            tabPane.getSelectionModel().select(findOne);
        } else {
            try {
                Tab tab = new Tab();
                tab.setId(tabId);
                tab.setText(source.getText());
                FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("encode/base64-view.fxml"));
                Node node = fxmlLoader.load();
                tab.setContent(node);
                tabPane.getTabs().add(tab);
                tabPane.getSelectionModel().select(tab);
                node.prefWidth(tabPane.getWidth());
                node.prefHeight(tabPane.getHeight());

            } catch (Exception e) {
                this.txtMsg.appendText(ExceptionUtils.getStackTrace(e));
            }
        }
    }
}