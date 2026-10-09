package br.gov.anvisa.portfolio.domain.servidor;

import br.gov.anvisa.portfolio.domain.shared.Textos;

/**
 * Agregado Servidor: host do portifolio com suas caracteristicas de hardware,
 * sistema operacional, ambiente e armazenamento.
 */
public final class Servidor {

    private Long id;
    private String nome;
    private boolean ligado;
    private String cpu;
    private String memoriaGb;
    private Long sistemaOperacionalId;
    private String tamanhoGb;
    private Long armazenamentoId;
    private Long ambienteId;
    private String aplicacao;
    private boolean possuiBackup;

    private Servidor() {
    }

    public static Servidor criar(Dados dados) {
        Servidor servidor = new Servidor();
        servidor.atualizarDados(dados);
        return servidor;
    }

    public static Servidor reconstituir(Long id, Dados dados, boolean possuiBackup) {
        Servidor servidor = new Servidor();
        servidor.id = id;
        servidor.atualizarDados(dados);
        servidor.possuiBackup = possuiBackup;
        return servidor;
    }

    public void atualizarDados(Dados dados) {
        this.nome = Textos.obrigatorio(dados.nome(), "Nome do servidor", 50);
        this.ligado = dados.ligado();
        this.cpu = Textos.opcional(dados.cpu(), 50);
        this.memoriaGb = Textos.opcional(dados.memoriaGb(), 50);
        this.sistemaOperacionalId = dados.sistemaOperacionalId();
        this.tamanhoGb = Textos.opcional(dados.tamanhoGb(), 50);
        this.armazenamentoId = dados.armazenamentoId();
        this.ambienteId = dados.ambienteId();
        this.aplicacao = Textos.opcional(dados.aplicacao(), 100);
    }

    public void marcarComBackup() {
        this.possuiBackup = true;
    }

    public void desmarcarBackup() {
        this.possuiBackup = false;
    }

    public Long id() {
        return id;
    }

    public String nome() {
        return nome;
    }

    public boolean ligado() {
        return ligado;
    }

    public String cpu() {
        return cpu;
    }

    public String memoriaGb() {
        return memoriaGb;
    }

    public Long sistemaOperacionalId() {
        return sistemaOperacionalId;
    }

    public String tamanhoGb() {
        return tamanhoGb;
    }

    public Long armazenamentoId() {
        return armazenamentoId;
    }

    public Long ambienteId() {
        return ambienteId;
    }

    public String aplicacao() {
        return aplicacao;
    }

    public boolean possuiBackup() {
        return possuiBackup;
    }

    /**
     * Conjunto de atributos cadastrais de um servidor.
     *
     * <p>Os campos quantitativos ({@code cpu}, {@code memoriaGb}, {@code tamanhoGb}) sao
     * mantidos como texto por compatibilidade com o DDL legado (VARCHAR2).
     */
    public record Dados(String nome,
                        boolean ligado,
                        String cpu,
                        String memoriaGb,
                        Long sistemaOperacionalId,
                        String tamanhoGb,
                        Long armazenamentoId,
                        Long ambienteId,
                        String aplicacao) {
    }
}