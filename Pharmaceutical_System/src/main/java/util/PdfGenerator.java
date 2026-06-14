package util;

import java.io.OutputStream;
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

        // Fonte para título
        Font titleFont = new Font(Font.HELVETICA, 14, Font.BOLD);
        Font normalFont = new Font(Font.HELVETICA, 10, Font.NORMAL);
        Font boldFont = new Font(Font.HELVETICA, 10, Font.BOLD);

        // Cabeçalho
        document.add(new Paragraph("RECIBO DE VENDA", titleFont));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("ID da Venda: " + sale.getId(), normalFont));
        document.add(new Paragraph("Data: " + sale.getSaleDate().toLocalDate().toString(), normalFont));
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
                table.addCell(new Phrase(item.getProductName() != null ? item.getProductName() : "Produto ID " + item.getProductId(), normalFont));
                table.addCell(new Phrase(String.valueOf(item.getQuantity()), normalFont));
                table.addCell(new Phrase("R$ " + item.getUnitPrice(), normalFont));
                table.addCell(new Phrase("R$ " + item.getSubtotal(), normalFont));
            }
        }
        document.add(table);
        document.add(new Paragraph(" "));

        // Total e pagamento
        document.add(new Paragraph("TOTAL: R$ " + sale.getTotalAmount(), boldFont));
        document.add(new Paragraph("Pagamento: " + sale.getPaymentMethod(), normalFont));
        document.add(new Paragraph("Status: " + sale.getPaymentStatus(), normalFont));

        document.close();
    }
}