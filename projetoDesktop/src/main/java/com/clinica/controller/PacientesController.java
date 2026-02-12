package com.clinica.controller;

import com.clinica.dao.PacienteDAO;
import com.clinica.model.Paciente;
import com.clinica.util.CPFUtil;
import com.clinica.util.TelefoneUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PacientesController extends MainController {
    @FXML private Button btnPacientes;
    @FXML private Button btnProfissionais;
    @FXML private Button btnConsultas;
    @FXML private Button btnLancamento;

    @FXML private Button btnSalvar;
    @FXML private Button btnExcluir;

    @FXML private TextField txtBusca;

    @FXML private TableView<Paciente> tblPacientes;
    @FXML private TableColumn<Paciente, String> colCpf;
    @FXML private TableColumn<Paciente, String> colNome;
    @FXML private TableColumn<Paciente, String> colNome1;

    @FXML private TextField txtCpf;
    @FXML private TextField txtNome;
    @FXML private TextField txtDataNasc;

    @FXML private TextField txtTelefone;
    @FXML private TableView<String> tblTelefones;
    @FXML private TableColumn<String, String> colNumero;

    @FXML private Label lblTela;

    private final ObservableList<Paciente> pacientes = FXCollections.observableArrayList();
    private final ObservableList<Paciente> pacientesFiltrados = FXCollections.observableArrayList();
    private final ObservableList<String> telefones = FXCollections.observableArrayList();

    private final DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private String cpfSelecionado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnPacientes.setOnAction(event -> goTo((Node) event.getSource(), "/com/clinica/views/pacientes_view.fxml"));
        btnProfissionais.setOnAction(event -> goTo((Node) event.getSource(), "/com/clinica/views/profissionais_view.fxml"));
        btnConsultas.setOnAction(event -> goTo((Node) event.getSource(), "/com/clinica/views/consultas_view.fxml"));
        btnLancamento.setOnAction(event -> goTo((Node) event.getSource(), "/com/clinica/views/lancamento_view.fxml"));

        btnSalvar.setOnAction(e -> salvar());
        btnExcluir.setOnAction(e -> excluir());

        tblTelefones.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                String sel = tblTelefones.getSelectionModel().getSelectedItem();
                if (sel != null) telefones.remove(sel);
            }
        });

        configurarTabelas();
        configurarBusca();
        configurarSelecao();

        carregarLista();
        atualizarEstado();
    }

    private void configurarTabelas() {
        colCpf.setCellValueFactory(v -> new SimpleStringProperty(formatarCpfSeguro(v.getValue().getCpf())));
        colNome.setCellValueFactory(v -> new SimpleStringProperty(nz(v.getValue().getNome())));
        colNome1.setCellValueFactory(v -> new SimpleStringProperty(primeiroTelefoneFormatado(v.getValue())));

        tblPacientes.setItems(pacientesFiltrados);

        colNumero.setCellValueFactory(v -> new SimpleStringProperty(formatarTelefoneSeguro(v.getValue())));
        tblTelefones.setItems(telefones);
    }

    private void configurarBusca() {
        txtBusca.textProperty().addListener((obs, o, n) -> filtrar(n));
    }

    private void configurarSelecao() {
        tblPacientes.getSelectionModel().selectedItemProperty().addListener((obs, o, sel) -> {
            if (sel == null) return;
            carregarNoFormulario(sel.getCpf());
        });
    }

    private void carregarLista() {
        try {
            PacienteDAO dao = new PacienteDAO();
            List<Paciente> lista = dao.listar();
            pacientes.setAll(lista);
            pacientesFiltrados.setAll(lista);
        } catch (Exception e) {
            e.printStackTrace();
            alerta(Alert.AlertType.ERROR, "Erro ao carregar pacientes.");
        }
    }

    private void filtrar(String termo) {
        String t = normalizarCpf(termo);
        if (t.isBlank()) {
            pacientesFiltrados.setAll(pacientes);
            return;
        }
        pacientesFiltrados.setAll(
                pacientes.stream()
                        .filter(p -> normalizarCpf(p.getCpf()).contains(t))
                        .toList()
        );
    }

    private void carregarNoFormulario(String cpf) {
        try {
            String c = normalizarCpf(cpf);
            PacienteDAO dao = new PacienteDAO();
            Paciente p = dao.buscar(c);
            if (p == null) return;

            cpfSelecionado = c;

            txtCpf.setText(formatarCpfSeguro(p.getCpf()));
            txtCpf.setDisable(true);

            txtNome.setText(nz(p.getNome()));
            txtDataNasc.setText(p.getDataNasc() == null ? "" : p.getDataNasc().toLocalDate().format(dataFormatter));

            telefones.clear();
            if (p.getTelefones() != null) {
                for (String tel : p.getTelefones()) {
                    String n = normalizarTelefone(tel);
                    if (!n.isBlank() && !telefones.contains(n)) telefones.add(n);
                }
            }

            atualizarEstado();
        } catch (Exception e) {
            e.printStackTrace();
            alerta(Alert.AlertType.ERROR, "Erro ao carregar paciente.");
        }
    }

    @FXML
    private void addTelefones() {
        String digitado = txtTelefone.getText();

        if (!TelefoneUtil.isValido(digitado)) {
            alerta(Alert.AlertType.WARNING, "Telefone inválido. Use (00) 0000-0000 ou (00) 00000-0000.");
            return;
        }

        String normalizado = normalizarTelefone(digitado);

        if (telefones.contains(normalizado)) {
            alerta(Alert.AlertType.WARNING, "Telefone já adicionado.");
            return;
        }

        telefones.add(normalizado);
        txtTelefone.clear();
    }

    @FXML
    private void salvar() {
        try {
            String cpf = normalizarCpf(txtCpf.getText());
            String nome = txtNome.getText() == null ? "" : txtNome.getText().trim();
            Date data = converterDataSql(txtDataNasc.getText());

            if (!CPFUtil.isValido(cpf)) {
                alerta(Alert.AlertType.WARNING, "CPF inválido.");
                return;
            }

            if (nome.isBlank()) {
                alerta(Alert.AlertType.WARNING, "Informe o nome.");
                return;
            }

            if (data == null) {
                alerta(Alert.AlertType.WARNING, "Data inválida (dd/MM/yyyy).");
                return;
            }

            if (telefones.isEmpty()) {
                alerta(Alert.AlertType.WARNING, "Adicione pelo menos um telefone.");
                return;
            }

            Paciente p = new Paciente();
            p.setCpf(cpf);
            p.setNome(nome);
            p.setDataNasc(data);
            p.setTelefones(new ArrayList<>(telefones));

            PacienteDAO dao = new PacienteDAO();
            Paciente existente = dao.buscar(cpf);

            boolean ok = existente == null ? dao.salvar(p) : dao.editar(p);

            if (!ok) {
                alerta(Alert.AlertType.ERROR, "Não foi possível salvar.");
                return;
            }

            limparFormulario();
            carregarLista();
            filtrar(txtBusca.getText());
            alerta(Alert.AlertType.INFORMATION, "Salvo com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
            alerta(Alert.AlertType.ERROR, "Erro ao salvar.");
        }
    }

    private void excluir() {
        if (cpfSelecionado == null || cpfSelecionado.isBlank()) {
            alerta(Alert.AlertType.WARNING, "Selecione um paciente na tabela.");
            return;
        }

        Optional<ButtonType> r = new Alert(Alert.AlertType.CONFIRMATION, "Excluir o paciente selecionado?", ButtonType.YES, ButtonType.NO)
                .showAndWait();

        if (r.isEmpty() || r.get() != ButtonType.YES) return;

        try {
            PacienteDAO dao = new PacienteDAO();
            boolean ok = dao.remover(cpfSelecionado);

            if (!ok) {
                alerta(Alert.AlertType.ERROR, "Não foi possível excluir.");
                return;
            }

            limparFormulario();
            carregarLista();
            filtrar(txtBusca.getText());
            alerta(Alert.AlertType.INFORMATION, "Excluído com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
            alerta(Alert.AlertType.ERROR, "Erro ao excluir.");
        }
    }

    private Date converterDataSql(String texto) {
        try {
            if (texto == null) return null;
            String v = texto.trim();
            if (v.isBlank()) return null;
            LocalDate localDate = LocalDate.parse(v, dataFormatter);
            return Date.valueOf(localDate);
        } catch (Exception e) {
            return null;
        }
    }

    private void limparFormulario() {
        cpfSelecionado = null;

        txtCpf.clear();
        txtCpf.setDisable(false);

        txtNome.clear();
        txtDataNasc.clear();
        txtTelefone.clear();

        telefones.clear();

        tblPacientes.getSelectionModel().clearSelection();
        atualizarEstado();
    }

    private void atualizarEstado() {
        boolean temSel = cpfSelecionado != null && !cpfSelecionado.isBlank();
        btnExcluir.setDisable(!temSel);
    }

    private String primeiroTelefoneFormatado(Paciente p) {
        if (p == null || p.getTelefones() == null || p.getTelefones().isEmpty()) return "";
        return formatarTelefoneSeguro(p.getTelefones().get(0));
    }

    private String normalizarCpf(String cpf) {
        if (cpf == null) return "";
        return cpf.replaceAll("\\D", "");
    }

    private String normalizarTelefone(String tel) {
        if (tel == null) return "";
        return tel.replaceAll("\\D", "");
    }

    private String formatarCpfSeguro(String cpf) {
        String c = normalizarCpf(cpf);
        if (c.length() != 11) return nz(cpf);
        return CPFUtil.formatar(c);
    }

    private String formatarTelefoneSeguro(String tel) {
        String t = normalizarTelefone(tel);
        if (t.length() == 10) return "(" + t.substring(0, 2) + ") " + t.substring(2, 6) + "-" + t.substring(6);
        if (t.length() == 11) return "(" + t.substring(0, 2) + ") " + t.substring(2, 7) + "-" + t.substring(7);
        return nz(tel);
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
