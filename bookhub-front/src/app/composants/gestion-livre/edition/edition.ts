import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CatalogueService } from '../../../core/services/catalogue-service';
import { Livre } from '../../../core/modeles/livre';
import { FormArray, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Header } from '../../../layout/header/header';
import { CategorieService } from '../../../core/services/categorie-service';
import { AuteurService } from '../../../core/services/auteur-service';
import { Location } from '@angular/common';

/**
 * Composant en charge de la création et de l'édition d'un livre.
 */
@Component({
  selector: 'app-edition',
  imports: [
    Header,
    ReactiveFormsModule,
  ],
  templateUrl: './edition.html',
  styleUrl: './edition.css'
})
export class Edition implements OnInit {

  private catalogueService: CatalogueService = inject(CatalogueService);
  private categorieService: CategorieService = inject(CategorieService);
  private auteurService: AuteurService = inject(AuteurService);

  private route = inject(ActivatedRoute);
  private router: Router = inject(Router);
  private location: Location = inject(Location);

  categories = this.categorieService.categories;
  auteurs = this.auteurService.authors;

  livre = signal<Livre | null>(null);

  errorMessage = signal('');
  successMessage = signal('');

  form = new FormGroup({
    titre: new FormControl('', {
      validators: [Validators.required, Validators.minLength(3), Validators.maxLength(150)],
      nonNullable: true
    }),
    isbn: new FormControl('', {
      validators: [Validators.required, Validators.pattern(/^(97(8|9))?\d{9}(\d|X)$/)], // Format ISBN 10/13
      nonNullable: true
    }),
    resume: new FormControl('', [Validators.maxLength(2000)]),
    nbPage: new FormControl<number | null>(null, [Validators.required, Validators.min(1)]),
    dateParution: new FormControl('', [Validators.required]),
    categorieId: new FormControl<number | null>(null, [Validators.required]),
    auteurId: new FormControl<number | null>(null, [Validators.required]),
  });

  exemplairesForm = new FormGroup({
    exemplaires: new FormArray([])
  })

  get exemplaires() {
    return this.exemplairesForm.get('exemplaires') as FormArray;
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      if (!params['id']) {
        return;
      }

      this.refreshLivreData({ id: +params['id'] } as Livre);
    });

    this.auteurService.refresh();
    this.categorieService.refresh();
  }

  /**
   * Sauvegarde du formulaire
   */
  save() {
    this.errorMessage.set('');
    this.form.markAllAsTouched();

    if (!this.form.valid) {
      this.showError("Le formulaire contient des erreurs");
      return;
    }

    const data: Partial<Livre> = this.form.value as Partial<Livre>;
    const livre = this.livre();

    if (!livre) {
      this.catalogueService.create(data).subscribe({
        next: (livre) => {
          this.router.navigate(['/librarian', 'books', livre.id, 'edit']);
        },
        error: () => {
          this.showError('Erreur lors de la création du livre');
        }
      });

      return;
    }

    this.catalogueService.update(livre, data).subscribe({
      next: () => {
        this.refreshLivreData(livre);
        this.showSuccess("Le livre a bien été mis à jour")
      },
      error: () => {
        this.showError('Erreur lors de la modification du livre');
      }
    });
  }

  /**
   * Affiche un message d'erreur.
   *
   * @param message
   */
  showError(message: string) {
    this.errorMessage.set(message);

    setTimeout(() => {
      this.errorMessage.set('');
    }, 5000);
  }

  /**
   * Affiche un message de succès.
   *
   * @param message
   */
  showSuccess(message: string) {
    this.successMessage.set(message);

    setTimeout(() => {
      this.successMessage.set('');
    }, 2000);
  }

  protected updateCover(files: FileList | null) {
    if (!files || !files.length) {
      return;
    }

    const livre = this.livre();

    if (!livre) {
      return;
    }

    this.catalogueService.updateCover(livre, files[0]).subscribe({
      next: () => {
        this.refreshLivreData(livre);
        this.showSuccess("Le livre a bien été mis à jour")
      },
      error: () => {
        this.showError('Erreur lors de la modification de la couverture livre');
      }
    })
  }

  protected refreshLivreData(livre: Livre) {
    this.catalogueService.getLivre(livre.id).subscribe({
      next: (livre) => {
          this.livre.update(() => livre);
          this.form.patchValue(livre);
      },
      error: () => {
        this.showError('Erreur lors de la récupération des informations livre');
      }
    });
  }

  protected goBack() {
    this.location.back();
  }

  /**
   * Ajoute un nouvel exemplaire vide au formulaire
   */
  addExemplaire(exemplaireData?: any) {
    const exemplaireForm = new FormGroup({
      id: new FormControl(exemplaireData?.id || null),
      codeBarre: new FormControl(exemplaireData?.codeBarre || '', [Validators.required]),
      etat: new FormControl(exemplaireData?.etat || 'NEUF', [Validators.required]),
      disponible: new FormControl(exemplaireData?.disponible ?? true)
    });

    this.exemplaires.push(exemplaireForm);
  }

  /**
   * Supprime un exemplaire de la liste (localement)
   */
  removeExemplaire(index: number) {
    this.exemplaires.removeAt(index);
  }
//
// // Dans ton ngOnInit, lors de la récupération du livre :
//   this.catalogueService.getLivre(id).subscribe(livre => {
//   this.livre.set(livre);
//
//   // On vide le tableau actuel au cas où
//   this.exemplaires.clear();
//
//   // On remplit le formulaire avec les données de base
//   this.form.patchValue(livre);
//
//   // On remplit le FormArray avec les exemplaires existants (si ton modèle Livre les contient)
//   if (livre.exemplaires) {
//   livre.exemplaires.forEach(ex => this.addExemplaire(ex));
// }
// });
}
