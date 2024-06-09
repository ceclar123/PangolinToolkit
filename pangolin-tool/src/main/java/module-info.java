module pangolin.tool {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.bootstrapfx.core;
    requires pangolin.util;

    opens com.a.b.c.d.pangolin.tool to javafx.fxml, javafx.graphics;
    opens com.a.b.c.d.pangolin.tool.controller to javafx.fxml, javafx.graphics;
    opens com.a.b.c.d.pangolin.tool.controller.encode to javafx.fxml, javafx.graphics;
}