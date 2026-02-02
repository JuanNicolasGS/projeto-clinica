package com.clinica.model;

import java.util.List;
import java.util.ArrayList;

public abstract class Pessoa{
    private List<String> telefones;
    private String nome;

    public Pessoa(String nome) {
        this.telefones = new ArrayList<>();
        this.nome = nome;
    }

    public void adicionarTelefone(String telefone){
        if(telefone != null && !telefone.isEmpty()){
            telefones.add(telefone);
        }
    }

    public List<String> getTelefones() {
        return telefones;
    }
    public void setTelefones(List<String> telefones) {
        this.telefones = telefones;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    
}