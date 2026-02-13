package com.example.demo;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/infos")
public class InfoController {

    // Vamos guardar os dados na memória por enquanto (é o jeito mais simples)
    private static InfoDTO dadosAtuais = new InfoDTO();

    // Quando a TV pedir os dados
    @GetMapping
    public InfoDTO getInfos() {
        return dadosAtuais;
    }

    // Quando o Admin salvar os dados
    @PostMapping
    public void atualizarInfos(@RequestBody InfoDTO novosDados) {
        dadosAtuais = novosDados;
    }

    // A classe que define o formato dos dados
    public static class InfoDTO {
        // Cursos
        public java.util.List<Curso> cursos = new java.util.ArrayList<>(
                java.util.List.of(
                        new Curso("Informática Básica", "Início: 15/03 • Vagas Abertas"),
                        new Curso("Atendimento ao Cliente", "Lista de Espera • Manhã"),
                        new Curso("Operador de Empilhadeira", "Parceria SENAI • Noturno")
                )
        );

        // Processos (Empresa)
        public java.util.List<ProcessoItem> processos = new java.util.ArrayList<>(
                java.util.List.of(
                        new ProcessoItem(
                                "SUPERMERCADOS CONDOR",
                                "08/06/26 - das 08h00 às 15h00",
                                java.util.List.of("Auxiliar de limpeza", "Operador de caixa", "Repositor de mercadorias")
                        )
                )
        );

        // Legado (compatibilidade)
        public String empresaNome = "SUPERMERCADOS CONDOR";
        public String empresaData = "08/06/26 - das 08h00 às 15h00";
        public java.util.List<String> processoVagas = new java.util.ArrayList<>(
                java.util.List.of("Auxiliar de limpeza", "Operador de caixa", "Repositor de mercadorias")
        );

        // Cronograma (Eventos)
        public java.util.List<CronogramaItem> cronograma = new java.util.ArrayList<>(
                java.util.List.of(
                        new CronogramaItem("05/08/2026", "MUTIRÃO GERAL DE VAGAS"),
                        new CronogramaItem("06/09/2026", "MUTIRÃO DA INCLUSÃO (PCD)"),
                        new CronogramaItem("04/11/2026", "PROCESSO SELETIVO BAIRROS")
                )
        );
    }

    public static class Curso {
        public String nome;
        public String detalhe;

        public Curso() {}

        public Curso(String nome, String detalhe) {
            this.nome = nome;
            this.detalhe = detalhe;
        }
    }

    public static class ProcessoItem {
        public String empresa;
        public String data;
        public java.util.List<String> texto;

        public ProcessoItem() {}

        public ProcessoItem(String empresa, String data, java.util.List<String> texto) {
            this.empresa = empresa;
            this.data = data;
            this.texto = texto;
        }
    }

    public static class CronogramaItem {
        public String data;
        public String evento;

        public CronogramaItem() {}

        public CronogramaItem(String data, String evento) {
            this.data = data;
            this.evento = evento;
        }
    }
}
