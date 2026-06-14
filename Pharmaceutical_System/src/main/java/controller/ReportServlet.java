package controller;

import java.io.IOException;
import java.util.List;

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
import model.Purchase;
import model.Sale;
import model.Supplier;
import model.Usuario;
import service.ReportService;
import util.ReportGenerator;

@WebServlet("/ReportServlet")
public class ReportServlet extends HttpServlet {

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
        ReportService service = new ReportService(em);

        try {
            String action = request.getParameter("action");
            String format = request.getParameter("format"); // "pdf" ou "csv"

            // --- EXPORTAÇÕES ---

            if ("estoque-baixo-pdf".equals(action)) {
                List<Product> produtos = service.relatorioEstoqueBaixo();
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment; filename=estoque_baixo.pdf");
                ReportGenerator.estoqueBaixoPdf(produtos, response.getOutputStream());
                return;
            }

            if ("estoque-baixo-csv".equals(action)) {
                List<Product> produtos = service.relatorioEstoqueBaixo();
                response.setContentType("text/csv; charset=UTF-8");
                response.setHeader("Content-Disposition", "attachment; filename=estoque_baixo.csv");
                ReportGenerator.estoqueBaixoCsv(produtos, response.getWriter());
                return;
            }

            if ("estoque-completo-pdf".equals(action)) {
                List<Product> produtos = service.relatorioEstoqueCompleto();
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment; filename=estoque_completo.pdf");
                ReportGenerator.estoqueBaixoPdf(produtos, response.getOutputStream());
                return;
            }

            if ("estoque-completo-csv".equals(action)) {
                List<Product> produtos = service.relatorioEstoqueCompleto();
                response.setContentType("text/csv; charset=UTF-8");
                response.setHeader("Content-Disposition", "attachment; filename=estoque_completo.csv");
                ReportGenerator.estoqueBaixoCsv(produtos, response.getWriter());
                return;
            }

            if ("compras-pdf".equals(action)) {
                List<Purchase> compras = service.relatorioCompras();
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment; filename=compras.pdf");
                ReportGenerator.comprasPdf(compras, response.getOutputStream());
                return;
            }

            if ("compras-csv".equals(action)) {
                List<Purchase> compras = service.relatorioCompras();
                response.setContentType("text/csv; charset=UTF-8");
                response.setHeader("Content-Disposition", "attachment; filename=compras.csv");
                ReportGenerator.comprasCsv(compras, response.getWriter());
                return;
            }

            if ("fornecedores-pdf".equals(action)) {
                List<Supplier> fornecedores = service.relatorioFornecedores();
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment; filename=fornecedores.pdf");
                ReportGenerator.fornecedoresPdf(fornecedores, response.getOutputStream());
                return;
            }

            if ("fornecedores-csv".equals(action)) {
                List<Supplier> fornecedores = service.relatorioFornecedores();
                response.setContentType("text/csv; charset=UTF-8");
                response.setHeader("Content-Disposition", "attachment; filename=fornecedores.csv");
                ReportGenerator.fornecedoresCsv(fornecedores, response.getWriter());
                return;
            }
            if ("vendas-pdf".equals(action)) {
                List<Sale> vendas = service.relatorioVendas();
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment; filename=vendas.pdf");
                ReportGenerator.vendasPdf(vendas, response.getOutputStream());
                return;
            }
            if ("vendas-csv".equals(action)) {
                List<Sale> vendas = service.relatorioVendas();
                response.setContentType("text/csv; charset=UTF-8");
                response.setHeader("Content-Disposition", "attachment; filename=vendas.csv");
                ReportGenerator.vendasCsv(vendas, response.getWriter());
                return;
            }
            // --- TELA PRINCIPAL ---
            request.setAttribute("totalEstoqueBaixo", service.relatorioEstoqueBaixo().size());
            request.setAttribute("totalCompras", service.relatorioCompras().size());
            request.setAttribute("totalFornecedores", service.relatorioFornecedores().size());
            request.getRequestDispatcher(AppPaths.REPORT_LISTA)
                   .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensagem", "Erro ao gerar relatório: " + e.getMessage());
            request.getRequestDispatcher(AppPaths.REPORT_LISTA)
                   .forward(request, response);
        } finally {
            if (em.isOpen()) em.close();
        }
    }
}