package com.kpi.courseproject.controllers;

import com.kpi.courseproject.Main;
import com.kpi.courseproject.interfaces.FileManager;
import com.kpi.courseproject.interfaces.Imprint;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MenuSettingController {

    @FXML
    private Button bAccept;
    @FXML
    private ComboBox<String> cbLanguage;

    @FXML
    public void initialize() {
        bAccept.setText(Main.bundle.getString("menu.setting.button.bAccept"));
        ObservableList<String> list = FXCollections.observableArrayList();
        list.add(Main.bundle.getString("menu.setting.cbLanguage.ukraine"));
        list.add(Main.bundle.getString("menu.setting.cbLanguage.english"));
        cbLanguage.setItems(list);
        cbLanguage.setValue(Main.bundle.getString("menu.setting.cbLanguage.english"));
    }

    public void bAcceptAction(ActionEvent e) {
        String lan = "en";
        if (cbLanguage.getValue().equals(Main.bundle.getString("menu.setting.cbLanguage.ukraine")) ) {
            lan = "uk";
        } else if (cbLanguage.getValue().equals(Main.bundle.getString("menu.setting.cbLanguage.english"))) {
            lan = "en";
        }
        Main.setting.setLocale(new Locale(lan));
        closeStage(e);
    }

    private void closeStage(Event e) {
        FileManager.write(Main.setting, FileManager.pathSetting);
        ((Stage)((Node)e.getSource()).getScene().getWindow()).close();
    }

}
