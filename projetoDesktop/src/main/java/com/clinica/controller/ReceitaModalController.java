package com.clinica.controller;

import com.clinica.dao.MedicamentoDAO;
import com.clinica.dao.ReceitaDAO;
import com.clinica.model.Consulta;
import com.clinica.model.Medicamento;
import com.clinica.model.Receita;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class ReceitaModalController extends MainController {
    @FXML private Label lblTitulo;

    @FXML private DatePicker dpData;
    @FXML private DatePicker dpValidade;
    @FXML private TextArea txtObs;

    @FXML private TextField txtBuscaMed;

    @FXML private TableView<Medicamento> tblMedicamentos;
    @FXML private TableColumn<Medicamento, String> colMedNome;
    @FXML private TableColumn<Medicamento, Void> colMedAcao;

    private final ObservableList<Medicamento> meds = FXCollections.observableArrayList();

    private Integer consultaId;
    private Integer receitaId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colMedNome.setCellValueFactory(v -> new SimpleStringProperty(v.getValue() == null ? "" : v.getValue().getNome()));
        tblMedicamentos.setItems(meds);
        configurarAcao();
        carregarSeExistir();
    }

    public void setConsultaId(Integer consultaId) {
        this.consultaId = consultaId;
        carregarSeExistir();
    }

    public void setMedicamentosIniciais(ObservableList<Medicamento> lista) {
        if (lista == null) return;
        meds.clear();
        meds.addAll(lista);
    }

    @FXML
    private void salvar() {
        if (consultaId == null) {
            alerta(Alert.AlertType.WARNING, "Consulta inválida.");
            return;
        }

        LocalDate ldData = dpData.getValue();
        if (ldData == null) {
            alerta(Alert.AlertType.WARNING, "Selecione a data.");
            return;
        }

        Date data = Date.from(ldData.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date validade = null;

        LocalDate ldVal = dpValidade.getValue();
        if (ldVal != null) validade = Date.from(ldVal.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Receita r = new Receita();
        r.setId(receitaId);
        r.setData(data);
        r.setValidade(validade);
        r.setObservacoes(txtObs.getText());
        r.setConsulta(new Consulta(consultaId, null, null, null, null, 0.0, null));
        r.setMedicamentos(new ArrayList<>(meds));

        try {
            ReceitaDAO dao = new ReceitaDAO();
            boolean ok = receitaId == null ? dao.salvar(r) : dao.editar(r);

            if (!ok) {
                alerta(Alert.AlertType.ERROR, "Não foi possível salvar.");
                return;
            }

            fechar();
        } catch (Exception e) {
            e.printStackTrace();
            alerta(Alert.AlertType.ERROR, "Erro ao salvar.");
        }
    }

    @FXML
    private void fechar() {
        Stage s = (Stage) tblMedicamentos.getScene().getWindow();
        s.close();
    }

    @FXML
    private void adicionarMedicamento() {
        String v = txtBuscaMed.getText() == null ? "" : txtBuscaMed.getText().trim();
        String dig = v.replaceAll("\\D", "");
        if (dig.isBlank()) return;

        try {
            MedicamentoDAO dao = new MedicamentoDAO();
            Medicamento m = dao.buscar(Integer.parseInt(dig));
            if (m == null) {
                alerta(Alert.AlertType.WARNING, "Medicamento não encontrado.");
                return;
            }
            if (meds.stream().anyMatch(x -> x.getId().equals(m.getId()))) {
                alerta(Alert.AlertType.WARNING, "Medicamento já adicionado.");
                return;
            }
            meds.add(m);
            txtBuscaMed.clear();
        } catch (Exception e) {
            e.printStackTrace();
            alerta(Alert.AlertType.ERROR, "Erro ao buscar medicamento.");
        }
    }

    private void configurarAcao() {
        colMedAcao.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Remover");
            {
                btn.getStyleClass().add("btn-danger");
                btn.setOnAction(e -> {
                    Medicamento m = getTableView().getItems().get(getIndex());
                    meds.remove(m);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void carregarSeExistir() {
        if (consultaId == null || dpData == null) return;
        try {
            ReceitaDAO dao = new ReceitaDAO();
            Receita r = dao.buscarPorConsulta(consultaId);
            if (r == null) return;

            receitaId = r.getId();

            if (r.getData() != null) {
                LocalDate ld = r.getData().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                dpData.setValue(ld);
            }
            if (r.getValidade() != null) {
                LocalDate ld = r.getValidade().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                dpValidade.setValue(ld);
            }

            txtObs.setText(r.getObservacoes());

            meds.clear();
            if (r.getMedicamentos() != null) meds.addAll(r.getMedicamentos());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void alerta(Alert.AlertType tipo, String msg) {
        Alert a = new Alert(tipo);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
