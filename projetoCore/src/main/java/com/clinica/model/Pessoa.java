package com.clinica.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Pessoa{
    private List<String> telefones;
    private String nome;

    public Pessoa(){
        this.telefones = new ArrayList<>();
    }

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