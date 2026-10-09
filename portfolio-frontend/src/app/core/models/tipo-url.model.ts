export type TipoUrl = 'PRODUCAO' | 'RELEASE' | 'HOMOLOGACAO' | 'DESENVOLVIMENTO';

export interface TipoUrlDescricao {
  nome: TipoUrl;
  rotulo: string;
  codigo: string;
}