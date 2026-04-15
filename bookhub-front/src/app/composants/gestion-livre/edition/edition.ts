import { ChangeDetectorRef, Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CatalogueService } from '../../../core/services/catalogue-service';
import { Livre } from '../../../core/modeles/livre';
import { FormArray, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CategorieService } from '../../../core/services/categorie-service';
import { AuteurService } from '../../../core/services/auteur-service';
import { Location } from '@angular/common';
import { Exemplaire } from '../../../core/modeles/exemplaire';

@Component({
  selector: 'app-edition',
  imports: [ReactiveFormsModule],
  templateUrl: './edition.html',
  styleUrl: './edition.css'
})
export class Edition implements OnInit {

  private catalogueService = inject(CatalogueService);
  private categorieService = inject(CategorieService);
  private auteurService = inject(AuteurService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private location = inject(Location);
  private cdr = inject(ChangeDetectorRef);

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
      validators: [Validators.required, Validators.pattern(/^(97(8|9))?\d{9}(\d|X)$/)],
      nonNullable: true
    }),
    resume: new FormControl('', [Validators.maxLength(2000)]),
    nbPage: new FormControl<number | null>(null, [Validators.required, Validators.min(1)]),
    dateParution: new FormControl('', [Validators.required]),
    categorieId: new FormControl<number | null>(null, [Validators.required]),
    auteurId: new FormControl<number | null>(null, [Validators.required])
  });

  exemplairesForm = new FormGroup({
    exemplaires: new FormArray<FormGroup>([])
  });

  get exemplaires() {
    return this.exemplairesForm.get('exemplaires') as FormArray;
  }

  ngOnInit() {
    this.auteurService.refresh();
    this.categorieService.refresh();

    this.route.params.subscribe(params => {
      if (params['id']) {
        this.refreshLivreData({ id: +params['id'] } as Livre);
      }
    });
  }

  save() {
    this.form.markAllAsTouched();

    if (!this.form.valid) {
      this.showError('Le formulaire contient des erreurs');
      return;
    }

    const data = this.form.value as Partial<Livre>;
    const livre = this.livre();

    if (!livre) {
      this.catalogueService.create(data).subscribe({
        next: (created) => this.router.navigate(['/librarian', 'books', created.id, 'edit']),
        error: () => this.showError('Erreur lors de la création du livre')
      });
      return;
    }

    this.catalogueService.update(livre, data).subscribe({
      next: () => {
        this.refreshLivreData(livre);
        this.showSuccess('Le livre a bien été mis à jour');
      },
      error: () => this.showError('Erreur lors de la modification du livre')
    });
  }

  protected updateCover(files: FileList | null) {
    if (!files?.length) return;

    const livre = this.livre();
    if (!livre) return;

    this.catalogueService.updateCover(livre, files[0]).subscribe({
      next: () => {
        this.refreshLivreData(livre);
        this.showSuccess('La couverture a bien été mise à jour');
      },
      error: () => this.showError('Erreur lors de la modification de la couverture')
    });
  }

  protected async refreshLivreData(livre: Livre) {
    this.catalogueService.getLivre(livre.id).subscribe({
      next: (l) => {
        setTimeout(() => {
          this.livre.set(l);
          this.form.patchValue(l);
        }, 100)

        this.refreshExemplaires(livre);
      },
      error: () => this.showError('Erreur lors de la récupération des informations livre')
    });
  }

  protected refreshExemplaires(livre: Livre) {
    this.catalogueService.getExemplaires(livre).subscribe({
      next: (exemplaires) => {
        this.exemplaires.clear();

        exemplaires.forEach(ex => {
          const dispoControl = new FormControl(ex.estDisponible ?? true);

          // SI L'ID EXISTE, ON DÉSACTIVE LE CONTRÔLE
          if (ex.id) {
            dispoControl.disable();
          }

          this.exemplaires.push(new FormGroup({
            id: new FormControl(ex.id),
            codeBarre: new FormControl(ex.codeBarre, [Validators.required]),
            etat: new FormControl(ex.etat || 'NEUF', [Validators.required]),
            estDisponible: dispoControl
          }));
        });

        this.cdr.detectChanges()
      },
      error: () => this.showError('Erreur lors du chargement des exemplaires')
    });
  }

  addExemplaire() {
    this.exemplaires.push(new FormGroup({
      id: new FormControl(null),
      codeBarre: new FormControl('', [Validators.required]),
      etat: new FormControl('NEUF', [Validators.required]),
      estDisponible: new FormControl(true)
    }));
  }

  removeExemplaire(index: number) {
    this.exemplaires.removeAt(index);
  }

  saveExemplaires() {
    this.exemplairesForm.markAllAsTouched();

    if (this.exemplairesForm.invalid) {
      this.showError('Veuillez corriger les erreurs dans les exemplaires');
      return;
    }

    const livre = this.livre();
    if (!livre) return;

    const data = this.exemplaires.value as Partial<Exemplaire>[];

    this.catalogueService.saveExemplaires(livre, data).subscribe({
      next: () => {
        this.refreshExemplaires(livre);
        this.showSuccess('Les exemplaires ont bien été enregistrés');
      },
      error: () => {
        this.refreshExemplaires(livre);
        this.showError('Erreur lors de la sauvegarde des exemplaires');
      }
    });
  }

  protected goBack() {
    this.router.navigate(['librarian', 'books']);
  }

  showError(message: string) {
    this.errorMessage.set(message);
    setTimeout(() => this.errorMessage.set(''), 5000);
  }

  showSuccess(message: string) {
    this.successMessage.set(message);
    setTimeout(() => this.successMessage.set(''), 2000);
  }
}
