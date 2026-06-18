package com.gestaonavios.gestaonavios.Model;

import com.gestaonavios.gestaonavios.Model.enums.FuncaoTripulante;

public abstract class Tripulante {
    private int id;
    private String nome;
    private String nif;
    private FuncaoTripulante funcao;
    private boolean disponivel;
    private String nacionalidade;
    private String certificacoes;

    protected Tripulante() {}

    public Tripulante(int id, String nome, String nif, FuncaoTripulante funcao,
                      boolean disponivel, String nacionalidade, String certificacoes) {
        this.id = id;
        this.nome = nome;
        this.nif = nif;
        this.funcao = funcao;
        this.disponivel = disponivel;
        this.nacionalidade = nacionalidade;
        this.certificacoes = certificacoes;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getNif() { return nif; }
    public void setNif(String nif) { this.nif = nif; }

    public FuncaoTripulante getFuncaoEnum() { return funcao; }
    public void setFuncao(FuncaoTripulante funcao) { this.funcao = funcao; }

    public boolean isDisponivel() { return disponivel; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }

    public String getNacionalidade() { return nacionalidade; }
    public void setNacionalidade(String nacionalidade) { this.nacionalidade = nacionalidade; }

    public String getCertificacoes() { return certificacoes; }
    public void setCertificacoes(String certificacoes) { this.certificacoes = certificacoes; }

    public abstract String getFuncao();

    @Override
    public String toString() { return nome + " (" + getFuncao() + ")"; }
}