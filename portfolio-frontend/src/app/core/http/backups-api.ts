import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { ApiClient } from '@core/http/api-client';
import { Pagina, PaginaParams } from '@core/models/comum.model';
import { Backup } from '@core/models/portfolio.model';

export interface BackupFiltro extends PaginaParams {
  termo?: string;
  servidorId?: number;
  ativo?: boolean;
}

@Injectable({ providedIn: 'root' })
export class BackupsApi {
  private readonly api = inject(ApiClient);

  listar(filtro: BackupFiltro = {}): Observable<Pagina<Backup>> {
    return this.api.pagina<Backup>('/backups', filtro);
  }

  buscar(id: number): Observable<Backup> {
    return this.api.get<Backup>(`/backups/${id}`);
  }
}
