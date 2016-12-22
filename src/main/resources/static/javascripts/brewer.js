var Brewer = Brewer || {};

Brewer.MaskMoney = (function() {

	function MaskMoney() {
		this.decimal = $('.js-decimal');
		this.plain = $('.js-plain');

	}

	MaskMoney.prototype.enable = function() {
		this.decimal.maskMoney({
			decimal : ',',
			thousands : '.',
			allowZero : true
		});
		this.plain.maskMoney({
			precision : 0,
			thousands : '.',
			allowZero : true
		});

	}

	return MaskMoney;
}());

$(function() {
	var maskMoney = new Brewer.MaskMoney();
	maskMoney.enable();
});
