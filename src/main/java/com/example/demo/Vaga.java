package com.example.demo;

import jakarta.persistence.*;

@Entity
public class Vaga {

    public static final String MODULO_VAGAS = "VAGAS";
    public static final String MODULO_MUTIROES = "MUTIROES";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String salario;
    private String escolaridade;
    private String descricao;
    private String categoria;
    private String codigo;
    private String empresa;
    private String modulo;
    private String tela;
    private String local;
    private String beneficios;
    @Column(length = 4000)
    private String atribuicoes;
    private String validade;

    // --- NOVO CAMPO ---
    private String experiencia; // Vai guardar "Sim" ou "Não"

    // --- Getters e Setters do Novo Campo ---
    public String getExperiencia() { return experiencia; }
    public void setExperiencia(String experiencia) { this.experiencia = experiencia; }

    // ... Mantenha os outros getters e setters antigos (Id, Titulo, etc...)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getSalario() { return salario; }
    public void setSalario(String salario) { this.salario = salario; }
    public String getEscolaridade() { return escolaridade; }
    public void setEscolaridade(String escolaridade) { this.escolaridade = escolaridade; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }
    public String getModulo() { return modulo; }
    public void setModulo(String modulo) { this.modulo = modulo; }
    public String getTela() { return tela; }
    public void setTela(String tela) { this.tela = tela; }
    public String getLocal() { return local; }
    public void setLocal(String local) { this.local = local; }
    public String getBeneficios() { return beneficios; }
    public void setBeneficios(String beneficios) { this.beneficios = beneficios; }
    public String getAtribuicoes() { return atribuicoes; }
    public void setAtribuicoes(String atribuicoes) { this.atribuicoes = atribuicoes; }
    public String getValidade() { return validade; }
    public void setValidade(String validade) { this.validade = validade; }
}
