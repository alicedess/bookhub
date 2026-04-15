import { Component, effect, inject, OnInit, signal } from '@angular/core';
import { Header } from '../../../layout/header/header';
import { CatalogueService } from '../../../core/services/catalogue-service';
import { RouterLink } from '@angular/router';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-liste',
  imports: [
    Header,
    RouterLink,
    DatePipe
  ],
  templateUrl: './liste.html',
  styleUrl: './liste.css',
})
export class Liste implements OnInit {
  private catalogueService: CatalogueService = inject(CatalogueService);
  protected searchResult = this.catalogueService.searchResult;
  protected searchQuery = signal<string>('');
  protected currentPage = signal(0);

  search = effect(() => {
    const query = this.searchQuery();
    const page = this.currentPage();

    this.catalogueService.search(query, 0, 0, page);
  });

  private _t: number|null = null;

  ngOnInit() {
    this.catalogueService.search("", 0, 0, 1);
  }

  protected onSearchUpdated(sq: string) {
    clearTimeout(this._t as number);

    this._t = setTimeout(() => {
      this.searchQuery.set(sq);
    }, 500) as unknown as number;
  }
}
