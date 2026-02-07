package com.clinica.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConsultasController {

    @FXML private TableView<?> tblConsultas; // depois você troca para TableView<Consulta>
    @FXML private ComboBox<String> cbStatus;
    @FXML private TextField txtBusca;
    @FXML private DatePicker dpDe, dpAte;

    @FXML
    private void initialize() {
        // Exemplo de status. Depois você preenche do seu enum/tabela.
        cbStatus.getItems().setAll("Todas", "Agendada", "Realizada", "Cancelada");
        cbStatus.setValue("Todas");

        // Duplo clique abre editar
        tblConsultas.setRowFactory(tv -> {
            TableRow<?> row = new TableRow<>();
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
        openConsultaModal(null); // null = nova
    }

    @FXML
    public void onEditar() {
        Object selected = tblConsultas.getSelectionModel().getSelectedItem();
        if (selected == null) {
            alert("Selecione uma consulta para editar.");
            return;
        }
        openConsultaModal(selected);
    }

    @FXML
    public void onExcluir() {
        Object selected = tblConsultas.getSelectionModel().getSelectedItem();
        if (selected == null) {
            alert("Selecione uma consulta para excluir.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar exclusão");
        confirm.setHeaderText("Excluir consulta selecionada?");
        confirm.setContentText("Essa ação não pode ser desfeita.");
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                // TODO: chamar seu service/DAO para excluir
                // TODO: atualizar tabela (reload)
            }
        });
    }

    @FXML
    public void onLimparFiltros() {
        txtBusca.clear();
        dpDe.setValue(null);
        dpAte.setValue(null);
        cbStatus.setValue("Todas");
        // TODO: recarregar tabela sem filtros
    }

    private void openConsultaModal(Object consultaOrNull) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/clinica/views/consulta_modal.fxml"));
            Parent root = loader.load();

            ConsultaModalController controller = loader.getController();
            controller.setConsulta(consultaOrNull); // você vai tipar depois para Consulta

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(consultaOrNull == null ? "Nova Consulta" : "Editar Consulta");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();

            if (controller.isSaved()) {
                // TODO: recarregar tabela (reload)
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            alert("Erro ao abrir modal: " + ex.getMessage());
        }
    }

    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Aviso");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
