package com.kpi.courseproject.controllers;

import com.kpi.courseproject.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MenuMatrixController {

    @FXML
    private Label lSlider;
    @FXML
    private Slider slider;
    @FXML
    private ScrollPane spMatrix;
    @FXML
    private Button bAccept, bRandom;
    @FXML
    private CheckBox cbDirected;
    private TextField[][] fields;
    private int[][] matrix = MainController.graph.getAdjacencyMatrix();

    @FXML
    public void initialize() {
        lSlider.setFont(new Font(14));
        bRandom.setText(Main.bundle.getString("menu.matrix.button.bRandom"));
        lSlider.setText(Main.bundle.getString("menu.matrix.label.lSlider"));
        bAccept.setText(Main.bundle.getString("menu.matrix.button.bAccept"));
        cbDirected.setText(Main.bundle.getString("menu.matrix.checkBox.cbDirected"));
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                lSlider.setText("" + (int)slider.getValue());
                showMatrix();
            }
        });
        writeMatrix(matrix);
    }

    public void bAcceptAction(ActionEvent e) {
         int[][] matrixAdjacency = readMatrix();
         if (matrixAdjacency!=null) {
             MainController.graph.setAdjacencyMatrix(matrixAdjacency);
             MainController.paintLabyrinth.removeAllRoutes();
             MainController.paintLabyrinth.paintGraph(true);
             closeStage(e);
         }
    }

    public void bRandomAction(ActionEvent e) {
        for (int i=0; i< fields.length; i++) {
            for (int j=i+1; j<fields[i].length; j++) {
                int value = (int)(Math.random() * 10) + 1;
                double v = Math.pow(fields.length, 2) * 79/54600 + fields.length * (-4283.)/54600 + (419./364);
                if ( Math.random() > v ) {
                    fields[i][j].setText("");
                    fields[j][i].setText("");
                } else {
                    fields[i][j].setText(value+"");
                    fields[j][i].setText(value+"");
                }
            }
        }
    }

    private int[][] readMatrix() {
        if (fields == null) {
            showError();
            return null;
        }
        int[][] matrixAdjacency = new int[fields.length][fields.length];
        for (int i=0; i<fields.length; i++) {
            for (int j=0; j<fields[i].length; j++) {
                if (fields[i][j].getText().equals("")) {
                    matrixAdjacency[i][j] = -1;
                } else {
                    Integer in = null;
                    try {
                        in = Integer.parseInt(fields[i][j].getText());
                    } catch (NumberFormatException ex) {
                        ex.getStackTrace();
                        showError();
                        return null;
                    }
                    matrixAdjacency[i][j] = in;
                }
            }
        }
        return matrixAdjacency;
    }

    private void writeMatrix(int[][] matrix) {
        slider.setValue(matrix.length);
        showMatrix();
        for (int i=0; i<fields.length && i<matrix.length; i++) {
            for (int j=0; j<fields[i].length && i<matrix[i].length; j++) {
                if (matrix[i][j]>=0) {
                    fields[i][j].setText(String.valueOf(matrix[i][j]));
                }
            }
        }
    }

    private void showMatrix() {
        GridPane pane = new GridPane();
        pane.setHgap(5);
        pane.setVgap(5);
        fields = new TextField[(int)slider.getValue()][(int)slider.getValue()];
        HandlerTextField handler = new HandlerTextField();
        for (int i=0; i<fields.length; i++) {
            for (int j=0; j<fields[i].length; j++) {
                TextField field = new TextField();
                field.setPrefColumnCount(1);
                fields[j][i] = field;
                pane.add(field, i, j);
                field.setOnKeyTyped(handler);
            }
        }
        spMatrix.setContent(pane);

    }

    class HandlerTextField implements EventHandler {
        @Override
        public void handle(Event event) {
            if (!cbDirected.isSelected()) {
                for (int i=0; i<fields.length; i++) {
                    for (int j=0; j<fields[i].length; j++) {
                        if (fields[i][j] == event.getSource()) {
                            fields[j][i].setText(((TextField)event.getSource()).getText());
                        }
                    }
                }
            }
        }
    }

    private void showError() {

    }

    private void closeStage (ActionEvent e) {
        ((Stage)((Node)e.getSource()).getScene().getWindow()).close();
    }


}
