<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.Supplier, model.Product, model.Usuario"%>
<%
    Usuario logado = (Usuario) session.getAttribute("usuarioLogado");
    if (logado == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    List<Supplier> suppliers = (List<Supplier>) request.getAttribute("suppliers");
    List<Product> resultados = (List<Product>) request.getAttribute("resultadosBusca");
    String termoBusca = (String) request.getAttribute("termoBusca");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Nova Compra - ERP Farmácia</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .form-group { margin-bottom: 14px; }
        .form-group label { display: block; font-weight: bold; margin-bottom: 4px; }
        .form-group input, .form-group select {
            width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box;
        }
        .form-row { display: flex; gap: 16px; }
        .form-row .form-group { flex: 1; }
        .item-row { display: flex; gap: 10px; align-items: flex-end; margin-bottom: 10px; padding: 10px; background: #f8f9fa; border-radius: 6px; }
        .item-row .form-group { margin-bottom: 0; }
        .resultado-item { padding: 6px 0; border-bottom: 1px solid #eee; display: flex; justify-content: space-between; align-items: center; }
        .resultado-item:last-child { border-bottom: none; }
    </style>
</head>
<body>
    <header style="display: flex; justify-content: space-between; align-items: center; padding: 10px 20px; background: #eee; border-bottom: 2px solid #ccc;">
        <h2>➕ Nova Compra</h2>
        <div>
            <span>Logado como: <strong><%=logado.getNome()%></strong></span>
            <a href="LogoutServlet" style="margin-left: 15px; color: red; text-decoration: none;">Sair</a>
        </div>
    </header>

    <main style="padding: 20px; max-width: 800px;">

        <% if (request.getAttribute("mensagem") != null) { %>
            <div style="background: #fff3cd; border: 1px solid #ffc107; padding: 10px 16px; border-radius: 6px; margin-bottom: 16px; color: #856404;">
                ⚠️ <%=request.getAttribute("mensagem")%>
            </div>
        <% } %>

        <!-- BUSCA DE PRODUTO -->
        <div style="margin-bottom: 20px;">
            <h3>Buscar Produto</h3>
            <form action="${pageContext.request.contextPath}/PurchaseServlet" method="GET" style="display: flex; gap: 10px; align-items: flex-end;">
                <input type="hidden" name="action" value="buscarProduto">
                <div class="form-group" style="flex: 1; margin-bottom: 0;">
                    <label>ID ou Nome</label>
                    <input type="text" name="termo" placeholder="Ex: 1 ou Tylenol" value="<%= termoBusca != null ? termoBusca : "" %>">
                </div>
                <button type="submit" class="btn btn-salvar" style="height: 38px;">🔍 Buscar</button>
            </form>
        </div>

        <!-- RESULTADOS DA BUSCA -->
        <%
        if (resultados != null && !resultados.isEmpty()) {
        %>
            <div style="margin-bottom: 20px; border: 1px solid #ddd; border-radius: 6px; padding: 12px; background: #f9f9f9;">
                <h4 style="margin: 0 0 8px 0;">Resultados da busca</h4>
                <div>
                <% for (Product p : resultados) { %>
                    <div class="resultado-item">
                        <span><strong><%= p.getName() %></strong> (ID: <%= p.getId() %>) - Preço Custo: R$ <%= p.getCostPrice() %></span>
                        <button type="button" class="btn btn-ativar" onclick="adicionarProdutoDaBusca(<%= p.getId() %>, '<%= p.getName() %>', <%= p.getCostPrice() %>)">
                            ➕ Adicionar
                        </button>
                    </div>
                <% } %>
                </div>
            </div>
        <% } else if (resultados != null && termoBusca != null && !termoBusca.isEmpty()) { %>
            <p style="color: #888;">Nenhum produto encontrado para "<strong><%= termoBusca %></strong>".</p>
        <% } %>

        <!-- FORMULÁRIO PRINCIPAL DA COMPRA -->
        <form method="post" action="PurchaseServlet" id="compraForm">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">

            <div class="form-group">
                <label>Fornecedor *</label>
                <select name="supplierId" required>
                    <option value="">-- Selecione --</option>
                    <%
                    if (suppliers != null) {
                        for (Supplier s : suppliers) {
                    %>
                        <option value="<%=s.getId()%>"><%=s.getCompanyName()%> — <%=s.getCnpj()%></option>
                    <%
                        }
                    }
                    %>
                </select>
            </div>

            <h3 style="margin-top: 24px;">Itens da Compra</h3>
            <div id="itens">
                <div class="item-row">
                    <div class="form-group" style="flex: 1;">
                        <label>ID do Produto</label>
                        <input type="number" name="productId" min="1" required placeholder="ID do produto">
                    </div>
                    <div class="form-group" style="width: 100px;">
                        <label>Quantidade</label>
                        <input type="number" name="quantity" min="1" value="1" required>
                    </div>
                    <div class="form-group" style="width: 130px;">
                        <label>Preço Unit. (R$)</label>
                        <input type="number" name="unitPrice" step="0.01" min="0" required>
                    </div>
                    <button type="button" class="btn btn-desativar" onclick="this.parentElement.remove()" style="height: 38px; align-self: flex-end;">✕</button>
                </div>
            </div>

            <button type="button" class="btn" onclick="adicionarItem()" style="margin-bottom: 20px;">
                ➕ Adicionar Item
            </button>

            <div style="display: flex; gap: 10px; margin-top: 10px;">
                <button type="submit" class="btn btn-ativar">Registrar Compra</button>
                <a href="PurchaseServlet" class="btn">Cancelar</a>
            </div>
        </form>
    </main>

    <script>
        // Função existente para adicionar uma linha vazia
        function adicionarItem() {
            const container = document.getElementById('itens');
            const div = document.createElement('div');
            div.className = 'item-row';
            div.innerHTML = `
                <div class="form-group" style="flex: 1;">
                    <label>ID do Produto</label>
                    <input type="number" name="productId" min="1" required placeholder="ID do produto">
                </div>
                <div class="form-group" style="width: 100px;">
                    <label>Quantidade</label>
                    <input type="number" name="quantity" min="1" value="1" required>
                </div>
                <div class="form-group" style="width: 130px;">
                    <label>Preço Unit. (R$)</label>
                    <input type="number" name="unitPrice" step="0.01" min="0" required>
                </div>
                <button type="button" class="btn btn-desativar" onclick="this.parentElement.remove()" style="height: 38px; align-self: flex-end;">✕</button>
            `;
            container.appendChild(div);
        }

        // Nova função: adicionar produto a partir do resultado da busca
        function adicionarProdutoDaBusca(id, nome, precoCusto) {
            const container = document.getElementById('itens');
            const rows = container.getElementsByClassName('item-row');
            let targetRow = null;

            // 1. Verifica se existe uma linha com campos vazios (productId vazio) para reutilizar
            for (let row of rows) {
                const inputs = row.querySelectorAll('input');
                let vazio = true;
                for (let inp of inputs) {
                    if (inp.name === 'productId' && inp.value.trim() !== '') {
                        vazio = false;
                        break;
                    }
                }
                if (vazio) {
                    targetRow = row;
                    break;
                }
            }

            // 2. Se não encontrou linha vazia, cria uma nova
            if (!targetRow) {
                adicionarItem();
                // Recupera a última linha criada
                const newRows = container.getElementsByClassName('item-row');
                targetRow = newRows[newRows.length - 1];
            }

            // 3. Preenche os campos
            const inputs = targetRow.querySelectorAll('input');
            for (let inp of inputs) {
                if (inp.name === 'productId') {
                    inp.value = id;
                } else if (inp.name === 'quantity') {
                    inp.value = 1;
                } else if (inp.name === 'unitPrice') {
                    inp.value = precoCusto;
                }
            }
        }
    </script>
</body>
</html>