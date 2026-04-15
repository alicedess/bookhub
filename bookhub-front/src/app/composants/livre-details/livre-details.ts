import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Header } from '../../layout/header/header';
import { Livre } from '../../core/modeles/livre';
import { CatalogueService } from '../../core/services/catalogue-service';
import { AuthService } from '../../core/services/auth-service';
import { EmpruntService } from '../../core/services/emprunt-service';
import { BookRatingComponent } from '../book-rating/book-rating';
import { DatePipe, DecimalPipe} from '@angular/common';
import { Evaluation } from '../../core/modeles/evaluation';
import { EvaluationService } from '../../core/services/evaluation-service';

@Component({
  selector: 'app-livre-details',
  imports: [Header, RouterLink, BookRatingComponent, DatePipe, DecimalPipe],
  templateUrl: './livre-details.html',
  styleUrl: './livre-details.css',
})
export class LivreDetails implements OnInit {
  private route             = inject(ActivatedRoute);
  private catalogueService  = inject(CatalogueService);
  private authService       = inject(AuthService);
  private empruntService    = inject(EmpruntService);
  private evaluationService = inject(EvaluationService);
  private router            = inject(Router);

  id          = 0;
  livre       = signal<Livre | null>(null);
  evaluations = signal<Evaluation[]>([]);
  chargement  = signal(true);
  flash       = signal<{ type: 'succes' | 'erreur'; texte: string } | null>(null);

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.id = +params['id'];
      this.chargement.set(true);

      this.catalogueService.getLivre(this.id).subscribe({
        next: (livre) => {
          this.livre.set(livre);
          this.chargement.set(false);
          this.evaluationService.getEvaluations(livre).subscribe({
            next: (evals) => this.evaluations.set(evals),
          });
        },
        error: () => this.chargement.set(false),
      });
    });
  }

  protected bookCoverUrl(): string {
    return `http://localhost:8080/api/books/${this.id}/cover`;
  }

  protected canComment(): boolean {
    return false; // @TODO
  }

  protected onClickReserver(): void {
    if (!this.authService.estConnecte()) {
      this.router.navigate(['/login'], { queryParams: { redirectTo: `/books/${this.id}` } });
    }
    // @TODO
  }

  protected onClickEmprunter(): void {
    if (!this.authService.estConnecte()) {
      this.router.navigate(['/login'], { queryParams: { redirectTo: `/books/${this.id}` } });
      return;
    }

    this.empruntService.emprunterLivre(this.id).subscribe({
      next: (response) => {
        this.afficherFlash('succes', response.message);
        this.catalogueService.getLivre(this.id).subscribe(l => this.livre.set(l));
      },
      error: (err) => {
        const msg = err?.error ?? 'Une erreur s\'est produite lors de l\'emprunt.';
        this.afficherFlash('erreur', msg);
      },
    });
  }

  private afficherFlash(type: 'succes' | 'erreur', texte: string): void {
    this.flash.set({ type, texte });
    setTimeout(() => this.flash.set(null), 5000);
  }
}
