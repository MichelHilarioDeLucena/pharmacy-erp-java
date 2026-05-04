package controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import config.AppPaths;
import model.Product;

@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("erp"); // Confirme o nome do seu
																					// persistence-unit
		EntityManager em = emf.createEntityManager();

		try {
			// Busca todos os produtos ativos no banco
			List<Product> produtos = em.createQuery("SELECT p FROM Product p WHERE p.active = true", Product.class)
					.getResultList();

			// Envia a lista para o JSP com o nome "produtosCadastrados"
			request.setAttribute("produtosCadastrados", produtos);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
			emf.close();
		}

		request.getRequestDispatcher(AppPaths.ENTRADA_ESTOQUE).forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String acao = request.getParameter("acao");

		if ("salvar".equals(acao)) {
			// 1. Inicia a conexão com o Banco (Ajuste o nome da sua Unidade de
			// Persistência)
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("erp-farmacia");
			EntityManager em = emf.createEntityManager();

			try {
				// 2. Pega os dados do formulário
				String barcode = request.getParameter("barcode");
				String name = request.getParameter("name");
				Integer quantidadeRecebida = Integer.parseInt(request.getParameter("currentStock"));

				em.getTransaction().begin();

				// 3. TENTA BUSCAR O PRODUTO PELO CÓDIGO DE BARRAS
				List<Product> resultados = em
						.createQuery("SELECT p FROM Product p WHERE p.barcode = :barcode", Product.class)
						.setParameter("barcode", barcode).getResultList();

				Product p;
				String msgFeedback;

				if (!resultados.isEmpty()) {
					// SE JÁ EXISTE: Apenas soma a quantidade!
					p = resultados.get(0);
					p.setCurrentStock(p.getCurrentStock() + quantidadeRecebida);

					// Atualiza preços caso tenham mudado na nova remessa
					p.setCostPrice(new BigDecimal(request.getParameter("costPrice")));
					p.setSalePrice(new BigDecimal(request.getParameter("salePrice")));
					p.setExpiryDate(LocalDate.parse(request.getParameter("expiryDate")));

					em.merge(p); // Atualiza no banco
					msgFeedback = "Estoque do produto '" + p.getName() + "' atualizado! Nova quantidade: "
							+ p.getCurrentStock();
				} else {
					// SE NÃO EXISTE: Cria um novo cadastro
					p = new Product();
					p.setBarcode(barcode);
					p.setName(name);
					p.setDescription(request.getParameter("description"));
					p.setCostPrice(new BigDecimal(request.getParameter("costPrice")));
					p.setSalePrice(new BigDecimal(request.getParameter("salePrice")));
					p.setCurrentStock(quantidadeRecebida);
					p.setMinStock(Integer.parseInt(request.getParameter("minStock")));
					p.setExpiryDate(LocalDate.parse(request.getParameter("expiryDate")));
					p.setActive(true);

					em.persist(p); // Salva novo no banco
					msgFeedback = "Novo produto '" + p.getName() + "' cadastrado com sucesso!";
				}

				em.getTransaction().commit();
				request.setAttribute("mensagem", msgFeedback);

			} catch (Exception e) {
				if (em.getTransaction().isActive())
					em.getTransaction().rollback();
				e.printStackTrace();
				request.setAttribute("erro", "Erro ao processar: " + e.getMessage());
			} finally {
				em.close();
				emf.close();
			}

			request.getRequestDispatcher(AppPaths.ENTRADA_ESTOQUE).forward(request, response);
		}
	}
}