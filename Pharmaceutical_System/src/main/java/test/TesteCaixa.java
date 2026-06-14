package test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import config.JPAUtil;
import model.Usuario;

public class TesteCaixa {

    public static void main(String[] args) {

        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            System.out.println("=== INICIANDO TESTE DE CAIXA ===");

            Usuario usuario = em.find(Usuario.class, 1);

            if (usuario == null) {
                throw new RuntimeException("Usuário não encontrado!");
            }

            System.out.println("Usuário: " + usuario.getEmail());

            tx.commit();
            System.out.println("=== TESTE FINALIZADO COM SUCESSO ===");

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}