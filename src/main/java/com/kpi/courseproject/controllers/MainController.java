package com.kpi.courseproject.controllers;


import com.kpi.courseproject.Main;
import com.kpi.courseproject.collection.ArrayListPlus;
import com.kpi.courseproject.collection.ListPlus;
import com.kpi.courseproject.interfaces.FileManager;
import com.kpi.courseproject.interfaces.Imprint;
import com.kpi.courseproject.logic.Graph;
import com.kpi.courseproject.interfaces.PaintLabyrinth;
import com.kpi.courseproject.logic.Mouse;
import com.kpi.courseproject.logic.Route;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MainController {

    @FXML
    private Button bMatrix, bTrap, bSearch, bMouseGo, bDeleteRoute, bExit;
    @FXML
    private AnchorPane pPaint;
    @FXML
    private ComboBox cbTypePlacing;
    @FXML
    private Label lListRoutes;
    @FXML
    private Slider sSize;
    @FXML
    private ListView<Route> listRoutes;

    public static Graph graph;
    public static PaintLabyrinth paintLabyrinth;

    @FXML
    private void initialize() {
        cbTypePlacing.setItems(FXCollections.observableArrayList("Random", "Circle", "Rectangle"));
        cbTypePlacing.setValue("Random");
        paintLabyrinth = new PaintLabyrinth(pPaint, readTypePlacing());
        sSize.setValue(0.5);
        setSizePPaint();
        setListRoutes();
        lListRoutes.setText(Main.bundle.getString("main.stage.button.lListRoutes"));
        bDeleteRoute.setText(Main.bundle.getString("main.stage.button.bDeleteRoute"));
        bMatrix.setText(Main.bundle.getString("main.stage.button.matrix"));
        bTrap.setText(Main.bundle.getString("main.stage.button.trap"));
        bSearch.setText(Main.bundle.getString("main.stage.button.search"));
        bExit.setText(Main.bundle.getString("main.stage.button.bExit"));
        bMouseGo.setText(Main.bundle.getString("main.stage.button.bMouseGo"));
        setChangeListeners();
        paintLabyrinth.paintGraph(true);
    }

    public void matrixAction (ActionEvent e) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("matrix.fxml"));
        Stage stMatrix = new Stage();
        stMatrix.setTitle(Main.bundle.getString("menu.matrix.title"));
        try {
            stMatrix.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        stMatrix.initModality(Modality.WINDOW_MODAL);
        stMatrix.initOwner(((Node)e.getSource()).getScene().getWindow());
        stMatrix.show();
    }

    public void trapAction (ActionEvent e) {
        if (graph!=null && !graph.getVerticals().isEmpty()) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("menu-trap.fxml"));
            Stage stMatrix = new Stage();
            stMatrix.setTitle(Main.bundle.getString("menu.trap.title"));
            try {
                stMatrix.setScene(new Scene(fxmlLoader.load()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            stMatrix.initModality(Modality.WINDOW_MODAL);
            stMatrix.initOwner(((Node)e.getSource()).getScene().getWindow());
            stMatrix.show();
        } else {
            showError();
        }
    }

    public void searchAction (ActionEvent e) {
        if (graph!=null && !graph.getVerticals().isEmpty()) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("search-menu.fxml"));
            Stage stSearch = new Stage();
            stSearch.setTitle(Main.bundle.getString("menu.search.title"));
            try {
                stSearch.setScene(new Scene(fxmlLoader.load()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            stSearch.initModality(Modality.WINDOW_MODAL);
            stSearch.initOwner(((Node)e.getSource()).getScene().getWindow());
            stSearch.show();
        } else {
            showError();
        }
    }

    public void bMouseGoAction (ActionEvent e) {
        Mouse mouse = new Mouse();
        Route route = listRoutes.getSelectionModel().getSelectedItem();
        if (route==null) {
            return;
        }
        mouse.go(route);
        offController();
        paintLabyrinth.newPaintMouse(mouse, this);
//        if (labyrinth!=null && labyrinth.getGraph()!=null) {
//            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mouse-go.fxml"));
//            Stage stSearch = new Stage();
//            stSearch.setTitle(Main.bundle.getString("mouse.title"));
//            try {
//                stSearch.setScene(new Scene(fxmlLoader.load()));
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//            stSearch.initModality(Modality.WINDOW_MODAL);
//            stSearch.initOwner(((Node)e.getSource()).getScene().getWindow());
//            stSearch.show();
//        } else {
//            showError();
//        }
    }

    public void bDeleteRouteAction (ActionEvent e) {
        List<Route> routeList = listRoutes.getSelectionModel().getSelectedItems();
        paintLabyrinth.removeRoutes(routeList);
        graph.removeRoutes(routeList);
        paintLabyrinth.paintGraph(false);
    }

    public void bExitAction (ActionEvent e) {
        closeStage(e);
    }

    private void offController () {
        bMatrix.setDisable(true);
        bTrap.setDisable(true);
        bSearch.setDisable(true);
        bMouseGo.setDisable(true);
        bDeleteRoute.setDisable(true);
        bExit.setDisable(true);
        cbTypePlacing.setDisable(true);
        sSize.setDisable(true);
    }

    public void onController () {
        bMatrix.setDisable(false);
        bTrap.setDisable(false);
        bSearch.setDisable(false);
        bMouseGo.setDisable(false);
        bDeleteRoute.setDisable(false);
        bExit.setDisable(false);
        cbTypePlacing.setDisable(false);
        sSize.setDisable(false);
    }

    private void setSizePPaint () {
        double value = sSize.getValue();
        pPaint.setPrefSize( pPaint.getMaxWidth()*value, pPaint.getMaxHeight()*value );
    }

    private void showError () {

    }

    public void setChangeListeners() {
        cbTypePlacing.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                paintLabyrinth.setTypePlacing(readTypePlacing());
                paintLabyrinth.paintGraph(true);
            }
        });
        sSize.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                setSizePPaint();
                paintLabyrinth.repaint();
            }
        });
    }

    private int readTypePlacing() {
        if (cbTypePlacing.getValue().equals("Random")) return 0;
        else if (cbTypePlacing.getValue().equals("Circle")) return 1;
        else return 2;
    }

    private void setListRoutes () {
        listRoutes.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listRoutes.setItems(graph.getRoutes());
    }

    private void closeStage(Event e) {
        ListPlus<Graph> graphs = new ArrayListPlus<>();
        for (Graph g: MainMenu.list) {
            graphs.add(g);
        }
        FileManager.write(Imprint.doImprint(graphs), FileManager.pathGraphs );
        ((Stage)((Node)e.getSource()).getScene().getWindow()).close();
    }

}