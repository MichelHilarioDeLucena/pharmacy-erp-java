package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private String nome;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(unique = true, nullable = false)
	private String login;

	@Column(name = "senha_hash", nullable = false)
	private String senhaHash;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Perfil perfil;

	@Column(nullable = false)
	private boolean ativo = true;

	@Column(name = "token_recuperacao")
	private String tokenRecuperacao;

	@Column(name = "token_expiracao")
	private java.time.LocalDateTime tokenExpiracao;
	private int tentativasFalhas;

	public int getTentativasFalhas() {
		return tentativasFalhas;
	}

	public void setTentativasFalhas(int tentativasFalhas) {
		this.tentativasFalhas = tentativasFalhas;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public String getTokenRecuperacao() {
		return tokenRecuperacao;
	}

	public void setTokenRecuperacao(String token) {
		this.tokenRecuperacao = token;
	}

	public java.time.LocalDateTime getTokenExpiracao() {
		return tokenExpiracao;
	}

	public void setTokenExpiracao(java.time.LocalDateTime data) {
		this.tokenExpiracao = data;
	}
}