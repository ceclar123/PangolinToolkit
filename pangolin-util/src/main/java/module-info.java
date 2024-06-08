module com.a.b.c.d.pangolin.util {
    requires transitive org.apache.commons.lang3;
    requires transitive org.apache.commons.io;


    opens com.a.b.c.d.pangolin.util to javafx.fxml;
    exports com.a.b.c.d.pangolin.util;
    exports com.a.b.c.d.pangolin.util.bean;
}