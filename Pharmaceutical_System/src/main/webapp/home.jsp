<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Usuario" %>
<%
    // Proteção: Se não tiver usuário na sessão, volta para o login
    Usuario logado = (Usuario) session.getAttribute("usuarioLogado");
    if (logado == null) {
        response.sendRedirect("index.html");
        return;
    }
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
            Olá, <strong><%= logado.getNome() %></strong> [<%= logado.getPerfil().name() %>] | 
            <a href="LogoutServlet">Sair</a>
        </div>
    </header>

    <div class="container-dashboard">
        <h1>Menu Principal</h1>
        <div class="grid-opcoes">
            
            <% if (logado.getPerfil().toString().equals("ADMIN") || logado.getPerfil().toString().equals("GERENTE")) { %>
                <a href="UsuarioServlet?acao=listar" class="card-opcao">
                    <h3>👥 Gestão de Pessoas</h3>
                    <p>Visualizar equipe e contatos.</p>
                </a>
            <% } %>

            <a href="vendas.jsp" class="card-opcao">
                <h3>💰 Frente de Caixa</h3>
                <p>Realizar vendas e consultas.</p>
            </a>

            <% if (!logado.getPerfil().toString().equals("CAIXA")) { %>
                <a href="estoque.jsp" class="card-opcao">
                    <h3>📦 Estoque</h3>
                    <p>Entrada de mercadorias e validade.</p>
                </a>
            <% } %>

        </div>
    </div>
</body>
</html>