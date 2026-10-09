package br.gov.anvisa.portfolio.domain.catalogo;

/**
 * Catalogo de dados de referencia (tabelas TD_*).
 *
 * <p>Todos os catalogos possuem a mesma estrutura (codigo, descricao e indicador de registro ativo),
 * portanto sao representados por um unico tipo de dominio ({@link Catalogo}) discriminado por este enum.
 */
public enum CatalogoTipo {

    ORIGEM("origens", "Origem", 50),
    AMBIENTE("ambientes", "Ambiente", 20),
    ACESSO("acessos", "Acesso", 20),
    AUTENTICACAO("autenticacoes", "Autenticacao", 50),
    BASE_DE_DADO("bases-de-dado", "Base de Dado", 50),
    FRAMEWORK("frameworks", "Framework", 50),
    SOFTWARE("softwares", "Software", 100),
    SISTEMA_OPERACIONAL("sistemas-operacionais", "Sistema Operacional", 200),
    ARMAZENAMENTO("armazenamentos", "Armazenamento", 200),
    TIPO_DOCUMENTACAO("tipos-documentacao", "Tipo de Documentacao", 50),
    TIPO_USUARIO("tipos-usuario", "Tipo de Usuario", 100),
    USUARIO_BANCO_DADOS("usuarios-banco-dados", "Usuario de Banco de Dados", 50),
    PROGRAMA("programas", "Programa", 255),
    SCHEMA("schemas", "Schema", 100);

    private final String recurso;
    private final String rotulo;
    private final int tamanhoDescricao;

    CatalogoTipo(String recurso, String rotulo, int tamanhoDescricao) {
        this.recurso = recurso;
        this.rotulo = rotulo;
        this.tamanhoDescricao = tamanhoDescricao;
    }

    public String recurso() {
        return recurso;
    }

    public String rotulo() {
        return rotulo;
    }

    /**
     * Tamanho maximo da descricao do item, conforme a coluna da tabela de catalogo.
     */
    public int tamanhoDescricao() {
        return tamanhoDescricao;
    }

    /**
     * Indica se o catalogo possui o atributo de versao (TD_SOFTWARE.DS_VERSAO).
     */
    public boolean possuiVersao() {
        return this == SOFTWARE;
    }

    /**
     * Indica se o catalogo possui indicador auxiliar de liberacao
     * (TD_PROGRAMA.ST_PROGRAMA_AUTORIZADO e TB_USUARIO_BANCO_DADOS.ST_LIBERA_USUARIO).
     */
    public boolean possuiIndicadorLiberacao() {
        return this == PROGRAMA || this == USUARIO_BANCO_DADOS;
    }

    /**
     * Resolve o catalogo a partir do segmento de URL (ex.: {@code bases-de-dado}).
     *
     * @throws br.gov.anvisa.portfolio.domain.shared.RecursoNaoEncontradoException se nao houver catalogo
     */
    public static CatalogoTipo doRecurso(String recurso) {
        for (CatalogoTipo tipo : values()) {
            if (tipo.recurso.equalsIgnoreCase(recurso)) {
                return tipo;
            }
        }
        throw new br.gov.anvisa.portfolio.domain.shared.RecursoNaoEncontradoException("Catalogo", recurso);
    }
}