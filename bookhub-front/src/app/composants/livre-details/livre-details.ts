import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Header } from '../../layout/header/header';
import { Livre } from '../../core/modeles/livre';
import { CatalogueService } from '../../core/services/catalogue-service';
import { AuthService } from '../../core/services/auth-service';
import { EmpruntService } from '../../core/services/emprunt-service';
import { BookRatingComponent } from '../book-rating/book-rating';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-livre-details',
  imports: [
    Header,
    RouterLink,
    BookRatingComponent,
    DatePipe
  ],
  templateUrl: './livre-details.html',
  styleUrl: './livre-details.css',
})
export class LivreDetails implements OnInit {
  private route = inject(ActivatedRoute);
  private catalogueService: CatalogueService = inject(CatalogueService);
  private authService: AuthService = inject(AuthService);
  private empruntService: EmpruntService = inject(EmpruntService);
  private router: Router = inject(Router);

  id = 0;

  livre = signal<Livre|null>(null)

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.id = +params['id'];

      this.catalogueService.getLivre(this.id).subscribe(livre => {
        this.livre.set(livre);
      });
    });
  }

  protected bookCoverUrl(livre: Livre) {
    return "http://localhost:8080/api/books/" + this.id + "/cover";
  }

  protected canComment(): boolean {
    return this.authService.estConnecte();
  }

  protected onClickReserver(): void {
    if (!this.authService.estConnecte()) {
      this.router.navigate(['/login'], {queryParams: { redirectTo: `/books/${this.id}` }});
      return;
    }

    // @TODO
  }

  protected onClickEmprunter()  {
    if (!this.authService.estConnecte()) {
      this.router.navigate(['/login'], {queryParams: { redirectTo: `/books/${this.id}` }});
      return;
    }

    this.empruntService.emprunterLivre(this.id).subscribe({
      next: (response: any) => {
        console.log('Emprunt réussi :', response);
        alert('✅ ' + response.message);

        // Recharger les données du livre pour mettre à jour la disponibilité
        this.catalogueService.getLivre(this.id).subscribe(livre => {
          this.livre.set(livre);
        });
      },
      error: (error: any) => {
        console.error('Erreur lors de l\'emprunt :', error);
        const errorMessage = error.error || 'Une erreur s\'est produite lors de l\'emprunt.';
        alert('❌ Erreur : ' + errorMessage);
      }
    });
  }
}
