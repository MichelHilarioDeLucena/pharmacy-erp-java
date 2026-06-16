package controller;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import config.AppPaths;
import config.JPAUtil;
import model.Perfil;
import model.Product;
import model.Usuario;
import service.PurchaseService;
import service.SupplierService;

@WebServlet("/PurchaseServlet")
public class PurchaseServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario logado = (session != null) ? (Usuario) session.getAttribute("usuarioLogado") : null;

        if (logado == null) {
            response.sendRedirect(request.getContextPath() + AppPaths.LOGIN_PAGE);
            return;
        }

        Perfil perfil = logado.getPerfil();
        boolean temAcesso = (perfil == Perfil.ADMIN || perfil == Perfil.GERENTE);

        if (!temAcesso) {
            response.sendRedirect(request.getContextPath() + AppPaths.HOME_PAGE);
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();
        PurchaseService purchaseService = new PurchaseService(em);
        SupplierService supplierService = new SupplierService(em);

        try {
            String action = request.getParameter("action");

            if ("confirmar".equals(action)) {
                Integer id = Integer.parseInt(request.getParameter("id"));
                em.getTransaction().begin();
                purchaseService.confirmar(id);
                em.getTransaction().commit();
                response.sendRedirect(request.getContextPath() + "/PurchaseServlet");

            } else if ("cancelar".equals(action)) {
                Integer id = Integer.parseInt(request.getParameter("id"));
                em.getTransaction().begin();
                purchaseService.cancelar(id);
                em.getTransaction().commit();
                response.sendRedirect(request.getContextPath() + "/PurchaseServlet");

            } else if ("novo".equals(action)) {
                request.setAttribute("suppliers", supplierService.listarAtivos());
                request.getRequestDispatcher(AppPaths.PURCHASE_FORM)
                       .forward(request, response);
            } else if ("buscarProduto".equals(action)) {
                String termo = request.getParameter("termo");
                List<Product> resultados = new ArrayList<>();
                EntityManager emBusca = JPAUtil.getEntityManager();
                try {
                    if (termo != null && !termo.trim().isEmpty()) {
                        // Tenta buscar por ID
                        try {
                            Long id = Long.parseLong(termo.trim());
                            Product p = emBusca.find(Product.class, id);
                            if (p != null && p.getActive()) {
                                resultados.add(p);
                            }
                        } catch (NumberFormatException e) {
                            // Busca por nome (case insensitive)
                            resultados = emBusca.createQuery(
                                "SELECT p FROM Product p WHERE LOWER(p.name) LIKE :nome AND p.active = true",
                                Product.class)
                                .setParameter("nome", "%" + termo.toLowerCase() + "%")
                                .getResultList();
                        }
                    }
                    request.setAttribute("resultadosBusca", resultados);
                    request.setAttribute("termoBusca", termo);
                } finally {
                    emBusca.close();
                }
                // Recarrega a lista de fornecedores (necessário para o formulário)
                em = JPAUtil.getEntityManager();
                try {
                    request.setAttribute("suppliers", new SupplierService(em).listarAtivos());
                } finally {
                    em.close();
                }
                request.getRequestDispatcher(AppPaths.PURCHASE_FORM).forward(request, response);
                return;
            } else if ("automatico".equals(action)) {
                em.getTransaction().begin();
                int pedidos = purchaseService.gerarPedidosAutomaticos(logado);
                em.getTransaction().commit();
                request.setAttribute("mensagem", pedidos + " pedido(s) gerado(s) automaticamente.");
                request.setAttribute("purchases", purchaseService.listarTodos());
                request.getRequestDispatcher(AppPaths.PURCHASE_LISTA)
                       .forward(request, response);

            } else if ("pendentes".equals(action)) {
                request.setAttribute("purchases", purchaseService.listarPorStatus("PENDENTE"));
                request.setAttribute("filtro", "PENDENTE");
                request.getRequestDispatcher(AppPaths.PURCHASE_LISTA)
                       .forward(request, response);

            } else {
                request.setAttribute("purchases", purchaseService.listarTodos());
                request.getRequestDispatcher(AppPaths.PURCHASE_LISTA)
                       .forward(request, response);
            }

        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            e.printStackTrace();
            request.setAttribute("mensagem", "Erro: " + e.getMessage());
            request.getRequestDispatcher(AppPaths.PURCHASE_LISTA)
                   .forward(request, response);
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario logado = (session != null) ? (Usuario) session.getAttribute("usuarioLogado") : null;

        if (logado == null) {
            response.sendRedirect(request.getContextPath() + AppPaths.LOGIN_PAGE);
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();
        PurchaseService purchaseService = new PurchaseService(em);
        SupplierService supplierService = new SupplierService(em);

        try {
            Integer supplierId = Integer.parseInt(request.getParameter("supplierId"));
            model.Supplier supplier = supplierService.buscarPorId(supplierId);

            model.Purchase purchase = new model.Purchase();
            purchase.setSupplier(supplier);
            purchase.setOperator(logado);

            String[] productIds   = request.getParameterValues("productId");
            String[] quantities   = request.getParameterValues("quantity");
            String[] unitPrices   = request.getParameterValues("unitPrice");

            if (productIds != null) {
                dao.ProductDAO productDAO = new dao.ProductDAO(em);
                for (int i = 0; i < productIds.length; i++) {
                    model.Product product = productDAO.buscarPorId(Long.parseLong(productIds[i]));
                    int qty = Integer.parseInt(quantities[i]);
                    java.math.BigDecimal price = new java.math.BigDecimal(unitPrices[i]);

                    model.PurchaseItem item = new model.PurchaseItem();
                    item.setProduct(product);
                    item.setQuantity(qty);
                    item.setUnitPrice(price);
                    item.setSubtotal(price.multiply(new java.math.BigDecimal(qty)));

                    purchase.addItem(item);
                }
            }

            em.getTransaction().begin();
            purchaseService.registrar(purchase);
            em.getTransaction().commit();

            response.sendRedirect(request.getContextPath() + "/PurchaseServlet");

        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            e.printStackTrace();
            request.setAttribute("mensagem", "Erro: " + e.getMessage());
            request.getRequestDispatcher(AppPaths.PURCHASE_FORM)
                   .forward(request, response);
        } finally {
            if (em.isOpen()) em.close();
        }
    }
}