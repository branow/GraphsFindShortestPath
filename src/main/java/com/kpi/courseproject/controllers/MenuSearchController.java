package com.kpi.courseproject.controllers;

import com.kpi.courseproject.Main;
import com.kpi.courseproject.collection.ArrayListPlus;
import com.kpi.courseproject.collection.ListPlus;
import com.kpi.courseproject.interfaces.DialogWindow;
import com.kpi.courseproject.logic.Graph;
import com.kpi.courseproject.logic.Vertical;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Iterator;
import java.util.List;


public class MenuSearchController {

    @FXML
    private ListView<Vertical> outputs, entrances;
    @FXML
    private Label lEntrances, lOutputs, lTypeSearch, lTypeShow;
    @FXML
    private Button bAccept, bRandom;
    @FXML
    private RadioButton rbDFS, rbBFS, rbFindShortestPath;
    @FXML
    private RadioButton rbShowAll, rbShowShortest, rbShowSafest, rbShowOptimal;
    private ToggleGroup groupSearch;
    private ToggleGroup groupShow;

    @FXML
    public void initialize () {
        groupSearch = new ToggleGroup();
        groupShow = new ToggleGroup();
        inputLists();
        bRandom.setText("R");
        bAccept.setText(Main.bundle.getString("menu.search.button.bAccept"));
        lEntrances.setText(Main.bundle.getString("menu.search.label.lEntrances"));
        lOutputs.setText(Main.bundle.getString("menu.search.label.lOutputs"));
        lTypeSearch.setText(Main.bundle.getString("menu.search.label.lTypeSearch"));
        lTypeShow.setText(Main.bundle.getString("menu.search.label.lTypeShow"));
        rbDFS.setText(Main.bundle.getString("menu.search.radioButton.rbDFS"));
        rbBFS.setText(Main.bundle.getString("menu.search.radioButton.rbBFS"));
        rbFindShortestPath.setText(Main.bundle.getString("menu.search.radioButton.rbFindShortestPath"));
        rbShowAll.setText(Main.bundle.getString("menu.search.radioButton.rbShowAll"));
        rbShowShortest.setText(Main.bundle.getString("menu.search.radioButton.rbShowShortest"));
        rbShowSafest.setText(Main.bundle.getString("menu.search.radioButton.rbShowSafest"));
        rbShowOptimal.setText(Main.bundle.getString("menu.search.radioButton.rbShowOptimal"));

        rbDFS.setToggleGroup(groupSearch);
        rbBFS.setToggleGroup(groupSearch);
        rbFindShortestPath.setToggleGroup(groupSearch);
        rbShowAll.setToggleGroup(groupShow);
        rbShowShortest.setToggleGroup(groupShow);
        rbShowSafest.setToggleGroup(groupShow);
        rbShowOptimal.setToggleGroup(groupShow);
        rbBFS.setSelected(true);
        rbShowAll.setSelected(true);
    }

    public void bAcceptAction (ActionEvent e) {
        Vertical start = entrances.getSelectionModel().getSelectedItem();
        ListPlus<Vertical> finish = new ArrayListPlus<>(outputs.getSelectionModel().getSelectedItems().toArray());
        if (start!=null && !finish.isEmpty()) {
            ListPlus<String> errors = new ArrayListPlus<>();
            MainController.graph.search(start, finish, getTypeSearchSelected(), getTypeShowSelected(), errors);
            if (!errors.isEmpty()) {
                showError(errors, e);
            } else {
                MainController.paintLabyrinth.paintGraph(false);
                closeStage(e);
            }
        }
    }

    public void bRandomAction (ActionEvent e) {
        ListPlus<Vertical> outputs = MainController.graph.getRandomExit();
        for (Vertical v: outputs) {
            this.outputs.getSelectionModel().select(v);
        }
    }

    private void inputLists () {
        ObservableList<Vertical> list = FXCollections.observableArrayList(MainController.graph.getVerticals().getList());
        outputs.setItems(list);
        entrances.setItems(list);
        outputs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void closeStage (ActionEvent e) {
        ((Stage)((Node)e.getSource()).getScene().getWindow()).close();
    }

    private void showError (ListPlus<String> errors, ActionEvent e) {
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        StringBuilder text = new StringBuilder(Main.bundle.getString("dialog.window.error.route"));
        Iterator<String> iterator = errors.iterator();
        while (iterator.hasNext()) {
            text.append("\n").append(iterator.next());
        }
        new DialogWindow( stage, text.toString(), DialogWindow.message);
    }

    private int getTypeSearchSelected() {
        Toggle toggle = groupSearch.getSelectedToggle();
        if (rbDFS.equals(toggle)) {
            return Graph.DFSearch;
        } else if (rbBFS.equals(toggle)) {
            return Graph.BFSearch;
        } else {
            return Graph.DijkstraSearch;
        }
    }

    private int getTypeShowSelected() {
        Toggle toggle = groupShow.getSelectedToggle();
        if (rbShowAll.equals(toggle)) {
            return Graph.ShowAll;
        } else if (rbShowShortest.equals(toggle)) {
            return Graph.ShowShortest;
        } else if (rbShowSafest.equals(toggle)) {
            return Graph.ShowSafest;
        } else {
            return Graph.ShowOptimal;
        }
    }

    private ListPlus<Object> listToListPlus (List<Object> list) {
        return new ArrayListPlus<Object>(list.toArray());
    }

}
