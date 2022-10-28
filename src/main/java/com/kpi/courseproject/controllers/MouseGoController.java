package com.kpi.courseproject.controllers;

import com.kpi.courseproject.Main;
import com.kpi.courseproject.collection.ArrayListPlus;
import com.kpi.courseproject.collection.ListPlus;
import com.kpi.courseproject.logic.Edge;
import com.kpi.courseproject.logic.Graph;
import com.kpi.courseproject.logic.Route;
import com.kpi.courseproject.logic.Vertical;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.util.Iterator;

public class MouseGoController {

    @FXML
    private Label lStep, lInfo;
    @FXML
    private Button bExit, bNext;

    private Graph copy;
    private int stage;
    private Vertical input;
    private ListPlus<Vertical> outputs;
    private ListPlus<String> errors;
    private ListPlus<Vertical> deleted;

    @FXML
    private void initialize() {
        try {
            copy = (Graph) MainController.graph.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        MainController.paintLabyrinth.removeAllRoutes();
        MainController.graph.removeAllRoutes();
        MainController.paintLabyrinth.paintGraph(false);
        bExit.setText(Main.bundle.getString("mouse.button.bExit"));
        bNext.setText(Main.bundle.getString("mouse.button.bNext"));
        setStage();
    }

    public void bExitAction (ActionEvent e) {
        MainController.graph.setAdjacencyMatrix(copy.getAdjacencyMatrix());
        MainController.graph.removeAllRoutes();
        MainController.graph.addRoutes(new ArrayListPlus<>(copy.getRoutes().toArray()));
        MainController.paintLabyrinth.removeAllRoutes();
        MainController.paintLabyrinth.paintGraph(true);
        close(e);
    }

    public void bNextAction (ActionEvent e) {
        stage++;
        setStage();
    }

    private void setStage () {
        switch (stage) {
            case 0: startStage();
                break;
            case 1: inputOutputsStage();
                break;
            case 2: routeBDFSStage();
                break;
            case 3: routeDijkstraStage();
                break;
            case 4: routeSafestStage();
                break;
            case 5: breakGraphStage();
                break;
            case 6: recalculateStage();
                break;
            default: bExit.fire();
        }
    }

    private void startStage () {
        lStep.setText(Main.bundle.getString("mouse.label.lStep.stage.start"));
        lInfo.setText(Main.bundle.getString("mouse.label.lInfo.stage.start"));
    }

    private void inputOutputsStage () {
        setInputOutputs();
        String info = lInfo.getText() + "\n" +
                Main.bundle.getString("mouse.label.lInfo.stage.inputOutputs.input") + " : " + input.toString() + "\n"
                +  Main.bundle.getString("mouse.label.lInfo.stage.inputOutputs.outputs") + " : " + outputs.toString();
        lStep.setText(Main.bundle.getString("mouse.label.lStep.stage.inputOutputs"));
        lInfo.setText(info);
    }

    private void setInputOutputs () {
        input = getRandomVertical();
        int size = MainController.graph.getVerticals().size()/5;
        outputs = new ArrayListPlus<>();
        for (int i=0; i<size; ) {
            boolean similar = false;
            Vertical temp = getRandomVertical();
            if (temp.equals(input)) {
                similar = true;
            }
            Iterator iterator = outputs.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().equals(temp)) {
                    similar = true;
                    break;
                }
            }
            if (!similar) {
                outputs.add(temp);
                i++;
            }
        }
    }

    private Vertical getRandomVertical () {
        return MainController.graph.getVerticals().get((int)(Math.random()* MainController.graph.getVerticals().size()));
    }

    private void routeBDFSStage () {
        setRouteBDFS();
        String info = lInfo.getText() + "\n" +
                Main.bundle.getString("mouse.label.lInfo.stage.routeBDFS") + " : " + "\n";
        for (Route route: MainController.graph.getRoutes()) {
            info += route.toString() + "\n";
        }
        lStep.setText(Main.bundle.getString("mouse.label.lStep.stage.routeBDFS"));
        lInfo.setText(info);
        MainController.paintLabyrinth.paintGraph(false);
    }

    private void setRouteBDFS () {
        errors = new ArrayListPlus<>();
        MainController.graph.search(input, outputs, Graph.BFSearch, Graph.ShowShortest, errors);
        MainController.graph.search(input, outputs, Graph.DFSearch, Graph.ShowShortest, errors);
    }

    private void routeDijkstraStage () {
        errors = new ArrayListPlus<>();
        MainController.graph.search(input, outputs, Graph.DijkstraSearch, Graph.ShowShortest, errors);
        Route route = null;
        if (!MainController.graph.getRoutes().isEmpty()) {
            route = MainController.graph.getRoutes().get(MainController.graph.getRoutes().size()-1);
        }
        String info = lInfo.getText() + "\n" +
                Main.bundle.getString("mouse.label.lInfo.stage.routeDijkstra.route") + " : " + route + "\n" +
                Main.bundle.getString("mouse.label.lInfo.stage.routeDijkstra.trap") + " : " + (route.getTrap() * 100) + " %";
        lStep.setText(Main.bundle.getString("mouse.label.lStep.stage.routeDijkstra"));
        lInfo.setText(info);
        MainController.paintLabyrinth.paintGraph(false);
    }

    private void routeSafestStage () {
        errors = new ArrayListPlus<>();
        MainController.graph.search(input, outputs, Graph.DijkstraSearch, Graph.ShowSafest, errors);
        Route route = null;
        if (!MainController.graph.getRoutes().isEmpty()) {
            route = MainController.graph.getRoutes().get(MainController.graph.getRoutes().size()-1);
        }
        String info = lInfo.getText() + "\n" +
                Main.bundle.getString("mouse.label.lInfo.stage.routeSafest.route") + " : " + route + "\n" +
                Main.bundle.getString("mouse.label.lInfo.stage.routeSafest.trap") + " : " + (route.getTrap() * 100) + " %";
        lStep.setText(Main.bundle.getString("mouse.label.lStep.stage.routeSafest"));
        lInfo.setText(info);
        MainController.paintLabyrinth.paintGraph(false);
    }

    private void breakGraphStage () {
        deleteVerticals();
        lStep.setText(Main.bundle.getString("mouse.label.lStep.stage.breakGraph"));
        lInfo.setText(lInfo.getText() + "\n" + (Main.bundle.getString("mouse.label.lInfo.stage.breakGraph")) + deleted);
        MainController.graph.removeAllRoutes();
        MainController.paintLabyrinth.removeAllRoutes();
        MainController.paintLabyrinth.paintGraph(true);
    }

    private void deleteVerticals () {
        Graph graph = MainController.graph;
        int size = graph.getVerticals().size()/6;
        deleted = new ArrayListPlus<>();
        for (int i=0; i<size; ) {
            boolean similar = false;
            Vertical temp = getRandomVertical();
            if (temp.equals(input)) {
                similar = true;
            }
            Iterator iterator = outputs.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().equals(temp)) {
                    similar = true;
                    break;
                }
            }
            if (!similar) {
                deleted.add(temp);
                i++;
            }
        }
        Iterator<Vertical> iterator = deleted.iterator();
        while (iterator.hasNext()) {
            Vertical temp = iterator.next();
            for (Edge e: temp.getEdges()) {
                e.getTail().removeEdge(e);
            }
            graph.getEdges().removeAll(  temp.getEdges() );
            graph.getVerticals().remove(temp);
        }
    }

    private void recalculateStage () {
        routeBDFSStage();
        routeDijkstraStage();
        routeSafestStage();
        lStep.setText(Main.bundle.getString("mouse.label.lStep.stage.recalculate"));
        bNext.setText(Main.bundle.getString("mouse.button.bNext.stage"));
    }

    private void close (Event e) {
        ((Stage)((Node)e.getSource()).getScene().getWindow()).close();
    }

}
