package com.clinica.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Observable;
import java.util.ResourceBundle;

import com.clinica.dao.PacienteDAO;
import com.clinica.model.Paciente;

public class PacientesController extends MainController{
    @FXML private Button btnPacientes;
    @FXML private Button btnProfissionais;
    @FXML private Button btnConsultas;
    @FXML private Button btnLancamento;
    @FXML private Button btnSalvar;
    @FXML private TextField txtCpf;
    @FXML private TextField txtNome;
    @FXML private TextField txtDataNasc;
    @FXML private TextField txtTelefone;
    @FXML private TableView<String> tbTelefones;
    @FXML private TableColumn<String, String> colNumero;
    private final ObservableList<String> telefones = FXCollections.observableArrayList();



    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnPacientes.setOnAction(event -> goTo((Node) event.getSource(), "/com/clinica/views/pacientes_view.fxml"));
        btnProfissionais.setOnAction(event -> goTo((Node) event.getSource(), "/com/clinica/views/profissionais_view.fxml"));
        btnConsultas.setOnAction(event -> goTo((Node) event.getSource(), "/com/clinica/views/consultas_view.fxml"));
        btnLancamento.setOnAction(event -> goTo((Node) event.getSource(), "/com/clinica/views/lancamento_view.fxml"));
        btnSalvar.setOnAction(event -> salvar());


        colNumero.setCellValueFactory(v -> new SimpleStringProperty(v.getValue()));
        tbTelefones.setItems(telefones);

    }

    private String normalizarTelefone(String tel){
        if (tel == null) return "";
        return tel.replaceAll("\\D","");
    }


    @FXML private void addTelefones(){
        String tel = normalizarTelefone(txtTelefone.getText());
        if (tel.isBlank()) return;
        if(telefones.contains(tel)) return;
        telefones.add(tel);
        txtTelefone.clear();
    }

    private Date converterData(String texto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.parse(texto, formatter);
        return (Date) Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }


    @FXML private void salvar(){
       try {
         String cpf = txtCpf.getText().replaceAll("\\D","");
        String nome = txtNome.getText().trim();
        Date data = converterData(txtDataNasc.getText());
        if (cpf.length() != 11) return;
        if (nome.isBlank()) return;
        if(txtDataNasc.getText().isBlank() || txtDataNasc.getText() == null) return; 

        PacienteDAO pacienteDAO = new PacienteDAO();
        Paciente existente = pacienteDAO.buscar(cpf);
        if(existente != null) return;
        Paciente paciente = new Paciente();
        paciente.setCpf(cpf);
        paciente.setNome(nome);
        paciente.setDataNasc(data);
        paciente.setTelefones(new ArrayList<>(telefones));
        pacienteDAO.salvar(paciente);
       } catch (Exception e) {
        e.printStackTrace();
       }

    
    }


}
