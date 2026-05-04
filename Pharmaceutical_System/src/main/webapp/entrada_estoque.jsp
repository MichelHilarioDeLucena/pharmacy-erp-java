<%@ page import="java.util.List" %>
<%@ page import="model.Product" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Entrada de Mercadorias - ERP</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        /* Pequeno ajuste para deixar os campos lado a lado em telas grandes */
        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 16px;
        }
        .form-row-3 {
            display: grid;
            grid-template-columns: 1fr 1fr 1fr;
            gap: 16px;
        }
    </style>
</head>
<body>

    <header>
        <div class="marca">💊 ERP Farmácia - Controle de Estoque</div>
        <div class="user-info">
            <span>Olá, <strong>Operador</strong></span>
            <a href="${pageContext.request.contextPath}/home.jsp">Voltar ao Painel</a>
        </div>
    </header>

    <div class="container-wide">
        <h2>📦 Nova Entrada de Mercadoria</h2>
        <p class="subtitulo">Preencha os dados abaixo para registrar os produtos enviados pelo fornecedor.</p>

        <% if (request.getAttribute("mensagem") != null) { %>
            <div class="sucesso"><%= request.getAttribute("mensagem") %></div>
        <% } %>
        <% if (request.getAttribute("erro") != null) { %>
            <div class="erro"><%= request.getAttribute("erro") %></div>
        <% } %>

        <form action="${pageContext.request.contextPath}/ProductServlet" method="POST">
            <input type="hidden" name="acao" value="salvar">

            <div class="form-row">
<div class="form-group">
    <label for="barcode">Código de Barras *</label>
    
    <input type="text" id="barcode" name="barcode" list="lista-barcodes" placeholder="Ex: 7891010101010 ou escolha..." required autocomplete="off">
    
    <datalist id="lista-barcodes">
        <% 
            List<Product> produtos = (List<Product>) request.getAttribute("produtosCadastrados");
            if (produtos != null) {
                for (Product p : produtos) {
        %>
                    <option value="<%= p.getBarcode() %>"> <%= p.getName() %> (Estoque atual: <%= p.getCurrentStock() %>) </option>
        <% 
                }
            } 
        %>
    </datalist>
</div>
                <div class="form-group">
                    <label for="name">Nome do Produto *</label>
                    <input type="text" id="name" name="name" placeholder="Ex: Amoxicilina 500mg" required>
                </div>
            </div>

            <div class="form-group">
                <label for="description">Descrição</label>
                <input type="text" id="description" name="description" placeholder="Detalhes adicionais, dosagem, etc.">
            </div>

            <div class="form-row-3">
                <div class="form-group">
                    <label for="costPrice">Preço de Custo (R$) *</label>
                    <input type="text" id="costPrice" name="costPrice" placeholder="0.00" required>
                </div>
                <div class="form-group">
                    <label for="salePrice">Preço de Venda (R$) *</label>
                    <input type="text" id="salePrice" name="salePrice" placeholder="0.00" required>
                </div>
                <div class="form-group">
                    <label for="expiryDate">Data de Validade *</label>
                    <input type="date" id="expiryDate" name="expiryDate" required>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="currentStock">Quantidade Recebida *</label>
                    <input type="number" id="currentStock" name="currentStock" placeholder="Ex: 50" required style="width: 100%; padding: 10px 13px; border: 1px solid var(--borda); border-radius: var(--radius-sm);">
                </div>
                <div class="form-group">
                    <label for="minStock">Estoque Mínimo de Alerta *</label>
                    <input type="number" id="minStock" name="minStock" value="5" required style="width: 100%; padding: 10px 13px; border: 1px solid var(--borda); border-radius: var(--radius-sm);">
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="categoryId">ID da Categoria</label>
                    <input type="number" id="categoryId" name="categoryId" placeholder="ID (Opcional)" style="width: 100%; padding: 10px 13px; border: 1px solid var(--borda); border-radius: var(--radius-sm);">
                </div>
                <div class="form-group">
                    <label for="supplierId">ID do Fornecedor</label>
                    <input type="number" id="supplierId" name="supplierId" placeholder="ID (Opcional)" style="width: 100%; padding: 10px 13px; border: 1px solid var(--borda); border-radius: var(--radius-sm);">
                </div>
            </div>

            <div style="text-align: right; margin-top: 20px;">
                <button type="submit" class="btn btn-salvar">📦 Registrar Entrada</button>
            </div>
        </form>
    </div>

</body>
</html>