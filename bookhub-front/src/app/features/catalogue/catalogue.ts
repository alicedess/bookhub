import {Component, inject, OnInit} from '@angular/core';
import {Header} from "../../layout/header/header";
import {CatalogueService} from '../../core/services/catalogue-service';

@Component({
  selector: 'app-catalogue',
    imports: [
        Header
    ],
  templateUrl: './catalogue.html',
  styleUrl: './catalogue.css',
})
export class Catalogue implements OnInit {
    private catalogueService: CatalogueService = inject(CatalogueService);



    ngOnInit(): void {
        this.catalogueService.getAll().subscribe({
          next: (value) => {
            console.log(value);
          }
        })
    }
}
