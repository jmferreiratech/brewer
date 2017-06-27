package com.algaworks.brewer.repository.helper.venda;

import com.algaworks.brewer.dto.VendaMes;
import com.algaworks.brewer.dto.VendaOrigem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.algaworks.brewer.model.Venda;
import com.algaworks.brewer.repository.filter.VendaFilter;

import java.math.BigDecimal;
import java.util.List;

public interface VendasQueries {

	public Page<Venda> filtrar(VendaFilter filtro, Pageable pageable);

    public Venda buscarComItens(Long codigo);

    public BigDecimal valorTotalNoAno();

    public BigDecimal valorTotalNoMes();

    public BigDecimal valorTicketMedioAno();

    public List<VendaMes> totalPorMes();

    public List<VendaOrigem> totalPorOrigem();
}
