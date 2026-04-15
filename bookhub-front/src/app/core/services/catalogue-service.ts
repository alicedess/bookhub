import { inject, Injectable, signal } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { ApiPageResponse } from '../modeles/api-page-response';
import { type Livre } from '../modeles/livre';
import { Exemplaire } from '../modeles/exemplaire';

@Injectable({
  providedIn: 'root'
})
export class CatalogueService {
  private readonly URL_BASE = environment.URL_BASE + '/books';

  private httpClient: HttpClient = inject(HttpClient);

  private _searchResult = signal<null | ApiPageResponse<Livre[]>>(null);
  searchResult = this._searchResult.asReadonly();

  search(query: string, categorieFilter: number, disponibiliteFilter: number, page: number) {
    const params = new HttpParams()
      .set('query', query)
      .set('page', page)
      .set('categorie', categorieFilter)
      .set('disponibilite', disponibiliteFilter);

    this.httpClient.get<ApiPageResponse<Livre[]>>(`${this.URL_BASE}?${params.toString()}`).subscribe({
      next: (payload) => {
        this._searchResult.set(payload);
      }
    });
  }

  getLivre(id: number) {
    return this.httpClient.get<Livre>(`${this.URL_BASE}/${id}`);
  }

  create(data: Partial<Livre>) {
    return this.httpClient.post<Livre>(`${this.URL_BASE}`, data);
  }

  update(livre: Livre, data: Partial<Livre>) {
    return this.httpClient.put(`${this.URL_BASE}/${livre.id}`, data);
  }

  updateCover(livre: Livre, file: File) {
    const formData = new FormData();
    formData.append('file', file, file.name);

    return this.httpClient.put(`${this.URL_BASE}/${livre.id}/cover`, formData);
  }

  getExemplaires(livre: Livre) {
    return this.httpClient.get<Exemplaire[]>(`${this.URL_BASE}/${livre.id}/exemplaires`)
  }

  saveExemplaires(livre: Livre, data: Partial<Exemplaire>[]) {
    return this.httpClient.post(`${this.URL_BASE}/${livre.id}/exemplaires`, data)
  }
}
