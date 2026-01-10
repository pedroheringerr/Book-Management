import { Routes } from '@angular/router';
import { authGuard } from './guard/auth-guard';
import { roleGuard } from './guard/role-guard';
import { Charts } from './pages/charts/charts';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    loadComponent: () =>
      import('./pages/home/home').then(m => m.Home)
  },
  {
    path: 'login',
    loadComponent: () =>
      import('./pages/login/login').then(m => m.Login)
  },
  {
    path: 'signup',
    loadComponent: () =>
      import('./pages/signup/signup').then(m => m.Signup)
  },
  {
    path: 'books/edit/:isbn',
    loadComponent: () =>
      import('./pages/edit-book/edit-book').then(m => m.EditBook),
    canActivate: [authGuard, roleGuard],
    data: { roles: [ 'ADMIN', 'USER' ] }
  },
  {
    path:'books/add',
    loadComponent: () =>
      import('./pages/edit-book/edit-book').then(m => m.EditBook),
    canActivate: [authGuard, roleGuard],
    data: { roles: [ 'ADMIN', 'USER' ] }
  },
  {
    path:'users',
    loadComponent: () =>
      import('./pages/user/user').then(m => m.User),
    canActivate: [authGuard, roleGuard],
    data: { roles: [ 'ADMIN' ] }
  },
  {
    path: 'users/edit/:id',
    loadComponent: () =>
      import('./pages/edit-user/edit-user').then(m => m.EditUser),
    canActivate: [authGuard, roleGuard],
    data: { roles: [ 'ADMIN' ] }
  },
  {
  path: 'charts',
  component: Charts
  }

];
