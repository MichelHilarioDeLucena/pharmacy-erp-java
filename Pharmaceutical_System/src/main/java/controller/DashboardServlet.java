package controller;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import config.AppPaths;
import config.JPAUtil;

@WebServlet("/DashboardServlet")
public class DashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		EntityManager em = JPAUtil.getEntityManager();

		try {
			List<Object[]> revenueByPayment = em.createNamedQuery("Sale.revenueByPayment", Object[].class)
					.getResultList();

			List<Object[]> topProducts = em.createNamedQuery("SaleItem.topSelling", Object[].class).setMaxResults(5)
					.getResultList();

			// Passa os dados para a página JSP
			request.setAttribute("revenueByPayment", revenueByPayment);
			request.setAttribute("topProducts", topProducts);

			// Dentro do DashboardServlet.java
			request.getRequestDispatcher(AppPaths.DASHBOARD_JSP).forward(request, response);
		} catch (Exception e) {
			throw new ServletException("Erro ao carregar dados do dashboard", e);
		} finally {
			em.close();
		}
	}
}