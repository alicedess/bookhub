import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth-service';
import { Header } from '../../../layout/header/header';


@Component({
  selector: 'app-inscription',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, Header],
  templateUrl: './inscription.html',
})
export class InscriptionComponent {

  formulaire: FormGroup;
  chargement = false;
  erreurServeur: string | null = null;

  private readonly REGEX_MDP = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{12,}$/;
  private readonly REGEX_DATE = /^\d{4}\-(0[1-9]|1[012])\-(0[1-9]|[12][0-9]|3[01])$/;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.formulaire = this.fb.group(
      {
        prenom: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
        nom:    ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
        dateNaissance: ['', [Validators.required, Validators.pattern(this.REGEX_DATE)]],
        email:  ['', [Validators.required, Validators.email]],
        password:  ['', [Validators.required, Validators.pattern(this.REGEX_MDP)]],
        confirmation: ['', Validators.required],
      },
      { validators: this.motsDePasseIdentiques }
    );
  }
  
  // raccourcis template
  get prenom()       { return this.formulaire.get('prenom')!; }
  get nom()          { return this.formulaire.get('nom')!; }
  get dateNaissance() {return this.formulaire.get('dateNaissance')!;}
  get email()        { return this.formulaire.get('email')!; }
  get password()   { return this.formulaire.get('password')!; }
  get confirmation() { return this.formulaire.get('confirmation')!; }

  // les deux mdp doivent correspondre
motsDePasseIdentiques(groupe: AbstractControl) {
  const mdp = groupe.get('password')?.value;
  const confirmation = groupe.get('confirmation')?.value;
  return mdp === confirmation ? null : { nonIdentiques: true };
}


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

    const { prenom, nom, dateNaissance, email, password, telephone } = this.formulaire.value;

    this.authService.sinscrire({ prenom, nom, dateNaissance, email, password, telephone }).subscribe({
      next: () => {
        this.router.navigate(['/auth/login'], {
          queryParams: { inscriptionReussie: true }
        });
      },
      error: (err: { status: number; }) => {
        this.chargement = false;
        if (err.status === 409) {
          this.erreurServeur = 'Cette adresse email est déjà utilisée.';
        } else {
          this.erreurServeur = 'Une erreur est survenue. Veuillez réessayer.';
        }
      }
    });
  }
}

export default InscriptionComponent;