import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CatalogueService {
    private readonly URL_BASE = environment.URL_BASE + '/books';

    private httpClient: HttpClient = inject(HttpClient);

    getAll(): Observable<any> {
      return this.httpClient.get(this.URL_BASE)
    }

}
