import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { API_CONFIG } from '@core/config/api.config';
import { Pagina, PaginaParams } from '@core/models/comum.model';

/**
 * Cliente HTTP padronizado da API, responsible por montar a URL base e os
 * parametros de paginacao, evitando repeticao em todos os servicos.
 */
@Injectable({ providedIn: 'root' })
export class ApiClient {
  private readonly http = inject(HttpClient);
  private readonly config = inject(API_CONFIG);

  get<T>(recurso: string, params?: Record<string, string | number | boolean | undefined>): Observable<T> {
    return this.http.get<T>(this.url(recurso), { params: this.toParams(params) });
  }

  post<T>(recurso: string, corpo: unknown): Observable<T> {
    return this.http.post<T>(this.url(recurso), corpo);
  }

  put<T>(recurso: string, corpo: unknown): Observable<T> {
    return this.http.put<T>(this.url(recurso), corpo);
  }

  patch<T>(
    recurso: string,
    corpo?: unknown,
    params?: Record<string, string | number | boolean | undefined>
  ): Observable<T> {
    return this.http.patch<T>(this.url(recurso), corpo ?? {}, { params: this.toParams(params) });
  }

  delete(recurso: string): Observable<void> {
    return this.http.delete<void>(this.url(recurso));
  }

  pagina<T>(recurso: string, paginacao?: PaginaParams): Observable<Pagina<T>> {
    return this.get<Pagina<T>>(recurso, {
      pagina: paginacao?.pagina ?? 0,
      tamanho: paginacao?.tamanho,
      ordenacao: paginacao?.ordenacao,
      crescente: paginacao?.crescente
    });
  }

  private url(recurso: string): string {
    const base = this.config.baseUrl.replace(/\/$/, '');
    const caminho = recurso.startsWith('/') ? recurso : `/${recurso}`;
    return `${base}${caminho}`;
  }

  private toParams(params?: Record<string, string | number | boolean | undefined>): HttpParams | undefined {
    if (!params) {
      return undefined;
    }
    return Object.entries(params).reduce<HttpParams>((acumulado, [chave, valor]) => {
      return valor === undefined || valor === null || valor === ''
        ? acumulado
        : acumulado.set(chave, String(valor));
    }, new HttpParams());
  }
}
