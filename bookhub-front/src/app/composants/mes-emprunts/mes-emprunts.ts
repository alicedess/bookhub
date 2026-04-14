import { Component, inject, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { EmpruntDTO } from '../../core/modeles/emprunt';
import { EmpruntService } from '../../core/services/emprunt-service';
import { Header } from '../../layout/header/header';

@Component({
  selector: 'app-mes-emprunts',
  imports: [
    Header,
    CommonModule,
    DatePipe,
    RouterLink,
  ],
  templateUrl: './mes-emprunts.html',
  styleUrl: './mes-emprunts.css',
})
export class MesEmprunts implements OnInit {
  empruntsEnCours: EmpruntDTO[] = [];
  empruntsHistorique: EmpruntDTO[] = [];
  ongletActif: 'enCours' | 'historique' = 'enCours';
  erreur: string | null = null;

  private empruntService: EmpruntService = inject(EmpruntService);

  ngOnInit(): void {
    this.chargerEmprunts();
  }

  chargerEmprunts(): void {
    this.erreur = null;
    this.empruntService.getMesEmprunts().subscribe({
      next: (data) => {
        this.empruntsEnCours = data.enCours;
        this.empruntsHistorique = data.historique;
      },
      error: () => {
        this.erreur = 'Impossible de charger vos emprunts pour le moment.';
      }
    });
  }

  chargerOnglet(onglet: 'enCours' | 'historique'): void {
    this.ongletActif = onglet;
  }

  aDesRetards(): boolean {
    return this.empruntsEnCours.some((e) => e.enRetard);
  }

  nbRetards(): number {
    return this.empruntsEnCours.filter((e) => e.enRetard).length;
  }

  // Retourne un libellé du nb de jours restants ou de retard
  joursRestants(emprunt: EmpruntDTO): string {
    const maintenant = new Date();
    const dateRetour = new Date(emprunt.dateRetourPrevue);
    const diffDate = dateRetour.getTime() - maintenant.getTime();
    const diffJours = Math.ceil(diffDate / (1000 * 60 * 60 * 24));

    if (diffJours < 0) {
      return `${Math.abs(diffJours)} jour(s) de retard`;
    }
    if (diffJours === 0) {
      return 'À rendre aujourd\'hui';
    }
    return `${diffJours} jour(s) restant(s)`;
  }
}