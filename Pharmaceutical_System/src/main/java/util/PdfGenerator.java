package util;

import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import model.Sale;
import model.SaleItem;

public class PdfGenerator {

    public static void generateReceipt(Sale sale, OutputStream os) throws Exception {
        Document document = new Document(PageSize.A6);
        PdfWriter.getInstance(document, os);
        document.open();

        Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
        Font boldFont = new Font(Font.HELVETICA, 10, Font.BOLD);
        Font normalFont = new Font(Font.HELVETICA, 10, Font.NORMAL);

        // Cabeçalho
        document.add(new Paragraph("RECIBO DE VENDA", titleFont));
        document.add(new Paragraph(" "));

        // Data e hora formatadas
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        document.add(new Paragraph("Data/Hora: " + sale.getSaleDate().format(fmt), normalFont));
        document.add(new Paragraph("ID da Venda: " + sale.getId(), normalFont));
        document.add(new Paragraph("Operador: " + sale.getOperator().getNome(), normalFont));
        if (sale.getClientId() != null) {
            document.add(new Paragraph("Cliente ID: " + sale.getClientId(), normalFont));
        }
        document.add(new Paragraph(" "));

        // Tabela de itens
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2.5f, 1f, 2f, 2f});
        table.addCell(new Phrase("Produto", boldFont));
        table.addCell(new Phrase("Qtd", boldFont));
        table.addCell(new Phrase("Preço Unit.", boldFont));
        table.addCell(new Phrase("Subtotal", boldFont));

        List<SaleItem> items = sale.getItems();
        if (items != null) {
            for (SaleItem item : items) {
                String nome = item.getProductName() != null ? item.getProductName() : "Produto ID " + item.getProductId();
                table.addCell(new Phrase(nome, normalFont));
                table.addCell(new Phrase(String.valueOf(item.getQuantity()), normalFont));
                table.addCell(new Phrase("R$ " + item.getUnitPrice(), normalFont));
                table.addCell(new Phrase("R$ " + item.getSubtotal(), normalFont));
            }
        }
        document.add(table);
        document.add(new Paragraph(" "));

        // Total, forma de pagamento e status
        document.add(new Paragraph("TOTAL: R$ " + sale.getTotalAmount(), boldFont));
        document.add(new Paragraph("Pagamento: " + sale.getPaymentMethod(), normalFont));
        document.add(new Paragraph("Status: " + sale.getPaymentStatus(), normalFont));

        // Se for pagamento em dinheiro, exibe valor pago e troco (opcional)
        if ("CASH".equals(sale.getPaymentMethod()) && sale.getAmountPaid() != null && sale.getChange() != null) {
            document.add(new Paragraph("Valor pago: R$ " + sale.getAmountPaid(), normalFont));
            document.add(new Paragraph("Troco: R$ " + sale.getChange(), normalFont));
        }

        document.close();
    }
}