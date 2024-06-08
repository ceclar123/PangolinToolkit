module com.a.b.c.d.pangolin.tool {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.bootstrapfx.core;
    requires com.a.b.c.d.pangolin.util;
    requires com.alibaba.fastjson2;

    opens com.a.b.c.d.pangolin.tool to javafx.fxml, javafx.graphics;
    opens com.a.b.c.d.pangolin.tool.controller to javafx.fxml, javafx.graphics;
    opens com.a.b.c.d.pangolin.tool.controller.encode to javafx.fxml, javafx.graphics;
}