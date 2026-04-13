import { Component, inject, OnInit, signal, effect } from '@angular/core';
import { Header } from '../../layout/header/header';
import { CatalogueService } from '../../core/services/catalogue-service';
import { JsonPipe } from '@angular/common';
import { CategorieService } from '../../core/services/categorie-service';

@Component({
  selector: 'app-catalogue',
  imports: [
    Header,
    JsonPipe
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

  search = effect(() => {
    const query = this.searchQuery();
    const categorieFilter = this.categorieFilter();
    const disponibiliteFilter = this.disponibiliteFilter();

    this.catalogueService.search(query, categorieFilter, disponibiliteFilter);
  });

  ngOnInit(): void {
    this.categorieService.refresh();
    this.catalogueService.search(this.searchQuery(), this.categorieFilter(), this.disponibiliteFilter());
  }

  protected onSearchUpdated(sq: string) {
    this.searchQuery.set(sq);
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
