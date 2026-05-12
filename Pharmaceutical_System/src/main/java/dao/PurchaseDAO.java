package dao;

import java.util.List;
import javax.persistence.EntityManager;
import model.Purchase;
import model.Supplier;

public class PurchaseDAO {

    private EntityManager em;

    public PurchaseDAO(EntityManager em) {
        this.em = em;
    }

    public void salvar(Purchase purchase) {
        if (purchase.getId() == null) {
            em.persist(purchase);
        } else {
            em.merge(purchase);
        }
    }

    public Purchase buscarPorId(Integer id) {
        return em.find(Purchase.class, id);
    }

    public List<Purchase> listarTodos() {
        return em.createQuery(
            "SELECT p FROM Purchase p ORDER BY p.purchaseDate DESC", Purchase.class)
            .getResultList();
    }

    public List<Purchase> listarPorFornecedor(Supplier supplier) {
        return em.createQuery(
            "SELECT p FROM Purchase p WHERE p.supplier = :supplier ORDER BY p.purchaseDate DESC", Purchase.class)
            .setParameter("supplier", supplier)
            .getResultList();
    }

    public List<Purchase> listarPorStatus(String status) {
        return em.createQuery(
            "SELECT p FROM Purchase p WHERE p.orderStatus = :status ORDER BY p.purchaseDate DESC", Purchase.class)
            .setParameter("status", status)
            .getResultList();
    }
}