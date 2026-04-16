import { Component, effect, inject, OnInit, signal } from '@angular/core';
import { Header } from '../../layout/header/header';
import { CatalogueService } from '../../core/services/catalogue-service';
import { CategorieService } from '../../core/services/categorie-service';
import { RouterLink } from '@angular/router';
import { Pagination } from '../shared/pagination/pagination';

@Component({
  selector: 'app-catalogue',
  imports: [Header, RouterLink, Pagination],
  templateUrl: './catalogue.html',
  styleUrl: './catalogue.css',
})
export class Catalogue implements OnInit {
  private catalogueService: CatalogueService = inject(CatalogueService);
  private categorieService: CategorieService = inject(CategorieService);

  protected categories          = this.categorieService.categories;
  protected searchResult        = this.catalogueService.searchResult;
  protected searchQuery         = signal<string>('');
  protected categorieFilter     = signal<number>(0);
  protected disponibiliteFilter = signal<number>(0);
  protected currentPage         = signal(0);

  private _t: number | null = null;

  search = effect(() => {
    this.catalogueService.search(
      this.searchQuery(),
      this.categorieFilter(),
      this.disponibiliteFilter(),
      this.currentPage(),
    );
  });

  ngOnInit(): void {
    this.categorieService.refresh();
  }

  protected onSearchUpdated(sq: string): void {
    clearTimeout(this._t as number);
    this._t = setTimeout(() => {
      this.currentPage.set(0);
      this.searchQuery.set(sq.trim());
    }, 400) as unknown as number;
  }

  protected onSelectCategorie(event: Event): void {
    this.currentPage.set(0);
    this.categorieFilter.set(Number.parseInt((event.target as HTMLSelectElement).value, 10));
  }

  protected onSelectDisponibilite(event: Event): void {
    this.currentPage.set(0);
    this.disponibiliteFilter.set(Number.parseInt((event.target as HTMLSelectElement).value, 10));
  }
}
