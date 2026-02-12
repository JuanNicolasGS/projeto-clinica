package com.clinica.controller;

import com.clinica.dao.ClinicaDAO;
import com.clinica.dao.ConsultaDAO;
import com.clinica.dao.MedicamentoDAO;
import com.clinica.dao.PacienteDAO;
import com.clinica.dao.ProfissionalDAO;
import com.clinica.model.Clinica;
import com.clinica.model.Consulta;
import com.clinica.model.Medicamento;
import com.clinica.model.Paciente;
import com.clinica.model.Profissional;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class LancamentoController extends MainController {
    @FXML private Button btnPacientes;
    @FXML private Button btnProfissionais;
    @FXML private Button btnConsultas;
    @FXML private Button btnLancamento;

    @FXML private ComboBox<Clinica> cbClinica;
    @FXML private ComboBox<Paciente> cbPaciente;
    @FXML private ComboBox<Profissional> cbProfissional;

    @FXML private TextField txtData;
    @FXML private TextField txtValor;
    @FXML private TextArea txtObs;

    @FXML private TextField txtBuscarMedicamento;
    @FXML private Button btnAdicionarMedicamento;

    @FXML private TableView<Medicamento> tblMedicamentos;
    @FXML private TableColumn<Medicamento, String> colMedicamento;

    @FXML private Button btnImprimirReceita;

    @FXML private Button btnSalvarConsulta;
    @FXML private Button btnCancelar;

    private final ObservableList<Clinica> clinicas = FXCollections.observableArrayList();
    private final ObservableList<Paciente> pacientes = FXCollections.observableArrayList();
    private final ObservableList<Profissional> profissionais = FXCollections.observableArrayList();
    private final ObservableList<Medicamento> medicamentos = FXCollections.observableArrayList();

    private final DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Integer idConsultaSalva;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnPacientes.setOnAction(event -> goTo((Node) event.getSource(), "/com/clinica/views/pacientes_view.fxml"));
        btnProfissionais.setOnAction(event -> goTo((Node) event.getSource(), "/com/clinica/views/profissionais_view.fxml"));
        btnConsultas.setOnAction(event -> goTo((Node) event.getSource(), "/com/clinica/views/consultas_view.fxml"));
        btnLancamento.setOnAction(event -> goTo((Node) event.getSource(), "/com/clinica/views/lancamento_view.fxml"));

        btnCancelar.setOnAction(e -> limpar());
        btnAdicionarMedicamento.setOnAction(e -> adicionarMedicamento());
        btnSalvarConsulta.setOnAction(e -> salvarConsulta());
        btnImprimirReceita.setOnAction(e -> abrirReceita());

        configurarCombos();
        configurarMedicamentos();

        carregarCombos();
        limpar();
    }

    private void configurarCombos() {
        cbClinica.setItems(clinicas);
        cbPaciente.setItems(pacientes);
        cbProfissional.setItems(profissionais);

        cbClinica.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Clinica item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNome());
            }
        });
        cbClinica.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Clinica item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNome());
            }
        });
        cbClinica.setConverter(new StringConverter<>() {
            @Override
            public String toString(Clinica object) {
                return object == null ? "" : object.getNome();
            }

            @Override
            public Clinica fromString(String string) {
                return null;
            }
        });

        cbPaciente.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Paciente item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNome() + " - " + item.getCpf());
            }
        });
        cbPaciente.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Paciente item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNome() + " - " + item.getCpf());
            }
        });
        cbPaciente.setConverter(new StringConverter<>() {
            @Override
            public String toString(Paciente object) {
                return object == null ? "" : object.getNome() + " - " + object.getCpf();
            }

            @Override
            public Paciente fromString(String string) {
                return null;
            }
        });

        cbProfissional.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Profissional item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNome() + " - " + item.getRegistro());
            }
        });
        cbProfissional.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Profissional item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNome() + " - " + item.getRegistro());
            }
        });
        cbProfissional.setConverter(new StringConverter<>() {
            @Override
            public String toString(Profissional object) {
                return object == null ? "" : object.getNome() + " - " + object.getRegistro();
            }

            @Override
            public Profissional fromString(String string) {
                return null;
            }
        });
    }

    private void configurarMedicamentos() {
        colMedicamento.setCellValueFactory(v -> new SimpleStringProperty(v.getValue() == null ? "" : v.getValue().getNome()));
        tblMedicamentos.setItems(medicamentos);

        tblMedicamentos.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                Medicamento sel = tblMedicamentos.getSelectionModel().getSelectedItem();
                if (sel != null) medicamentos.remove(sel);
            }
        });
    }

    private void carregarCombos() {
        clinicas.clear();
        clinicas.addAll(new ClinicaDAO().listar());

        try {
            pacientes.clear();
            pacientes.addAll(new PacienteDAO().listar());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            profissionais.clear();
            profissionais.addAll(new ProfissionalDAO().listar());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void adicionarMedicamento() {
        String v = txtBuscarMedicamento.getText() == null ? "" : txtBuscarMedicamento.getText().trim();
        String dig = v.replaceAll("\\D", "");
        if (dig.isBlank()) return;

        try {
            MedicamentoDAO dao = new MedicamentoDAO();
            Medicamento m = dao.buscar(Integer.parseInt(dig));
            if (m == null) {
                alerta(Alert.AlertType.WARNING, "Medicamento não encontrado.");
                return;
            }
            if (medicamentos.stream().anyMatch(x -> x.getId().equals(m.getId()))) {
                alerta(Alert.AlertType.WARNING, "Medicamento já adicionado.");
                return;
            }
            medicamentos.add(m);
            txtBuscarMedicamento.clear();
        } catch (Exception e) {
            e.printStackTrace();
            alerta(Alert.AlertType.ERROR, "Erro ao buscar medicamento.");
        }
    }

    private void salvarConsulta() {
        Clinica clinica = cbClinica.getValue();
        Paciente paciente = cbPaciente.getValue();
        Profissional profissional = cbProfissional.getValue();
        Date data = parseData(txtData.getText());
        double valor = parseValor(txtValor.getText());
        String obs = txtObs.getText();

        if (clinica == null) {
            alerta(Alert.AlertType.WARNING, "Selecione a clínica.");
            return;
        }
        if (paciente == null) {
            alerta(Alert.AlertType.WARNING, "Selecione o paciente.");
            return;
        }
        if (profissional == null) {
            alerta(Alert.AlertType.WARNING, "Selecione o profissional.");
            return;
        }
        if (data == null) {
            alerta(Alert.AlertType.WARNING, "Data inválida (dd/MM/yyyy).");
            return;
        }

        try {
            Consulta c = new Consulta(null, paciente, profissional, clinica, data, valor, obs);
            ConsultaDAO dao = new ConsultaDAO();
            Integer id = dao.inserirConsultaRetornandoId(c);

            if (id == null) {
                alerta(Alert.AlertType.ERROR, "Não foi possível salvar a consulta.");
                return;
            }

            idConsultaSalva = id;
            alerta(Alert.AlertType.INFORMATION, "Consulta salva. ID: " + id);
        } catch (Exception e) {
            e.printStackTrace();
            alerta(Alert.AlertType.ERROR, "Erro ao salvar consulta.");
        }
    }

    private void abrirReceita() {
        if (idConsultaSalva == null) {
            alerta(Alert.AlertType.WARNING, "Salve a consulta primeiro.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/clinica/views/modals/receita_modal.fxml"));
            Parent root = loader.load();

            ReceitaModalController controller = loader.getController();
            controller.setConsultaId(idConsultaSalva);
            controller.setMedicamentosIniciais(medicamentos);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Receita");
            stage.setScene(new Scene(root));
            stage.setWidth(900);
            stage.setHeight(620);
            stage.setMinWidth(900);
            stage.setMinHeight(620);

            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            alerta(Alert.AlertType.ERROR, "Não foi possível abrir a receita.");
        }
    }

    private void limpar() {
        idConsultaSalva = null;
        cbClinica.getSelectionModel().clearSelection();
        cbPaciente.getSelectionModel().clearSelection();
        cbProfissional.getSelectionModel().clearSelection();
        txtData.clear();
        txtValor.clear();
        txtObs.clear();
        txtBuscarMedicamento.clear();
        medicamentos.clear();
    }

    private Date parseData(String texto) {
        try {
            if (texto == null) return null;
            String v = texto.trim();
            if (v.isBlank()) return null;
            LocalDate ld = LocalDate.parse(v, dataFormatter);
            return Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
        } catch (Exception e) {
            return null;
        }
    }

    private double parseValor(String texto) {
        try {
            if (texto == null) return 0.0;
            String v = texto.trim();
            if (v.isBlank()) return 0.0;
            v = v.replace(".", "").replace(",", ".");
            return Double.parseDouble(v);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private void alerta(Alert.AlertType tipo, String msg) {
        Alert a = new Alert(tipo);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
