package com.example.demo;

import jakarta.persistence.*;

@Entity
public class Vaga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String salario;
    private String descricao;
    private String categoria;
    private String codigo;
    private String local;
    private String beneficios;

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
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getLocal() { return local; }
    public void setLocal(String local) { this.local = local; }
    public String getBeneficios() { return beneficios; }
    public void setBeneficios(String beneficios) { this.beneficios = beneficios; }
}