import { inject, Injectable, signal } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { EmpruntDTO, EmpruntResponse, MesEmpruntsResponse} from '../modeles/emprunt';
import { ApiPageResponse } from '../modeles/api-page-response';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class EmpruntService {
  private readonly URL_BASE = environment.URL_BASE + '/loans';
  private readonly URL_MY_LOANS = this.URL_BASE + '/my';
  private readonly URL_RETURN = this.URL_BASE + '/{idEmprunt}/return';

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

  emprunterLivre(idLivre: number): Observable<EmpruntResponse> {
    return this.httpClient.post<EmpruntResponse>(`${this.URL_BASE}?idLivre=${idLivre}`, {});
  }

  getTousLesEmprunts(page = 0, size = 20, sortBy = 'dateDebut'): Observable<ApiPageResponse<EmpruntDTO[]>> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sortBy', sortBy);

    return this.httpClient.get<ApiPageResponse<EmpruntDTO[]>>(this.URL_BASE, { params });
  }

  retournerLivre(idEmprunt: number) : Observable<EmpruntResponse> {
    return this.httpClient.put<EmpruntResponse>(
      this.URL_RETURN.replace('{idEmprunt}', String(idEmprunt)),
      {},
    );
  }
}
