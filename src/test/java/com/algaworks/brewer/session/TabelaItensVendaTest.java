package com.algaworks.brewer.session;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.algaworks.brewer.model.Cerveja;

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
		addCerveja(1, valor, 1L);

		assertEquals(new BigDecimal(valor), tabelaItensVenda.getValorTotal());
	}

	@Test
	public void deveCalcularValorTotalComVariosItens() throws Exception {
		addCerveja(1, "8.90", 1L);
		addCerveja(2, "4.99", 2L);

		assertEquals(new BigDecimal("18.88"), tabelaItensVenda.getValorTotal());
	}

	@Test
	public void deveManterTamanhoListaParaMesmasCervejas() throws Exception {
		addCerveja(1, "4.50", 1L);
		addCerveja(1, "4.50", 1L);

		assertEquals(1, tabelaItensVenda.size());
	}

	@Test
	public void deveCalcularValorTotalComMesmasCervejas() throws Exception {
		addCerveja(1, "4.50", 1L);
		addCerveja(1, "4.50", 1L);

		assertEquals(new BigDecimal("9.00"), tabelaItensVenda.getValorTotal());
	}

	@Test
	public void deveAlterarQuantidadeDoItem() throws Exception {
		Cerveja c1 = novaCerveja("4.50", 1L);
		tabelaItensVenda.adicionarItem(c1, 1);
		tabelaItensVenda.alterarQuantidadeItens(c1, 3);

		assertEquals(new BigDecimal("13.50"), tabelaItensVenda.getValorTotal());
	}

	private void addCerveja(Integer quantidade, String valor, Long codigo) {
		Cerveja cerveja = novaCerveja(valor, codigo);
		tabelaItensVenda.adicionarItem(cerveja, quantidade);
	}

	private Cerveja novaCerveja(String valor, Long codigo) {
		Cerveja cerveja = new Cerveja();
		cerveja.setCodigo(codigo);
		BigDecimal val = new BigDecimal(valor);
		cerveja.setValor(val);
		return cerveja;
	}
}
