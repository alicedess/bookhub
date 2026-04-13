import { inject, Injectable, signal } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';
import { ApiPageResponse } from '../modeles/api-page-response';
import { type Livre } from '../modeles/livre';
import { Categorie } from '../modeles/categorie';

@Injectable({
  providedIn: 'root'
})
export class CatalogueService {
    private readonly URL_BASE = environment.URL_BASE + '/books';

    private httpClient: HttpClient = inject(HttpClient);

    private _searchResult = signal<null|ApiPageResponse<Livre[]>>(null);
    searchResult = this._searchResult.asReadonly();

    search(query: string, categorieFilter: number, disponibiliteFilter: number) {
      const filters = [
        `query=${encodeURIComponent(query)}`,
        `categorie=${encodeURIComponent(categorieFilter)}`,
        `disponibilite=${encodeURIComponent(disponibiliteFilter)}`,
      ].join('&');

      console.log(filters);

      this.httpClient.get<ApiPageResponse<Livre[]>>(`${this.URL_BASE}?${filters}`).subscribe({
        next: (payload) => {
          this._searchResult.set(payload);
        }
      })
    }

}
