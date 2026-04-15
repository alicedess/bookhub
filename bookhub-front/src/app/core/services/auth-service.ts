import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

export interface DemandeInscription {
  prenom: string;
  nom: string;
  dateNaissance: string;
  email: string;
  password: string;
  telephone: string;
}

export interface DemandeConnexion {
  email: string;
  password: string;
}

export interface ReponseConnexion {
  token: string;
}

export interface UpdateProfilDTO {
  oldPassword?: string;
  profil: {
    prenom?: string;
    nom?: string;
    telephone?: string;
    password?: string;
    pseudo?: string;
  }
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly URL_BASE = 'http://localhost:8080/api/auth';
  private readonly URL_BASE_USER = 'http://localhost:8080/api/user';
  private readonly CLE_TOKEN = 'bookhub_token';

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  sinscrire(donnees: DemandeInscription): Observable<void> {
    return this.http.post<void>(`${this.URL_BASE}/register`, donnees);
  }

  seConnecter(donnees: DemandeConnexion): Observable<ReponseConnexion> {
    return this.http.post<ReponseConnexion>(`${this.URL_BASE}/login`, donnees);
  }

  sauvegarderToken(token: string): void {
    localStorage.setItem(this.CLE_TOKEN, token);
  }

  obtenirToken(): string | null {
    return localStorage.getItem(this.CLE_TOKEN);
  }

  // vérifie que l'utilisateur est connecté en vérifiant le token
  estConnecte(): boolean {
    const token = this.obtenirToken();
    if (!token) return false;
    try {
      // on découpe le token en 3 parties (header [0], payload [1], signature[2]), on récupère le payload et on vérifie que la date d'expiration (exp) n'est pas dépassée (multiplié par 1000 pour comparer les deux dans la même unité)
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.exp * 1000 > Date.now();
    } catch {
      return false;
    }
  }

  // récupère les informations de l'utilisateur connecté (payload du token), créée pour le composant profil
  obtenirUtilisateur(): { sub: string; role: string } | null {
    const token = this.obtenirToken();
    if (!token) return null;
    try {
      return JSON.parse(atob(token.split('.')[1]));
    } catch {
      return null;
    }
  }

  obtenirProfilUtilisateur(): Observable<any> {
    return this.http.get(`${this.URL_BASE_USER}/me`);
  }

  // on récupère le rôle contenu dans le payload du back
  obtenirRole(): string | null {
    const token = this.obtenirToken();
    if (!token) return null;
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const role = payload.role ?? null;

      if (!role) {
        return null;
      }

      return role.replace('ROLE_', '');
    } catch {
      return null;
    }
  }

  obtenirUtilisateur(): { email: string; nom: string; prenom: string } | null {
    const token = this.obtenirToken();
    if (!token) return null;
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return {
        email: payload.email,
        nom: payload.nom,
        prenom: payload.prenom
      };
    } catch {
      return null;
    }
  }

  seDeconnecter(): void {
    localStorage.removeItem(this.CLE_TOKEN);
    this.router.navigate(['/connexion']);
  }

  modifierProfil(donnees: UpdateProfilDTO): Observable<void> {
    return this.http.put<void>(`${this.URL_BASE_USER}/me`, donnees);
  }

  supprimerCompte(): Observable<void> {
    return this.http.delete<void>(`${this.URL_BASE_USER}/me`);
  }
}
