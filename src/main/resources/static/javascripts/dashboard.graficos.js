var Brewer = Brewer || {};

Brewer.GraficoVendaPorMes = (function() {

    function GraficoVendaPorMes() {
        this.ctx = $('#graficoVendasPorMes')[0].getContext('2d');
    }

    GraficoVendaPorMes.prototype.iniciar = function() {
        $.ajax({
            url: 'vendas/totalPorMes',
            method: 'GET',
            success: onDadosRecebidos.bind(this),
        })
    };

    function onDadosRecebidos(dados) {
        var meses = dados.map(function (obj) {
            return obj.mes;
        });
        var valores = dados.map(function (obj) {
            return obj.total;
        });

        var graficoVendasPorMes = new Chart(this.ctx, {
            type: 'line',
            data: {
                labels: meses,
                datasets: [{
                    label: 'Vendas por Mês',
                    backgroundColor: 'rgba(26,179,148,0.5)',
                    pointBorderColor: 'rgba(26,179,148,1)',
                    pointBackgroundColor: '#fff',
                    data: valores,
                }],
            },
        });
    }
    return GraficoVendaPorMes;

}());

Brewer.GraficoVendaPorOrigem = (function() {

    function GraficoVendaPorOrigem() {
        this.ctx = $('#graficoVendasPorOrigem')[0].getContext('2d');
    }

    GraficoVendaPorOrigem.prototype.iniciar = function() {
        $.ajax({
            url: 'vendas/totalPorOrigem',
            method: 'GET',
            success: onDadosRecebidos.bind(this),
        })
    };

    function onDadosRecebidos(dados) {
        var meses = dados.map(function (obj) {
            return obj.mes;
        });
        var cervejasNacionais = dados.map(function (obj) {
            return obj.totalNacional;
        });
        var cervejasInternacionais = dados.map(function (obj) {
            return obj.totalInternacional;
        });

        var graficoVendasPorOrigem = new Chart(this.ctx, {
            type: 'bar',
            data: {
                labels: meses,
                datasets: [{
                    label: 'Nacional',
                    backgroundColor: 'rgba(220,220,220,0.5)',
                    data: cervejasNacionais,
                },
                {
                    label: 'Iternacional',
                    backgroundColor: 'rgba(26,179,148,0.5)',
                    data: cervejasInternacionais,
                }],
            },
        });
    }

    return GraficoVendaPorOrigem;
}());

$(function() {
    var graficoVendaPorMes = new Brewer.GraficoVendaPorMes();
    graficoVendaPorMes.iniciar();

    var graficoVendaPorOrigem = new Brewer.GraficoVendaPorOrigem();
    graficoVendaPorOrigem.iniciar();
});
