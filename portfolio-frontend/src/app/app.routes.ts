import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'painel'
  },
  {
    path: 'painel',
    loadComponent: () => import('./features/painel/painel.component').then((m) => m.PainelComponent)
  },
  {
    path: 'sistemas',
    loadComponent: () => import('./features/sistemas/sistemas-lista.component').then((m) => m.SistemasListaComponent)
  },
  {
    path: 'sistemas/:id',
    loadComponent: () => import('./features/sistemas/sistema-detalhe.component').then((m) => m.SistemaDetalheComponent)
  },
  {
    path: '**',
    redirectTo: 'painel'
  }
];
