import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';

import { SistemasApi } from '@core/http/sistemas-api';

@Component({
  selector: 'pf-sistemas-lista',
  standalone: true,
  template: `
    <h2>Sistemas</h2>
    <button type="button" (click)="irParaDetalhe(1)">Exemplo</button>
  `,
  styles: []
})
export class SistemasListaComponent {
  private readonly api = inject(SistemasApi);
  private readonly router = inject(Router);

  irParaDetalhe(id: number): void {
    this.router.navigate(['/sistemas', id]);
  }
}
