import { ActivatedRouteSnapshot, CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth-service';
import { inject } from '@angular/core';


  // Hiérarchie des rôles = ADMIN > LIBRARIAN > USER
   // un admin 3 peut donc accéder à tout ce qu'un user 1 ou librarian 2 peut voir
  const HIERARCHIE_ROLES: Record<string, number> = {
    USER: 1,
    LIBRARIAN: 2,
    ADMIN: 3,
  };

  export const roleGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
    const authService = inject(AuthService);
    const router = inject(Router);

    const roleRequis: string = route.data['role'];
    const roleUtilisateur = authService.obtenirRole();
  
    if (!roleUtilisateur || !roleRequis) {
      return router.createUrlTree(['/login']);
    }

    // rôle de niveau 99 = inconnu, donc pas accès
    const niveauRequis = HIERARCHIE_ROLES[roleRequis] ?? 99; // rôle inconnu
    const niveauUtilisateur = HIERARCHIE_ROLES[roleUtilisateur] ?? 0; // pas de rôle
  
    if (niveauUtilisateur >= niveauRequis) {
      return true;
    }

    // 403 si rôle pas suffisant
    return router.createUrlTree(['/forbidden']);
};
