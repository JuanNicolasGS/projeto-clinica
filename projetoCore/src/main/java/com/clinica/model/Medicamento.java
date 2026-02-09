package com.clinica.model;

public class Medicamento {
    private Integer id;
    private String nome;
    
    public Medicamento(Integer id, String nome) {
        setId(id);
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        if (id == null || id > 0) {
            this.id = id;
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
