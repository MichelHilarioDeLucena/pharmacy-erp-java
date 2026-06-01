document.addEventListener("DOMContentLoaded", function () {
    var data = window.dashboardData || {};

    function getLabels(section) {
        if (data[section] && data[section].labels) {
            return data[section].labels;
        }
        return [];
    }

    function getValues(section) {
        if (data[section] && data[section].values) {
            return data[section].values;
        }
        return [];
    }

    function createChart(id, type, labels, values, label) {
        var ctx = document.getElementById(id);
        if (!ctx) return;

        new Chart(ctx, {
            type: type,
            data: {
                labels: labels,
                datasets: [{
                    label: label,
                    data: values,
                    borderWidth: 2
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: type !== 'bar'
                    }
                }
            }
        });
    }

    createChart("paymentChart", "pie", getLabels("payment"), getValues("payment"), "Pagamento");
    createChart("productsChart", "bar", getLabels("products"), getValues("products"), "Produtos");
    createChart("dayChart", "line", getLabels("day"), getValues("day"), "Venda por Dia");
    createChart(
    "weekChart",
    "bar",
    data.week ? data.week.labels : [],
    data.week ? data.week.values : [],
    "Venda por Semana"
);
    createChart("monthChart", "bar", getLabels("month"), getValues("month"), "Venda por Mês");
    createChart("yearChart", "line", getLabels("year"), getValues("year"), "Venda por Ano");
});