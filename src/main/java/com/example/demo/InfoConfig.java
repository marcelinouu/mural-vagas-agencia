package com.example.demo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "info_config")
public class InfoConfig {

    @Id
    private Long id;

    @Lob
    @Column(name = "cursos_json")
    private String cursosJson;

    @Lob
    @Column(name = "processos_json")
    private String processosJson;

    @Lob
    @Column(name = "cronograma_json")
    private String cronogramaJson;

    @Column(name = "empresa_nome")
    private String empresaNome;

    @Column(name = "empresa_data")
    private String empresaData;

    @Lob
    @Column(name = "processo_vagas_json")
    private String processoVagasJson;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCursosJson() {
        return cursosJson;
    }

    public void setCursosJson(String cursosJson) {
        this.cursosJson = cursosJson;
    }

    public String getProcessosJson() {
        return processosJson;
    }

    public void setProcessosJson(String processosJson) {
        this.processosJson = processosJson;
    }

    public String getCronogramaJson() {
        return cronogramaJson;
    }

    public void setCronogramaJson(String cronogramaJson) {
        this.cronogramaJson = cronogramaJson;
    }

    public String getEmpresaNome() {
        return empresaNome;
    }

    public void setEmpresaNome(String empresaNome) {
        this.empresaNome = empresaNome;
    }

    public String getEmpresaData() {
        return empresaData;
    }

    public void setEmpresaData(String empresaData) {
        this.empresaData = empresaData;
    }

    public String getProcessoVagasJson() {
        return processoVagasJson;
    }

    public void setProcessoVagasJson(String processoVagasJson) {
        this.processoVagasJson = processoVagasJson;
    }
}
