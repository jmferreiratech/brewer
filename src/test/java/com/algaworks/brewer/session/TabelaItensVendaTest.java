package com.algaworks.brewer.session;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.session.TabelaItensVenda;

public class TabelaItensVendaTest {

	private TabelaItensVenda tabelaItensVenda;

	@Before
	public void setUp() {
		tabelaItensVenda = new TabelaItensVenda();
	}

	@Test
	public void deveCalcularValorTotalSemItens() throws Exception {
		assertEquals(BigDecimal.ZERO, tabelaItensVenda.getValorTotal());
	}

	@Test
	public void deveCalcularValorTotalComUmItem() throws Exception {
		String valor = "8.90";
		addCerveja(1, valor);

		assertEquals(new BigDecimal(valor), tabelaItensVenda.getValorTotal());
	}

	@Test
	public void deveCalcularValorTotalComVariosItens() throws Exception {
		addCerveja(1, "8.90");
		addCerveja(2, "4.99");

		assertEquals(new BigDecimal("18.88"), tabelaItensVenda.getValorTotal());

	}

	private void addCerveja(Integer quantidade, String valor) {
		Cerveja cerveja = new Cerveja();
		BigDecimal val = new BigDecimal(valor);
		cerveja.setValor(val);
		tabelaItensVenda.adicionarItem(cerveja, quantidade);
	}
}
