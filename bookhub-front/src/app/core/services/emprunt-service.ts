import { inject, Injectable, signal } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { EmpruntCreationResponse, EmpruntDTO, MesEmpruntsResponse } from '../modeles/emprunt';
import {Observable} from 'rxjs';
import { ApiPageResponse } from '../modeles/api-page-response';

@Injectable({
  providedIn: 'root',
})
export class EmpruntService {
  private readonly URL_BASE = environment.URL_BASE + '/loans';
  private readonly URL_MY_LOANS = this.URL_BASE + '/my';

  private httpClient: HttpClient = inject(HttpClient);

  private _searchResult = signal<null | ApiPageResponse<EmpruntDTO[]>>(null);
  searchResult = this._searchResult.asReadonly();

  search(query: string, page: number) {
    const params = new HttpParams()
      .set('query', query)
      .set('page', page)

    this.httpClient.get<ApiPageResponse<EmpruntDTO[]>>(`${this.URL_BASE}?${params.toString()}`).subscribe({
      next: (payload) => {
        this._searchResult.set(payload);
      }
    });
  }

  getMesEmprunts(): Observable<MesEmpruntsResponse> {
    return this.httpClient.get<MesEmpruntsResponse>(this.URL_MY_LOANS);
  }

  emprunterLivre(idLivre: number): Observable<EmpruntCreationResponse> {
    return this.httpClient.post<EmpruntCreationResponse>(`${this.URL_BASE}?idLivre=${idLivre}`, {});
  }
}
