import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import { MesEmpruntsResponse } from '../modeles/emprunt';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class EmpruntService {
  private readonly URL_BASE = environment.URL_BASE + '/loans/my';

  private httpClient: HttpClient = inject(HttpClient);

  getMesEmprunts(): Observable<MesEmpruntsResponse> {
    return this.httpClient.get<MesEmpruntsResponse>(this.URL_BASE);
  }
}
