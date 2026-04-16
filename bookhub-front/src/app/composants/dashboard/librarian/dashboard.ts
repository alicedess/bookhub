import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { StatsService } from '../../../core/services/stats-service';
import { Stats } from '../../../core/modeles/stats';

@Component({
  selector: 'app-librarian-dashboard',
  imports: [RouterLink],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard implements OnInit {
  private statsService = inject(StatsService);

  stats = signal<Stats | null>(null);
  chargement = signal(true);
  erreur = signal<string | null>(null);

  ngOnInit(): void {
    this.statsService.getStats().subscribe({
      next: (data) => {
        this.stats.set(data);
        this.chargement.set(false);
      },
      error: () => {
        this.erreur.set('Impossible de charger les statistiques. Vérifiez votre connexion ou vos droits d\'accès.');
        this.chargement.set(false);
      },
    });
  }
}
