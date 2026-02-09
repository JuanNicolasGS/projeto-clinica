package com.clinica.model;

public class Clinica {
    private Integer id;
    private String nome;

    public Clinica(Integer id, String nome) {
        setId(id);
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        if (id == null || id > 0) {
            this.id = id;
        } else {
            System.out.println("Set ID inválido: IDs numéricos devem ser positivos!");
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
