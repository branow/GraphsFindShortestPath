package com.kpi.courseproject;

import com.kpi.courseproject.collection.ArrayListPlus;
import com.kpi.courseproject.controllers.MainMenu;
import com.kpi.courseproject.interfaces.FileManager;
import com.kpi.courseproject.interfaces.Imprint;
import com.kpi.courseproject.interfaces.Setting;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

public class Main extends Application {

    public static Setting setting = (Setting) FileManager.read(FileManager.pathSetting);
//    public static Setting setting = new Setting();
    public static ResourceBundle bundle = setting.getBundle();

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(bundle.getString("main.menu.title"));
        stage.setScene(scene);
        stage.setOnCloseRequest(e->{
            FileManager.write(setting, FileManager.pathSetting);
            FileManager.write(Imprint.doImprint(new ArrayListPlus<>(MainMenu.list.stream().toList().toArray())), FileManager.pathGraphs );
        });
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}