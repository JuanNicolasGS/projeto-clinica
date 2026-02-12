package com.clinica.controller;

import javafx.fxml.Initializable;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public abstract class MainController implements Initializable {
    public void goTo(Node source, String path){
        try {
            Parent root = FXMLLoader.load(getClass().getResource(path)); // carrega o novo fxml
            Stage stage = (Stage) source.getScene().getWindow(); // captura o atual fxml
            stage.setScene(new Scene(root)); // trocar o atual pelo novo
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
