package controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import config.JPAUtil;
import model.Product;
import model.Sale;
import model.SaleItem;
import model.Usuario;

@WebServlet("/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// O doGet carrega a tela com os botões de produtos e a lista de clientes
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		EntityManager em = JPAUtil.getEntityManager();
		try {

			List<Product> products = em.createNamedQuery("Product.findActiveWithStock", Product.class).getResultList();

			List<Object[]> clients = em.createNativeQuery("SELECT id, nome_completo, cpf FROM clientes")
					.getResultList();
			request.setAttribute("productsList", products);
			request.setAttribute("clientsList", clients);

			request.getRequestDispatcher("/views/cashier/checkout.jsp").forward(request, response);
		} finally {
			em.close();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String action = request.getParameter("action");

		@SuppressWarnings("unchecked")
		List<SaleItem> cart = (List<SaleItem>) session.getAttribute("cart");
		if (cart == null) {
			cart = new ArrayList<>();
		}

		EntityManager em = JPAUtil.getEntityManager(); // Abre conexão

		try {
			if ("add".equals(action)) {
				Integer prodId = Integer.parseInt(request.getParameter("productId"));

				// Busca o produto real no banco para pegar o nome e preço certos
				Product product = em.find(Product.class, prodId);

				if (product != null) {
					SaleItem item = new SaleItem();
					item.setProductId(prodId);
					item.setProductName(product.getName());
					item.setQuantity(1); // Fixo 1 por clique para simular scanner
					item.setUnitPrice(product.getSalePrice());
					item.setSubtotal(product.getSalePrice().multiply(new BigDecimal(1)));

					cart.add(item);
					session.setAttribute("cart", cart);
				}

				// Redireciona de volta pro Servlet (doGet) para recarregar as listas
				response.sendRedirect(request.getContextPath() + "/CheckoutServlet");

			} else if ("finish".equals(action)) {
				if (cart.isEmpty()) {
					response.sendRedirect(request.getContextPath() + "/CheckoutServlet?error=empty");
					return;
				}

				em.getTransaction().begin();
				Sale newSale = new Sale();
				newSale.setOperator((Usuario) session.getAttribute("usuarioLogado"));
				newSale.setPaymentMethod(request.getParameter("paymentMethod"));

				String clientIdStr = request.getParameter("clientId");
				if (clientIdStr != null && !clientIdStr.isEmpty()) {
					newSale.setClientId(Integer.parseInt(clientIdStr));
				}

				for (SaleItem i : cart) {
					newSale.addItem(i);

					// EXTRA: Já dando baixa no estoque!
					Product p = em.find(Product.class, i.getProductId());
					p.setCurrentStock(p.getCurrentStock() - i.getQuantity());
					em.merge(p);
				}

				// ... dentro do commit ...
				em.persist(newSale);
				em.getTransaction().commit();

				// Pega o ID gerado pelo banco
				Integer generatedId = newSale.getId();

				session.removeAttribute("cart");
				// Redireciona passando o ID da venda
				response.sendRedirect(request.getContextPath() + "/CheckoutServlet?success=true&saleId=" + generatedId);
			}
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw new ServletException("Erro na venda", e);
		} finally {
			em.close();
		}
	}
}