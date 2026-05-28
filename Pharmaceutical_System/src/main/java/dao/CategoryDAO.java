package dao;

import java.util.List;
import javax.persistence.EntityManager;
import model.Category;

public class CategoryDAO {

    private EntityManager em;

    public CategoryDAO(EntityManager em) {
        this.em = em;
    }

    public List<Category> listarTodos() {
        return em.createQuery(
            "SELECT c FROM Category c ORDER BY c.name", Category.class)
            .getResultList();
    }

    public Category buscarPorId(Integer id) {
        return em.find(Category.class, id);
    }
}