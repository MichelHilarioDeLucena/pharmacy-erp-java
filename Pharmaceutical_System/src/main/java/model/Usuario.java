package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity // Diz que esta classe é uma tabela no banco
@Table(name = "usuarios") // Nome exato da tabela no MySQL
public class Usuario {

	@Id // Diz que este é o Primary Key
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Diz que é AUTO_INCREMENT
	private Integer id;

	@Column(unique = true, nullable = false)
	private String login;

	@Column(name = "senha_hash", nullable = false)
	private String senhaHash;

	@Column(nullable = false)
	private String perfil; // ADMIN, FUNCIONARIO, etc.

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenhaHash() {
		return senhaHash;
	}

	public void setSenhaHash(String senhaHash) {
		this.senhaHash = senhaHash;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

	// Aqui entram os Getters e Setters vazios (para o Java conseguir ler e gravar)
}