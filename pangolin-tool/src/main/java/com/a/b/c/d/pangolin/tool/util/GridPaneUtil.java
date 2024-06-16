package com.a.b.c.d.pangolin.tool.util;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.Map;

public class GridPaneUtil {
    private GridPaneUtil() {
    }

    public static final int PERCENT_BASE = 100;

    public static Map<Integer, Double> getPercentWidthtColMap(GridPane gridPane) {
        Map<Integer, Double> percentColMap = new HashMap<Integer, Double>();
        int cols = gridPane.getColumnCount();
        for (int i = 0; i < cols; i++) {
            double percent = gridPane.getColumnConstraints().get(i).getPercentWidth();
            if (percent > 0 && percent <= PERCENT_BASE) {
                percentColMap.put(i, percent);
            }
        }
        return percentColMap;
    }

    public static double getPrefWidthSum(GridPane gridPane) {
        double sum = 0;
        int cols = gridPane.getColumnCount();
        for (int i = 0; i < cols; i++) {
            double percent = gridPane.getColumnConstraints().get(i).getPercentWidth();
            double prefWidth = gridPane.getColumnConstraints().get(i).getPrefWidth();
            if (percent < 0 && prefWidth > 0) {
                sum += prefWidth;
            }
        }
        return sum;
    }

    public static void setAutoPercentWidth(GridPane gridPane) {
        Map<Integer, Double> map = getPercentWidthtColMap(gridPane);
        if (MapUtils.isEmpty(map)) {
            return;
        }

        double prefWidthSum = getPrefWidthSum(gridPane);
        for (Map.Entry<Integer, Double> item : map.entrySet()) {
            // 比例置空
            gridPane.getColumnConstraints().get(item.getKey()).setPercentWidth(Region.USE_COMPUTED_SIZE);
            gridPane.getColumnConstraints().get(item.getKey()).prefWidthProperty().bind(gridPane.widthProperty().subtract(prefWidthSum).multiply(item.getValue()).divide(PERCENT_BASE));
        }
    }


    public static Map<Integer, Double> getPercentHeightRowMap(GridPane gridPane) {
        Map<Integer, Double> percentRowMap = new HashMap<Integer, Double>();
        int rows = gridPane.getRowCount();
        for (int i = 0; i < rows; i++) {
            double percent = gridPane.getRowConstraints().get(i).getPercentHeight();
            if (percent > 0 && percent <= PERCENT_BASE) {
                percentRowMap.put(i, percent);
            }
        }
        return percentRowMap;
    }

    public static double getPrefHeightSum(GridPane gridPane) {
        double sum = 0;
        int rows = gridPane.getRowCount();
        for (int i = 0; i < rows; i++) {
            double percent = gridPane.getRowConstraints().get(i).getPercentHeight();
            double prefHeight = gridPane.getRowConstraints().get(i).getPrefHeight();
            if (percent < 0 && prefHeight > 0) {
                sum += prefHeight;
            }
        }
        return sum;
    }

    public static void setAutoPercentHeight(GridPane gridPane) {
        Map<Integer, Double> map = getPercentHeightRowMap(gridPane);
        if (MapUtils.isEmpty(map)) {
            return;
        }

        double prefHeightSum = getPrefHeightSum(gridPane);
        for (Map.Entry<Integer, Double> item : map.entrySet()) {
            // 比例置空
            gridPane.getRowConstraints().get(item.getKey()).setPercentHeight(Region.USE_COMPUTED_SIZE);
            gridPane.getRowConstraints().get(item.getKey()).prefHeightProperty().bind(gridPane.heightProperty().subtract(prefHeightSum).multiply(item.getValue()).divide(PERCENT_BASE));
        }
    }
}
