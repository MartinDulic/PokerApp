module hr.dulic.pokerapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.rmi;
    requires java.naming;
    requires java.xml;

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
    exports hr.dulic.pokerapp.utils.gameUtils;
    opens hr.dulic.pokerapp.utils.gameUtils to javafx.fxml;
    exports hr.dulic.pokerapp.utils.networkUtils;
    opens hr.dulic.pokerapp.utils.networkUtils to javafx.fxml;
    exports hr.dulic.pokerapp.utils.viewUtils;
    opens hr.dulic.pokerapp.utils.viewUtils to javafx.fxml;
}