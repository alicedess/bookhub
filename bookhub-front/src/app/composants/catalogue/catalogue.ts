import { Component, inject, OnInit, signal, effect } from '@angular/core';
import { Header } from '../../layout/header/header';
import { CatalogueService } from '../../core/services/catalogue-service';
import { CategorieService } from '../../core/services/categorie-service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-catalogue',
  imports: [
    Header,
    RouterLink
  ],
  templateUrl: './catalogue.html',
  styleUrl: './catalogue.css'
})
export class Catalogue implements OnInit {
  private catalogueService: CatalogueService = inject(CatalogueService);
  private categorieService: CategorieService = inject(CategorieService);

  protected categories = this.categorieService.categories;

  protected searchQuery = signal<string>('');
  protected categorieFilter = signal<number>(0);
  protected disponibiliteFilter = signal<number>(0);

  protected searchResult = this.catalogueService.searchResult;

  protected currentPage = signal(0);

  private _t: number|null = null;

  search = effect(() => {
    const query = this.searchQuery();
    const categorieFilter = this.categorieFilter();
    const disponibiliteFilter = this.disponibiliteFilter();
    const page = this.currentPage();

    this.catalogueService.search(query, categorieFilter, disponibiliteFilter, page);
  });

  ngOnInit(): void {
    this.categorieService.refresh();
    this.catalogueService.search(this.searchQuery(), this.categorieFilter(), this.disponibiliteFilter(), this.currentPage());
  }

  protected onSearchUpdated(sq: string) {
    clearTimeout(this._t as number);

    this._t = setTimeout(() => {
      this.searchQuery.set(sq.trim());
    }, 500) as unknown as number;
  }

  protected onSelectCategorie(event: Event) {
    const target = event.target as HTMLSelectElement;
    const value = Number.parseInt(target.value, 10);

    this.categorieFilter.set(value);
  }

  protected onSelectDisponibilite(event: Event) {
    const target = event.target as HTMLSelectElement;
    const value = Number.parseInt(target.value, 10);

    this.disponibiliteFilter.set(value);
  }
}
