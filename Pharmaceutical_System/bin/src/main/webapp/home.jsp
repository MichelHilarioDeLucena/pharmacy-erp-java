<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Usuario, model.Perfil, config.AppPaths"%>
<%
    Usuario logado = (Usuario) session.getAttribute("usuarioLogado");
    if (logado == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    Perfil perfil = logado.getPerfil();
    boolean ehAdmin   = (perfil == Perfil.ADMIN);
    boolean ehGerente = (perfil == Perfil.GERENTE);
    boolean ehCaixa   = (perfil == Perfil.CAIXA);
    String  nomePerfil = (perfil != null) ? perfil.name() : "SEM PERFIL";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Home - ERP Farmácia</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
    <header>
        <div class="user-info">
            Olá, <strong><%= logado.getNome() %></strong> [<%= nomePerfil %>] |
            <a href="LogoutServlet">Sair</a>
        </div>
    </header>

    <div class="container-dashboard">
        <h1>Menu Principal</h1>
        <div class="grid-opcoes">

            <%-- DASHBOARD: Apenas Admin e Gerente --%>
            <% if (ehAdmin || ehGerente) { %>
                <a href="<%= request.getContextPath() + AppPaths.DASHBOARD_SERVLET %>" class="card-opcao">
                    <div class="icon">📊</div>
                    <h3>Dashboard de Vendas</h3>
                    <p>Análise de faturamento e produtos top.</p>
                </a>
            <% } %>

            <%-- GESTÃO DE PESSOAS: Apenas Admin e Gerente --%>
            <% if (ehAdmin || ehGerente) { %>
                <a href="<%= request.getContextPath() + AppPaths.USUARIO_LISTAR_ACAO %>" class="card-opcao">
                    <div class="icon">👥</div>
                    <h3>Gestão de Pessoas</h3>
                    <p>Visualizar equipe e contatos.</p>
                </a>
            <% } %>

            <%-- FRENTE DE CAIXA: Acesso Geral --%>
            <a href="<%= request.getContextPath() + AppPaths.CHECKOUT_SERVLET %>" class="card-opcao">
                <div class="icon">💰</div>
                <h3>Frente de Caixa</h3>
                <p>Realizar vendas e consultas.</p>
            </a>

            <%-- ESTOQUE: Apenas se NÃO for Caixa --%>
            <% if (!ehCaixa) { %>
                <a href="<%= request.getContextPath() + AppPaths.PRODUTO_SERVLET %>" class="card-opcao">
                    <div class="icon">📦</div>
                    <h3>Estoque</h3>
                    <p>Entrada de mercadorias e validade.</p>
                </a>
            <% } %>

            <%-- FORNECEDORES: Apenas Admin e Gerente --%>
            <% if (ehAdmin || ehGerente) { %>
                <a href="<%= request.getContextPath() + AppPaths.SUPPLIER_SERVLET %>" class="card-opcao">
                    <div class="icon">🏭</div>
                    <h3>Fornecedores</h3>
                    <p>Cadastro e gestão de fornecedores.</p>
                </a>
            <% } %>

            <%-- COMPRAS: Apenas Admin e Gerente --%>
            <% if (ehAdmin || ehGerente) { %>
                <a href="<%= request.getContextPath() + AppPaths.PURCHASE_SERVLET %>" class="card-opcao">
                    <div class="icon">🛒</div>
                    <h3>Compras</h3>
                    <p>Histórico e pedidos automáticos.</p>
                </a>
            <% } %>
            
			<% if (ehAdmin || ehGerente) { %>
			    <a href="<%= request.getContextPath() + AppPaths.REPORT_SERVLET %>" class="card-opcao">
			        <div class="icon">📊</div>
			        <h3>Relatórios</h3>
			        <p>Estoque, compras e fornecedores.</p>
			    </a>
			<% } %>
        </div>
    </div>
</body>
</html>