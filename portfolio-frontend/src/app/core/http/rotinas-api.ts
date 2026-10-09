import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { ApiClient } from '@core/http/api-client';
import { Pagina, PaginaParams } from '@core/models/comum.model';
import { Rotina, RotinaRequest } from '@core/models/portfolio.model';

export interface RotinaFiltro extends PaginaParams {
  termo?: string;
  schemaId?: number;
  ativo?: boolean;
}

@Injectable({ providedIn: 'root' })
export class RotinasApi {
  private readonly api = inject(ApiClient);

  listar(filtro: RotinaFiltro = {}): Observable<Pagina<Rotina>> {
    return this.api.pagina<Rotina>('/rotinas', filtro);
  }

  buscar(id: number): Observable<Rotina> {
    return this.api.get<Rotina>(`/rotinas/${id}`);
  }

  criar(request: RotinaRequest): Observable<Rotina> {
    return this.api.post<Rotina>('/rotinas', request);
  }

  alterar(id: number, request: RotinaRequest): Observable<Rotina> {
    return this.api.put<Rotina>(`/rotinas/${id}`, request);
  }

  alterarAtivo(id: number, ativo: boolean): Observable<Rotina> {
    return this.api.patch<Rotina>(`/rotinas/${id}/ativo`, undefined, { ativo });
  }

  excluir(id: number): Observable<void> {
    return this.api.delete(`/rotinas/${id}`);
  }
}
