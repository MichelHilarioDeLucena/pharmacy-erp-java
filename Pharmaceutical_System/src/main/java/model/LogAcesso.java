package model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "log_acessos")
public class LogAcesso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id", nullable = true)
	private Usuario usuario;

	private String acao;
	private String ip;
	private String resultado;

	@Column(name = "data_hora")
	private LocalDateTime dataHora;

	@Column(columnDefinition = "TEXT")
	private String detalhes;

	public LogAcesso() {
		// [CORREÇÃO] Timestamp definido no construtor, não na declaração do campo.
		// Garante que o valor reflete o momento exato da criação do registro.
		this.dataHora = LocalDateTime.now();
	}

	public LogAcesso(Usuario usuario, String acao, String ip, String resultado, String detalhes) {
		this(); // chama o construtor vazio para inicializar dataHora
		this.usuario = usuario;
		this.acao = acao;
		this.ip = ip;
		this.resultado = resultado;
		this.detalhes = detalhes;
	}

	// Getters e Setters
	public Integer getId() {
		return id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario u) {
		this.usuario = u;
	}

	public String getAcao() {
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String r) {
		this.resultado = r;
	}

	public LocalDateTime getDataHora() {
		return dataHora;
	}
}