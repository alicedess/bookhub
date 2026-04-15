import { Component } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../core/services/auth-service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Header } from '../../../layout/header/header';

@Component({
  selector: 'app-profil',
  imports: [CommonModule, ReactiveFormsModule, RouterLink, Header],
  templateUrl: './profil.html',
  standalone: true
})
export class Profil  {

  formulaire: FormGroup;
  chargement = false;
  erreurServeur: string | null = null;
  messageSucces: string | null = null;
  utilisateur: any = null;

  private readonly REGEX_MDP = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{12,}$/;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.formulaire = this.fb.group(
      {
        prenom: ['', Validators.required],
        nom: ['', Validators.required],
        telephone: ['', [Validators.required, Validators.pattern(/^\+?[0-9]{10,15}$/)]],
        password: ['', [Validators.required, Validators.pattern(this.REGEX_MDP)]],
        confirmation: ['', Validators.required],
      },
      { validators: this.motsDePasseIdentiques }
    );

  }

  ngOnInit() {
    this.chargerInformationsUtilisateur();
  }

    // raccourcis template
    get prenom()       { return this.formulaire.get('prenom')!; }
    get nom()          { return this.formulaire.get('nom')!; }
    get dateNaissance() {return this.formulaire.get('dateNaissance')!;}
    get email()        { return this.formulaire.get('email')!; }
    get password()   { return this.formulaire.get('password')!; }
    get confirmation() { return this.formulaire.get('confirmation')!; }
    get telephone()     { return this.formulaire.get('telephone')!; }

    chargerInformationsUtilisateur() {
      this.authService.obtenirProfilUtilisateur().subscribe({
        next: (utilisateur) => {
          this.utilisateur = utilisateur;
          this.formulaire.patchValue({
            prenom: utilisateur.prenom,
            nom: utilisateur.nom,
            telephone: utilisateur.telephone,
          });
        },
        error: () => {
          this.erreurServeur = 'Impossible de charger le profil.';
        }
      });
    }

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
    this.messageSucces = null;

    const { prenom, nom, telephone, password } = this.formulaire.value;

    this.authService.modifierProfil({ prenom, nom, telephone, password }).subscribe({
      next: () => {
        this.chargement = false;
        this.messageSucces = 'Votre profil a été mis à jour avec succès.';
      },
      error: () => {
        this.chargement = false;
        this.erreurServeur = 'Une erreur est survenue.';
      }
    });
  }

  supprimerCompte(): void {
    if(confirm('Êtes-vous sûr de vouloir supprimer votre compte ? Cette action est irréversible.')) {
      this.chargement = true;
      this.authService.supprimerCompte().subscribe({
        next: () => {
          this.router.navigate(['/login'], {
            queryParams: { compteSupprime: true }
          });
        },
        error: () => {
          this.chargement = false;
          this.erreurServeur = "Une erreur est survenur lors de la suppression du compte.";
        }
      });
    }
  }
}

export default Profil;