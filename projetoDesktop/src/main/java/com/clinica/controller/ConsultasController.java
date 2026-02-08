package com.clinica.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConsultasController {

    @FXML private TableView<Object> tblConsultas;

    @FXML
    private void initialize() {
        // Duplo clique abre modal de edição (visual)
        tblConsultas.setRowFactory(tv -> {
            TableRow<Object> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && !row.isEmpty()) {
                    onEditar();
                }
            });
            return row;
        });
    }

    @FXML
    public void onNova() {
        openConsultaModal("Nova Consulta");
    }

    @FXML
    public void onEditar() {
        if (tblConsultas.getSelectionModel().getSelectedItem() == null) {
            info("Selecione uma consulta (visual).");
            return;
        }
        openConsultaModal("Editar Consulta");
    }

    @FXML
    public void onReceita() {
        if (tblConsultas.getSelectionModel().getSelectedItem() == null) {
            info("Selecione uma consulta (visual).");
            return;
        }
        openReceitaModal();
    }

    @FXML
    public void onExcluir() {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Excluir");
        a.setHeaderText("Excluir consulta?");
        a.setContentText("Apenas visual (sem banco).");
        a.showAndWait();
    }

    /* ===================== MODAIS ===================== */

    private void openConsultaModal(String title) {
        openModal("/com/clinica/views/modals/consulta_modal.fxml", title);
    }

    private void openReceitaModal() {
        openModal("/com/clinica/views/modals/receita_modal.fxml", "Receita");
    }

    private void openModal(String fxml, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
        } catch (Exception ex) {
            info("Erro ao abrir modal:\n" + ex.getMessage());
        }
    }

    private void info(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }
}
