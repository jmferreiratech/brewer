$(function() {
	var decimal = $('.js-decimal');
	decimal.maskMoney({
		decimal : ',',
		thousands : '.',
		allowZero : true
	});

	var plain = $('.js-plain');
	plain.maskMoney({
		precision : 0,
		thousands : '.',
		allowZero : true
	});
});
