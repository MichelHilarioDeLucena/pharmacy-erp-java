package controller;

import java.io.IOException;
import java.time.LocalDate;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import config.JPAUtil;
import service.SupplierProductService;
import service.ProductService;
import service.SupplierService;

@WebServlet("/ProdutoFornecedorServlet")
public class ProdutoFornecedorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        Integer produtoId = Integer.parseInt(request.getParameter("produtoId"));
        Integer fornecedorId = Integer.parseInt(request.getParameter("fornecedorId"));

        EntityManager em = JPAUtil.getEntityManager();
        SupplierProductService service = new SupplierProductService(em);

        try {
            em.getTransaction().begin();
            if ("vincular".equals(action)) {
                LocalDate dataCompra = LocalDate.parse(request.getParameter("dataCompra"));
                service.vincular(produtoId, fornecedorId, dataCompra);
            } else if ("desvincular".equals(action)) {
                service.desvincular(produtoId, fornecedorId);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            request.setAttribute("mensagem", "Erro: " + e.getMessage());
        } finally {
            em.close();
        }

        response.sendRedirect(request.getContextPath() + "/ProdutoFornecedorServlet?produtoId=" + produtoId);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String produtoIdParam = request.getParameter("produtoId");
        if (produtoIdParam == null || produtoIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/ProductServlet");
            return;
        }

        Integer produtoId = Integer.parseInt(produtoIdParam);
        EntityManager em = JPAUtil.getEntityManager();

        try {
            ProductService productService = new ProductService(em);
            SupplierService supplierService = new SupplierService(em);
            SupplierProductService relService = new SupplierProductService(em);

            request.setAttribute("produto", productService.buscarPorId(Long.valueOf(produtoId)));
            request.setAttribute("todosFornecedores", supplierService.listarAtivos());
            request.setAttribute("vinculos", relService.listarPorProduto(produtoId));

            request.getRequestDispatcher("/views/product/fornecedoresVinculados.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensagem", "Erro ao carregar vínculos.");
            response.sendRedirect(request.getContextPath() + "/ProductServlet");
        } finally {
            em.close();
        }
    }
}