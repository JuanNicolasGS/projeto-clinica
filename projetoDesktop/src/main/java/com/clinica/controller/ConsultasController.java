package com.clinica.controller;

import com.clinica.dao.ConsultaDAO;
import com.clinica.model.Consulta;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ConsultasController extends MainController {
    @FXML private Button btnPacientes;
    @FXML private Button btnProfissionais;
    @FXML private Button btnConsultas;
    @FXML private Button btnLancamento;

    @FXML private Button btnExcluir;
    @FXML private Button btnNova;

    @FXML private TextField txtBusca;
    @FXML private Button btnFiltrar;

    @FXML private TableView<Consulta> tblConsultas;
    @FXML private TableColumn<Consulta, Number> colId;
    @FXML private TableColumn<Consulta, String> colData;
    @FXML private TableColumn<Consulta, String> colPaciente;
    @FXML private TableColumn<Consulta, String> colProfissional;
    @FXML private TableColumn<Consulta, String> colClinica;
    @FXML private TableColumn<Consulta, String> colValor;

    private final ObservableList<Consulta> consultas = FXCollections.observableArrayList();
    private final ObservableList<Consulta> consultasFiltradas = FXCollections.observableArrayList();

    private final DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Integer idSelecionado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnPacientes.setOnAction(event -> goTo((Node) event.getSource(), "/com/clinica/views/pacientes_view.fxml"));
        btnProfissionais.setOnAction(event -> goTo((Node) event.getSource(), "/com/clinica/views/profissionais_view.fxml"));
        btnConsultas.setOnAction(event -> goTo((Node) event.getSource(), "/com/clinica/views/consultas_view.fxml"));
        btnLancamento.setOnAction(event -> goTo((Node) event.getSource(), "/com/clinica/views/lancamento_view.fxml"));

        btnNova.setOnAction(e -> abrirModalConsulta(null));
        btnExcluir.setOnAction(e -> excluirSelecionada());
        btnFiltrar.setOnAction(e -> filtrar(txtBusca.getText()));

        configurarTabela();
        configurarSelecao();

        carregarLista();
        atualizarEstado();
    }

    private void configurarTabela() {
        colId.setCellValueFactory(v -> new SimpleObjectProperty<>(v.getValue().getId()));

        colData.setCellValueFactory(v -> {
            if (v.getValue().getData() == null) return new SimpleStringProperty("");
            LocalDate ld = v.getValue().getData().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return new SimpleStringProperty(ld.format(dataFormatter));
        });

        colPaciente.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getPaciente() == null ? "" : nz(v.getValue().getPaciente().getNome())));
        colProfissional.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getProfissional() == null ? "" : nz(v.getValue().getProfissional().getNome())));
        colClinica.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getClinica() == null ? "" : nz(v.getValue().getClinica().getNome())));
        colValor.setCellValueFactory(v -> new SimpleStringProperty(formatarValor(v.getValue().getValor())));

        tblConsultas.setItems(consultasFiltradas);
    }

    private void configurarSelecao() {
        tblConsultas.getSelectionModel().selectedItemProperty().addListener((obs, o, sel) -> {
            idSelecionado = sel == null ? null : sel.getId();
            atualizarEstado();
            if (sel != null && sel.getId() != null) {
                tblConsultas.setOnMouseClicked(e -> {
                    if (e.getClickCount() == 2) abrirModalConsulta(sel.getId());
                });
            }
        });
    }

    private void carregarLista() {
        try {
            ConsultaDAO dao = new ConsultaDAO();
            List<Consulta> lista = dao.listarDetalhada();
            consultas.setAll(lista);
            consultasFiltradas.setAll(lista);
        } catch (Exception e) {
            e.printStackTrace();
            alerta(Alert.AlertType.ERROR, "Erro ao carregar consultas.");
        }
    }

    private void filtrar(String termo) {
        String t = termo == null ? "" : termo.trim();
        if (t.isBlank()) {
            consultasFiltradas.setAll(consultas);
            return;
        }

        String dig = t.replaceAll("\\D", "");

        if (!dig.isBlank()) {
            consultasFiltradas.setAll(
                    consultas.stream()
                            .filter(c -> c.getId() != null && String.valueOf(c.getId()).contains(dig))
                            .toList()
            );
            return;
        }

        String tl = t.toLowerCase();
        consultasFiltradas.setAll(
                consultas.stream()
                        .filter(c ->
                                (c.getPaciente() != null && nz(c.getPaciente().getNome()).toLowerCase().contains(tl)) ||
                                        (c.getProfissional() != null && nz(c.getProfissional().getNome()).toLowerCase().contains(tl)) ||
                                        (c.getClinica() != null && nz(c.getClinica().getNome()).toLowerCase().contains(tl)))
                        .toList()
        );
    }

    private void excluirSelecionada() {
        if (idSelecionado == null) {
            alerta(Alert.AlertType.WARNING, "Selecione uma consulta.");
            return;
        }

        Optional<ButtonType> r = new Alert(Alert.AlertType.CONFIRMATION, "Excluir a consulta selecionada?", ButtonType.YES, ButtonType.NO)
                .showAndWait();

        if (r.isEmpty() || r.get() != ButtonType.YES) return;

        try {
            ConsultaDAO dao = new ConsultaDAO();
            boolean ok = dao.excluirConsulta(idSelecionado);

            if (!ok) {
                alerta(Alert.AlertType.ERROR, "Não foi possível excluir.");
                return;
            }

            idSelecionado = null;
            carregarLista();
            filtrar(txtBusca.getText());
            atualizarEstado();
            alerta(Alert.AlertType.INFORMATION, "Excluída com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
            alerta(Alert.AlertType.ERROR, "Erro ao excluir.");
        }
    }

    private void abrirModalConsulta(Integer idConsulta) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/clinica/views/modals/consulta_modal.fxml"));
            Parent root = loader.load();

            ConsultaModalController controller = loader.getController();
            controller.setConsultaId(idConsulta);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(idConsulta == null ? "Nova Consulta" : "Editar Consulta");
            stage.setScene(new Scene(root));
            stage.setWidth(820);
            stage.setHeight(560);
            stage.setMinWidth(820);
            stage.setMinHeight(560);
            stage.showAndWait();

            carregarLista();
            filtrar(txtBusca.getText());
        } catch (Exception e) {
            e.printStackTrace();
            alerta(Alert.AlertType.ERROR, "Não foi possível abrir o modal.");
        }
    }

    private void atualizarEstado() {
        btnExcluir.setDisable(idSelecionado == null);
    }

    private String formatarValor(double v) {
        return String.format("R$ %.2f", v).replace('.', ',');
    }

    private String nz(String s) {
        return s == null ? "" : s;
    }

    private void alerta(Alert.AlertType tipo, String msg) {
        Alert a = new Alert(tipo);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
