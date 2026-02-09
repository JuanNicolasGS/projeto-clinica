package com.clinica.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Receita {
    private Integer id;
    private Date data;
    private Date validade;
    private String observacoes;
    private Consulta consulta;
    private List<Medicamento> medicamentos = new ArrayList<>();
    
    public Receita() {
    }

    public Receita(Integer id, Date data, Date validade, String observacoes, Consulta consulta) {
        setId(id);
        this.data = data;
        this.validade = validade;
        this.observacoes = observacoes;
        this.consulta = consulta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        if (id == null || id > 0) {
            this.id = id;
        }
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Date getValidade() {
        return validade;
    }

    public void setValidade(Date validade) {
        this.validade = validade;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }
    
    public List<Medicamento> getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(List<Medicamento> medicamentos) {
        this.medicamentos = medicamentos;
    }

    public void adicionarMedicamento(Medicamento medicamento){
        if(medicamento != null){
            medicamentos.add(medicamento);
        }
    }
}
