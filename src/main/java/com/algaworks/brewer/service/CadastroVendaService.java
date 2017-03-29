package com.algaworks.brewer.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.model.ItemVenda;
import com.algaworks.brewer.model.Venda;
import com.algaworks.brewer.repository.Vendas;

@Service
public class CadastroVendaService {

	@Autowired
	private Vendas vendas;

	@Transactional
	public void salvar(Venda venda) {
		if (venda.isNova()) {
			venda.setDataCriacao(LocalDateTime.now());
		}
		venda.setValorTotal(valorTotal(venda));
		if (null != venda.getDataEntrega()) {
			venda.setDataHoraEntrega(LocalDateTime.of(venda.getDataEntrega(), venda.getHoraEntrega()));
		}
		vendas.save(venda);
	}

	private BigDecimal valorTotal(Venda venda) {
		BigDecimal valorTotalItens = venda.getItens()
				.stream().map(ItemVenda::getValorTotal)
				.reduce(BigDecimal::add).get();
		BigDecimal valorTotal = valorTotalItens
				.add(Optional.ofNullable(venda.getValorFrete()).orElse(BigDecimal.ZERO))
				.subtract(Optional.ofNullable(venda.getValorDesconto()).orElse(BigDecimal.ZERO));
		return valorTotal;
	}
}
