package com.clinica.controller;

import com.clinica.dao.ClinicaDAO;
import com.clinica.dao.ProfissionalDAO;
import com.clinica.model.Clinica;
import com.clinica.model.Profissional;
import com.clinica.util.TelefoneUtil;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProfissionaisController extends MainController {
    @FXML private Button btnPacientes;
    @FXML private Button btnProfissionais;
    @FXML private Button btnConsultas;
    @FXML private Button btnLancamento;

    @FXML private TextField txtBusca;

    @FXML private TableView<Profissional> tblProfissionais;
    @FXML private TableColumn<Profissional, Number> colId;
    @FXML private TableColumn<Profissional, String> colNome;
    @FXML private TableColumn<Profissional, String> colEspecialidade;
    @FXML private TableColumn<Profissional, String> colRegistro;

    @FXML private ComboBox<Clinica> cbClinica;
    @FXML private TextField txtNome;
    @FXML private TextField txtEspecialidade;
    @FXML private TextField txtRegistro;

    @FXML private TextField txtTelefone;
    @FXML private Button btnAdicionarTelefone;
    @FXML private TableView<String> tblTelefones;
    @FXML private TableColumn<String, String> colNumero;

    @FXML private Button btnSalvar;
    @FXML private Button btnExcluir;

    private final ObservableList<Profissional> profissionais = FXCollections.observableArrayList();
    private final ObservableList<Profissional> profissionaisFiltrados = FXCollections.observableArrayList();
    private final ObservableList<Clinica> clinicas = FXCollections.observableArrayList();
    private final ObservableList<String> telefones = FXCollections.observableArrayList();

    private Integer idSelecionado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnPacientes.setOnAction(event -> goTo((Node) event.getSource(), "/com/clinica/views/pacientes_view.fxml"));
        btnProfissionais.setOnAction(event -> goTo((Node) event.getSource(), "/com/clinica/views/profissionais_view.fxml"));
        btnConsultas.setOnAction(event -> goTo((Node) event.getSource(), "/com/clinica/views/consultas_view.fxml"));
        btnLancamento.setOnAction(event -> goTo((Node) event.getSource(), "/com/clinica/views/lancamento_view.fxml"));

        btnAdicionarTelefone.setOnAction(e -> addTelefones());
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
        configurarClinicas();

        carregarClinicas();
        carregarLista();
        atualizarEstado();
    }

    private void configurarTabelas() {
        colId.setCellValueFactory(v -> new SimpleObjectProperty<>(v.getValue().getId()));
        colNome.setCellValueFactory(v -> new SimpleStringProperty(nz(v.getValue().getNome())));
        colEspecialidade.setCellValueFactory(v -> new SimpleStringProperty(nz(v.getValue().getEspecialidade())));
        colRegistro.setCellValueFactory(v -> new SimpleStringProperty(nz(v.getValue().getRegistro())));
        tblProfissionais.setItems(profissionaisFiltrados);

        colNumero.setCellValueFactory(v -> new SimpleStringProperty(formatarTelefoneSeguro(v.getValue())));
        tblTelefones.setItems(telefones);
    }

    private void configurarBusca() {
        txtBusca.textProperty().addListener((obs, o, n) -> filtrar(n));
    }

    private void configurarSelecao() {
        tblProfissionais.getSelectionModel().selectedItemProperty().addListener((obs, o, sel) -> {
            if (sel == null) return;
            carregarNoFormulario(sel.getId());
        });
    }

    private void configurarClinicas() {
        cbClinica.setItems(clinicas);

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
    }

    private void carregarClinicas() {
        clinicas.clear();
        clinicas.addAll(new ClinicaDAO().listar());
    }

    private void carregarLista() {
        try {
            ProfissionalDAO dao = new ProfissionalDAO();
            List<Profissional> lista = dao.listar();
            profissionais.setAll(lista);
            profissionaisFiltrados.setAll(lista);
        } catch (Exception e) {
            e.printStackTrace();
            alerta(Alert.AlertType.ERROR, "Erro ao carregar profissionais.");
        }
    }

    private void filtrar(String termo) {
        String t = termo == null ? "" : termo.trim();
        if (t.isBlank()) {
            profissionaisFiltrados.setAll(profissionais);
            return;
        }
        String tn = t.toLowerCase();
        profissionaisFiltrados.setAll(
                profissionais.stream()
                        .filter(p -> String.valueOf(p.getId() == null ? "" : p.getId()).contains(t)
                                || nz(p.getNome()).toLowerCase().contains(tn)
                                || nz(p.getRegistro()).toLowerCase().contains(tn)
                                || nz(p.getEspecialidade()).toLowerCase().contains(tn))
                        .toList()
        );
    }

    private void carregarNoFormulario(Integer id) {
        if (id == null) return;
        try {
            ProfissionalDAO dao = new ProfissionalDAO();
            Profissional p = dao.buscar(id);
            if (p == null) return;

            idSelecionado = p.getId();

            txtNome.setText(nz(p.getNome()));
            txtEspecialidade.setText(nz(p.getEspecialidade()));
            txtRegistro.setText(nz(p.getRegistro()));
            cbClinica.getSelectionModel().select(p.getClinica());

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
            alerta(Alert.AlertType.ERROR, "Erro ao carregar profissional.");
        }
    }

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

    private void salvar() {
        try {
            Clinica clinica = cbClinica.getValue();
            String nome = txtNome.getText() == null ? "" : txtNome.getText().trim();
            String esp = txtEspecialidade.getText() == null ? "" : txtEspecialidade.getText().trim();
            String reg = txtRegistro.getText() == null ? "" : txtRegistro.getText().trim();

            if (clinica == null) {
                alerta(Alert.AlertType.WARNING, "Selecione a clínica.");
                return;
            }
            if (nome.isBlank()) {
                alerta(Alert.AlertType.WARNING, "Informe o nome.");
                return;
            }
            if (reg.isBlank()) {
                alerta(Alert.AlertType.WARNING, "Informe o registro.");
                return;
            }
            if (telefones.isEmpty()) {
                alerta(Alert.AlertType.WARNING, "Adicione pelo menos um telefone.");
                return;
            }

            Profissional p = new Profissional();
            p.setId(idSelecionado);
            p.setNome(nome);
            p.setEspecialidade(esp);
            p.setRegistro(reg);
            p.setClinica(clinica);
            p.setTelefones(new ArrayList<>(telefones));

            ProfissionalDAO dao = new ProfissionalDAO();
            boolean ok = idSelecionado == null ? dao.salvar(p) : dao.editar(p);

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
        if (idSelecionado == null) {
            alerta(Alert.AlertType.WARNING, "Selecione um profissional na tabela.");
            return;
        }

        Optional<ButtonType> r = new Alert(Alert.AlertType.CONFIRMATION, "Excluir o profissional selecionado?", ButtonType.YES, ButtonType.NO)
                .showAndWait();

        if (r.isEmpty() || r.get() != ButtonType.YES) return;

        try {
            ProfissionalDAO dao = new ProfissionalDAO();
            boolean ok = dao.remover(idSelecionado);

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

    private void limparFormulario() {
        idSelecionado = null;
        txtNome.clear();
        txtEspecialidade.clear();
        txtRegistro.clear();
        txtTelefone.clear();
        cbClinica.getSelectionModel().clearSelection();
        telefones.clear();
        tblProfissionais.getSelectionModel().clearSelection();
        atualizarEstado();
    }

    private void atualizarEstado() {
        btnExcluir.setDisable(idSelecionado == null);
    }

    private String normalizarTelefone(String tel) {
        if (tel == null) return "";
        return tel.replaceAll("\\D", "");
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
