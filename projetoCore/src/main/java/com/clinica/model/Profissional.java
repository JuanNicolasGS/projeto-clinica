package com.clinica.model;

import java.util.List;

public class Profissional extends Pessoa {
    private Integer id;
    private String especialidade;
    private String registro;
    private Clinica clinica;

    public Profissional() {
        super();
    }

    public Profissional(Integer id, String nome, String telefone, String especialidade, String registro, Clinica clinica) {
        super(nome);
        adicionarTelefone(telefone);
        this.id = id;
        this.especialidade = especialidade;
        this.registro = registro;
        this.clinica = clinica;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        if (id == null || id > 0) {
            this.id = id;
        }
    }

    public Clinica getClinica() {
        return clinica;
    }

    public void setClinica(Clinica clinica) {
        this.clinica = clinica;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

    public void setTelefones(List<String> telefones) {
        for (String tel : telefones) {
            this.adicionarTelefone(tel);
        }
    }
}
