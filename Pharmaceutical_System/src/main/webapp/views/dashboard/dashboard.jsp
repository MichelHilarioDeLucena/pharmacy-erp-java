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
    .chart-box { background: white; padding: 20px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); width: 45%; min-width: 400px; margin-bottom: 20px;}
    h2 { text-align: center; color: #333; margin-bottom: 30px; }
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

        <div class="chart-box">
            <canvas id="dayChart"></canvas>
        </div>

        <div class="chart-box">
            <canvas id="monthChart"></canvas>
        </div>

        <div class="chart-box" style="width: 92%;"> <canvas id="yearChart"></canvas>
        </div>
    </div>

    <%
        // Recuperando os dados originais do Servlet
        List<Object[]> revenueByPayment = (List<Object[]>) request.getAttribute("revenueByPayment");
        List<Object[]> topProducts = (List<Object[]>) request.getAttribute("topProducts");

        // Recuperando os novos dados do Servlet
        List<Object[]> revenueByDay = (List<Object[]>) request.getAttribute("revenueByDay");
        List<Object[]> revenueByMonth = (List<Object[]>) request.getAttribute("revenueByMonth");
        List<Object[]> revenueByYear = (List<Object[]>) request.getAttribute("revenueByYear");

        // --- BUILDERS ORIGINAIS ---
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

        // --- NOVOS BUILDERS (Lendo de trás pra frente para a linha do tempo ficar da Esquerda pra Direita) ---
        
        // Faturamento por Dia (Índices: 0=Ano, 1=Mês, 2=Dia, 3=Valor)
        StringBuilder labelsDay = new StringBuilder();
        StringBuilder dataDay = new StringBuilder();
        if (revenueByDay != null) {
            for (int i = revenueByDay.size() - 1; i >= 0; i--) {
                Object[] row = revenueByDay.get(i);
                labelsDay.append("'").append(row[2]).append("/").append(row[1]).append("',"); // Formato Dia/Mês
                dataDay.append(row[3]).append(",");
            }
        }

        // Faturamento por Mês (Índices: 0=Ano, 1=Mês, 2=Valor)
        StringBuilder labelsMonth = new StringBuilder();
        StringBuilder dataMonth = new StringBuilder();
        if (revenueByMonth != null) {
            for (int i = revenueByMonth.size() - 1; i >= 0; i--) {
                Object[] row = revenueByMonth.get(i);
                labelsMonth.append("'").append(row[1]).append("/").append(row[0]).append("',"); // Formato Mês/Ano
                dataMonth.append(row[2]).append(",");
            }
        }

        // Faturamento por Ano (Índices: 0=Ano, 1=Valor)
        StringBuilder labelsYear = new StringBuilder();
        StringBuilder dataYear = new StringBuilder();
        if (revenueByYear != null) {
            for (int i = revenueByYear.size() - 1; i >= 0; i--) {
                Object[] row = revenueByYear.get(i);
                labelsYear.append("'").append(row[0]).append("',"); // Formato Ano
                dataYear.append(row[1]).append(",");
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

        // === GRÁFICO 3: Faturamento por Dia (Linha/Line) ===
        const ctxDay = document.getElementById('dayChart').getContext('2d');
        new Chart(ctxDay, {
            type: 'line',
            data: {
                labels: [<%= labelsDay.toString() %>],
                datasets: [{
                    label: 'Faturamento Diário (R$)',
                    data: [<%= dataDay.toString() %>],
                    borderColor: '#dc3545',
                    backgroundColor: 'rgba(220, 53, 69, 0.2)',
                    fill: true,
                    tension: 0.3
                }]
            },
            options: {
                responsive: true,
                plugins: { title: { display: true, text: 'Faturamento Recente (Por Dia)', font: {size: 18} } },
                scales: { y: { beginAtZero: true } }
            }
        });

        // === GRÁFICO 4: Faturamento por Mês (Barra/Bar) ===
        const ctxMonth = document.getElementById('monthChart').getContext('2d');
        new Chart(ctxMonth, {
            type: 'bar',
            data: {
                labels: [<%= labelsMonth.toString() %>],
                datasets: [{
                    label: 'Faturamento Mensal (R$)',
                    data: [<%= dataMonth.toString() %>],
                    backgroundColor: '#6f42c1'
                }]
            },
            options: {
                responsive: true,
                plugins: { title: { display: true, text: 'Faturamento Histórico (Por Mês)', font: {size: 18} } },
                scales: { y: { beginAtZero: true } }
            }
        });

        // === GRÁFICO 5: Faturamento por Ano (Barra/Bar) ===
        const ctxYear = document.getElementById('yearChart').getContext('2d');
        new Chart(ctxYear, {
            type: 'bar',
            data: {
                labels: [<%= labelsYear.toString() %>],
                datasets: [{
                    label: 'Faturamento Anual (R$)',
                    data: [<%= dataYear.toString() %>],
                    backgroundColor: '#28a745'
                }]
            },
            options: {
                responsive: true,
                plugins: { title: { display: true, text: 'Faturamento Geral (Por Ano)', font: {size: 18} } },
                scales: { y: { beginAtZero: true } }
            }
        });
    </script>
</body>
</html>