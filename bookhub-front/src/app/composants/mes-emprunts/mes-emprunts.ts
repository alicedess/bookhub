import { Component, inject, OnInit } from '@angular/core';
import { EmpruntDTO } from '../../core/modeles/emprunt';
import { EmpruntService } from '../../core/services/emprunt-service';
import { Header } from '../../layout/header/header';

@Component({
  selector: 'app-mes-emprunts',
  imports: [
    Header
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
}
