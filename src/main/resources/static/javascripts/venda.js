Brewer.Venda = (function() {

	function Venda(tabelaItens) {
		this.tabelaItens = tabelaItens;
		this.valorTotalBox = $('.js-valor-total-box');
		this.totalBoxContainer = $('.js-total-box-container');
		this.valorFreteInput = $('#valorFrete');
		this.valorDescontoInput = $('#valorDesconto');

		this.valorTotalItens = 0;
		this.valorFrete = 0;
		this.valorDesconto = 0;
	}

	Venda.prototype.iniciar = function() {
		this.tabelaItens.on('tabela-itens-atualizado', onTabelaItensAtualizada
				.bind(this));
		this.valorFreteInput.on('keyup', onValorFreteAlterado.bind(this));
		this.valorDescontoInput.on('keyup', onValorDescontoAlterado.bind(this));

		this.tabelaItens.on('tabela-itens-atualizado', onValoresAlterados
				.bind(this));
		this.valorFreteInput.on('keyup', onValoresAlterados.bind(this));
		this.valorDescontoInput.on('keyup', onValoresAlterados.bind(this));
	}

	function onTabelaItensAtualizada(evento, valorTotalItens) {
		this.valorTotalItens = valorTotalItens == null ? 0
				: numeral(valorTotalItens);
	}

	function onValorFreteAlterado(evento) {
		this.valorFrete = Brewer.recuperarValor($(evento.target).val());
	}

	function onValorDescontoAlterado(evento) {
		this.valorDesconto = Brewer.recuperarValor($(evento.target).val());
	}

	function onValoresAlterados() {
		var valorTotal = this.valorTotalItens + this.valorFrete
				- this.valorDesconto;
		this.valorTotalBox.html(Brewer.formatarMoeda(valorTotal));
		this.totalBoxContainer.toggleClass('text-danger', valorTotal < 0);
	}

	return Venda;
}());

$(function() {
	var autocomplete = new Brewer.Autocomplete();
	autocomplete.iniciar();

	var tabelaItens = new Brewer.TabelaItens(autocomplete);
	tabelaItens.inicar();

	var venda = new Brewer.Venda(tabelaItens);
	venda.iniciar();
});
