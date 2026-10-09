package br.gov.anvisa.portfolio.infrastructure.persistence.catalog;

import br.gov.anvisa.portfolio.domain.catalogo.CatalogoTipo;

/**
 * Metadados das tabelas de catalogo (TD_* e TB_USUARIO_BANCO_DADOS).
 *
 * <p>Os nomes de tabela e coluna sao metadados fixos do banco; nenhum valor oriundo de
 * requisicao e utilizado para montar SQL, o que elimina risco de injecao.
 */
public enum CatalogoTabela {

    ORIGEM(CatalogoTipo.ORIGEM, "TD_ORIGEM", "CO_SEQ_ORIGEM", "NO_ORIGEM", null, null, "SQ_COSEQORIGEM"),
    AMBIENTE(CatalogoTipo.AMBIENTE, "TD_AMBIENTE", "CO_SEQ_AMBIENTE", "DS_AMBIENTE", null, null, "SQ_COSEQAMBIENTE"),
    ACESSO(CatalogoTipo.ACESSO, "TD_ACESSO", "CO_SEQ_ACESSO", "DS_ACESSO", null, null, "SQ_COSEQACESSO"),
    AUTENTICACAO(CatalogoTipo.AUTENTICACAO, "TD_AUTENTICACAO", "CO_SEQ_AUTENTICACAO", "NO_AUTENTICACAO", null, null,
            "SQ_COSEQAUTENTICACAO"),
    BASE_DE_DADO(CatalogoTipo.BASE_DE_DADO, "TD_BASEDEDADO", "CO_SEQ_BASEDEDADO", "NO_BASEDEDADO", null, null,
            "SQ_COSEQBASEDEDADOS"),
    FRAMEWORK(CatalogoTipo.FRAMEWORK, "TD_FRAMEWORK", "CO_SEQ_FRAMEWORK", "NO_FRAMEWORK", null, null, "SQ_COSEQFRAMEWORK"),
    SOFTWARE(CatalogoTipo.SOFTWARE, "TD_SOFTWARE", "CO_SEQ_SOFTWARE", "NO_SOFTWARE", "DS_VERSAO", null, "SQ_COSEQSOFTWARE"),
    SISTEMA_OPERACIONAL(CatalogoTipo.SISTEMA_OPERACIONAL, "TD_SIST_OPER", "CO_SEQ_SIST_OPER", "DS_SIST_OPER", null, null,
            "SQ_COSEQSISTOPER"),
    ARMAZENAMENTO(CatalogoTipo.ARMAZENAMENTO, "TD_ARMAZENAMENTO", "CO_SEQ_ARMAZENAMENTO", "DS_ARMAZENAMENTO", null, null,
            "SQ_COSEQARMAZENAMENTO"),
    TIPO_DOCUMENTACAO(CatalogoTipo.TIPO_DOCUMENTACAO, "TD_TIPO_DOCUMENTACAO", "CO_SEQ_TIPO_DOCUMENTACAO",
            "DS_TIPO_DOCUMENTACAO", null, null, "SQ_COSEQTIPODOCUMENTACAO"),
    TIPO_USUARIO(CatalogoTipo.TIPO_USUARIO, "TD_TIPO_USUARIO", "CO_SEQ_TIPO_USUARIO", "DS_TIPO_USUARIO", null, null,
            "SQ_TPUSU_COSEQTIPOUSUARIO"),
    USUARIO_BANCO_DADOS(CatalogoTipo.USUARIO_BANCO_DADOS, "TB_USUARIO_BANCO_DADOS", "CO_SEQ_USUARIO_BANCO_DADOS",
            "NO_USUARIO_BANCO_DADOS", null, "ST_LIBERA_USUARIO", "SQ_USUARIOBANCODADOS"),
    PROGRAMA(CatalogoTipo.PROGRAMA, "TD_PROGRAMA", "CO_SEQ_PROGRAMA", "DS_PROGRAMA", null, "ST_PROGRAMA_AUTORIZADO",
            "SQ_COSEQPROGRAMA"),
    // O trigger legado TBI_SCHEMA utiliza a sequence SQ_COSEQSISTEMA; a API usa a sequence correta.
    SCHEMA(CatalogoTipo.SCHEMA, "TD_SCHEMA", "CO_SEQ_SCHEMA", "DS_SCHEMA", null, null, "SQ_COSEQSCHEMA");

    private final CatalogoTipo tipo;
    private final String tabela;
    private final String colunaPk;
    private final String colunaDescricao;
    private final String colunaVersao;
    private final String colunaLiberado;
    private final String sequence;

    CatalogoTabela(CatalogoTipo tipo, String tabela, String colunaPk, String colunaDescricao,
                    String colunaVersao, String colunaLiberado, String sequence) {
        this.tipo = tipo;
        this.tabela = tabela;
        this.colunaPk = colunaPk;
        this.colunaDescricao = colunaDescricao;
        this.colunaVersao = colunaVersao;
        this.colunaLiberado = colunaLiberado;
        this.sequence = sequence;
    }

    public CatalogoTipo tipo() {
        return tipo;
    }

    public String tabela() {
        return tabela;
    }

    public String colunaPk() {
        return colunaPk;
    }

    public String colunaDescricao() {
        return colunaDescricao;
    }

    public String colunaVersao() {
        return colunaVersao;
    }

    public String colunaLiberado() {
        return colunaLiberado;
    }

    public String sequence() {
        return sequence;
    }

    public static CatalogoTabela de(CatalogoTipo tipo) {
        for (CatalogoTabela catalogo : values()) {
            if (catalogo.tipo == tipo) {
                return catalogo;
            }
        }
        throw new IllegalArgumentException("Catalogo sem metadados: %s".formatted(tipo));
    }
}