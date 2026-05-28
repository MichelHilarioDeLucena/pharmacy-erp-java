<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.Product"%>
<%@ page import="model.Usuario, model.Perfil"%>
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
        .badge-baixo  { background: #dc3545; color: white; padding: 2px 8px; border-radius: 10px; font-size: 0.8em; }
        .badge-ok     { background: #28a745; color: white; padding: 2px 8px; border-radius: 10px; font-size: 0.8em; }
        .alerta-box   { background: #fff3cd; border: 1px solid #ffc107; padding: 10px 16px; border-radius: 6px; margin-bottom: 16px; color: #856404; }
    </style>
</head>
<body>
    <header style="display: flex; justify-content: space-between; align-items: center; padding: 10px 20px; background: #eee; border-bottom: 2px solid #ccc;">
        <h2>📦 Gestão de Estoque</h2>
        <div>
            <span>Logado como: <strong><%=logado.getNome()%></strong> (<%=logado.getPerfil()%>)</span>
            <a href="LogoutServlet" style="margin-left: 15px; color: red; text-decoration: none;">Sair</a>
        </div>
    </header>

    <main style="padding: 20px;">

        <% if (request.getAttribute("mensagem") != null) { %>
            <div class="alerta-box">⚠️ <%=request.getAttribute("mensagem")%></div>
        <% } %>

        <% if (exibirAlerta) { %>
            <div class="alerta-box">⚠️ Exibindo apenas produtos com estoque abaixo do mínimo.</div>
        <% } %>

        <div style="display: flex; gap: 10px; margin-bottom: 20px;">
            <a href="ProductServlet" class="btn">Todos os Produtos</a>
            <a href="ProductServlet?action=estoqueBaixo" class="btn btn-desativar">⚠️ Estoque Baixo</a>
            <% if (temAcesso) { %>
                <a href="ProductServlet?action=novo" class="btn btn-novo">➕ Novo Produto</a>
            <% } %>
            <a href="home.jsp" class="btn">← Voltar</a>
            
        </div>

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
                        String badgeClass    = estoqueBaixo ? "badge-baixo" : "badge-ok";
                        String badgeTexto    = estoqueBaixo ? "⚠️ Baixo" : "OK";
                        String statusTexto   = p.getActive() ? "Ativo" : "Inativo";
                        String classStatus   = p.getActive() ? "status-ativo" : "status-inativo";
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
                    <td>
                        <div style="display: flex; gap: 5px; flex-wrap: wrap;">
                            <!-- Entrada de estoque via formulário inline -->
                            <form method="post" action="ProductServlet" style="display: inline-flex; gap: 4px;">
                                <input type="hidden" name="action" value="entrada">
                                <input type="hidden" name="id" value="<%=p.getId()%>">
                                <input type="number" name="quantidade" min="1" value="1" style="width: 55px;">
                                <button type="submit" class="btn btn-ativar">+ Entrada</button>
                            </form>
                            <% if (temAcesso) { %>
                                <a href="ProductServlet?action=editar&id=<%=p.getId()%>" class="btn btn-editar">✎ Editar</a>
                                <a href="ProductServlet?action=desativar&id=<%=p.getId()%>"
                                   class="btn btn-desativar"
                                   onclick="return confirm('Desativar o produto <%=p.getName()%>?');">
                                   Desativar
                                </a>
                            <% } %>
                            <a href="ProdutoFornecedorServlet?produtoId=<%= p.getId() %>">Fornecedores</a>
                        </div>
                    </td>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td colspan="10" style="text-align: center;">Nenhum produto encontrado.</td>
                </tr>
                <%
                }
                %>
            </tbody>
        </table>
    </main>

    <script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>
