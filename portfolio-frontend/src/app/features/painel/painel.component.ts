import { Component, inject } from '@angular/core';

import { PainelApi } from '@core/http/painel-api';

@Component({
  selector: 'pf-painel',
  standalone: true,
  template: `
    <h1>Portifolio de Sistemas</h1>
    <p>Indicadores serão carregados via API.</p>
  `,
  styles: []
})
export class PainelComponent {
  readonly painelApi = inject(PainelApi);
}
