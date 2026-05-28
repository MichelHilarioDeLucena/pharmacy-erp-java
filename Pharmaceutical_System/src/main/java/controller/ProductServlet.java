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
import java.util.List;
import config.AppPaths;
import config.JPAUtil;
import model.Product;
import model.Supplier;
import service.ProductService;
import service.SupplierService;


@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EntityManager em = JPAUtil.getEntityManager();
        ProductService service = new ProductService(em);

        try {
            String action = request.getParameter("action");

            if ("editar".equals(action)) {
                Long id = Long.parseLong(request.getParameter("id"));
                Product product = service.buscarPorId(id);
                List<Supplier> fornecedores = new SupplierService(em).listarAtivos();
            	request.setAttribute("fornecedores", fornecedores);
                request.setAttribute("produto", product);
                request.getRequestDispatcher(AppPaths.PRODUTO_FORM)
                       .forward(request, response);

            } else if ("desativar".equals(action)) {
            	Long id = Long.parseLong(request.getParameter("id"));

                em.getTransaction().begin();
                service.desativar(id);
                em.getTransaction().commit();
                response.sendRedirect(request.getContextPath() + "/ProductServlet");

            } else if ("estoqueBaixo".equals(action)) {
                request.setAttribute("produtos", service.listarEstoqueBaixo());
                request.setAttribute("alertaEstoque", true);
                request.getRequestDispatcher(AppPaths.PRODUTO_LISTA)
                       .forward(request, response);

            } else if ("novo".equals(action)) {
            	List<Supplier> fornecedores = new SupplierService(em).listarAtivos();
            	request.setAttribute("fornecedores", fornecedores);
                request.getRequestDispatcher(AppPaths.PRODUTO_FORM)
                       .forward(request, response);

            } else {
                request.setAttribute("produtos", service.listarTodos());
                request.getRequestDispatcher(AppPaths.PRODUTO_LISTA)

                       .forward(request, response);
            }

        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            e.printStackTrace();
            request.setAttribute("mensagem", "Erro ao carregar produtos.");
            request.getRequestDispatcher(AppPaths.PRODUTO_LISTA)

                   .forward(request, response);
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EntityManager em = JPAUtil.getEntityManager();
        ProductService service = new ProductService(em);

        try {
            String action = request.getParameter("action");

            if ("entrada".equals(action)) {
            	Long id = Long.parseLong(request.getParameter("id"));
                Integer quantidade = Integer.parseInt(request.getParameter("quantidade"));
                em.getTransaction().begin();
                service.entradaEstoque(id, quantidade);
                em.getTransaction().commit();
                response.sendRedirect(request.getContextPath() + "/ProductServlet");
                return;
            }
            Product product = new Product();

            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isEmpty()) {
                product = service.buscarPorId(Long.parseLong(idParam));

            }

            product.setName(request.getParameter("nome"));
            product.setBarcode(request.getParameter("codigoBarras"));
            product.setDescription(request.getParameter("descricao"));
            product.setCostPrice(new BigDecimal(request.getParameter("precoCusto")));
            product.setSalePrice(new BigDecimal(request.getParameter("precoVenda")));
            product.setCurrentStock(Integer.parseInt(request.getParameter("qtdAtual")));
            product.setMinStock(Integer.parseInt(request.getParameter("qtdMinima")));
            String supplierIdParam = request.getParameter("supplierId");
            if (supplierIdParam != null && !supplierIdParam.isEmpty()) {
                product.setSupplierId(Integer.parseInt(supplierIdParam));
            }
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
            request.getRequestDispatcher(AppPaths.PRODUTO_FORM)
                   .forward(request, response);
        } finally {
            if (em.isOpen()) em.close();
        }
    }
}