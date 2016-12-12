package com.algaworks.brewer.model;

import org.hibernate.validator.constraints.NotBlank;

public class Cerveja {
	@NotBlank
	private String sku;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}
}
