package br.gov.anvisa.portfolio.domain.usuario;

import java.time.LocalDate;

import br.gov.anvisa.portfolio.domain.shared.RegraDeNegocioException;
import br.gov.anvisa.portfolio.domain.shared.Textos;

/**
 * Agregado Usuario: vinculo entre um usuario interno e um usuario de banco de dados de consulta.
 */
public record Usuario(String username,
                      String host,
                      Long tipoUsuarioId,
                      LocalDate inicioVigencia,
                      LocalDate fimVigencia,
                      Long usuarioBancoDadosId,
                      String usuarioSecundario) {

    public Usuario {
        username = Textos.codigo(username, "Usuario", 30);
        host = Textos.obrigatorio(host, "Host", 100);
        if (tipoUsuarioId == null) {
            throw new RegraDeNegocioException("O tipo de usuario e obrigatorio");
        }
        if (usuarioBancoDadosId == null) {
            throw new RegraDeNegocioException("O usuario de banco de dados e obrigatorio");
        }
        if (inicioVigencia == null) {
            throw new RegraDeNegocioException("A data de inicio de vigencia e obrigatoria");
        }
        if (fimVigencia != null && fimVigencia.isBefore(inicioVigencia)) {
            throw new RegraDeNegocioException(
                    "A data de fim de vigencia deve ser igual ou posterior a data de inicio");
        }
        usuarioSecundario = Textos.opcional(usuarioSecundario, 30);
    }

    /**
     * Indica vigencia atual do usuario.
     */
    public boolean vigente(LocalDate referencia) {
        return fimVigencia == null || !fimVigencia.isBefore(referencia);
    }

    public Usuario comVigencia(LocalDate novaDataInicio, LocalDate novaDataFim) {
        return new Usuario(username, host, tipoUsuarioId, novaDataInicio, novaDataFim,
                usuarioBancoDadosId, usuarioSecundario);
    }
}