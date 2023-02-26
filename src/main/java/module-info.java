module best.practice.guiJavaFx {
	requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.base;

    opens best.practice.guiJavaFx to javafx.fxml;
    opens best.practice.model to javafx.base;
    exports best.practice.guiJavaFx;
}
