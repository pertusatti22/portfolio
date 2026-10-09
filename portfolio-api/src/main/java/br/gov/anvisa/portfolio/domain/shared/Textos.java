package br.gov.anvisa.portfolio.domain.shared;

/**
 * Utilitarios de normalizacao e validacao de texto reutilizados pelos agregados.
 */
public final class Textos {

    private Textos() {
    }

    /**
     * Normaliza um texto obrigatorio, lancando excecao quando ausente ou acima do tamanho maximo.
     */
    public static String obrigatorio(String valor, String campo, int tamanhoMaximo) {
        String normalizado = normalizar(valor);
        if (normalizado == null || normalizado.isEmpty()) {
            throw new RegraDeNegocioException("O campo %s e obrigatorio".formatted(campo));
        }
        if (normalizado.length() > tamanhoMaximo) {
            throw new RegraDeNegocioException(
                    "O campo %s deve ter no maximo %d caracteres".formatted(campo, tamanhoMaximo));
        }
        return normalizado;
    }

    /**
     * Normaliza um texto opcional, validando apenas o tamanho maximo.
     */
    public static String opcional(String valor, int tamanhoMaximo) {
        String normalizado = normalizar(valor);
        if (normalizado != null && normalizado.length() > tamanhoMaximo) {
            throw new RegraDeNegocioException(
                    "O campo deve ter no maximo %d caracteres".formatted(tamanhoMaximo));
        }
        return normalizado == null || normalizado.isEmpty() ? null : normalizado;
    }

    /**
     * Normaliza um codigo (sigla) para caixa alta, garantindo unicidade consistente.
     */
    public static String codigo(String valor, String campo, int tamanhoMaximo) {
        return obrigatorio(valor, campo, tamanhoMaximo).toUpperCase();
    }

    public static String normalizar(String valor) {
        return valor == null ? null : valor.trim();
    }
}