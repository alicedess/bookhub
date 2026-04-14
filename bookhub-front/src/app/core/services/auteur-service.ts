import { inject, Injectable, signal } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import { Auteur } from '../modeles/auteur';

@Injectable({
  providedIn: 'root'
})
export class AuteurService {
    private readonly URL_BASE = environment.URL_BASE + '/authors';

    private httpClient: HttpClient = inject(HttpClient);

    private _authors = signal<Auteur[]>([]);
    authors = this._authors.asReadonly();

    public refresh() {
      this.httpClient.get<Auteur[]>(this.URL_BASE).subscribe({
        next: (payload) => {
          this._authors.set(payload);
        }
      });
    }

}
