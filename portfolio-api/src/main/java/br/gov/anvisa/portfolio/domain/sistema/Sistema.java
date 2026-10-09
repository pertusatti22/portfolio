package br.gov.anvisa.portfolio.domain.sistema;

import br.gov.anvisa.portfolio.domain.shared.Textos;

/**
 * Agregado Sistema: informacoes de um sistema de informacao catalogado no portifolio.
 */
public final class Sistema {

    private Long id;
    private String sigla;
    private String nome;
    private Long origemId;
    private String objetivo;
    private String arquivo;
    private boolean anvisa;
    private boolean integracaoContinua;
    private String classificacaoBem;
    private Long baseDeDadoId;
    private boolean registroAtivo;
    private boolean validado;

    private Sistema() {
    }

    /**
     * Cria um novo sistema (ainda sem identificador, atribuido pela persistencia).
     */
    public static Sistema criar(Dados dados) {
        Sistema sistema = new Sistema();
        sistema.registroAtivo = true;
        sistema.validado = false;
        sistema.atualizarDados(dados);
        return sistema;
    }

    /**
     * Reconstitui um sistema existente a partir da persistencia.
     */
    public static Sistema reconstituir(Long id, Dados dados, boolean registroAtivo, boolean validado) {
        Sistema sistema = new Sistema();
        sistema.id = id;
        sistema.atualizarDados(dados);
        sistema.registroAtivo = registroAtivo;
        sistema.validado = validado;
        return sistema;
    }

    public void atualizarDados(Dados dados) {
        this.sigla = Textos.codigo(dados.sigla(), "Sigla", 50);
        this.nome = Textos.obrigatorio(dados.nome(), "Nome", 150);
        this.origemId = dados.origemId();
        this.objetivo = Textos.opcional(dados.objetivo(), 1000);
        this.arquivo = Textos.opcional(dados.arquivo(), 150);
        this.anvisa = dados.anvisa();
        this.integracaoContinua = dados.integracaoContinua();
        this.classificacaoBem = Textos.opcional(dados.classificacaoBem(), 100);
        this.baseDeDadoId = dados.baseDeDadoId();
    }

    public void validar() {
        this.validado = true;
    }

    public void desativar() {
        this.registroAtivo = false;
    }

    public void ativar() {
        this.registroAtivo = true;
    }

    public Long id() {
        return id;
    }

    public String sigla() {
        return sigla;
    }

    public String nome() {
        return nome;
    }

    public Long origemId() {
        return origemId;
    }

    public String objetivo() {
        return objetivo;
    }

    public String arquivo() {
        return arquivo;
    }

    public boolean anvisa() {
        return anvisa;
    }

    public boolean integracaoContinua() {
        return integracaoContinua;
    }

    public String classificacaoBem() {
        return classificacaoBem;
    }

    public Long baseDeDadoId() {
        return baseDeDadoId;
    }

    public boolean registroAtivo() {
        return registroAtivo;
    }

    public boolean validado() {
        return validado;
    }

    /**
     * Conjunto de atributos cadastrais de um sistema, compartilhado entre criacao e alteracao.
     */
    public record Dados(String sigla,
                        String nome,
                        Long origemId,
                        String objetivo,
                        String arquivo,
                        boolean anvisa,
                        boolean integracaoContinua,
                        String classificacaoBem,
                        Long baseDeDadoId) {
    }
}