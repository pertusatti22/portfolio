import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { ApiClient } from '@core/http/api-client';
import { Pagina, PaginaParams } from '@core/models/comum.model';
import { Backup, Catalogo, Servidor, ServidorRequest, Sistema } from '@core/models/portfolio.model';

export interface ServidorFiltro extends PaginaParams {
  termo?: string;
  ambienteId?: number;
  sistemaOperacionalId?: number;
  ligado?: boolean;
}

@Injectable({ providedIn: 'root' })
export class ServidoresApi {
  private readonly api = inject(ApiClient);

  listar(filtro: ServidorFiltro = {}): Observable<Pagina<Servidor>> {
    return this.api.pagina<Servidor>('/servidores', filtro);
  }

  listarPorIds(ids: number[]): Observable<Servidor[]> {
    if (ids.length === 0) {
      return new Observable<Servidor[]>((observador) => {
        observador.next([]);
        observador.complete();
      });
    }
    return this.api.post<Servidor[]>('/servidores/lista', { ids });
  }

  buscar(id: number): Observable<Servidor> {
    return this.api.get<Servidor>(`/servidores/${id}`);
  }

  criar(request: ServidorRequest): Observable<Servidor> {
    return this.api.post<Servidor>('/servidores', request);
  }

  alterar(id: number, request: ServidorRequest): Observable<Servidor> {
    return this.api.put<Servidor>(`/servidores/${id}`, request);
  }

  alterarLigado(id: number, ligado: boolean): Observable<Servidor> {
    return this.api.patch<Servidor>(`/servidores/${id}/ligado`, undefined, { ligado });
  }

  excluir(id: number): Observable<void> {
    return this.api.delete(`/servidores/${id}`);
  }

  listarBasesDeDado(servidorId: number): Observable<Catalogo[]> {
    return this.api.get<Catalogo[]>(`/servidores/${servidorId}/bases-de-dado`);
  }

  substituirBasesDeDado(servidorId: number, ids: number[]): Observable<Catalogo[]> {
    return this.api.put<Catalogo[]>(`/servidores/${servidorId}/bases-de-dado`, { ids });
  }

  listarBackups(servidorId: number): Observable<number[]> {
    return this.api.get<number[]>(`/servidores/${servidorId}/backups`);
  }

  listarBackupsDetalhado(servidorId: number): Observable<Backup[]> {
    return this.api.get<Backup[]>(`/servidores/${servidorId}/backups/detalhado`);
  }

  substituirBackups(servidorId: number, ids: number[]): Observable<number[]> {
    return this.api.put<number[]>(`/servidores/${servidorId}/backups`, { ids });
  }

  listarSistemas(servidorId: number): Observable<Sistema[]> {
    return this.api.get<Sistema[]>(`/servidores/${servidorId}/sistemas`);
  }
}
