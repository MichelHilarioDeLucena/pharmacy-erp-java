package model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "produtos")
@NamedQueries({
		@NamedQuery(name = "Product.findActiveWithStock", query = "SELECT p FROM Product p WHERE p.active = true AND p.currentStock > 0") })
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "codigo_barras", unique = true)
	private String barcode;

	@Column(name = "nome", nullable = false)
	private String name;

	@Column(name = "descricao", columnDefinition = "TEXT")
	private String description;

	@Column(name = "preco_custo", nullable = false)
	private BigDecimal costPrice;

	@Column(name = "preco_venda", nullable = false)
	private BigDecimal salePrice;

	@Column(name = "qtd_atual", nullable = false)
	private Integer currentStock;

	@Column(name = "qtd_minima", nullable = false)
	private Integer minStock;

	@Column(name = "categoria_id")
	private Integer categoryId;

	@Column(name = "fornecedor_id")
	private Integer supplierId;

	@Column(name = "ativo")
	private Boolean active = true;

	// --- GETTERS & SETTERS ---

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public Integer getCurrentStock() {
		return currentStock;
	}

	public void setCurrentStock(Integer currentStock) {
		this.currentStock = currentStock;
	}

	public Integer getMinStock() {
		return minStock;
	}

	public void setMinStock(Integer minStock) {
		this.minStock = minStock;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
}