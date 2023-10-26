module hr.dulic.pokerapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens hr.dulic.pokerapp to javafx.fxml;
    exports hr.dulic.pokerapp;
    exports hr.dulic.pokerapp.controllers;
    opens hr.dulic.pokerapp.controllers to javafx.fxml;
    exports hr.dulic.pokerapp.model;
    opens hr.dulic.pokerapp.model to javafx.fxml;
    exports hr.dulic.pokerapp.model.enums;
    opens hr.dulic.pokerapp.model.enums to javafx.fxml;
    exports hr.dulic.pokerapp.utils;
    opens hr.dulic.pokerapp.utils to javafx.fxml;
}