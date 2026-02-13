package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController // Diz que essa classe vai responder requisições da Web
@RequestMapping("/api/vagas") // O endereço base será: localhost:8080/vagas
public class VagaController {

    @Autowired
    private VagaRepository repository; // Injeta o repositório que vc acabou de criar

    // 1. Cadastrar uma vaga (POST)
    // Quem acessar isso enviando dados, vai salvar no banco
    @PostMapping
    public Vaga cadastrar(@RequestBody Vaga vaga) {
        // Aqui você pode forçar salvar como maiúsculo se quiser
        vaga.setCategoria(vaga.getCategoria().toUpperCase());
        return repository.save(vaga);
    }

    // 2. Listar TODAS as vagas (GET)
    @GetMapping
    public List<Vaga> listarTodas() {
        return repository.findAll();
    }

    // 3. Listar por Categoria (GET) - O segredo da TV!
    // Ex: localhost:8080/vagas/filtro?tipo=CONVENCIONAL
    @GetMapping("/filtro")
    public List<Vaga> listarPorCategoria(@RequestParam String tipo) {
        return repository.findByCategoria(tipo.toUpperCase());
    }
    // 4. Deletar Vaga (DELETE)
    // Ex: O site manda DELETE para localhost:8080/vagas/5
    @DeleteMapping("/{id}")
    public void deletarVaga(@PathVariable Long id) {
        repository.deleteById(id);
    }
    // 5. Gerar Relatório PDF
    // 5. Gerar Relatório PDF (Compacto e Agrupado)
    @GetMapping("/relatorio")
    public void gerarRelatorio(jakarta.servlet.http.HttpServletResponse response) throws java.io.IOException {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=relatorio_vagas.pdf";
        response.setHeader(headerKey, headerValue);

        List<Vaga> todasVagas = repository.findAll();

        try {
            com.lowagie.text.Document document = new com.lowagie.text.Document(com.lowagie.text.PageSize.A4);
            com.lowagie.text.pdf.PdfWriter.getInstance(document, response.getOutputStream());

            document.open();

            // Fontes
            com.lowagie.text.Font fonteTitulo = com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA_BOLD, 16);
            com.lowagie.text.Font fonteCategoria = com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA_BOLD, 12, java.awt.Color.WHITE);
            com.lowagie.text.Font fonteCabecalho = com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA_BOLD, 9);
            com.lowagie.text.Font fonteDados = com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 9);

            // Título Principal
            com.lowagie.text.Paragraph titulo = new com.lowagie.text.Paragraph("RELATÓRIO DE VAGAS DISPONÍVEIS", fonteTitulo);
            titulo.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
            titulo.setSpacingAfter(10);
            document.add(titulo);

            // Definição das Categorias para o Loop
            String[] categorias = {"CONVENCIONAL", "SUPERIOR", "PCD"};
            String[] nomesExibicao = {"VAGAS CONVENCIONAIS", "ENSINO SUPERIOR", "VAGAS PCD"};
            java.awt.Color[] cores = {
                    new java.awt.Color(0, 86, 179), // Azul
                    new java.awt.Color(211, 84, 0), // Laranja
                    new java.awt.Color(22, 160, 133) // Verde
            };

            for (int i = 0; i < categorias.length; i++) {
                String catAtual = categorias[i];
                String nomeExibicao = nomesExibicao[i];
                java.awt.Color corAtual = cores[i];

                // Filtra as vagas desta categoria
                List<Vaga> vagasDaCategoria = todasVagas.stream()
                        .filter(v -> v.getCategoria().equalsIgnoreCase(catAtual))
                        .toList();

                if (vagasDaCategoria.isEmpty()) continue; // Se não tiver vaga, pula

                // --- Tabela da Categoria ---
                // Configura 4 colunas: Cód (10%), Título (35%), Salário (20%), Detalhes/Benefícios (35%)
                com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(new float[]{1f, 3.5f, 2f, 3.5f});
                table.setWidthPercentage(100);
                table.setSpacingBefore(10);
                table.setSpacingAfter(10);

                // 1. Cabeçalho da Categoria (Barra Colorida)
                com.lowagie.text.pdf.PdfPCell cellTitulo = new com.lowagie.text.pdf.PdfPCell(new com.lowagie.text.Phrase(nomeExibicao, fonteCategoria));
                cellTitulo.setColspan(4);
                cellTitulo.setBackgroundColor(corAtual);
                cellTitulo.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
                cellTitulo.setPadding(6);
                table.addCell(cellTitulo);

                // 2. Cabeçalhos das Colunas
                addCellHeader(table, "CÓD", fonteCabecalho);
                addCellHeader(table, "CARGO / FUNÇÃO", fonteCabecalho);
                addCellHeader(table, "SALÁRIO", fonteCabecalho);
                addCellHeader(table, "EXP / BENEFÍCIOS", fonteCabecalho);

                // 3. Linhas das Vagas
                for (Vaga vaga : vagasDaCategoria) {
                    // Cód
                    table.addCell(new com.lowagie.text.Phrase(vaga.getCodigo() != null ? vaga.getCodigo() : "-", fonteDados));

                    // Título
                    table.addCell(new com.lowagie.text.Phrase(vaga.getTitulo(), fonteDados));

                    // Salário
                    table.addCell(new com.lowagie.text.Phrase("R$ " + vaga.getSalario(), fonteDados));

                    // Detalhes (Juntei Experiência e Benefícios pra economizar espaço)
                    String exp = (vaga.getExperiencia() != null && vaga.getExperiencia().equals("Sim")) ? "[COM EXP] " : "";
                    String ben = vaga.getBeneficios() != null ? vaga.getBeneficios() : "";
                    table.addCell(new com.lowagie.text.Phrase(exp + ben, fonteDados));
                }

                document.add(table);
            }

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método auxiliar pra não repetir código de célula de cabeçalho
    private void addCellHeader(com.lowagie.text.pdf.PdfPTable table, String text, com.lowagie.text.Font font) {
        com.lowagie.text.pdf.PdfPCell cell = new com.lowagie.text.pdf.PdfPCell(new com.lowagie.text.Phrase(text, font));
        cell.setBackgroundColor(java.awt.Color.LIGHT_GRAY);
        cell.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
        cell.setPadding(4);
        table.addCell(cell);
    }
}