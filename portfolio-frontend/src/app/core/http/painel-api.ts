import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { ApiClient } from '@core/http/api-client';
import { Painel } from '@core/models/painel.model';

@Injectable({ providedIn: 'root' })
export class PainelApi {
  private readonly api = inject(ApiClient);

  resumo(): Observable<Painel> {
    return this.api.get<Painel>('/painel/resumo');
  }
}
