import { Component, inject, OnInit, signal } from '@angular/core';
import { EmpruntDTO } from '../../core/modeles/emprunt';
import { EmpruntService } from '../../core/services/emprunt-service';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-gestion-emprunts',
  imports: [
    DatePipe
  ],
  templateUrl: './gestion-emprunts.html',
  styleUrl: './gestion-emprunts.css',
})
export class GestionEmprunts implements OnInit {
  empruntsEnCours = signal<EmpruntDTO[]> ([]);
  erreur: string | null = null;
  messageSucces: string | null = null;

  private empruntService: EmpruntService = inject(EmpruntService);

  ngOnInit(): void {
    this.chargerEmprunts()
  }

  chargerEmprunts(): void {
    this.erreur = null;

    this.empruntService.getTousLesEmprunts().subscribe({
      next: (data) => {
        this.empruntsEnCours.set(data.content);
      },
      error: () => {
        this.erreur = 'Impossible de charger les emprunts pour le moment.';
      }
    });
  }

  onClickRetournerLivre(emprunt: EmpruntDTO): void {
    this.erreur = null;
    this.messageSucces = null;

    this.empruntService.retournerLivre(emprunt.idEmprunt).subscribe({
      next: () => {
        this.messageSucces = `Livre "${emprunt.titreLivre}" retourné avec succès.`;
        this.chargerEmprunts();
      },
      error: () => {
        this.erreur = `Impossible de retourner le livre "${emprunt.titreLivre}" pour le moment.`;
      }
    });
  }

  calculerRetard(dateRetourPrevu: Date): number {
    const now = new Date();
    const datePrevue = new Date(dateRetourPrevu);
    const diff = now.getTime() - datePrevue.getTime();
    return Math.max(0, Math.ceil(diff / (1000 * 60 * 60 * 24)));
  }


}
