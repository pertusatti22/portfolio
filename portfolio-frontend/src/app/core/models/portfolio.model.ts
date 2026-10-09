import { TipoUrl } from './tipo-url.model';

export type { TipoUrl } from './tipo-url.model';

export interface Sistema {
  id: number;
  sigla: string;
  nome: string;
  origemId: number | null;
  origem: string | null;
  objetivo: string | null;
  arquivo: string | null;
  anvisa: boolean;
  integracaoContinua: boolean;
  classificacaoBem: string | null;
  baseDeDadoId: number | null;
  baseDeDado: string | null;
  registroAtivo: boolean;
  validado: boolean;
}

export interface SistemaRequest {
  sigla: string;
  nome: string;
  origemId?: number | null;
  objetivo?: string | null;
  arquivo?: string | null;
  anvisa?: boolean;
  integracaoContinua?: boolean;
  classificacaoBem?: string | null;
  baseDeDadoId?: number | null;
  ativo?: boolean;
  validado?: boolean;
}

export interface SchemaVinculado {
  schemaId: number;
  descricao: string | null;
  principal: boolean;
}

export interface SistemaDetalhe {
  sistema: Sistema;
  componentes: Componente[];
  documentacoes: Documentacao[];
  schemas: SchemaVinculado[];
  softwares: Catalogo[];
  servidores: Servidor[];
  urls: Url[];
}

export interface Componente {
  id: number;
  sistemaId: number;
  nome: string;
  descricao: string | null;
  registroAtivo: boolean;
}

export interface ComponenteRequest {
  nome: string;
  descricao?: string | null;
  ativo?: boolean;
}

export interface Documentacao {
  id: number;
  sistemaId: number;
  urlRepositorio: string;
  tipoDocumentacaoId: number;
  tipoDocumentacao: string | null;
  registroAtivo: boolean;
}

export interface DocumentacaoRequest {
  urlRepositorio: string;
  tipoDocumentacaoId: number;
  ativo?: boolean;
}

export interface Servidor {
  id: number;
  nome: string;
  ligado: boolean;
  cpu: string | null;
  memoriaGb: string | null;
  sistemaOperacionalId: number | null;
  sistemaOperacional: string | null;
  tamanhoGb: string | null;
  armazenamentoId: number | null;
  armazenamento: string | null;
  ambienteId: number | null;
  ambiente: string | null;
  aplicacao: string | null;
  possuiBackup: boolean;
}

export interface ServidorRequest {
  nome: string;
  ligado?: boolean;
  cpu?: string | null;
  memoriaGb?: string | null;
  sistemaOperacionalId?: number | null;
  tamanhoGb?: string | null;
  armazenamentoId?: number | null;
  ambienteId?: number | null;
  aplicacao?: string | null;
}

export interface Url {
  id: number;
  endereco: string;
  tipo: TipoUrl;
  tipoRotulo: string;
  ambienteId: number | null;
  ambiente: string | null;
  acessoId: number | null;
  acesso: string | null;
  frameworkId: number | null;
  framework: string | null;
  autenticacaoId: number | null;
  autenticacao: string | null;
  registroAtivo: boolean;
}

export interface UrlRequest {
  endereco: string;
  tipo?: TipoUrl;
  ambienteId?: number | null;
  acessoId?: number | null;
  frameworkId?: number | null;
  autenticacaoId?: number | null;
  ativo?: boolean;
}

export interface Catalogo {
  id: number;
  descricao: string;
  versao: string | null;
  liberado: boolean | null;
  registroAtivo: boolean;
}

export interface CatalogoRequest {
  descricao: string;
  versao?: string | null;
  liberado?: boolean | null;
  ativo?: boolean;
}

export interface Rotina {
  id: number;
  nomeJob: string;
  owner: string | null;
  schemaId: number | null;
  schema: string | null;
  intervaloExecucao: string | null;
  registroAtivo: boolean;
}

export interface RotinaRequest {
  nomeJob: string;
  owner?: string | null;
  schemaId?: number | null;
  intervaloExecucao?: string | null;
  ativo?: boolean;
}

export interface Backup {
  id: number;
  frequencia: string | null;
  horario: string | null;
  caminho: string | null;
  scriptJob: string | null;
  instancia: string | null;
  tipoInstancia: string | null;
  conteudoBackup: string | null;
  data: string | null;
  tempoFullSemanal: string | null;
  frequenciaIncremental: string | null;
  horarioIncremental: string | null;
  tempoIncremental: string | null;
  archiveLog: string | null;
  tempoArchiveLog: string | null;
  eliminaArchiveLog: boolean;
  retencao: string | null;
  retencaoIncremental: string | null;
  retencaoArchiveLog: string | null;
  appType: string | null;
  instance: string | null;
  subclient: string | null;
  backupType: string | null;
  storagePolicy: string | null;
  servidoresIds: number[];
}

export interface Usuario {
  username: string;
  host: string;
  tipoUsuarioId: number;
  tipoUsuario: string | null;
  inicioVigencia: string;
  fimVigencia: string | null;
  usuarioBancoDadosId: number;
  usuarioBancoDados: string | null;
  usuarioSecundario: string | null;
  vigente: boolean;
}

export interface UsuarioRequest {
  username: string;
  host: string;
  tipoUsuarioId: number;
  inicioVigencia: string;
  fimVigencia?: string | null;
  usuarioBancoDadosId: number;
  usuarioSecundario?: string | null;
}

export interface UsuarioHistorico {
  username: string;
  host: string;
  tipoUsuarioId: number;
  tipoUsuario: string | null;
  inicioVigencia: string;
  fimVigencia: string | null;
  usuarioBancoDadosId: number;
  usuarioBancoDados: string | null;
  usuarioAplicacao: string | null;
  dataRegistro: string | null;
  operacao: string;
}

export interface VinculosSchemasRequest {
  schemaIds: number[];
  principalId?: number | null;
}

export interface VinculosIdsRequest {
  ids: number[];
}
