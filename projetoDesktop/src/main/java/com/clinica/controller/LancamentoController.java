package com.clinica.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class LancamentoController extends MainController {
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
