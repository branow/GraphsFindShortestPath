module com.kpi.courseproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens com.kpi.courseproject to javafx.fxml, javafx.base;
    exports com.kpi.courseproject;
    exports com.kpi.courseproject.controllers;
    opens com.kpi.courseproject.controllers to javafx.fxml;
    opens com.kpi.courseproject.logic to javafx.base;
    opens com.kpi.courseproject.interfaces to javafx.base;
    opens com.kpi.courseproject.controllers.support to javafx.base;

}