import { ActivatedRouteSnapshot, CanActivateFn, Router } from '@angular/router';
import { Auth } from '../services/auth';
import { inject } from '@angular/core';

export const roleGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state) => {
  const auth = inject(Auth);
  const router = inject(Router);

  const requiredRoles = route.data['roles'] as string[] | undefined;

  if (!auth.isAuthenticated()) {
    router.navigate(['/login']);
    return false;
  }

  if (!requiredRoles || requiredRoles.length === 0) {
    return true;
  }

  const hasAccess = requiredRoles.some(role => auth.hasRole(role));

  if (!hasAccess) {
    router.navigate(['/unauthorized']);
    return false;
  }

  return true;
};
