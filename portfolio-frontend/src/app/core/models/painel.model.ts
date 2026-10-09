import { ItemContagem } from './comum.model';

/** Indicadores agregados exibidos no painel inicial. */
export interface Painel {
  totalSistemas: number;
  sistemasAtivos: number;
  sistemasNaoValidados: number;
  totalServidores: number;
  servidoresLigados: number;
  totalUrls: number;
  totalRotinas: number;
  totalBackups: number;
  totalUsuarios: number;
  usuariosInativos: number;
  sistemasPorOrigem: ItemContagem[];
  sistemasPorBaseDeDado: ItemContagem[];
  urlsPorTipo: ItemContagem[];
}
