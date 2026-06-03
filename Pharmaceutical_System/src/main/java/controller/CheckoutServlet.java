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

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Object[]> clients = em.createNativeQuery("SELECT id, nome_completo, cpf FROM clientes")
                    .getResultList();
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

        EntityManager em = JPAUtil.getEntityManager();

        try {
            if ("buscar".equals(action)) {
                // Busca produto por ID ou nome
                String termo = request.getParameter("termo");
                List<Product> resultados = new ArrayList<>();

                try {
                    // Tenta buscar por ID primeiro
                    Long id = Long.parseLong(termo.trim());
                    Product p = em.find(Product.class, id);
                    if (p != null && p.getActive() && p.getCurrentStock() > 0) {
                        resultados.add(p);
                    }
                } catch (NumberFormatException e) {
                    // Se não for número, busca por nome
                    resultados = em.createQuery(
                        "SELECT p FROM Product p WHERE LOWER(p.name) LIKE :nome AND p.active = true AND p.currentStock > 0",
                        Product.class)
                        .setParameter("nome", "%" + termo.toLowerCase() + "%")
                        .getResultList();
                }

                List<Object[]> clients = em.createNativeQuery("SELECT id, nome_completo, cpf FROM clientes")
                        .getResultList();
                request.setAttribute("clientsList", clients);
                request.setAttribute("resultados", resultados);
                request.setAttribute("termoBusca", termo);
                session.setAttribute("cart", cart);
                request.getRequestDispatcher("/views/cashier/checkout.jsp").forward(request, response);
                
//                String termo = request.getParameter("termo");
                System.out.println("DEBUG BUSCA - termo: '" + termo + "'");
                resultados = new ArrayList<>();

                try {
                    Long id = Long.parseLong(termo.trim());
                    System.out.println("DEBUG - buscando por ID: " + id);
                    Product p = em.find(Product.class, id);
                    System.out.println("DEBUG - produto encontrado: " + p);
                    if (p != null) {
                        System.out.println("DEBUG - ativo: " + p.getActive() + " estoque: " + p.getCurrentStock());
                    }
                    if (p != null && p.getActive() && p.getCurrentStock() > 0) {
                        resultados.add(p);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("DEBUG - buscando por nome: " + termo);
                    resultados = em.createQuery(
                        "SELECT p FROM Product p WHERE LOWER(p.name) LIKE :nome AND p.active = true AND p.currentStock > 0",
                        Product.class)
                        .setParameter("nome", "%" + termo.toLowerCase() + "%")
                        .getResultList();
                    System.out.println("DEBUG - resultados por nome: " + resultados.size());
                }

            } else if ("add".equals(action)) {
                Long prodId = Long.parseLong(request.getParameter("productId"));
                int quantidade = Integer.parseInt(request.getParameter("quantidade"));

                Product product = em.find(Product.class, prodId);

                if (product != null && product.getActive()) {
                    if (quantidade > product.getCurrentStock()) {
                        // Estoque insuficiente — volta com mensagem
                        List<Object[]> clients = em.createNativeQuery("SELECT id, nome_completo, cpf FROM clientes")
                                .getResultList();
                        request.setAttribute("clientsList", clients);
                        request.setAttribute("erro", "Estoque insuficiente para \"" + product.getName() + "\". Disponível: " + product.getCurrentStock());
                        session.setAttribute("cart", cart);
                        request.getRequestDispatcher("/views/cashier/checkout.jsp").forward(request, response);
                        return;
                    }

                    // Verifica se produto já está no carrinho e atualiza quantidade
                    boolean encontrado = false;
                    for (SaleItem item : cart) {
                    	if (item.getProductId().longValue() == prodId) {
                            item.setQuantity(item.getQuantity() + quantidade);
                            item.setSubtotal(item.getUnitPrice().multiply(new BigDecimal(item.getQuantity())));
                            encontrado = true;
                            break;
                        }
                    }

                    if (!encontrado) {
                        SaleItem item = new SaleItem();
                        item.setProductId(prodId.intValue());
                        item.setProductName(product.getName());
                        item.setQuantity(quantidade);
                        item.setUnitPrice(product.getSalePrice());
                        item.setSubtotal(product.getSalePrice().multiply(new BigDecimal(quantidade)));
                        cart.add(item);
                    }

                    session.setAttribute("cart", cart);
                }

                response.sendRedirect(request.getContextPath() + "/CheckoutServlet");

            } else if ("remover".equals(action)) {
                int index = Integer.parseInt(request.getParameter("index"));
                if (index >= 0 && index < cart.size()) {
                    cart.remove(index);
                    session.setAttribute("cart", cart);
                }
                response.sendRedirect(request.getContextPath() + "/CheckoutServlet");

            } else if ("limpar".equals(action)) {
                session.removeAttribute("cart");
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
                    Product p = em.find(Product.class, Long.valueOf(i.getProductId()));
                    p.setCurrentStock(p.getCurrentStock() - i.getQuantity());
                    em.merge(p);
                }

                em.persist(newSale);
                em.getTransaction().commit();

                Integer generatedId = newSale.getId();
                session.removeAttribute("cart");
                response.sendRedirect(request.getContextPath() + "/CheckoutServlet?success=true&saleId=" + generatedId);
            }

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new ServletException("Erro no checkout", e);
        } finally {
            em.close();
        }
    }
}