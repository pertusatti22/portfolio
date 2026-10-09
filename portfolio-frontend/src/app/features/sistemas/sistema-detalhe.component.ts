import { Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'pf-sistema-detalhe',
  standalone: true,
  template: `
    <h2>Detalhe do Sistema</h2>
    <p>Detalhes serão carregados para ID: {{ id }}</p>
  `,
  styles: []
})
export class SistemaDetalheComponent {
  private readonly route = inject(ActivatedRoute);
  readonly id = this.route.snapshot.paramMap.get('id');
}
