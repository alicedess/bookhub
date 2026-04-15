import { Livre } from '../modeles/livre';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Evaluation } from '../modeles/evaluation';

@Injectable({
  providedIn: 'root',
})
export class EvaluationService {
  private readonly URL_BASE = environment.URL_BASE + '/books';

  private httpClient: HttpClient = inject(HttpClient);

  public getEvaluations(livre: Livre) {
    return this.httpClient.get<Evaluation[]>(`${this.URL_BASE}/${livre.id}/ratings`);
  }
}
