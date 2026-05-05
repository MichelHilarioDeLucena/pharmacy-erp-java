package model;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2026-05-05T15:55:00.765-0300")
@StaticMetamodel(LogAcesso.class)
public class LogAcesso_ {
	public static volatile SingularAttribute<LogAcesso, Integer> id;
	public static volatile SingularAttribute<LogAcesso, Usuario> usuario;
	public static volatile SingularAttribute<LogAcesso, String> acao;
	public static volatile SingularAttribute<LogAcesso, String> ip;
	public static volatile SingularAttribute<LogAcesso, String> resultado;
	public static volatile SingularAttribute<LogAcesso, LocalDateTime> dataHora;
}
