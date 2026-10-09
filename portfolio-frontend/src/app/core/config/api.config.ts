import { InjectionToken } from '@angular/core';
import { environment } from '@env/environment';

export interface ApiConfig {
  readonly baseUrl: string;
}

/**
 * Endereco base da API, injetavel para permitir apontar o mesmo componente
 * de biblioteca para ambientes diferentes sem recompilar a aplicacao.
 */
export const API_CONFIG = new InjectionToken<ApiConfig>('pf.api.config', {
  providedIn: 'root',
  factory: (): ApiConfig => ({ baseUrl: environment.apiBaseUrl })
});
