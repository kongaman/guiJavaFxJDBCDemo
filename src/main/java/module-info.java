module best.practice.guiJavaFx {
	requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;

    opens best.practice.guiJavaFx to javafx.fxml;
    exports best.practice.guiJavaFx;
}
