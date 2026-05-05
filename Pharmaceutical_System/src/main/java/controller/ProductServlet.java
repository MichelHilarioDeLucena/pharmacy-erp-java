package controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import config.JPAUtil;
import model.Product;
import service.ProductService;

@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // Lista todos os produtos
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EntityManager em = JPAUtil.getEntityManager();
        ProductService service = new ProductService(em);

        try {
            String action = request.getParameter("action");

            if ("editar".equals(action)) {
                Integer id = Integer.parseInt(request.getParameter("id"));
                Product product = service.buscarPorId(id);
                request.setAttribute("produto", product);
                request.getRequestDispatcher("/views/inventory/formProduto.jsp")
                       .forward(request, response);

            } else if ("desativar".equals(action)) {
                Integer id = Integer.parseInt(request.getParameter("id"));
                em.getTransaction().begin();
                service.desativar(id);
                em.getTransaction().commit();
                response.sendRedirect(request.getContextPath() + "/ProductServlet");

            } else if ("estoqueBaixo".equals(action)) {
                request.setAttribute("produtos", service.listarEstoqueBaixo());
                request.setAttribute("alertaEstoque", true);
                request.getRequestDispatcher("/views/inventory/listaProdutos.jsp")
                       .forward(request, response);
            } else if ("novo".equals(action)) {
                request.getRequestDispatcher("/views/inventory/formProduto.jsp")
                       .forward(request, response);            
            } else {
                request.setAttribute("produtos", service.listarTodos());
                request.getRequestDispatcher("/views/inventory/listaProdutos.jsp")
                       .forward(request, response);
            }

        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            e.printStackTrace();
            request.setAttribute("mensagem", "Erro ao carregar produtos.");
            request.getRequestDispatcher("/views/inventory/listaProdutos.jsp")
                   .forward(request, response);
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    // Cadastra ou atualiza produto
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EntityManager em = JPAUtil.getEntityManager();
        ProductService service = new ProductService(em);

        try {
            String action = request.getParameter("action");

            if ("entrada".equals(action)) {
                // Entrada de inventory
                Integer id = Integer.parseInt(request.getParameter("id"));
                Integer quantidade = Integer.parseInt(request.getParameter("quantidade"));
                em.getTransaction().begin();
                service.entradaEstoque(id, quantidade);
                em.getTransaction().commit();
                response.sendRedirect(request.getContextPath() + "/ProductServlet");
                return;
            }

            // Cadastro ou edição
            Product product = new Product();

            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isEmpty()) {
                product = service.buscarPorId(Integer.parseInt(idParam));
            }

            product.setName(request.getParameter("nome"));
            product.setBarcode(request.getParameter("codigoBarras"));
            product.setDescription(request.getParameter("descricao"));
            product.setCostPrice(new BigDecimal(request.getParameter("precoCusto")));
            product.setSalePrice(new BigDecimal(request.getParameter("precoVenda")));
            product.setCurrentStock(Integer.parseInt(request.getParameter("qtdAtual")));
            product.setMinStock(Integer.parseInt(request.getParameter("qtdMinima")));

            String dataValidade = request.getParameter("dataValidade");
            if (dataValidade != null && !dataValidade.isEmpty()) {
                product.setExpirationDate(LocalDate.parse(dataValidade));
            }

            String categoriaId = request.getParameter("categoriaId");
            if (categoriaId != null && !categoriaId.isEmpty()) {
                product.setCategoryId(Integer.parseInt(categoriaId));
            }

            em.getTransaction().begin();
            if (product.getId() == null) {
                service.cadastrar(product);
            } else {
                service.atualizar(product);
            }
            em.getTransaction().commit();

            response.sendRedirect(request.getContextPath() + "/ProductServlet");

        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            e.printStackTrace();
            request.setAttribute("mensagem", "Erro: " + e.getMessage());
            request.getRequestDispatcher("/views/inventory/formProduto.jsp")
                   .forward(request, response);
        } finally {
            if (em.isOpen()) em.close();
        }
    }
}