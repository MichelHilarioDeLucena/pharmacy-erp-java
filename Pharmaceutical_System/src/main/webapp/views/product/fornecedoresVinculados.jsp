<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Product, model.Supplier, model.SupplierProduct, java.util.List, java.time.LocalDate"%>
<%
    Product produto = (Product) request.getAttribute("produto");
    List<Supplier> todosFornecedores = (List<Supplier>) request.getAttribute("todosFornecedores");
    List<SupplierProduct> vinculos = (List<SupplierProduct>) request.getAttribute("vinculos");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Vincular Fornecedores - <%= produto.getName() %></title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <h2>Gerenciar Fornecedores do Produto: <%= produto.getName() %></h2>

    <h3>Fornecedores Vinculados</h3>
    <table border="1">
        <thead>
            <tr><th>Fornecedor</th><th>Data da Última Compra</th><th>Ação</th></tr>
        </thead>
        <tbody>
            <% for (SupplierProduct v : vinculos) {
                Supplier s = null;
                for (Supplier sup : todosFornecedores) {
                    if (sup.getId().equals(v.getSupplierId())) { s = sup; break; }
                }
                if (s == null) continue;
            %>
            <tr>
                <td><%= s.getCompanyName() %></td>
                <td><%= v.getPurchaseDate() %></td>
                <td>
                    <form action="ProdutoFornecedorServlet" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="desvincular">
                        <input type="hidden" name="produtoId" value="<%= produto.getId() %>">
                        <input type="hidden" name="fornecedorId" value="<%= v.getSupplierId() %>">
                        <button type="submit" onclick="return confirm('Desvincular?')">Desvincular</button>
                    </form>
                </td>
            </tr>
            <% } %>
            <% if (vinculos.isEmpty()) { %>
            <tr><td colspan="3">Nenhum fornecedor vinculado.</td></tr>
            <% } %>
        </tbody>
    </table>

    <h3>Adicionar Fornecedor</h3>
    <form action="ProdutoFornecedorServlet" method="post">
        <input type="hidden" name="action" value="vincular">
        <input type="hidden" name="produtoId" value="<%= produto.getId() %>">
        <label>Fornecedor:</label>
        <select name="fornecedorId" required>
            <option value="">-- Selecione --</option>
            <% for (Supplier s : todosFornecedores) {
                boolean jaVinculado = false;
                for (SupplierProduct v : vinculos) {
                    if (v.getSupplierId().equals(s.getId())) { jaVinculado = true; break; }
                }
                if (!jaVinculado) {
            %>
                <option value="<%= s.getId() %>"><%= s.getCompanyName() %></option>
            <% } } %>
        </select>
        <label>Data da Compra:</label>
        <input type="date" name="dataCompra" value="<%= LocalDate.now() %>" required>
        <button type="submit">Vincular</button>
    </form>

    <br>
    <a href="${pageContext.request.contextPath}/ProductServlet">Voltar para Produtos</a>
</body>
</html>