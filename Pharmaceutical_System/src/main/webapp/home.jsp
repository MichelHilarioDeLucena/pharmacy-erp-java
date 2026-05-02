<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Usuario, model.Perfil"%>
<%
    Usuario logado = (Usuario) session.getAttribute("usuarioLogado");
    if (logado == null) {
        response.sendRedirect("${pageContext.request.contextPath}/login.jsp");
        return;
    }

    // [CORREÇÃO] Perfil resolvido uma única vez, com guard contra null.
    // Antes, logado.getPerfil().name() e .toString() eram chamados diretamente
    // no HTML — NPE garantido se o perfil não estivesse preenchido.
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
    <title>Dashboard - ERP Farmácia</title>
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

            <% if (ehAdmin || ehGerente) { %>
                <a href="UsuarioServlet?acao=listar" class="card-opcao">
                    <h3>👥 Gestão de Pessoas</h3>
                    <p>Visualizar equipe e contatos.</p>
                </a>
            <% } %>

            <a href="${pageContext.request.contextPath}/CheckoutServlet" class="card-opcao">
                <h3>💰 Frente de Caixa</h3>
                <p>Realizar vendas e consultas.</p>
            </a>

            <% if (!ehCaixa) { %>
                <a href="estoque.jsp" class="card-opcao">
                    <h3>📦 Estoque</h3>
                    <p>Entrada de mercadorias e validade.</p>
                </a>
            <% } %>

        </div>
    </div>
</body>
</html>