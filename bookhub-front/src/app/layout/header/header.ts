import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth-service';

@Component({
  selector: 'app-header',
  imports: [RouterLink, RouterLinkActive, CommonModule],
  templateUrl: './header.html',
})
export class Header {
  isMobileMenuOpen = false;

  constructor(private authService: AuthService) {}

  get isLoggedIn(): boolean {
    return this.authService.estConnecte();
  }

  get currentUser() {
    return this.authService.obtenirUtilisateur();
  }

  toggleMobileMenu() {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
  }

  logout() {
    this.authService.seDeconnecter();
  }

  get isLibrairian(){
    return this.authService.obtenirRole() === "LIBRARIAN";

  }

  get isAdmin(){
    return this.authService.obtenirRole() === "ADMIN";

  }
}
