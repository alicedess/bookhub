import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { InscriptionComponent } from './composants/authentification/inscription/inscription';
import { ConnexionComponent } from './composants/authentification/connexion/connexion';
import Profil from './composants/authentification/profil/profil';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, InscriptionComponent, ConnexionComponent, Profil],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('bookhub-front');
}
