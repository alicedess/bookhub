import { Component } from '@angular/core';
import { Header } from '../header/header';
import { RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-gestion',
  imports: [
    Header,
    RouterLink,
    RouterOutlet
  ],
  templateUrl: './gestion.html',
  styleUrl: './gestion.css',
})
export class Gestion {}
