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

	public Usuario buscarPorLogin(String login) {
		try {
			TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u WHERE u.login = :login", Usuario.class);
			query.setParameter("login", login);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null; 
		}
	}

	public void salvar(Usuario usuario) {
		if (usuario.getId() == null) {
			em.persist(usuario);
		} else {
			em.merge(usuario); 
		}
	}

	public Usuario buscarPorId(Integer id) {
		return em.find(Usuario.class, id);
	}

	public List<Usuario> listarTodos() {
		return em.createQuery("SELECT u FROM Usuario u ORDER BY u.login", Usuario.class).getResultList();
	}

	public Usuario buscarPorToken(String token) {
		try {
			return em.createQuery("SELECT u FROM Usuario u WHERE u.tokenRecuperacao = :token", Usuario.class)
					.setParameter("token", token).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Usuario buscarPorEmail(String email) {
		try {
			TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class);
			query.setParameter("email", email);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null; 
		}
	}
}