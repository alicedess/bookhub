import { HttpInterceptorFn } from '@angular/common/http';
import { AuthService } from '../services/auth-service';
import { inject } from '@angular/core';

// l'intercepteur jwt ajoute automatiquement le token d'authentification (Authorization: `Bearer) à chaque requête sortante (HTTP vers API)
export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.obtenirToken();

  if (token && req.url.startsWith('http://localhost:8080')) {
    const requeteAvecToken = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(requeteAvecToken);
  }
  return next(req);
};
