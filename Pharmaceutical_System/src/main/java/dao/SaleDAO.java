package dao;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;

import model.Sale;

public class SaleDAO {

	private EntityManager em;

	public SaleDAO(EntityManager em) {
		this.em = em;
	}

	public void salvar(Sale sale) {
		if (sale.getId() == null) {
			em.persist(sale);
		} else {
			em.merge(sale);
		}
	}

	public Sale buscarPorId(Integer id) {
		return em.find(Sale.class, id);
	}

	public List<Sale> listarTodos() {
		return em.createQuery("SELECT s FROM Sale s ORDER BY s.saleDate DESC", Sale.class).getResultList();
	}

	public Long totalVendas() {
		return em.createQuery("SELECT COUNT(s) FROM Sale s", Long.class).getSingleResult();
	}

	public BigDecimal faturamentoTotal() {
		BigDecimal total = em.createQuery("SELECT COALESCE(SUM(s.totalAmount), 0) FROM Sale s", BigDecimal.class)
				.getSingleResult();
		return total != null ? total : BigDecimal.ZERO;
	}
}