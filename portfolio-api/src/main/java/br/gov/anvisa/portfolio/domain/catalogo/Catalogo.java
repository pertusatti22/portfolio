package br.gov.anvisa.portfolio.domain.catalogo;

/**
 * Item de um catalogo de dados de referencia.
 *
 * @param id             codigo sequencial do item
 * @param descricao      descricao do item (unicidade garantida por indice unico no banco)
 * @param versao         versao, aplicavel apenas a TD_SOFTWARE
 * @param liberado       indicador auxiliar de liberacao (programa autorizado / usuario liberado)
 * @param registroAtivo  indicador de registro ativo (S/N)
 */
public record Catalogo(Long id, String descricao, String versao, Boolean liberado, boolean registroAtivo) {

    public Catalogo {
        descricao = descricao == null ? null : descricao.trim();
        versao = versao == null ? null : versao.trim();
    }

    public static Catalogo de(Long id, String descricao, boolean registroAtivo) {
        return new Catalogo(id, descricao, null, null, registroAtivo);
    }

    public Catalogo comId(Long novoId) {
        return new Catalogo(novoId, descricao, versao, liberado, registroAtivo);
    }

    public Catalogo ativar() {
        return new Catalogo(id, descricao, versao, liberado, true);
    }

    public Catalogo desativar() {
        return new Catalogo(id, descricao, versao, liberado, false);
    }
}