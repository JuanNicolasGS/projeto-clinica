package com.clinica.model;

public class Exame {
    private Integer id;
    private Paciente paciente;
    private Profissional profissional;
    private String nome;
    private String prioridade;
    private String observacoes;
    
    public Exame(Integer id, Paciente paciente, Profissional profissional, String nome, String prioridade, String observacoes) {
        setId(id);
        this.paciente = paciente;
        this.profissional = profissional;
        this.nome = nome;
        this.prioridade = prioridade;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
