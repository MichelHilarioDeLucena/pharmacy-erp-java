package controller;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import config.JPAUtil;
import model.Sale;
import util.PdfGenerator;

@WebServlet("/ReceiptServlet")
public class ReceiptServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String saleIdStr = request.getParameter("saleId");

		if (saleIdStr == null || saleIdStr.isEmpty()) {
			response.sendRedirect("CheckoutServlet");
			return;
		}

		EntityManager em = JPAUtil.getEntityManager();

		try {
			Integer id = Integer.parseInt(saleIdStr);
			Sale sale = em.find(Sale.class, id);

			if (sale != null) {
				response.setContentType("application/pdf");

				response.setHeader("Content-Disposition", "attachment; filename=recibo_venda_" + id + ".pdf");

				PdfGenerator.generateReceipt(sale, response.getOutputStream());
			} else {
				response.getWriter().print("Erro: Venda não encontrada no banco.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException("Erro ao gerar PDF: " + e.getMessage());
		} finally {
			em.close();
		}
	}
}