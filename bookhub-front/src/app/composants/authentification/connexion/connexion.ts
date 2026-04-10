import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { Router, RouterLink, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../../core/services/auth-service';
import { Header } from '../../../layout/header/header';

@Component({
  selector: 'app-connexion',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, Header],
  templateUrl: './connexion.html',
})
export class ConnexionComponent implements OnInit {

  formulaire: FormGroup;
  chargement = false;
  erreurServeur: string | null = null;
  inscriptionReussie = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute  // pour lire le query param venant de l'inscription
  ) {
    this.formulaire = this.fb.group({
      email:      ['', [Validators.required, Validators.email]],
      motDePasse: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.inscriptionReussie = params['inscriptionReussie'] === 'true';
    });
  }

  get email()      { return this.formulaire.get('email')!; }
  get motDePasse() { return this.formulaire.get('motDePasse')!; }

  afficherErreur(champ: AbstractControl): boolean {
    return champ.invalid && (champ.dirty || champ.touched);
  }


  soumettre(): void {
    if (this.formulaire.invalid) {
      this.formulaire.markAllAsTouched();
      return;
    }

    this.chargement = true;
    this.erreurServeur = null;

    const { email, motDePasse } = this.formulaire.value;

    this.authService.seConnecter({ email, motDePasse }).subscribe({
      next: (reponse) => {
        this.authService.sauvegarderToken(reponse.token);
        
        const role = this.authService.obtenirRole();

        const redirections: Record<string, string> = {
          USER: '/dashboard',
          LIBRARIAN: '/librarian',
          ADMIN: '/admin',
        };

        this.router.navigate([redirections[role ?? ''] ?? '/dashboard']);
      },
      error: (err) => {
        this.chargement = false;
        if (err.status === 401) {
          this.erreurServeur = 'Email ou mot de passe incorrect.';
        } else {
          this.erreurServeur = 'Une erreur est survenue.';
        }
      }
    });
  }
}

export default ConnexionComponent;