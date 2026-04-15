import { Component, OnInit, signal } from '@angular/core';
import { Header } from '../../../layout/header/header';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-librarian-dashboard',
  imports: [
    Header,
    RouterLink
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard implements OnInit {
  totalLivres = signal(0);
  empruntsEnCours = signal(0);
  totalUtilisateurs = signal(0);
  nombreRetards = signal(0);

  // Simulation de données pour la liste des retards
  retardsRecents = signal([
    { id: 1, utilisateur: 'Jean Dupont', livre: 'Les Misérables', joursRetard: 5 },
    { id: 2, utilisateur: 'Lucas Artigaud', livre: '1984', joursRetard: 2 },
    { id: 3, utilisateur: 'Marie Smith', livre: 'Fondation', joursRetard: 12 },
  ]);

  ngOnInit() {
    // Ici, tu appelleras tes services pour charger les vraies stats
    this.totalLivres.set(1250);
    this.empruntsEnCours.set(42);
    this.totalUtilisateurs.set(85);
    this.nombreRetards.set(3);
  }
}
