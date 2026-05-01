package test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import config.JPAUtil;
import model.Usuario;
// import model.Produto;
// import model.Venda;

public class TesteCaixa {

    public static void main(String[] args) {

        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            System.out.println("=== INICIANDO TESTE DE CAIXA ===");

            // 🔹 Buscar um usuário (exemplo: caixa)
            Usuario usuario = em.find(Usuario.class, 1);

            if (usuario == null) {
                throw new RuntimeException("Usuário não encontrado!");
            }

            System.out.println("Usuário: " + usuario.getEmail());

            /*
            // 🔹 Buscar produto
            Produto produto = em.find(Produto.class, 1);

            if (produto == null) {
                throw new RuntimeException("Produto não encontrado!");
            }

            System.out.println("Produto: " + produto.getNome());

            // 🔹 Criar venda
            Venda venda = new Venda();
            venda.setUsuario(usuario);
            venda.setTotal(produto.getPreco());
            venda.setData(new Date());

            // 🔹 Atualizar estoque
            produto.setQuantidadeAtual(produto.getQuantidadeAtual() - 1);

            em.persist(venda);
            em.merge(produto);

            System.out.println("Venda registrada com sucesso!");
            */

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