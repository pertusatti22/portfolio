package br.gov.anvisa.portfolio.domain.componente;

import br.gov.anvisa.portfolio.domain.shared.Textos;

/**
 * Agregado Componente: funcionalidade utilizada por um sistema.
 */
public final class Componente {

    private Long id;
    private Long sistemaId;
    private String nome;
    private String descricao;
    private boolean registroAtivo;

    private Componente() {
    }

    public static Componente criar(Long sistemaId, String nome, String descricao) {
        Componente componente = new Componente();
        componente.sistemaId = sistemaId;
        componente.registroAtivo = true;
        componente.atualizarDados(nome, descricao);
        return componente;
    }

    public static Componente reconstituir(Long id, Long sistemaId, String nome, String descricao, boolean registroAtivo) {
        Componente componente = new Componente();
        componente.id = id;
        componente.sistemaId = sistemaId;
        componente.registroAtivo = registroAtivo;
        componente.atualizarDados(nome, descricao);
        return componente;
    }

    public void atualizarDados(String nome, String descricao) {
        this.nome = Textos.obrigatorio(nome, "Nome do componente", 50);
        this.descricao = Textos.obrigatorio(descricao, "Descricao do componente", 600);
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

    public Long sistemaId() {
        return sistemaId;
    }

    public String nome() {
        return nome;
    }

    public String descricao() {
        return descricao;
    }

    public boolean registroAtivo() {
        return registroAtivo;
    }
}