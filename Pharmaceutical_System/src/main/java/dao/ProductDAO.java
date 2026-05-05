package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import model.Product;

public class ProductDAO {

    private EntityManager em;

    public ProductDAO(EntityManager em) {
        this.em = em;
    }

    public void salvar(Product product) {
        if (product.getId() == null) {
            em.persist(product);
        } else {
            em.merge(product);
        }
    }

    public void deletar(Product product) {
        em.remove(em.contains(product) ? product : em.merge(product));
    }

    public Product buscarPorId(Long id) {
        return em.find(Product.class, id);
    }

    public List<Product> listarTodos() {
        return em.createQuery(
            "SELECT p FROM Product p ORDER BY p.name", Product.class)
            .getResultList();
    }

    public List<Product> listarAtivos() {
        return em.createQuery(
            "SELECT p FROM Product p WHERE p.active = true ORDER BY p.name", Product.class)
            .getResultList();
    }

    public List<Product> listarEstoqueBaixo() {
        return em.createQuery(
            "SELECT p FROM Product p WHERE p.active = true AND p.currentStock <= p.minStock ORDER BY p.name", Product.class)
            .getResultList();
    }

    public Product buscarPorBarcode(String barcode) {
        try {
            return em.createQuery(
                "SELECT p FROM Product p WHERE p.barcode = :barcode", Product.class)
                .setParameter("barcode", barcode)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}