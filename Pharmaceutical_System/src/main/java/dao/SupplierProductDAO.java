package dao;

import javax.persistence.EntityManager;
import model.SupplierProduct;
import model.SupplierProductId;
import java.util.List;

public class SupplierProductDAO {

    private EntityManager em;

    public SupplierProductDAO(EntityManager em) {
        this.em = em;
    }

    public void salvar(SupplierProduct rel) {
        em.persist(rel);
    }

    public void remover(SupplierProductId id) {
        SupplierProduct rel = em.find(SupplierProduct.class, id);
        if (rel != null) em.remove(rel);
    }

    public List<SupplierProduct> listarPorProduto(Integer productId) {
        return em.createQuery("SELECT sp FROM SupplierProduct sp WHERE sp.productId = :pid", SupplierProduct.class)
                 .setParameter("pid", productId)
                 .getResultList();
    }

    public List<SupplierProduct> listarPorFornecedor(Integer supplierId) {
        return em.createQuery("SELECT sp FROM SupplierProduct sp WHERE sp.supplierId = :sid", SupplierProduct.class)
                 .setParameter("sid", supplierId)
                 .getResultList();
    }

    public boolean exists(Integer productId, Integer supplierId) {
        Long count = em.createQuery("SELECT COUNT(sp) FROM SupplierProduct sp WHERE sp.productId = :pid AND sp.supplierId = :sid", Long.class)
                       .setParameter("pid", productId)
                       .setParameter("sid", supplierId)
                       .getSingleResult();
        return count > 0;
    }
}