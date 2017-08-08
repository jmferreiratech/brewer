package com.algaworks.brewer.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import com.algaworks.brewer.validation.SKU;

@Entity
@Table(name = "cerveja")
public class Cerveja implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;

	@SKU
	@NotBlank
	private String sku;

	@NotBlank
	private String nome;

	@NotBlank
	@Size(max = 50, message = "O tamanho da descrição deve estar entre 1 e {max}")
	private String descricao;

	@NotNull(message = "Valor é obrigatório")
	@DecimalMin(value = "0.01")
	@DecimalMax(value = "9999999.99", message = "Valor não pode ser superior a R$ 9.999.999,99")
	private BigDecimal valor;

	@NotNull(message = "Teor Alcóolico é obrigatório")
	@DecimalMax(value = "100.00", message = "Teor Alcóolico não pode ser superior a {value}%")
	@Column(name = "teor_alcoolico")
	private BigDecimal teorAlcoolico;

	@NotNull(message = "Comissão é obrigatória")
	@DecimalMax(value = "100.00", message = "Comissão não pode ser superior a {value}%")
	private BigDecimal comissao;

	@NotNull(message = "Quantidade em estoque é obrigatória")
	@Max(value = 9999, message = "Quantidade em estoque não pode ser superior a {value}")
	@Column(name = "quantidade_estoque")
	private Integer quantidadeEstoque;

	@NotNull(message = "Origem é obrigatória")
	@Enumerated(EnumType.STRING)
	private Origem origem;

	@NotNull(message = "Sabor é obrigatório")
	@Enumerated(EnumType.STRING)
	private Sabor sabor;

	@NotNull(message = "Estilo é obrigatório")
	@ManyToOne
	@JoinColumn(name = "codigo_estilo")
	private Estilo estilo;

	private String foto;

	@Column(name = "content_type")
	private String contentType;

	@Transient
    private boolean novaFoto;

	@PrePersist
	@PreUpdate
	private void prePersistUpdate() {
		sku = sku.toUpperCase();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getTeorAlcoolico() {
		return teorAlcoolico;
	}

	public void setTeorAlcoolico(BigDecimal teorAlcoolico) {
		this.teorAlcoolico = teorAlcoolico;
	}

	public BigDecimal getComissao() {
		return comissao;
	}

	public void setComissao(BigDecimal comissao) {
		this.comissao = comissao;
	}

	public Integer getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(Integer quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}

	public Origem getOrigem() {
		return origem;
	}

	public void setOrigem(Origem origem) {
		this.origem = origem;
	}

	public Sabor getSabor() {
		return sabor;
	}

	public void setSabor(Sabor sabor) {
		this.sabor = sabor;
	}

	public Estilo getEstilo() {
		return estilo;
	}

	public void setEstilo(Estilo estilo) {
		this.estilo = estilo;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getContentType() {
		return StringUtils.isEmpty(contentType) ? MediaType.IMAGE_PNG_VALUE : contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getFotoOuMock() {
		return StringUtils.isEmpty(foto) ? "cerveja-mock.png" : foto;
	}

	public boolean isNova() {
		return null == getCodigo();
	}

    public boolean isNovaFoto() {
        return novaFoto;
    }

    public void setNovaFoto(boolean novaFoto) {
        this.novaFoto = novaFoto;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cerveja other = (Cerveja) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}
}
