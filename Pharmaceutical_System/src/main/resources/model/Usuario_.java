package model;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2026-05-05T15:55:00.771-0300")
@StaticMetamodel(Usuario.class)
public class Usuario_ {
	public static volatile SingularAttribute<Usuario, Integer> id;
	public static volatile SingularAttribute<Usuario, String> nome;
	public static volatile SingularAttribute<Usuario, String> email;
	public static volatile SingularAttribute<Usuario, String> login;
	public static volatile SingularAttribute<Usuario, String> senhaHash;
	public static volatile SingularAttribute<Usuario, Perfil> perfil;
	public static volatile SingularAttribute<Usuario, Boolean> ativo;
	public static volatile SingularAttribute<Usuario, String> tokenRecuperacao;
	public static volatile SingularAttribute<Usuario, LocalDateTime> tokenExpiracao;
	public static volatile SingularAttribute<Usuario, Integer> tentativasFalhas;
}
