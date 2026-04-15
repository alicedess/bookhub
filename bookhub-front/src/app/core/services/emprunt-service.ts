import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { EmpruntCreationResponse, MesEmpruntsResponse } from '../modeles/emprunt';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class EmpruntService {
  private readonly URL_BASE = environment.URL_BASE + '/loans';
  private readonly URL_MY_LOANS = this.URL_BASE + '/my';

  private httpClient: HttpClient = inject(HttpClient);

  getMesEmprunts(): Observable<MesEmpruntsResponse> {
    return this.httpClient.get<MesEmpruntsResponse>(this.URL_MY_LOANS);
  }

  emprunterLivre(idLivre: number): Observable<EmpruntCreationResponse> {
    return this.httpClient.post<EmpruntCreationResponse>(`${this.URL_BASE}?idLivre=${idLivre}`, {});
  }
}
