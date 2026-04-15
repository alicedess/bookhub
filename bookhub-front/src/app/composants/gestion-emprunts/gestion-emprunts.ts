import { Component, inject, OnInit } from '@angular/core';
import { EmpruntDTO } from '../../core/modeles/emprunt';
import { EmpruntService } from '../../core/services/emprunt-service';
import { Header } from '../../layout/header/header';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-gestion-emprunts',
  imports: [
    Header,
    DatePipe
  ],
  templateUrl: './gestion-emprunts.html',
  styleUrl: './gestion-emprunts.css',
})
export class GestionEmprunts implements OnInit {
  empruntsEnCours: EmpruntDTO[] = [];
  erreur: string | null = null;
  messageSucces: string | null = null;

  private empruntService: EmpruntService = inject(EmpruntService);

  ngOnInit(): void {
    this.chargerEmprunts();
  }

  chargerEmprunts(): void {
    this.erreur = null;

    this.empruntService.getAllActiveLoans().subscribe({
      next: (data) => {
        this.empruntsEnCours = data;
      },
      error: () => {
        this.erreur = 'Impossible de charger les emprunts en cours.';
      }
    });
  }

  retournerLivre(emprunt: EmpruntDTO): void {
    this.erreur = null;
    this.messageSucces = null;

    this.empruntService.retournerLivre(emprunt.idEmprunt).subscribe({
      next: (response) => {
        this.messageSucces = response.message;
        this.chargerEmprunts();
      },
      error: () => {
        this.erreur = 'Impossible d\'enregistrer le retour.';
      }
    });
  }

  calculerRetard(dateRetourPrevue: Date): number {
    const now = new Date();
    const datePrevue = new Date(dateRetourPrevue);
    const diff = now.getTime() - datePrevue.getTime();
    return Math.max(0, Math.ceil(diff / (1000 * 60 * 60 * 24)));
  }
}
