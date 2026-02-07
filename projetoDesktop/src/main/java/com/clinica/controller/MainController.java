package com.clinica.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.List;

public class MainController {

    @FXML private StackPane contentPane;

    @FXML private Button btnPacientes;
    @FXML private Button btnProfissionais;
    @FXML private Button btnConsultas;
    @FXML private Button btnLancamento;

    @FXML private Label lblStatus;

    private static final String VIEW_PACIENTES     = "/com/clinica/views/pacientes_view.fxml";
    private static final String VIEW_PROFISSIONAIS = "/com/clinica/views/profissionais_view.fxml";
    private static final String VIEW_LANCAMENTO    = "/com/clinica/views/lancamento_view.fxml";
    private static final String VIEW_CONSULTAS = "/com/clinica/views/consultas_view.fxml";


    @FXML
    private void initialize() {
        setStatus("init");
        loadView(VIEW_PACIENTES);
        setSelected(btnPacientes);
        setStatus("Pacientes");
    }

    @FXML
    private void goPacientes(ActionEvent e) {
        setStatus("Clicou Pacientes");
        loadView(VIEW_PACIENTES);
        setSelected(btnPacientes);
    }

    @FXML
    private void goProfissionais(ActionEvent e) {
        setStatus("Clicou Profissionais");
        loadView(VIEW_PROFISSIONAIS);
        setSelected(btnProfissionais);
    }

    @FXML
    private void goConsultas(ActionEvent e) {
        setStatus("Consultas");
        loadView(VIEW_CONSULTAS);
        setSelected(btnConsultas);
    }

    @FXML
    private void goLancamento(ActionEvent e) {
        setStatus("Clicou Lançamento");
        loadView(VIEW_LANCAMENTO);
        setSelected(btnLancamento);
    }

    private void loadView(String resourcePath) {
        try {
            Node view = FXMLLoader.load(getClass().getResource(resourcePath));
            contentPane.getChildren().setAll(view);
        } catch (IOException ex) {
            Label error = new Label("Erro ao carregar: " + resourcePath + "\n" + ex);
            contentPane.getChildren().setAll(error);
        }
    }

    private void setSelected(Button selected) {
        List<Button> buttons = List.of(btnPacientes, btnProfissionais, btnConsultas, btnLancamento);
        for (Button b : buttons) {
            b.getStyleClass().remove("menu-selected");
            if (!b.getStyleClass().contains("menu-button")) b.getStyleClass().add("menu-button");
        }
        if (!selected.getStyleClass().contains("menu-selected")) selected.getStyleClass().add("menu-selected");
    }

    private void setStatus(String text) {
        if (lblStatus != null) lblStatus.setText(text);
    }
}
