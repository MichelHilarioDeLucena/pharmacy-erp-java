package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "vendas")
@NamedQueries({
		// Suas queries originais em JPQL continuam aqui perfeitas
		@NamedQuery(name = "Sale.revenueByPayment", query = "SELECT s.paymentMethod, SUM(s.totalAmount) FROM Sale s GROUP BY s.paymentMethod"),
		@NamedQuery(name = "Sale.totalRevenue", query = "SELECT COALESCE(SUM(s.totalAmount), 0) FROM Sale s"),
		@NamedQuery(name = "Sale.totalSales", query = "SELECT COUNT(s) FROM Sale s") })
@NamedNativeQueries({
		// Novas queries em SQL Nativo para o MySQL 8 (Adeus erros de validação!)
		@NamedNativeQuery(name = "Sale.revenueByDay", query = "SELECT YEAR(data_venda), MONTH(data_venda), DAY(data_venda), SUM(valor_total) "
				+ "FROM vendas " + "GROUP BY YEAR(data_venda), MONTH(data_venda), DAY(data_venda) "
				+ "ORDER BY YEAR(data_venda) DESC, MONTH(data_venda) DESC, DAY(data_venda) DESC"),

		@NamedNativeQuery(name = "Sale.revenueByMonth", query = "SELECT YEAR(data_venda), MONTH(data_venda), SUM(valor_total) "
				+ "FROM vendas " + "GROUP BY YEAR(data_venda), MONTH(data_venda) "
				+ "ORDER BY YEAR(data_venda) DESC, MONTH(data_venda) DESC"),

		@NamedNativeQuery(name = "Sale.revenueByYear", query = "SELECT YEAR(data_venda), SUM(valor_total) "
				+ "FROM vendas " + "GROUP BY YEAR(data_venda) " + "ORDER BY YEAR(data_venda) DESC") })

public class Sale {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario operator; // Mantive 'Usuario' como tipo caso você ainda não tenha traduzido essa classe

	@Column(name = "cliente_id")
	private Integer clientId;

	@Column(name = "data_venda")
	private LocalDateTime saleDate = LocalDateTime.now();

	@Column(name = "valor_total", nullable = false)
	private BigDecimal totalAmount = BigDecimal.ZERO;

	@Column(name = "forma_pagamento", nullable = false)
	private String paymentMethod;

	@Column(name = "status_pagamento")
	private String paymentStatus = "CONCLUIDO";

	@OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
	private List<SaleItem> items = new ArrayList<>();

	public void addItem(SaleItem item) {
		items.add(item);
		item.setSale(this); // Amarra o item a esta venda
		this.totalAmount = this.totalAmount.add(item.getSubtotal());
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Usuario getOperator() {
		return operator;
	}

	public void setOperator(Usuario operator) {
		this.operator = operator;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public LocalDateTime getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(LocalDateTime saleDate) {
		this.saleDate = saleDate;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public List<SaleItem> getItems() {
		return items;
	}

	public void setItems(List<SaleItem> items) {
		this.items = items;
	}

	// GERE OS GETTERS E SETTERS (Alt + Shift + S no Eclipse)
}