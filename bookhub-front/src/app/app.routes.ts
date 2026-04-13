import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth-guard';
import { roleGuard } from './core/guards/role-guard';

export const routes: Routes = [
// CF docs/Navigation.md
// Pages publiques
//   { path: '', loadComponent: () => import('./composants/catalogue/') },
  { path: 'login', loadComponent: () => import('./composants/authentification/connexion/connexion') },
  { path: 'register', loadComponent: () => import('./composants/authentification/inscription/inscription') },
//   { path: 'books', loadComponent: () => import('') },
//   { path: 'books/:id', loadComponent: () => import('') },

 
// rôle USER
//   {
//     path: 'dashboard',
//     canActivate: [authGuard],  
//     loadComponent: () => import('')
//   },
//   {
//     path: 'loans/my',
//     canActivate: [authGuard],
//     loadComponent: () => import('')
//   },
//   {
//     path: 'reservations/my',
//     canActivate: [authGuard],
//     loadComponent: () => import('')
//   },
  { path: 'profile', canActivate: [authGuard], loadComponent: () => import('./composants/authentification/profil/profil') },
  

// rôle LIBRARIAN 
  {
    path: 'librarian',
    canActivate: [authGuard, roleGuard],
    data: { role: 'LIBRARIAN' },
    children: [
    //   { path: '', loadComponent: () => import('') },
    //   { path: 'books', loadComponent: () => import('') },
    //   { path: 'books/new', loadComponent: () => import('') },
    //   { path: 'books/:id/edit', loadComponent: () => import('') },
    //   { path: 'loans', loadComponent: () => import('') },
    //   { path: 'reviews', loadComponent: () => import('') },
    ]
  },

// rôle ADMIN
  {
    path: 'admin',
    canActivate: [authGuard, roleGuard],
    data: { role: 'ADMIN' },
    children: [
    //   { path: '', loadComponent: () => import('') },
    //   { path: 'users', loadComponent: () => import('') },
    ]
  },
];