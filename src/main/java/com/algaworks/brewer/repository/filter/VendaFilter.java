package com.algaworks.brewer.repository.filter;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.algaworks.brewer.model.StatusVenda;

public class VendaFilter {

	private Long codigo;
	private StatusVenda status;
	private LocalDate dataDe;
	private LocalDate dataAte;
	private BigDecimal valorDe;
	private BigDecimal valorAte;
	private String nomeCliente;
	private String cpfOuCnpj;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public StatusVenda getStatus() {
		return status;
	}

	public void setStatus(StatusVenda status) {
		this.status = status;
	}

	public LocalDate getDataDe() {
		return dataDe;
	}

	public void setDataDe(LocalDate dataDe) {
		this.dataDe = dataDe;
	}

	public LocalDate getDataAte() {
		return dataAte;
	}

	public void setDataAte(LocalDate dataAte) {
		this.dataAte = dataAte;
	}

	public BigDecimal getValorDe() {
		return valorDe;
	}

	public void setValorDe(BigDecimal valorDe) {
		this.valorDe = valorDe;
	}

	public BigDecimal getValorAte() {
		return valorAte;
	}

	public void setValorAte(BigDecimal valorAte) {
		this.valorAte = valorAte;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public String getCpfOuCnpj() {
		return cpfOuCnpj;
	}

	public void setCpfOuCnpj(String cpfOuCnpj) {
		this.cpfOuCnpj = cpfOuCnpj;
	}
}
