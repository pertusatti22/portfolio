package br.gov.anvisa.portfolio.infrastructure.persistence.support;

/**
 * Conversao dos indicadores S/N do banco (COLUNA ST_*) para booleanos de dominio.
 */
public final class Indicadores {

    public static final String SIM = "S";
    public static final String NAO = "N";

    private Indicadores() {
    }

    public static String de(boolean valor) {
        return valor ? SIM : NAO;
    }

    public static boolean para(String valor) {
        return valor != null && SIM.equalsIgnoreCase(valor.trim());
    }

    public static Boolean paraBooleano(String valor) {
        return valor == null ? null : para(valor);
    }
}