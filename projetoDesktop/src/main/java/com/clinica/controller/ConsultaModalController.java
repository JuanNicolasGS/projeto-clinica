package com.clinica.controller;

import com.clinica.dao.ClinicaDAO;
import com.clinica.dao.ConsultaDAO;
import com.clinica.dao.PacienteDAO;
import com.clinica.dao.ProfissionalDAO;
import com.clinica.model.Clinica;
import com.clinica.model.Consulta;
import com.clinica.model.Paciente;
import com.clinica.model.Profissional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class ConsultaModalController extends MainController {
    @FXML private ComboBox<Clinica> cbClinica;
    @FXML private ComboBox<Profissional> cbProfissional;
    @FXML private ComboBox<Paciente> cbPaciente;

    @FXML private DatePicker dpData;
    @FXML private TextField txtValor;
    @FXML private TextArea txtObs;

    private final ObservableList<Clinica> clinicas = FXCollections.observableArrayList();
    private final ObservableList<Profissional> profissionais = FXCollections.observableArrayList();
    private final ObservableList<Paciente> pacientes = FXCollections.observableArrayList();

    private Integer consultaId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarCombos();
        carregarCombos();
        if (consultaId != null) carregarConsulta(consultaId);
    }

    public void setConsultaId(Integer id) {
        this.consultaId = id;
    }

    private void configurarCombos() {
        cbClinica.setItems(clinicas);
        cbProfissional.setItems(profissionais);
        cbPaciente.setItems(pacientes);

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

    private void carregarCombos() {
        clinicas.clear();
        clinicas.addAll(new ClinicaDAO().listar());

        try {
            profissionais.clear();
            profissionais.addAll(new ProfissionalDAO().listar());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            pacientes.clear();
            pacientes.addAll(new PacienteDAO().listar());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void carregarConsulta(int id) {
        try {
            ConsultaDAO dao = new ConsultaDAO();
            Consulta c = dao.buscarPorId(id);
            if (c == null) return;

            cbClinica.getSelectionModel().select(c.getClinica());
            cbProfissional.getSelectionModel().select(c.getProfissional());
            cbPaciente.getSelectionModel().select(c.getPaciente());

            if (c.getData() != null) {
                LocalDate ld = c.getData().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                dpData.setValue(ld);
            }

            txtValor.setText(formatarValor(c.getValor()));
            txtObs.setText(c.getObservacoes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void salvar() {
        Clinica clinica = cbClinica.getValue();
        Profissional profissional = cbProfissional.getValue();
        Paciente paciente = cbPaciente.getValue();
        LocalDate ld = dpData.getValue();
        double valor = parseValor(txtValor.getText());
        String obs = txtObs.getText();

        if (clinica == null) {
            alerta(Alert.AlertType.WARNING, "Selecione a clínica.");
            return;
        }
        if (profissional == null) {
            alerta(Alert.AlertType.WARNING, "Selecione o profissional.");
            return;
        }
        if (paciente == null) {
            alerta(Alert.AlertType.WARNING, "Selecione o paciente.");
            return;
        }
        if (ld == null) {
            alerta(Alert.AlertType.WARNING, "Selecione a data.");
            return;
        }

        try {
            Date data = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Consulta c = new Consulta(consultaId, paciente, profissional, clinica, data, valor, obs);

            ConsultaDAO dao = new ConsultaDAO();
            boolean ok = consultaId == null ? dao.inserirConsulta(c) : dao.editarConsulta(c);

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
    private void cancelar() {
        fechar();
    }

    private void fechar() {
        Stage s = (Stage) cbClinica.getScene().getWindow();
        s.close();
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

    private String formatarValor(double v) {
        return String.format("R$ %.2f", v).replace('.', ',');
    }

    private void alerta(Alert.AlertType tipo, String msg) {
        Alert a = new Alert(tipo);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
