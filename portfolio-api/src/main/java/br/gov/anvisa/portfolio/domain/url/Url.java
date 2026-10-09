package br.gov.anvisa.portfolio.domain.url;

import br.gov.anvisa.portfolio.domain.shared.Textos;

/**
 * Agregado URL: endereco de acesso a um sistema ou componente, com classificacao de
 * ambiente, tipo de acesso, framework e mecanismo de autenticacao.
 */
public final class Url {

    private Long id;
    private String endereco;
    private TipoUrl tipo;
    private Long ambienteId;
    private Long acessoId;
    private Long frameworkId;
    private Long autenticacaoId;
    private boolean registroAtivo;

    private Url() {
    }

    public static Url criar(TipoUrl tipo, Dados dados) {
        Url url = new Url();
        url.registroAtivo = true;
        url.atualizarDados(tipo, dados);
        return url;
    }

    public static Url reconstituir(Long id, TipoUrl tipo, Dados dados, boolean registroAtivo) {
        Url url = new Url();
        url.id = id;
        url.registroAtivo = registroAtivo;
        url.atualizarDados(tipo, dados);
        return url;
    }

    public void atualizarDados(TipoUrl tipo, Dados dados) {
        this.endereco = Textos.obrigatorio(dados.endereco(), "URL", 255);
        this.tipo = tipo == null ? TipoUrl.padrao() : tipo;
        this.ambienteId = dados.ambienteId();
        this.acessoId = dados.acessoId();
        this.frameworkId = dados.frameworkId();
        this.autenticacaoId = dados.autenticacaoId();
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

    public String endereco() {
        return endereco;
    }

    public TipoUrl tipo() {
        return tipo;
    }

    public Long ambienteId() {
        return ambienteId;
    }

    public Long acessoId() {
        return acessoId;
    }

    public Long frameworkId() {
        return frameworkId;
    }

    public Long autenticacaoId() {
        return autenticacaoId;
    }

    public boolean registroAtivo() {
        return registroAtivo;
    }

    /**
     * Atributos de classificacao de uma URL.
     */
    public record Dados(String endereco,
                        Long ambienteId,
                        Long acessoId,
                        Long frameworkId,
                        Long autenticacaoId) {
    }
}