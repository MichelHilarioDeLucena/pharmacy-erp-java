package controller;

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
import model.Supplier;
import model.Usuario;
import service.SupplierService;

@WebServlet("/SupplierServlet")
public class SupplierServlet extends HttpServlet {

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
        SupplierService service = new SupplierService(em);

        try {
            String action = request.getParameter("action");

            if ("editar".equals(action)) {
                Integer id = Integer.parseInt(request.getParameter("id"));
                Supplier supplier = service.buscarPorId(id);
                request.setAttribute("supplier", supplier);
                request.getRequestDispatcher(AppPaths.SUPPLIER_FORM)
                       .forward(request, response);

            } else if ("desativar".equals(action)) {
                Integer id = Integer.parseInt(request.getParameter("id"));
                em.getTransaction().begin();
                service.desativar(id);
                em.getTransaction().commit();
                response.sendRedirect(request.getContextPath() + "/SupplierServlet");

            } else if ("reativar".equals(action)) {
                Integer id = Integer.parseInt(request.getParameter("id"));
                em.getTransaction().begin();
                service.reativar(id);
                em.getTransaction().commit();
                response.sendRedirect(request.getContextPath() + "/SupplierServlet");

            } else if ("novo".equals(action)) {
                request.getRequestDispatcher(AppPaths.SUPPLIER_FORM)
                       .forward(request, response);

            } else {
                request.setAttribute("suppliers", service.listarTodos());
                request.getRequestDispatcher(AppPaths.SUPPLIER_LISTA)
                       .forward(request, response);
            }

        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            e.printStackTrace();
            request.setAttribute("mensagem", "Erro ao carregar fornecedores.");
            request.getRequestDispatcher(AppPaths.SUPPLIER_LISTA)
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
        SupplierService service = new SupplierService(em);

        try {
            Supplier supplier = new Supplier();

            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isEmpty()) {
                supplier = service.buscarPorId(Integer.parseInt(idParam));
            }

            supplier.setCompanyName(request.getParameter("companyName"));
            supplier.setCnpj(request.getParameter("cnpj"));
            supplier.setSupplyCategory(request.getParameter("supplyCategory"));
            supplier.setPhone(request.getParameter("phone"));
            supplier.setEmail(request.getParameter("email"));

            em.getTransaction().begin();
            if (supplier.getId() == null) {
                service.cadastrar(supplier);
            } else {
                service.atualizar(supplier);
            }
            em.getTransaction().commit();

            response.sendRedirect(request.getContextPath() + "/SupplierServlet");

        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            e.printStackTrace();
            request.setAttribute("mensagem", "Erro: " + e.getMessage());
            request.getRequestDispatcher(AppPaths.SUPPLIER_FORM)
                   .forward(request, response);
        } finally {
            if (em.isOpen()) em.close();
        }
    }
}