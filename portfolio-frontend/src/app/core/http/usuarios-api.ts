import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { ApiClient } from '@core/http/api-client';
import { Pagina, PaginaParams } from '@core/models/comum.model';
import { Usuario, UsuarioHistorico, UsuarioRequest } from '@core/models/portfolio.model';

export interface UsuarioFiltro extends PaginaParams {
  termo?: string;
  tipoUsuarioId?: number;
  usuarioBancoDadosId?: number;
  somenteVigentes?: boolean;
}

export interface UsuarioHistoricoFiltro extends PaginaParams {
  username?: string;
  tipoUsuarioId?: number;
  operacao?: string;
}

@Injectable({ providedIn: 'root' })
export class UsuariosApi {
  private readonly api = inject(ApiClient);

  listar(filtro: UsuarioFiltro = {}): Observable<Pagina<Usuario>> {
    return this.api.pagina<Usuario>('/usuarios', filtro);
  }

  listarHistorico(filtro: UsuarioHistoricoFiltro = {}): Observable<Pagina<UsuarioHistorico>> {
    return this.api.pagina<UsuarioHistorico>('/usuarios/historico', filtro);
  }

  buscar(username: string): Observable<Usuario> {
    return this.api.get<Usuario>(`/usuarios/${encodeURIComponent(username)}`);
  }

  criar(request: UsuarioRequest): Observable<Usuario> {
    return this.api.post<Usuario>('/usuarios', request);
  }

  alterar(username: string, request: UsuarioRequest): Observable<Usuario> {
    return this.api.put<Usuario>(`/usuarios/${encodeURIComponent(username)}`, request);
  }

  inativar(username: string, fimVigencia?: string | null): Observable<Usuario> {
    return this.api.patch<Usuario>(
      `/usuarios/${encodeURIComponent(username)}/inativar`,
      undefined,
      fimVigencia ? { dataFim: fimVigencia } : undefined
    );
  }

  excluir(username: string): Observable<void> {
    return this.api.delete(`/usuarios/${encodeURIComponent(username)}`);
  }
}
