package com.shopme.common.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "brands")
public class Brand {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 45, nullable = false, unique = true)
	private String name;
	
	@Column(length = 128, nullable = false)
	private String logo;
	
	@ManyToMany
	@JoinTable(
			name = "brands_categories"
			, joinColumns = @JoinColumn(name = "brand_id")
			, inverseJoinColumns = @JoinColumn(name = "category_id")
			)
	private Set<Category> categories = new HashSet<>();
	
	public Brand() {
	}
	
	
	
	
	public Brand(String name) {
		super();
		this.name = name;
	}
	
	




	public Brand(String name, String logo) {
		super();
		this.name = name;
		this.logo = logo;
	}




	public Brand(Integer id, String name, String logo) {
		super();
		this.id = id;
		this.name = name;
		this.logo = logo;
	}

	public Brand(Integer id, String name, String logo, Set<Category> categories) {
		super();
		this.id = id;
		this.name = name;
		this.logo = logo;
		this.categories = categories;
	}


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}


	public Set<Category> getCategories() {
		return categories;
	}


	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}


	@Override
	public String toString() {
		return "Brand [id=" + id + ", name=" + name + ", logo=" + logo + "]";
	}
	
	
	
	
	

}
