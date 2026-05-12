package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import model.Supplier;

public class SupplierDAO {

    private EntityManager em;

    public SupplierDAO(EntityManager em) {
        this.em = em;
    }

    public void salvar(Supplier supplier) {
        if (supplier.getId() == null) {
            em.persist(supplier);
        } else {
            em.merge(supplier);
        }
    }

    public Supplier buscarPorId(Integer id) {
        return em.find(Supplier.class, id);
    }

    public List<Supplier> listarTodos() {
        return em.createQuery(
            "SELECT s FROM Supplier s ORDER BY s.companyName", Supplier.class)
            .getResultList();
    }

    public List<Supplier> listarAtivos() {
        return em.createQuery(
            "SELECT s FROM Supplier s WHERE s.active = true ORDER BY s.companyName", Supplier.class)
            .getResultList();
    }

    public Supplier buscarPorCnpj(String cnpj) {
        try {
            return em.createQuery(
                "SELECT s FROM Supplier s WHERE s.cnpj = :cnpj", Supplier.class)
                .setParameter("cnpj", cnpj)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}