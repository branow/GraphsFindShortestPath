package com.kpi.courseproject.controllers;

import com.kpi.courseproject.Main;
import com.kpi.courseproject.collection.ArrayListPlus;
import com.kpi.courseproject.interfaces.FileManager;
import com.kpi.courseproject.interfaces.Imprint;
import com.kpi.courseproject.logic.Graph;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;


public class MainMenu {

    @FXML
    private Button bExit, bSetting, bRename, bDelete, bCreate;
    @FXML
    private ListView<Graph> listGraphs;
    @FXML
    private Label lGraphs;
    @FXML
    private TextField tfNameGraph;
    public static ObservableList<Graph> list;

    @FXML
    public void initialize() {
        list = Imprint.readImprint(FileManager.read(FileManager.pathGraphs));
        tfNameGraph.setEditable(false);
        bExit.setText(Main.bundle.getString("main.menu.button.bExit"));
        bSetting.setText(Main.bundle.getString("main.menu.button.bSetting"));
        bRename.setText(Main.bundle.getString("main.menu.button.bRename"));
        bCreate.setText(Main.bundle.getString("main.menu.button.bCreate"));
        bDelete.setText(Main.bundle.getString("main.menu.button.bDelete"));
        lGraphs.setText(Main.bundle.getString("main.menu.label.lGraphs"));
        listGraphs.setItems(list);
    }

    public void bSettingAction (ActionEvent e) {
        Stage stage = new Stage();
        stage.setTitle(Main.bundle.getString("menu.setting.title"));
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("menu-setting.fxml"));
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        stage.initOwner(((Node)e.getSource()).getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }

    public void bCreateAction (ActionEvent e) {
        list.add(new Graph());
    }

    public void bRenameAction (ActionEvent e) {
        tfNameGraph.setEditable(true);
    }

    public void listClickAction (MouseEvent e) {
        Graph labyrinth = null;
        Graph selct = listGraphs.getSelectionModel().getSelectedItem();
        for (Graph l: list) {
            if (l.equals(selct)) {
                labyrinth = l;
            }
        }
        if (labyrinth==null) {
            return;
        }
        if (e.getClickCount()>1) {
            openMainStage(e, labyrinth);
        } else {
            tfNameGraph.setText(labyrinth.getName());
        }
    }

    private void openMainStage (Event e, Graph graph) {
        MainController.graph = graph;
        Stage stage = new Stage();
        stage.setTitle(Main.bundle.getString("main.stage.title"));
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("main-stage.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        stage.show();
    }

    public void tfNameKeyAction (KeyEvent e) {
        if (e.getCode().equals(KeyCode.ENTER)) {
            listGraphs.getSelectionModel().getSelectedItem().setName(tfNameGraph.getText());
            tfNameGraph.setEditable(false);
        }
    }

    public void bDeleteAction (ActionEvent e) {
        list.remove(listGraphs.getSelectionModel().getSelectedItem());
    }

    public void bExitAction (ActionEvent e) {
        closeStage(e);
        Platform.exit();
    }

    public void closeStage (Event e) {
        FileManager.write(Imprint.doImprint(new ArrayListPlus<>(MainMenu.list.stream().toList().toArray())), FileManager.pathGraphs );
        ((Stage)((Node)e.getSource()).getScene().getWindow()).close();
    }
}
