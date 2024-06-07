module com.a.b.c.d.pangolin.tool {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;


    opens com.a.b.c.d.pangolin.tool to javafx.fxml, javafx.graphics;
}