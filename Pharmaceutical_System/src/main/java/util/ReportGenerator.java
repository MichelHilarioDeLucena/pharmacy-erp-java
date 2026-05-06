package util;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import model.Product;
import model.Purchase;
import model.PurchaseItem;
import model.Supplier;

public class ReportGenerator {

    // =============================================
    // ESTOQUE BAIXO — PDF
    // =============================================
    public static void estoqueBaixoPdf(List<Product> produtos, OutputStream os) throws Exception {
        Document doc = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(doc, os);
            doc.open();

            Font titulo = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font cabecalho = new Font(Font.HELVETICA, 10, Font.BOLD);
            Font corpo = new Font(Font.HELVETICA, 9, Font.NORMAL);

            doc.add(new Paragraph("Relatório de Estoque Baixo", titulo));
            doc.add(new Paragraph("Gerado em: " + java.time.LocalDate.now()));
            doc.add(new Paragraph(" "));

            PdfPTable tabela = new PdfPTable(6);
            tabela.setWidthPercentage(100);
            tabela.setWidths(new float[]{1f, 3f, 2f, 2f, 1.5f, 1.5f});

            String[] colunas = {"ID", "Nome", "Cód. Barras", "Preço Venda", "Qtd. Atual", "Qtd. Mínima"};
            for (String col : colunas) {
                PdfPCell cell = new PdfPCell(new Phrase(col, cabecalho));
                cell.setBackgroundColor(new java.awt.Color(200, 200, 200));
                cell.setPadding(5);
                tabela.addCell(cell);
            }

            for (Product p : produtos) {
                tabela.addCell(new Phrase(String.valueOf(p.getId()), corpo));
                tabela.addCell(new Phrase(p.getName(), corpo));
                tabela.addCell(new Phrase(p.getBarcode() != null ? p.getBarcode() : "-", corpo));
                tabela.addCell(new Phrase("R$ " + p.getSalePrice(), corpo));
                tabela.addCell(new Phrase(String.valueOf(p.getCurrentStock()), corpo));
                tabela.addCell(new Phrase(String.valueOf(p.getMinStock()), corpo));
            }

            doc.add(tabela);
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Total de produtos: " + produtos.size()));

        } finally {
            if (doc.isOpen()) doc.close();
        }
    }

    // =============================================
    // ESTOQUE BAIXO — CSV
    // =============================================
    public static void estoqueBaixoCsv(List<Product> produtos, PrintWriter writer) {
        writer.println("ID,Nome,Codigo Barras,Preco Venda,Qtd Atual,Qtd Minima,Validade");
        for (Product p : produtos) {
            writer.printf("%s,\"%s\",%s,%s,%d,%d,%s%n",
                p.getId(),
                p.getName().replace("\"", "\"\""),
                p.getBarcode() != null ? p.getBarcode() : "",
                p.getSalePrice(),
                p.getCurrentStock(),
                p.getMinStock(),
                p.getExpirationDate() != null ? p.getExpirationDate().toString() : ""
            );
        }
    }

    // =============================================
    // COMPRAS — PDF
    // =============================================
    public static void comprasPdf(List<Purchase> compras, OutputStream os) throws Exception {
        Document doc = new Document(PageSize.A4.rotate());
        try {
            PdfWriter.getInstance(doc, os);
            doc.open();

            Font titulo = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font cabecalho = new Font(Font.HELVETICA, 10, Font.BOLD);
            Font corpo = new Font(Font.HELVETICA, 9, Font.NORMAL);

            doc.add(new Paragraph("Relatório de Compras", titulo));
            doc.add(new Paragraph("Gerado em: " + java.time.LocalDate.now()));
            doc.add(new Paragraph(" "));

            PdfPTable tabela = new PdfPTable(7);
            tabela.setWidthPercentage(100);
            tabela.setWidths(new float[]{0.8f, 3f, 2f, 2f, 2f, 1.5f, 1.5f});

            String[] colunas = {"ID", "Fornecedor", "Operador", "Data", "Status", "Itens", "Total"};
            for (String col : colunas) {
                PdfPCell cell = new PdfPCell(new Phrase(col, cabecalho));
                cell.setBackgroundColor(new java.awt.Color(200, 200, 200));
                cell.setPadding(5);
                tabela.addCell(cell);
            }

            for (Purchase c : compras) {
                tabela.addCell(new Phrase(String.valueOf(c.getId()), corpo));
                tabela.addCell(new Phrase(c.getSupplier().getCompanyName(), corpo));
                tabela.addCell(new Phrase(c.getOperator().getNome(), corpo));
                tabela.addCell(new Phrase(c.getPurchaseDate().toLocalDate().toString(), corpo));
                tabela.addCell(new Phrase(c.getOrderStatus(), corpo));
                tabela.addCell(new Phrase(String.valueOf(c.getItems().size()), corpo));
                tabela.addCell(new Phrase("R$ " + c.getTotalAmount(), corpo));
            }

            doc.add(tabela);
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Total de compras: " + compras.size()));

        } finally {
            if (doc.isOpen()) doc.close();
        }
    }

    // =============================================
    // COMPRAS — CSV
    // =============================================
    public static void comprasCsv(List<Purchase> compras, PrintWriter writer) {
        writer.println("ID,Fornecedor,CNPJ,Operador,Data,Status,Qtd Itens,Total");
        for (Purchase c : compras) {
            writer.printf("%d,\"%s\",%s,\"%s\",%s,%s,%d,%s%n",
                c.getId(),
                c.getSupplier().getCompanyName().replace("\"", "\"\""),
                c.getSupplier().getCnpj(),
                c.getOperator().getNome().replace("\"", "\"\""),
                c.getPurchaseDate().toLocalDate().toString(),
                c.getOrderStatus(),
                c.getItems().size(),
                c.getTotalAmount()
            );
        }
    }

    // =============================================
    // FORNECEDORES — PDF
    // =============================================
    public static void fornecedoresPdf(List<Supplier> fornecedores, OutputStream os) throws Exception {
        Document doc = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(doc, os);
            doc.open();

            Font titulo = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font cabecalho = new Font(Font.HELVETICA, 10, Font.BOLD);
            Font corpo = new Font(Font.HELVETICA, 9, Font.NORMAL);

            doc.add(new Paragraph("Relatório de Fornecedores", titulo));
            doc.add(new Paragraph("Gerado em: " + java.time.LocalDate.now()));
            doc.add(new Paragraph(" "));

            PdfPTable tabela = new PdfPTable(5);
            tabela.setWidthPercentage(100);
            tabela.setWidths(new float[]{3f, 2.5f, 2f, 2f, 1.5f});

            String[] colunas = {"Razão Social", "CNPJ", "Telefone", "E-mail", "Status"};
            for (String col : colunas) {
                PdfPCell cell = new PdfPCell(new Phrase(col, cabecalho));
                cell.setBackgroundColor(new java.awt.Color(200, 200, 200));
                cell.setPadding(5);
                tabela.addCell(cell);
            }

            for (Supplier s : fornecedores) {
                tabela.addCell(new Phrase(s.getCompanyName(), corpo));
                tabela.addCell(new Phrase(s.getCnpj(), corpo));
                tabela.addCell(new Phrase(s.getPhone() != null ? s.getPhone() : "-", corpo));
                tabela.addCell(new Phrase(s.getEmail() != null ? s.getEmail() : "-", corpo));
                tabela.addCell(new Phrase(s.getActive() ? "Ativo" : "Inativo", corpo));
            }

            doc.add(tabela);
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Total de fornecedores: " + fornecedores.size()));

        } finally {
            if (doc.isOpen()) doc.close();
        }
    }

    // =============================================
    // FORNECEDORES — CSV
    // =============================================
    public static void fornecedoresCsv(List<Supplier> fornecedores, PrintWriter writer) {
        writer.println("Razao Social,CNPJ,Categoria,Telefone,Email,Status");
        for (Supplier s : fornecedores) {
            writer.printf("\"%s\",%s,%s,%s,%s,%s%n",
                s.getCompanyName().replace("\"", "\"\""),
                s.getCnpj(),
                s.getSupplyCategory() != null ? s.getSupplyCategory() : "",
                s.getPhone() != null ? s.getPhone() : "",
                s.getEmail() != null ? s.getEmail() : "",
                s.getActive() ? "Ativo" : "Inativo"
            );
        }
    }
}