var Brewer = Brewer || {};

Brewer.MascaraCpfCnpj = (function() {

	function MascaraCpfCnpj() {
		this.radioTipoPessoa = $('.js-radio-tipo-pessoa');
		this.labelCpfCnpj = $('[for=cpfOuCnpj]');
		this.inputCpfCnpj = $('#cpfOuCnpj');
	}

	MascaraCpfCnpj.prototype.iniciar = function() {
		this.radioTipoPessoa.on('change', onTipoPessoaAlterado.bind(this));
	}

	function onTipoPessoaAlterado(evento) {
		var tipoPessoaSelecionado = $(evento.currentTarget);
		this.labelCpfCnpj.text(tipoPessoaSelecionado.data('documento'));
		this.inputCpfCnpj.mask(tipoPessoaSelecionado.data('mascara'));
		this.inputCpfCnpj.val('');
		this.inputCpfCnpj.removeAttr('disabled');
	}
	return MascaraCpfCnpj;
}());

$(function() {
	var mascaraCpfCnpj = new Brewer.MascaraCpfCnpj();
	mascaraCpfCnpj.iniciar();
});