package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import model.Usuario;

public class UsuarioDAO {

	private EntityManager em;

	public UsuarioDAO(EntityManager em) {
		this.em = em;
	}

	// Busca usuário para o login
	public Usuario buscarPorLogin(String login) {
		try {
			TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u WHERE u.login = :login", Usuario.class);
			query.setParameter("login", login);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null; // Mais fácil de tratar no Servlet que uma Exception
		}
	}

	// Salva (INSERT) ou Atualiza (UPDATE)
	public void salvar(Usuario usuario) {
		if (usuario.getId() == null) {
			em.persist(usuario);
		} else {
			em.merge(usuario); // [CORREÇÃO 2] Merge explícito garante que bloqueio e contador sejam gravados
		}
	}

	// Busca por ID (para edições ou alternar status)
	public Usuario buscarPorId(Integer id) {
		return em.find(Usuario.class, id);
	}

	// [NOVO] Lista todos os usuários ordenados por login — para listaUsuarios.jsp
	public List<Usuario> listarTodos() {
		return em.createQuery("SELECT u FROM Usuario u ORDER BY u.login", Usuario.class).getResultList();
	}
}