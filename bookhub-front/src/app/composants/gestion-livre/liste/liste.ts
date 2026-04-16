import { Component, effect, inject, OnInit, signal } from '@angular/core';
import { CatalogueService } from '../../../core/services/catalogue-service';
import { RouterLink } from '@angular/router';
import { DatePipe } from '@angular/common';
import { Pagination } from '../../shared/pagination/pagination';

@Component({
  selector: 'app-liste',
  imports: [RouterLink, DatePipe, Pagination],
  templateUrl: './liste.html',
  styleUrl: './liste.css',
})
export class Liste implements OnInit {
  private catalogueService: CatalogueService = inject(CatalogueService);

  protected searchResult = this.catalogueService.searchResult;
  protected searchQuery  = signal<string>('');
  protected currentPage  = signal(0);

  search = effect(() => {
    this.catalogueService.search(this.searchQuery(), 0, 0, this.currentPage());
  });

  private _t: number | null = null;

  ngOnInit(): void {
    this.catalogueService.search('', 0, 0, 0);
  }

  protected onSearchUpdated(sq: string): void {
    clearTimeout(this._t as number);
    this._t = setTimeout(() => {
      this.currentPage.set(0); // retour à la page 1 sur nouvelle recherche
      this.searchQuery.set(sq.trim());
    }, 400) as unknown as number;
  }
}
