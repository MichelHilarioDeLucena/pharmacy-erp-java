<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.Product, model.Usuario, model.Perfil"%>
<%
    Usuario logado = (Usuario) session.getAttribute("usuarioLogado");
    if (logado == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    Perfil perfil = logado.getPerfil();
    boolean ehAdmin   = (perfil == Perfil.ADMIN);
    boolean ehGerente = (perfil == Perfil.GERENTE);
    boolean temAcesso = ehAdmin || ehGerente;

    Boolean alertaEstoque = (Boolean) request.getAttribute("alertaEstoque");
    boolean exibirAlerta  = (alertaEstoque != null && alertaEstoque);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Estoque - ERP Farmácia</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .badge-baixo  { background: #dc3545; color: white; padding: 2px 8px; border-radius: 20px; font-size: 0.7rem; font-weight: 600; }
        .badge-ok     { background: #28a745; color: white; padding: 2px 8px; border-radius: 20px; font-size: 0.7rem; font-weight: 600; }
        .status-ativo { color: #16a34a; font-weight: 600; }
        .status-inativo{ color: #dc2626; font-weight: 600; }
        .acoes-form { display: flex; gap: 6px; flex-wrap: wrap; }
        .btn-pequeno { padding: 4px 8px; font-size: 12px; }
    </style>
</head>
<body>
    <header>
        <div class="marca">📦 Gestão de Estoque</div>
        <div class="user-info">
            <span>Olá, <strong><%=logado.getNome()%></strong> (<%=logado.getPerfil()%>)</span>
            <a href="${pageContext.request.contextPath}/LogoutServlet">Sair</a>
        </div>
    </header>

    <div class="container-wide">
        <h2>Produtos</h2>

        <% if (request.getAttribute("mensagem") != null) { %>
            <div class="<%= request.getAttribute("mensagem").toString().contains("Erro") ? "erro" : "sucesso" %>">
                <%= request.getAttribute("mensagem") %>
            </div>
        <% } %>

        <% if (exibirAlerta) { %>
            <div class="erro">⚠️ Exibindo apenas produtos com estoque abaixo do mínimo.</div>
        <% } %>

        <div style="display: flex; gap: 12px; margin-bottom: 24px; flex-wrap: wrap;">
            <a href="ProductServlet" class="btn">Todos os Produtos</a>
            <a href="ProductServlet?action=estoqueBaixo" class="btn btn-desativar">⚠️ Estoque Baixo</a>
            <% if (temAcesso) { %>
                <a href="ProductServlet?action=novo" class="btn btn-salvar">➕ Novo Produto</a>
            <% } %>
            <a href="home.jsp" class="btn">← Voltar</a>
        </div>

        <div style="overflow-x: auto;">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nome</th>
                        <th>Cód. Barras</th>
                        <th>Preço Custo</th>
                        <th>Preço Venda</th>
                        <th>Qtd. Atual</th>
                        <th>Qtd. Mínima</th>
                        <th>Validade</th>
                        <th>Status</th>
                        <th>Ações</th>
                    </tr>
                </thead>
                <tbody>
                <%
                List<Product> produtos = (List<Product>) request.getAttribute("produtos");
                if (produtos != null && !produtos.isEmpty()) {
                    for (Product p : produtos) {
                        boolean estoqueBaixo = p.getCurrentStock() <= p.getMinStock();
                        String badgeClass = estoqueBaixo ? "badge-baixo" : "badge-ok";
                        String badgeTexto = estoqueBaixo ? "⚠️ Baixo" : "OK";
                        String statusTexto = p.getActive() ? "Ativo" : "Inativo";
                        String classStatus = p.getActive() ? "status-ativo" : "status-inativo";
                %>
                    <tr>
                        <td><%=p.getId()%></td>
                        <td><%=p.getName()%></td>
                        <td><%=p.getBarcode() != null ? p.getBarcode() : "-"%></td>
                        <td>R$ <%=p.getCostPrice()%></td>
                        <td>R$ <%=p.getSalePrice()%></td>
                        <td>
                            <%=p.getCurrentStock()%>
                            <span class="<%=badgeClass%>"><%=badgeTexto%></span>
                        </td>
                        <td><%=p.getMinStock()%></td>
                        <td><%=p.getExpirationDate() != null ? p.getExpirationDate().toString() : "-"%></td>
                        <td class="<%=classStatus%>"><%=statusTexto%></td>
                        <td class="acoes-form">
                            <form method="post" action="ProductServlet" style="display: inline-flex; gap: 4px;">
                                <input type="hidden" name="action" value="entrada">
                                <input type="hidden" name="id" value="<%=p.getId()%>">
                                <input type="number" name="quantidade" min="1" value="1" style="width: 55px;">
                                <button type="submit" class="btn btn-ativar btn-pequeno">+ Entrada</button>
                            </form>
                            <% if (temAcesso) { %>
                                <a href="ProductServlet?action=editar&id=<%=p.getId()%>" class="btn btn-editar btn-pequeno">✏️ Editar</a>
                                <% if (p.getActive()) { %>
                                    <a href="ProductServlet?action=desativar&id=<%=p.getId()%>" class="btn btn-desativar btn-pequeno"
                                       onclick="return confirm('Desativar o produto <%=p.getName()%>?');">Desativar</a>
                                <% } else { %>
                                    <a href="ProductServlet?action=reativar&id=<%=p.getId()%>" class="btn btn-ativar btn-pequeno"
                                       onclick="return confirm('Reativar o produto <%=p.getName()%>?');">Reativar</a>
                                <% } %>
                                <a href="ProdutoFornecedorServlet?produtoId=<%=p.getId()%>" class="btn btn-pequeno">🏭 Fornecedores</a>
                            <% } %>
                        </td>
                    </tr>
                <%
                    }
                } else {
                %>
                    <tr><td colspan="10" style="text-align: center;">Nenhum produto encontrado.</td></tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>