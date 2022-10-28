package com.kpi.courseproject.interfaces;

import com.kpi.courseproject.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DialogWindow {

    public final static int message = 0;
    public final static int question = 1;
    public boolean choice;
    private Button bAccept, bCancel;
    private Label lText;

    public DialogWindow (Stage parent, String text, int type) {
        lText = new Label();
        lText.setText(text);
        bAccept = new Button(Main.bundle.getString("dialog.window.button.bAccept"));
        bCancel = new Button(Main.bundle.getString("dialog.window.button.bCancel"));
        bAccept.setPrefWidth(100);
        bCancel.setPrefWidth(100);
        bAccept.setOnAction(e-> bAcceptAction(e));
        bCancel.setOnAction(e-> bCancelAction(e));
        createStage(parent, type);
    }

    private void createStage(Stage parent, int type) {
        Stage stage = new Stage();
        stage.setWidth(400);
        stage.setHeight(300);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(parent.getScene().getWindow());
        BorderPane pane = new BorderPane();
        FlowPane flowPane = new FlowPane(10,10);;
        flowPane.setAlignment(Pos.CENTER);
        pane.setCenter(lText);
        pane.setBottom(flowPane);
        Scene scene = new Scene(pane);
        if (type == message) {
            massage(stage, flowPane);
        } else if (type == question) {
            question(stage, flowPane);
        }
        stage.setScene(scene);
        stage.show();
    }

    private void massage(Stage stage, FlowPane flowPane) {
        stage.setTitle(Main.bundle.getString("dialog.window.message.title"));
        flowPane.getChildren().add(bAccept);
    }

    private void question(Stage stage, FlowPane flowPane) {
        stage.setTitle(Main.bundle.getString("dialog.window.question.title"));
        flowPane.getChildren().addAll(bAccept, bCancel);
    }

    private void bAcceptAction(ActionEvent e) {
        choice = true;
        closeStage(e);
    }

    private void bCancelAction(ActionEvent e) {
        choice = false;
        closeStage(e);
    }

    private void closeStage (ActionEvent e) {
        ((Stage)((Node)e.getSource()).getScene().getWindow()).close();
    }

}
