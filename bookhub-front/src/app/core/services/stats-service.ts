import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Stats } from '../modeles/stats';

@Injectable({
  providedIn: 'root',
})
export class StatsService {
  private readonly URL_BASE = environment.URL_BASE + '/stats';

  private httpClient: HttpClient = inject(HttpClient);

  getStats(): Observable<Stats> {
    return this.httpClient.get<Stats>(this.URL_BASE);
  }
}
