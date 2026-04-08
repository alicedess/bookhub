import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

export interface DemandeInscription {
  prenom: string;
  nom: string;
  email: string;
  motDePasse: string;
}

export interface DemandeConnexion {
  email: string;
  motDePasse: string;
}

export interface ReponseConnexion {
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly URL_BASE = 'http://localhost:8080/api/auth';
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

  estConnecte(): boolean {
    const token = this.obtenirToken();
    if (!token) return false;
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.exp * 1000 > Date.now();
    } catch {
      return false;
    }
  }

  obtenirRole(): string | null {
    const token = this.obtenirToken();
    if (!token) return null;
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.role ?? null;
    } catch {
      return null;
    }
  }

  seDeconnecter(): void {
    localStorage.removeItem(this.CLE_TOKEN);
    this.router.navigate(['/connexion']);
  }
}