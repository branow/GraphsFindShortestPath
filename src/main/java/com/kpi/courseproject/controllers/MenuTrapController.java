package com.kpi.courseproject.controllers;

import com.kpi.courseproject.Main;
import com.kpi.courseproject.collection.ListPlus;
import com.kpi.courseproject.controllers.support.WrapTableTraps;
import com.kpi.courseproject.logic.Edge;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MenuTrapController {

    @FXML
    private Button bRandom, bAccept, bCancel;
    @FXML
    private TableView<WrapTableTraps> tTraps;
    @FXML
    private TableColumn<WrapTableTraps, String> ctEdges;
    @FXML
    private TableColumn<WrapTableTraps, String> ctTraps;
    private ObservableList<WrapTableTraps> table;

    @FXML
    public void initialize() {
        table = getTable();
        ctEdges.setCellValueFactory(new PropertyValueFactory<>("name"));
        ctTraps.setCellValueFactory(new PropertyValueFactory<>("trap"));
        tTraps.setItems(table);
        bRandom.setText(Main.bundle.getString("menu.trap.button.bRandom"));
        bAccept.setText(Main.bundle.getString("menu.trap.button.bAccept"));
        bCancel.setText(Main.bundle.getString("menu.trap.button.bCancel"));
        ctEdges.setText(Main.bundle.getString("menu.trap.column.ctEdges"));
        ctTraps.setText(Main.bundle.getString("menu.trap.column.ctTraps"));
        tTraps.setEditable(true);
        ctTraps.setEditable(true);
        ctTraps.setCellFactory(TextFieldTableCell.forTableColumn());
        ctTraps.setOnEditCommit(e->{
            WrapTableTraps traps = ctEdges.getTableView().getItems().get(e.getTablePosition().getRow());
            traps.setTrap(e.getNewValue());
            Objects.requireNonNull(getInverse(traps)).setTrap(e.getNewValue());
        });
    }

    public void bRandomAction (ActionEvent e) {
        for (WrapTableTraps traps: table) {
            double trap = Math.random()-(Math.random()/4);
            if (trap>0.08) {
                BigDecimal decimal = new BigDecimal(trap, new MathContext(2, RoundingMode.HALF_DOWN));
                traps.setTrap(Double.toString(decimal.doubleValue()));
                Objects.requireNonNull(getInverse(traps)).setTrap(Double.toString(decimal.doubleValue()));
            } else {
                traps.setTrap(Double.toString(0));
                Objects.requireNonNull(getInverse(traps)).setTrap(Double.toString(0));
            }
        }
    }

    public void bAcceptAction (ActionEvent e) {
        for (WrapTableTraps traps: table) {
            for (Edge edge: MainController.graph.getEdges()) {
                if (edge.getName().equals(traps.getName())) {
                    double t = Double.parseDouble(String.valueOf(traps.getTrap()));
                    edge.setTrap(t);
                }
            }
        }
        MainController.paintLabyrinth.paintGraph(false);
        close(e);
    }

    public void bCancelAction (ActionEvent e) {
        close(e);
    }

    private void close (ActionEvent e) {
        ((Stage)((Node)e.getSource()).getScene().getWindow()).close();
    }

    private ObservableList<WrapTableTraps> getTable () {
        List<WrapTableTraps> list = new ArrayList<>();
        for (Edge edge: MainController.graph.getEdges()) {
            WrapTableTraps traps = new WrapTableTraps( new SimpleStringProperty(edge.getName()),  new SimpleStringProperty(Double.toString(edge.getTrap())) );
            if (!list.contains(traps)) {
                list.add(traps);
            }
        }
        return FXCollections.observableArrayList(list);
    }

    private WrapTableTraps getInverse (WrapTableTraps start) {
        String str = start.getName();
        String inverse =  str.substring(str.lastIndexOf(" ")+1,  str.length()) + " -> " + str.substring(0,  str.indexOf(" "));
        for (WrapTableTraps traps: table) {
            if (traps.getName().equals(inverse)) {
                return traps;
            }
        }
        return null;
    }

}
