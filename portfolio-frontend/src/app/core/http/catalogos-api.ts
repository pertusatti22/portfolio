import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { ApiClient } from '@core/http/api-client';
import { Pagina, PaginaParams } from '@core/models/comum.model';
import { Catalogo, CatalogoRequest } from '@core/models/portfolio.model';

export interface CatalogoFiltro extends PaginaParams {
  filtro?: string;
  somenteAtivos?: boolean;
}

@Injectable({ providedIn: 'root' })
export class CatalogosApi {
  private readonly api = inject(ApiClient);

  listarTodos(recurso: string, somenteAtivos = false): Observable<Catalogo[]> {
    return this.api.get<Catalogo[]>(`/catalogos/${recurso}/todos`, { somenteAtivos });
  }

  listar(recurso: string, filtro: CatalogoFiltro = {}): Observable<Pagina<Catalogo>> {
    return this.api.pagina<Catalogo>(`/catalogos/${recurso}`, {
      pagina: filtro.pagina,
      tamanho: filtro.tamanho,
      ordenacao: filtro.ordenacao,
      crescente: filtro.crescente,
      filtro: filtro.filtro,
      somenteAtivos: filtro.somenteAtivos
    });
  }

  buscar(recurso: string, id: number): Observable<Catalogo> {
    return this.api.get<Catalogo>(`/catalogos/${recurso}/${id}`);
  }

  criar(recurso: string, request: CatalogoRequest): Observable<Catalogo> {
    return this.api.post<Catalogo>(`/catalogos/${recurso}`, request);
  }

  alterar(recurso: string, id: number, request: CatalogoRequest): Observable<Catalogo> {
    return this.api.put<Catalogo>(`/catalogos/${recurso}/${id}`, request);
  }

  desativar(recurso: string, id: number): Observable<void> {
    return this.api.patch<void>(`/catalogos/${recurso}/${id}/desativar`);
  }

  excluir(recurso: string, id: number): Observable<void> {
    return this.api.delete(`/catalogos/${recurso}/${id}`);
  }
}
