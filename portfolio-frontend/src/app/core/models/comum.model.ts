/** Envelope padronizado de resultados paginados da API. */
export interface Pagina<T> {
  conteudo: T[];
  pagina: number;
  tamanho: number;
  total: number;
  totalPaginas: number;
}

/** Corpo padronizado de erro devolvido pela API. */
export interface ApiErro {
  timestamp: string;
  status: number;
  erro: string;
  mensagem: string;
  caminho: string;
  campos?: { campo: string; mensagem: string }[];
}

/** Parmetros de paginacao e ordenacao aceitos pelos endpoints de listagem. */
export interface PaginaParams {
  pagina?: number;
  tamanho?: number;
  ordenacao?: string;
  crescente?: boolean;
}

/** Item agrupado dos graficos do painel. */
export interface ItemContagem {
  rotulo: string;
  total: number;
}
