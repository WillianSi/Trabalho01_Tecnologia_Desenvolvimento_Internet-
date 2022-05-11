package model;

import java.util.Date;

public class Produto {
	private int id;
	private String name;
	private Date fabrica;
	private Date validade;
	private Company company;
	
	public Produto() {}
	
	public Produto(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getFabrica() {
		return fabrica;
	}

	public void setFabrica(Date fabrica) {
		this.fabrica = fabrica;
	}

	public Date getValidade() {
		return validade;
	}

	public void setValidade(Date validade) {
		this.validade = validade;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
}
