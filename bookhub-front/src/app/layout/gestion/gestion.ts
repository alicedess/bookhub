import { Component, signal } from '@angular/core';
import { Header } from '../header/header';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-gestion',
  imports: [Header, RouterLink, RouterLinkActive, RouterOutlet],
  templateUrl: './gestion.html',
  styleUrl: './gestion.css',
})
export class Gestion {
  sidebarOuvert = signal(false);
}
