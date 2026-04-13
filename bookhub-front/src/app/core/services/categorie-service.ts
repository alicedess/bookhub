import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Categorie } from '../modeles/categorie';

@Injectable({
  providedIn: 'root'
})
export class CategorieService {
  private readonly URL_BASE = environment.URL_BASE + '/categories';

  private httpClient: HttpClient = inject(HttpClient);

  private _categories = signal<Categorie[]>([]);
  categories = this._categories.asReadonly();

  public refresh() {
    this.httpClient.get<Categorie[]>(this.URL_BASE).subscribe({
      next: (payload) => {
        this._categories.set(payload);
      }
    });
  }

}
