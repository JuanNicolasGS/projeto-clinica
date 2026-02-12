package com.clinica.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ConsultasController extends MainController{
    @FXML private Button btnPacientes;
    @FXML private Button btnProfissionais;
    @FXML private Button btnConsultas;
    @FXML private Button btnLancamento;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnPacientes.setOnAction(event -> goTo((Node) event.getSource(), "/com/clinica/views/pacientes_view.fxml"));
        btnProfissionais.setOnAction(event -> goTo((Node) event.getSource(), "/com/clinica/views/profissionais_view.fxml"));
        btnConsultas.setOnAction(event -> goTo((Node) event.getSource(), "/com/clinica/views/consultas_view.fxml"));
        btnLancamento.setOnAction(event -> goTo((Node) event.getSource(), "/com/clinica/views/lancamento_view.fxml"));
    }
}
