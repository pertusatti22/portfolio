package br.gov.anvisa.portfolio.domain.painel;

import java.util.List;

/**
 * Porta de saida para indicadores agregados do portifolio (read model do dashboard).
 */
public interface PainelPort {

    ResumoPortfolio montarResumo();

    /**
     * Resumo executivo do portifolio.
     */
    record ResumoPortfolio(long totalSistemas,
                           long sistemasAtivos,
                           long sistemasNaoValidados,
                           long totalServidores,
                           long servidoresLigados,
                           long totalUrls,
                           long totalRotinas,
                           long totalBackups,
                           long totalUsuarios,
                           long usuariosInativos,
                           List<ItemContagem> sistemasPorOrigem,
                           List<ItemContagem> sistemasPorBaseDeDado,
                           List<ItemContagem> urlsPorTipo) {
    }

    /**
     * Item de um grafico de contagem.
     *
     * @param rotulo  descricao do agrupamento
     * @param total   quantidade de registros no agrupamento
     */
    record ItemContagem(String rotulo, long total) {
    }
}