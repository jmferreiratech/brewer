package com.algaworks.brewer.repository.helper.venda;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.algaworks.brewer.model.StatusVenda;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.algaworks.brewer.model.Venda;
import com.algaworks.brewer.repository.filter.VendaFilter;
import com.algaworks.brewer.repository.paginacao.PaginacaoUtil;

public class VendasImpl implements VendasQueries {

	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private PaginacaoUtil paginacaoUtil;

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public Page<Venda> filtrar(VendaFilter filtro, Pageable pageable) {
		Criteria criteria = criteriaFromFilter(filtro);
		paginacaoUtil.preparar(criteria, pageable);
		List<Venda> vendasFiltradas = criteria.list();
		return new PageImpl<>(vendasFiltradas, pageable, total(filtro));
	}

	@Transactional(readOnly = true)
	@Override
	public Venda buscarComItens(Long codigo) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Venda.class);
		criteria.createAlias("itens", "i", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("codigo", codigo));
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return (Venda) criteria.uniqueResult();

	}

	@Override
	public BigDecimal valorTotalNoAno() {
		Optional<BigDecimal> optional = Optional.ofNullable(
				manager.createQuery("select sum(valorTotal) from Venda where year(dataCriacao) = :ano and status = :status", BigDecimal.class)
						.setParameter("ano", Year.now().getValue())
						.setParameter("status", StatusVenda.EMITIDA)
						.getSingleResult());
		return optional.orElse(BigDecimal.ZERO);
	}

	@Override
	public BigDecimal valorTotalNoMes() {
		Optional<BigDecimal> optional = Optional.ofNullable(
				manager.createQuery("select sum(valorTotal) from Venda where month(dataCriacao) = :mes and status = :status", BigDecimal.class)
						.setParameter("mes", MonthDay.now().getMonthValue())
						.setParameter("status", StatusVenda.EMITIDA)
						.getSingleResult());
		return optional.orElse(BigDecimal.ZERO);
	}

	@Override
	public BigDecimal valorTicketMedioAno() {
		Optional<BigDecimal> optional = Optional.ofNullable(
				manager.createQuery("select sum(valorTotal)/count(*) from Venda where year(dataCriacao) = :ano and status = :status", BigDecimal.class)
						.setParameter("ano", Year.now().getValue())
						.setParameter("status", StatusVenda.EMITIDA)
						.getSingleResult());
		return optional.orElse(BigDecimal.ZERO);
	}

	private Long total(VendaFilter filtro) {
		Criteria criteria = criteriaFromFilter(filtro);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	private Criteria criteriaFromFilter(VendaFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Venda.class);
		criteria.createAlias("cliente", "c");

		if (null != filtro.getCodigo()) {
			criteria.add(Restrictions.eq("codigo", filtro.getCodigo()));
		}
		if (null != filtro.getStatus()) {
			criteria.add(Restrictions.eq("status", filtro.getStatus()));
		}
		if (null != filtro.getDataDe()) {
			criteria.add(Restrictions.ge("dataCriacao", LocalDateTime.of(filtro.getDataDe(), LocalTime.MIN)));
		}
		if (null != filtro.getDataAte()) {
			criteria.add(Restrictions.le("dataCriacao", LocalDateTime.of(filtro.getDataAte(), LocalTime.MAX)));
		}
		if (null != filtro.getValorDe()) {
			criteria.add(Restrictions.ge("valorTotal", filtro.getValorDe()));
		}
		if (null != filtro.getValorAte()) {
			criteria.add(Restrictions.le("valorTotal", filtro.getValorAte()));
		}
		if (!StringUtils.isEmpty(filtro.getNomeCliente())) {
			criteria.add(Restrictions.ilike("c.nome", filtro.getNomeCliente(), MatchMode.ANYWHERE));
		}
		if (!StringUtils.isEmpty(filtro.getCpfOuCnpj())) {
			criteria.add(Restrictions.ilike("c.cpfOuCnpj", filtro.getCpfOuCnpj(), MatchMode.ANYWHERE));
		}
		return criteria;
	}
}
