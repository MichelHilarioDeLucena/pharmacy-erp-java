<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Dashboard de Vendas - ERP</title>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<style>
    body { font-family: Arial, sans-serif; background: #f4f7f6; padding: 20px; }
    .dashboard-container { display: flex; gap: 20px; flex-wrap: wrap; justify-content: center; }
    .chart-box { background: white; padding: 20px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); width: 45%; min-width: 400px; }
    h2 { text-align: center; color: #333; }
</style>
</head>
<body>

    <h2>📊 Painel de Vendas</h2>

    <div class="dashboard-container">
        <div class="chart-box">
            <canvas id="paymentChart"></canvas>
        </div>

        <div class="chart-box">
            <canvas id="productsChart"></canvas>
        </div>
    </div>

    <%
        // Recuperando os dados do Servlet
        List<Object[]> revenueByPayment = (List<Object[]>) request.getAttribute("revenueByPayment");
        List<Object[]> topProducts = (List<Object[]>) request.getAttribute("topProducts");

        // Construindo strings no formato de array do JavaScript ["Pix", "Cartão", ...]
        StringBuilder labelsPayment = new StringBuilder();
        StringBuilder dataPayment = new StringBuilder();
        if (revenueByPayment != null) {
            for (Object[] row : revenueByPayment) {
                labelsPayment.append("'").append(row[0]).append("',");
                dataPayment.append(row[1]).append(",");
            }
        }

        StringBuilder labelsProducts = new StringBuilder();
        StringBuilder dataProducts = new StringBuilder();
        if (topProducts != null) {
            for (Object[] row : topProducts) {
                labelsProducts.append("'").append(row[0]).append("',");
                dataProducts.append(row[1]).append(",");
            }
        }
    %>

    <script>
        // === GRÁFICO 1: Faturamento por Pagamento (Pizza/Pie) ===
        const ctxPayment = document.getElementById('paymentChart').getContext('2d');
        new Chart(ctxPayment, {
            type: 'pie',
            data: {
                labels: [<%= labelsPayment.toString() %>],
                datasets: [{
                    label: 'Faturamento (R$)',
                    data: [<%= dataPayment.toString() %>],
                    backgroundColor: ['#007bff', '#28a745', '#ffc107', '#dc3545', '#6c757d']
                }]
            },
            options: {
                responsive: true,
                plugins: { title: { display: true, text: 'Faturamento por Método de Pagamento', font: {size: 18} } }
            }
        });

        // === GRÁFICO 2: Produtos Mais Vendidos (Barras/Bar) ===
        const ctxProducts = document.getElementById('productsChart').getContext('2d');
        new Chart(ctxProducts, {
            type: 'bar',
            data: {
                labels: [<%= labelsProducts.toString() %>],
                datasets: [{
                    label: 'Quantidade Vendida',
                    data: [<%= dataProducts.toString() %>],
                    backgroundColor: '#17a2b8'
                }]
            },
            options: {
                responsive: true,
                plugins: { title: { display: true, text: 'Top 5 Produtos Mais Vendidos', font: {size: 18} } },
                scales: { y: { beginAtZero: true } }
            }
        });
    </script>
</body>
</html>