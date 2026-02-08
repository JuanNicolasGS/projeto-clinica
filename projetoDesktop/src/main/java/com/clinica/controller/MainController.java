package com.clinica.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.util.List;

public class MainController {

    @FXML private StackPane contentPane;
    @FXML private Label lblStatus;

    @FXML private Button btnPacientes;
    @FXML private Button btnProfissionais;
    @FXML private Button btnConsultas;
    @FXML private Button btnExames;
    @FXML private Button btnLancamento;

    private static final String V_LANCAMENTO = "/com/clinica/views/lancamento_view.fxml";
    private static final String V_PACIENTES     = "/com/clinica/views/pacientes_view.fxml";
    private static final String V_PROFISSIONAIS = "/com/clinica/views/profissionais_view.fxml";
    private static final String V_CONSULTAS     = "/com/clinica/views/consultas_view.fxml";
    private static final String V_EXAMES        = "/com/clinica/views/exames_view.fxml";

    @FXML
    private void initialize() {
        loadView(V_PACIENTES);
        setSelected(btnPacientes);
        lblStatus.setText("Pacientes");
    }

    @FXML private void goPacientes()     { nav("Pacientes", btnPacientes, V_PACIENTES); }
    @FXML private void goProfissionais() { nav("Profissionais", btnProfissionais, V_PROFISSIONAIS); }
    @FXML private void goConsultas()     { nav("Consultas", btnConsultas, V_CONSULTAS); }
    @FXML private void goExames()        { nav("Exames", btnExames, V_EXAMES); }

    private void nav(String title, Button selected, String view) {
        loadView(view);
        setSelected(selected);
        lblStatus.setText(title);
    }

    private void loadView(String resourcePath) {
        try {
            var url = getClass().getResource(resourcePath);
            if (url == null) {
                contentPane.getChildren().setAll(new Label("View não encontrada:\n" + resourcePath));
                return;
            }
            Node view = FXMLLoader.load(url);
            contentPane.getChildren().setAll(view);
        } catch (Exception ex) {
            contentPane.getChildren().setAll(new Label("Erro ao carregar view:\n" + ex.getMessage()));
        }
    }

    private void setSelected(Button selected) {
        clearIfNotNull(btnPacientes);
        clearIfNotNull(btnProfissionais);
        clearIfNotNull(btnConsultas);
        clearIfNotNull(btnExames);
        clearIfNotNull(btnLancamento); // se existir

        if (selected != null && !selected.getStyleClass().contains("menu-selected")) {
            selected.getStyleClass().add("menu-selected");
        }
    }

    private void clearIfNotNull(Button b) {
        if (b != null) b.getStyleClass().remove("menu-selected");
    }

    @FXML
    private void goLancamento() {
        nav("Lançamento", btnLancamento, V_LANCAMENTO);
    }

}
