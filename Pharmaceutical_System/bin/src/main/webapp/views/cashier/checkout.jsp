<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.SaleItem, model.Product, java.math.BigDecimal"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>POS Checkout - ERP</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style2.css">
</head>
<body>

    <div class="checkout-container">
        <h2>Point of Sale</h2>

        <% if ("true".equals(request.getParameter("success"))) { 
            String saleId = request.getParameter("saleId"); 
        %>
        <div class="alert-success">
            <b>Venda finalizada com sucesso e estoque atualizado!</b><br>
            <a href="${pageContext.request.contextPath}/ReceiptServlet?saleId=<%=saleId%>" class="btn-receipt">
               Baixar Cupom Fiscal (PDF)
            </a>
        </div>
        <% } %>

        <h3>Produtos Disponíveis (Clique para "Bipar")</h3>
        <div class="product-grid">
            <%
            List<Product> products = (List<Product>) request.getAttribute("productsList");
            if (products != null) {
                for (Product p : products) {
            %>
            <div class="product-card">
                <h4><%=p.getName()%></h4>
                <p class="price">R$ <%=p.getSalePrice()%></p>
                <p><small>Estoque: <%=p.getCurrentStock()%></small></p>
                <form action="${pageContext.request.contextPath}/CheckoutServlet" method="POST">
                    <input type="hidden" name="action" value="add"> 
                    <input type="hidden" name="productId" value="<%=p.getId()%>">
                    <button type="submit">Adicionar</button>
                </form>
            </div>
            <%
                }
            }
            %>
        </div>

        <h3>Carrinho</h3>
        <table class="cart-table">
            <thead>
                <tr>
                    <th>Produto</th>
                    <th>Qtd</th>
                    <th>Preço Unit.</th>
                    <th>Subtotal</th>
                </tr>
            </thead>
            <tbody>
                <%
                List<SaleItem> cart = (List<SaleItem>) session.getAttribute("cart");
                BigDecimal grandTotal = BigDecimal.ZERO;
                if (cart != null && !cart.isEmpty()) {
                    for (SaleItem item : cart) {
                        grandTotal = grandTotal.add(item.getSubtotal());
                %>
                <tr>
                    <td><%=item.getProductName()%></td>
                    <td class="center"><%=item.getQuantity()%></td>
                    <td class="right">R$ <%=item.getUnitPrice()%></td>
                    <td class="right"><strong>R$ <%=item.getSubtotal()%></strong></td>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td colspan="4" class="center empty-cart">Carrinho vazio</td>
                </tr>
                <% } %>
            </tbody>
        </table>
        
        <h2 class="total-label">Total: <span class="total-value">R$ <%=grandTotal%></span></h2>

        <fieldset class="payment-section">
            <legend>Pagamento</legend>
            <form id="checkoutForm" action="${pageContext.request.contextPath}/CheckoutServlet" method="POST" onsubmit="iniciarPagamento(event)">
                <input type="hidden" name="action" value="finish"> 
                
                <div class="form-group">
                    <label>Cliente (Opcional):</label> 
                    <select name="clientId">
                        <option value="">-- Consumidor Final (Não identificado) --</option>
                        <%
                        List<Object[]> clients = (List<Object[]>) request.getAttribute("clientsList");
                        if (clients != null) {
                            for (Object[] c : clients) {
                        %>
                        <option value="<%=c[0]%>"><%=c[1]%> (CPF: <%=c[2]%>)</option>
                        <%
                            }
                        }
                        %>
                    </select>
                </div>
                
                <div class="form-group">
                    <label>Forma de Pagamento:</label> 
                    <select name="paymentMethod" id="paymentMethod" required>
                        <option value="PIX">Pix</option>
                        <option value="CREDIT_CARD">Cartão de Crédito</option>
                        <option value="DEBIT_CARD">Cartão de Débito</option>
                        <option value="CASH">Dinheiro</option>
                    </select>
                </div>

                <button type="submit" class="btn-finalizar" <%=(cart == null || cart.isEmpty()) ? "disabled" : ""%>>
                    Finalizar Venda
                </button>
            </form>
        </fieldset>
    </div>

    <div id="modalPix" class="modal-overlay">
        <div class="modal-content">
            <h3>Pagamento via Pix</h3>
            <p>Escaneie o QR Code abaixo:</p>
            <div class="qr-code-fake">QR CODE FAKE</div>
            <p>Aguardando pagamento... (<span id="pixTimer">5</span>s)</p>
            <button type="button" class="btn-pagar btn-pular" onclick="confirmarPagamento()">Pular (Simular Pago)</button>
        </div>
    </div>

    <div id="modalCartao" class="modal-overlay">
        <div class="modal-content">
            <h3 id="tituloCartao">Pagamento via Cartão</h3>
            <p>Insira a senha do cliente:</p>
            <input type="password" id="senhaCartao" class="input-senha" placeholder="****" maxlength="4">
            <button type="button" class="btn-pagar" onclick="validarSenha()">Confirmar Senha</button>
            <button type="button" class="btn-pagar btn-pular" onclick="fecharModais()">Cancelar</button>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>