package com.clinica.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class PacientesController {

    @FXML
    private void onNovo() {
        info("Novo (visual) - aqui você pode abrir um modal depois.");
    }

    @FXML
    private void onSalvar() {
        info("Salvar (visual) - sem banco por enquanto.");
    }

    @FXML
    private void onExcluir() {
        info("Excluir (visual) - sem banco por enquanto.");
    }

    @FXML
    private void onLimpar() {
        info("Limpar (visual).");
    }

    private void info(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }
}
