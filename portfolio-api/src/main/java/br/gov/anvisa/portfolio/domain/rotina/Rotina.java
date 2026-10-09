package br.gov.anvisa.portfolio.domain.rotina;

import br.gov.anvisa.portfolio.domain.shared.Textos;

/**
 * Agregado Rotina: job agendado no banco de dados, vinculado a um schema.
 */
public final class Rotina {

    private Long id;
    private Long schemaId;
    private String owner;
    private String nomeJob;
    private String intervaloExecucao;
    private boolean registroAtivo;

    private Rotina() {
    }

    public static Rotina criar(Long schemaId, String owner, String nomeJob, String intervaloExecucao) {
        Rotina rotina = new Rotina();
        rotina.schemaId = schemaId;
        rotina.registroAtivo = true;
        rotina.atualizarDados(owner, nomeJob, intervaloExecucao);
        return rotina;
    }

    public static Rotina reconstituir(Long id,
                                      Long schemaId,
                                      String owner,
                                      String nomeJob,
                                      String intervaloExecucao,
                                      boolean registroAtivo) {
        Rotina rotina = new Rotina();
        rotina.id = id;
        rotina.schemaId = schemaId;
        rotina.registroAtivo = registroAtivo;
        rotina.atualizarDados(owner, nomeJob, intervaloExecucao);
        return rotina;
    }

    public void atualizarDados(String owner, String nomeJob, String intervaloExecucao) {
        this.owner = Textos.codigo(owner, "Owner", 50);
        this.nomeJob = Textos.codigo(nomeJob, "Nome do job", 50);
        this.intervaloExecucao = Textos.obrigatorio(intervaloExecucao, "Frequencia de execucao", 100);
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

    public Long schemaId() {
        return schemaId;
    }

    public String owner() {
        return owner;
    }

    public String nomeJob() {
        return nomeJob;
    }

    public String intervaloExecucao() {
        return intervaloExecucao;
    }

    public boolean registroAtivo() {
        return registroAtivo;
    }
}