import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { ApiClient } from '@core/http/api-client';
import { Pagina, PaginaParams } from '@core/models/comum.model';
import {
  Catalogo,
  Componente,
  ComponenteRequest,
  Documentacao,
  DocumentacaoRequest,
  SchemaVinculado,
  Sistema,
  SistemaDetalhe,
  SistemaRequest,
  VinculosIdsRequest,
  VinculosSchemasRequest
} from '@core/models/portfolio.model';

export interface SistemaFiltro extends PaginaParams {
  termo?: string;
  origemId?: number;
  baseDeDadoId?: number;
  ativo?: boolean;
  validado?: boolean;
}

@Injectable({ providedIn: 'root' })
export class SistemasApi {
  private readonly api = inject(ApiClient);

  listar(filtro: SistemaFiltro = {}): Observable<Pagina<Sistema>> {
    return this.api.pagina<Sistema>('/sistemas', filtro);
  }

  buscar(id: number): Observable<Sistema> {
    return this.api.get<Sistema>(`/sistemas/${id}`);
  }

  detalhar(id: number): Observable<SistemaDetalhe> {
    return this.api.get<SistemaDetalhe>(`/sistemas/${id}/detalhe`);
  }

  criar(request: SistemaRequest): Observable<Sistema> {
    return this.api.post<Sistema>('/sistemas', request);
  }

  alterar(id: number, request: SistemaRequest): Observable<Sistema> {
    return this.api.put<Sistema>(`/sistemas/${id}`, request);
  }

  validar(id: number): Observable<Sistema> {
    return this.api.patch<Sistema>(`/sistemas/${id}/validar`);
  }

  alterarAtivo(id: number, ativo: boolean): Observable<Sistema> {
    return this.api.patch<Sistema>(`/sistemas/${id}/ativo`, undefined, { ativo });
  }

  excluir(id: number): Observable<void> {
    return this.api.delete(`/sistemas/${id}`);
  }

  listarSchemas(sistemaId: number): Observable<SchemaVinculado[]> {
    return this.api.get<SchemaVinculado[]>(`/sistemas/${sistemaId}/schemas`);
  }

  substituirSchemas(sistemaId: number, request: VinculosSchemasRequest): Observable<SchemaVinculado[]> {
    return this.api.put<SchemaVinculado[]>(`/sistemas/${sistemaId}/schemas`, request);
  }

  listarSoftwares(sistemaId: number): Observable<Catalogo[]> {
    return this.api.get<Catalogo[]>(`/sistemas/${sistemaId}/softwares`);
  }

  substituirSoftwares(sistemaId: number, ids: number[]): Observable<Catalogo[]> {
    return this.api.put<Catalogo[]>(`/sistemas/${sistemaId}/softwares`, { ids });
  }

  listarServidoresVinculados(sistemaId: number): Observable<number[]> {
    return this.api.get<number[]>(`/sistemas/${sistemaId}/servidores`);
  }

  substituirServidores(sistemaId: number, ids: number[]): Observable<number[]> {
    return this.api.put<number[]>(`/sistemas/${sistemaId}/servidores`, { ids });
  }

  listarUrlsVinculadas(sistemaId: number): Observable<number[]> {
    return this.api.get<number[]>(`/sistemas/${sistemaId}/urls`);
  }

  substituirUrls(sistemaId: number, ids: number[]): Observable<number[]> {
    return this.api.put<number[]>(`/sistemas/${sistemaId}/urls`, { ids });
  }

  listarComponentes(sistemaId: number, paginacao?: PaginaParams): Observable<Pagina<Componente>> {
    return this.api.pagina<Componente>(`/sistemas/${sistemaId}/componentes`, paginacao);
  }

  buscarComponente(sistemaId: number, componenteId: number): Observable<Componente> {
    return this.api.get<Componente>(`/sistemas/${sistemaId}/componentes/${componenteId}`);
  }

  criarComponente(sistemaId: number, request: ComponenteRequest): Observable<Componente> {
    return this.api.post<Componente>(`/sistemas/${sistemaId}/componentes`, request);
  }

  alterarComponente(
    sistemaId: number,
    componenteId: number,
    request: ComponenteRequest
  ): Observable<Componente> {
    return this.api.put<Componente>(`/sistemas/${sistemaId}/componentes/${componenteId}`, request);
  }

  excluirComponente(sistemaId: number, componenteId: number): Observable<void> {
    return this.api.delete(`/sistemas/${sistemaId}/componentes/${componenteId}`);
  }

  listarDocumentacoes(sistemaId: number, paginacao?: PaginaParams): Observable<Pagina<Documentacao>> {
    return this.api.pagina<Documentacao>(`/sistemas/${sistemaId}/documentacoes`, paginacao);
  }

  criarDocumentacao(sistemaId: number, request: DocumentacaoRequest): Observable<Documentacao> {
    return this.api.post<Documentacao>(`/sistemas/${sistemaId}/documentacoes`, request);
  }

  alterarDocumentacao(
    sistemaId: number,
    documentacaoId: number,
    request: DocumentacaoRequest
  ): Observable<Documentacao> {
    return this.api.put<Documentacao>(`/sistemas/${sistemaId}/documentacoes/${documentacaoId}`, request);
  }

  excluirDocumentacao(sistemaId: number, documentacaoId: number): Observable<void> {
    return this.api.delete(`/sistemas/${sistemaId}/documentacoes/${documentacaoId}`);
  }
}
