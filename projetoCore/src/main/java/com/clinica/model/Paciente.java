package com.clinica.model;

import java.sql.Date;

public class Paciente extends Pessoa{
    private String cpf;
    private Date dataNasc;

    public Paciente(){}

    public Paciente(String nome, String telefone, String cpf, Date dataNasc){
        super(nome);
        adicionarTelefone(telefone);
        this.cpf = cpf;
        this.dataNasc = dataNasc;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Date getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(Date dataNasc) {
        this.dataNasc = dataNasc;
    }
}
