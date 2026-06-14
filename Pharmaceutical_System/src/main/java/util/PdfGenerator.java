package util;

import java.io.OutputStream;

// IMPORTS ESPECÍFICOS DO OPENPDF (NÃO use java.awt.*)
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import model.Sale;

public class PdfGenerator {

	public static void generateReceipt(Sale sale, OutputStream os) throws Exception {
		Document document = new Document(PageSize.A6);
		try {
			PdfWriter.getInstance(document, os);
			document.open();

			com.lowagie.text.Font font = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 12,
					com.lowagie.text.Font.BOLD);

			document.add(new Paragraph("RECIBO DE VENDA", font));
			document.add(new Paragraph("ID: " + sale.getId()));
			document.add(new Paragraph("Valor: R$ " + sale.getTotalAmount()));

		} finally {
			if (document.isOpen()) {
				document.close();
			}
		}
	}
}