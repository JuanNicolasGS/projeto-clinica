package com.clinica.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

public class LancamentoController {

    @FXML private TextArea txtObsConsulta;
    @FXML private TextField txtBuscaMedicamento;

    @FXML
    private void initialize() {
        // apenas para garantir que o controller carrega
    }

    @FXML
    private void onCancelarConsulta() {
        info("Cancelar (visual).");
    }

    @FXML
    private void onSalvarConsulta() {
        info("Salvar Consulta (visual).");
    }

    @FXML
    private void onAddMedicamento() {
        if (txtBuscaMedicamento != null) {
            info("Adicionar medicamento: " + txtBuscaMedicamento.getText());
            txtBuscaMedicamento.clear();
        } else {
            info("Adicionar medicamento (visual).");
        }
    }

    @FXML
    private void onImprimirReceita() {
        info("Imprimir Receita (visual).");
    }

    private void info(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }
}
