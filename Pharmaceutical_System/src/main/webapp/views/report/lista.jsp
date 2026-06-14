<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Usuario, model.Perfil"%>
<%
    Usuario logado = (Usuario) session.getAttribute("usuarioLogado");
    if (logado == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    Integer totalEstoqueBaixo = (Integer) request.getAttribute("totalEstoqueBaixo");
    Integer totalCompras      = (Integer) request.getAttribute("totalCompras");
    Integer totalFornecedores = (Integer) request.getAttribute("totalFornecedores");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Relatórios - ERP Farmácia</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .report-card {
            background: #f8f9fa;
            border: 1px solid #dee2e6;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
        }
        .report-card h3 {
            margin-top: 0;
            border-bottom: 2px solid #ccc;
            padding-bottom: 8px;
        }
        .report-card .resumo {
            font-size: 1.1em;
            margin-bottom: 12px;
            color: #555;
        }
        .btn-group { display: flex; gap: 10px; flex-wrap: wrap; }
    </style>
</head>
<body>
    <header style="display: flex; justify-content: space-between; align-items: center; padding: 10px 20px; background: #eee; border-bottom: 2px solid #ccc;">
        <h2>Relatórios</h2>
        <div>
            <span>Logado como: <strong><%=logado.getNome()%></strong> (<%=logado.getPerfil()%>)</span>
            <a href="LogoutServlet" style="margin-left: 15px; color: red; text-decoration: none;">Sair</a>
        </div>
    </header>

    <main style="padding: 20px; max-width: 900px;">

        <% if (request.getAttribute("mensagem") != null) { %>
            <div style="background: #fff3cd; border: 1px solid #ffc107; padding: 10px 16px; border-radius: 6px; margin-bottom: 16px; color: #856404;">
                ⚠️ <%=request.getAttribute("mensagem")%>
            </div>
        <% } %>

        <a href="home.jsp" class="btn" style="margin-bottom: 20px; display: inline-block;">Home</a>

        <!-- ESTOQUE BAIXO -->
        <div class="report-card">
            <h3>Estoque Baixo</h3>
            <p class="resumo">Produtos com quantidade atual abaixo do mínimo: <strong><%=totalEstoqueBaixo != null ? totalEstoqueBaixo : 0%></strong></p>
            <div class="btn-group">
                <a href="ReportServlet?action=estoque-baixo-pdf" class="btn btn-desativar">⬇ PDF</a>
                <a href="ReportServlet?action=estoque-baixo-csv" class="btn btn-ativar">⬇ CSV</a>
            </div>
        </div>

        <!-- ESTOQUE COMPLETO -->
        <div class="report-card">
            <h3>Estoque Completo</h3>
            <p class="resumo">Todos os produtos ativos com seus níveis de estoque.</p>
            <div class="btn-group">
                <a href="ReportServlet?action=estoque-completo-pdf" class="btn btn-desativar">⬇ PDF</a>
                <a href="ReportServlet?action=estoque-completo-csv" class="btn btn-ativar">⬇ CSV</a>
            </div>
        </div>

        <!-- COMPRAS -->
        <div class="report-card">
            <h3>Compras</h3>
            <p class="resumo">Total de compras registradas: <strong><%=totalCompras != null ? totalCompras : 0%></strong></p>
            <div class="btn-group">
                <a href="ReportServlet?action=compras-pdf" class="btn btn-desativar">⬇ PDF</a>
                <a href="ReportServlet?action=compras-csv" class="btn btn-ativar">⬇ CSV</a>
            </div>
        </div>

        <!-- FORNECEDORES -->
        <div class="report-card">
            <h3>Fornecedores</h3>
            <p class="resumo">Total de fornecedores cadastrados: <strong><%=totalFornecedores != null ? totalFornecedores : 0%></strong></p>
            <div class="btn-group">
                <a href="ReportServlet?action=fornecedores-pdf" class="btn btn-desativar">PDF</a>
                <a href="ReportServlet?action=fornecedores-csv" class="btn btn-ativar">CSV</a>
            </div>
        </div>

    </main>
</body>
</html>
