package com.clinica.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ConsultaModalController {

    @FXML private Label lblTitulo;

    @FXML private ComboBox<?> cbPaciente; // depois tipa para Paciente
    @FXML private ComboBox<?> cbMedico;   // depois tipa para Profissional
    @FXML private DatePicker dpData;
    @FXML private TextField txtHora;
    @FXML private ComboBox<String> cbStatus;
    @FXML private TextArea txtObs;

    private Object consulta; // depois tipa para Consulta
    private boolean saved = false;

    @FXML
    private void initialize() {
        cbStatus.getItems().setAll("Agendada", "Realizada", "Cancelada");
        cbStatus.setValue("Agendada");
    }

    public void setConsulta(Object consultaOrNull) {
        this.consulta = consultaOrNull;

        if (consultaOrNull == null) {
            lblTitulo.setText("Nova Consulta");
            // defaults
            dpData.setValue(java.time.LocalDate.now());
            txtHora.setText("");
            txtObs.clear();
        } else {
            lblTitulo.setText("Editar Consulta");
            // TODO: preencher campos com dados da consulta selecionada
            // dpData.setValue(...)
            // txtHora.setText(...)
            // cbStatus.setValue(...)
            // txtObs.setText(...)
        }
    }

    public boolean isSaved() {
        return saved;
    }

    @FXML
    private void onSalvar() {
        // Validação mínima
        if (dpData.getValue() == null) {
            alert("Informe a data da consulta.");
            return;
        }
        if (txtHora.getText() == null || txtHora.getText().trim().isEmpty()) {
            alert("Informe a hora (HH:mm).");
            return;
        }

        // TODO: validar HH:mm e salvar via service/DAO
        // if (consulta == null) criar
        // else atualizar

        saved = true;
        close();
    }

    @FXML
    private void onCancelar() {
        saved = false;
        close();
    }

    private void close() {
        Stage stage = (Stage) lblTitulo.getScene().getWindow();
        stage.close();
    }

    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle("Validação");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
