package br.gov.anvisa.portfolio.infrastructure.persistence.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidade de persistencia da tabela TB_USUARIO.
 *
 * <p>O historico de vigencias e mantido por trigger do banco (TB_HIST_USUARIO) e consultado
 * por JDBC, sem entidade JPA.
 */
@Entity
@Table(name = "TB_USUARIO")
@Getter
@Setter
public class UsuarioEntity {

    @Id
    @Column(name = "CO_USERNAME", length = 30)
    private String username;

    @Column(name = "DS_HOST", nullable = false, length = 100)
    private String host;

    @Column(name = "CO_TIPO_USUARIO", nullable = false)
    private Long tipoUsuarioId;

    @Column(name = "DT_INICIO_VIGENCIA", nullable = false)
    private LocalDate inicioVigencia;

    @Column(name = "DT_FIM_VIGENCIA")
    private LocalDate fimVigencia;

    @Column(name = "CO_USUARIO_BANCO_DADOS", nullable = false)
    private Long usuarioBancoDadosId;

    @Column(name = "CO_USERNAME_SEG", length = 30)
    private String usuarioSecundario;
}