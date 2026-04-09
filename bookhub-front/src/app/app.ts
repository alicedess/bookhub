import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { InscriptionComponent } from './composants/authentification/inscription/inscription';
import { ConnexionComponent } from './composants/authentification/connexion/connexion';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, InscriptionComponent, ConnexionComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('bookhub-front');
}
