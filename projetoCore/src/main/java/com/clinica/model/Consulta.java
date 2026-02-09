package com.clinica.model;

import java.util.Date;

public class Consulta {
    private Integer id;
    private Paciente paciente;
    private Profissional profissional;
    private Clinica clinica;
    private Date data;
    private double valor;
    private String observacoes;

    public Consulta(Integer id, Paciente paciente, Profissional profissional, Clinica clinica, Date data, double valor, String observacoes) {
        setId(id);
        this.paciente = paciente;
        this.profissional = profissional;
        this.clinica = clinica;
        this.data = data;
        this.valor = valor;
        this.observacoes = observacoes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        if (id == null || id > 0) {
            this.id = id;
        }
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Profissional getProfissional() {
        return profissional;
    }

    public void setProfissional(Profissional profissional) {
        this.profissional = profissional;
    }

    public Clinica getClinica() {
        return clinica;
    }

    public void setClinica(Clinica clinica) {
        this.clinica = clinica;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
