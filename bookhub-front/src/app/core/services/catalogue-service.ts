import { inject, Injectable, signal } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import {environment} from '../../../environments/environment';
import { ApiPageResponse } from '../modeles/api-page-response';
import { type Livre } from '../modeles/livre';

@Injectable({
  providedIn: 'root'
})
export class CatalogueService {
    private readonly URL_BASE = environment.URL_BASE + '/books';

    private httpClient: HttpClient = inject(HttpClient);

    private _searchResult = signal<null|ApiPageResponse<Livre[]>>(null);
    searchResult = this._searchResult.asReadonly();

    search(query: string, categorieFilter: number, disponibiliteFilter: number, page: number) {
      const params = new HttpParams()
        .set('query', query)
        .set("page", page)
        .set("categorie", categorieFilter)
        .set("disponibilite", disponibiliteFilter);

      this.httpClient.get<ApiPageResponse<Livre[]>>(`${this.URL_BASE}?${params.toString()}`).subscribe({
        next: (payload) => {
          this._searchResult.set(payload);
        }
      })
    }

}
