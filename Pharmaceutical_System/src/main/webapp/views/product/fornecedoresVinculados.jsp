<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Product, model.Supplier, model.SupplierProduct, java.util.List, java.time.LocalDate"%>
<%
    Product produto = (Product) request.getAttribute("produto");
    List<Supplier> todosFornecedores = (List<Supplier>) request.getAttribute("todosFornecedores");
    List<SupplierProduct> vinculos = (List<SupplierProduct>) request.getAttribute("vinculos");
    if (produto == null) {
        response.sendRedirect(request.getContextPath() + "/ProductServlet");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Fornecedores do Produto - <%= produto.getName() %></title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .container-vinculo {
            max-width: 800px;
            margin: 40px auto;
        }
        .card {
            background: white;
            border-radius: var(--radius);
            box-shadow: var(--sombra);
            padding: 24px;
            margin-bottom: 24px;
            border: 1px solid var(--borda);
        }
        .card h3 {
            margin-top: 0;
            border-bottom: 1px solid var(--borda);
            padding-bottom: 12px;
            margin-bottom: 20px;
        }
        .tabela-vinculo {
            width: 100%;
            margin-top: 10px;
        }
        .tabela-vinculo th, .tabela-vinculo td {
            padding: 10px 8px;
            border-bottom: 1px solid var(--borda);
            text-align: left;
        }
        .form-vincular {
            display: flex;
            gap: 12px;
            align-items: flex-end;
            flex-wrap: wrap;
        }
        .form-vincular .form-group {
            flex: 1;
            margin-bottom: 0;
        }
        .form-vincular select, .form-vincular input {
            width: 100%;
        }
        .btn-remover {
            background: none;
            border: none;
            color: var(--vermelho);
            cursor: pointer;
            font-size: 1.2rem;
            padding: 4px 8px;
        }
        .btn-remover:hover {
            background: var(--vermelho-claro);
            border-radius: var(--radius-sm);
        }
    </style>
</head>
<body>
    <header>
        <div class="marca">🏭 Vínculo de Fornecedores</div>
        <div class="user-info">
            <a href="${pageContext.request.contextPath}/ProductServlet">← Voltar para Produtos</a>
        </div>
    </header>

    <div class="container-vinculo">
        <div class="card">
            <h3>Produto: <%= produto.getName() %> (ID <%= produto.getId() %>)</h3>
            <p><strong>Código de barras:</strong> <%= produto.getBarcode() != null ? produto.getBarcode() : "-" %></p>
        </div>

        <div class="card">
            <h3>Fornecedores Vinculados</h3>
            <table class="tabela-vinculo">
                <thead>
                    <tr>
                        <th>Fornecedor</th>
                        <th>Data da última compra</th>
                        <th>Ação</th>
                    </tr>
                </thead>
                <tbody>
                <%
                    if (vinculos != null && !vinculos.isEmpty()) {
                        for (SupplierProduct v : vinculos) {
                            Supplier s = null;
                            for (Supplier sup : todosFornecedores) {
                                if (sup.getId().equals(v.getSupplierId())) { s = sup; break; }
                            }
                            if (s == null) continue;
                %>
                    <tr>
                        <td><%= s.getCompanyName() %> (CNPJ: <%= s.getCnpj() %>)</td>
                        <td><%= v.getPurchaseDate() %></td>
                        <td>
                            <form action="ProdutoFornecedorServlet" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="desvincular">
                                <input type="hidden" name="produtoId" value="<%= produto.getId() %>">
                                <input type="hidden" name="fornecedorId" value="<%= v.getSupplierId() %>">
                                <button type="submit" class="btn-remover" onclick="return confirm('Desvincular fornecedor?')">✖ Remover</button>
                            </form>
                        </td>
                    </tr>
                <%
                        }
                    } else {
                %>
                    <tr><td colspan="3">Nenhum fornecedor vinculado a este produto.</td></tr>
                <% } %>
                </tbody>
            </table>
        </div>

        <div class="card">
            <h3>Adicionar Fornecedor</h3>
            <form action="ProdutoFornecedorServlet" method="post" class="form-vincular">
                <input type="hidden" name="action" value="vincular">
                <input type="hidden" name="produtoId" value="<%= produto.getId() %>">
                <div class="form-group">
                    <label>Fornecedor</label>
                    <select name="fornecedorId" required>
                        <option value="">-- Selecione --</option>
                        <%
                            for (Supplier s : todosFornecedores) {
                                boolean jaVinculado = false;
                                for (SupplierProduct v : vinculos) {
                                    if (v.getSupplierId().equals(s.getId())) { jaVinculado = true; break; }
                                }
                                if (!jaVinculado) {
                        %>
                            <option value="<%= s.getId() %>"><%= s.getCompanyName() %> (CNPJ: <%= s.getCnpj() %>)</option>
                        <% } } %>
                    </select>
                </div>
                <div class="form-group">
                    <label>Data da compra</label>
                    <input type="date" name="dataCompra" value="<%= LocalDate.now() %>" required>
                </div>
                <button type="submit" class="btn btn-salvar">Vincular</button>
            </form>
        </div>
    </div>
</body>
</html>