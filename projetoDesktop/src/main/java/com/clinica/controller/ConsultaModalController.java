package com.clinica.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ConsultaModalController {

    @FXML private Label lblTitulo;

    public void setTitulo(String titulo) {
        lblTitulo.setText(titulo);
    }

    @FXML
    private void onSalvar() {
        close();
    }

    @FXML
    private void onCancelar() {
        close();
    }

    private void close() {
        Stage st = (Stage) lblTitulo.getScene().getWindow();
        st.close();
    }
}
