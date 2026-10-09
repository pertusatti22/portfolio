import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { ApiClient } from '@core/http/api-client';
import { Pagina, PaginaParams } from '@core/models/comum.model';
import { TipoUrl } from '@core/models/tipo-url.model';
import { Url, UrlRequest } from '@core/models/portfolio.model';

export interface UrlFiltro extends PaginaParams {
  termo?: string;
  sistemaId?: number;
  tipo?: TipoUrl;
  ambienteId?: number;
  ativo?: boolean;
}

export interface TipoUrlResponse {
  nome: TipoUrl;
  rotulo: string;
  codigo: string;
}

@Injectable({ providedIn: 'root' })
export class UrlsApi {
  private readonly api = inject(ApiClient);

  listarTipos(): Observable<TipoUrlResponse[]> {
    return this.api.get<TipoUrlResponse[]>('/urls/tipos');
  }

  listar(filtro: UrlFiltro = {}): Observable<Pagina<Url>> {
    return this.api.pagina<Url>('/urls', filtro);
  }

  buscar(id: number): Observable<Url> {
    return this.api.get<Url>(`/urls/${id}`);
  }

  criar(request: UrlRequest): Observable<Url> {
    return this.api.post<Url>('/urls', request);
  }

  alterar(id: number, request: UrlRequest): Observable<Url> {
    return this.api.put<Url>(`/urls/${id}`, request);
  }

  alterarAtivo(id: number, ativo: boolean): Observable<Url> {
    return this.api.patch<Url>(`/urls/${id}/ativo`, undefined, { ativo });
  }

  excluir(id: number): Observable<void> {
    return this.api.delete(`/urls/${id}`);
  }
}
