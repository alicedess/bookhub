import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CatalogueService } from '../../../core/services/catalogue-service';
import { Livre } from '../../../core/modeles/livre';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Header } from '../../../layout/header/header';
import { CategorieService } from '../../../core/services/categorie-service';
import { AuteurService } from '../../../core/services/auteur-service';

/**
 * Composant en charge de la création et de l'édition d'un livre.
 */
@Component({
  selector: 'app-edition',
  imports: [
    Header,
    ReactiveFormsModule
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

  categories = this.categorieService.categories;
  auteurs = this.auteurService.authors;

  livre = signal<Livre | null>(null);

  errorMessage = signal('');
  successMessage = signal('');

  form = new FormGroup({
    titre: new FormControl(),
    isbn: new FormControl(),
    resume: new FormControl(),
    nbPage: new FormControl(),
    dateParution: new FormControl(),
    categorieId: new FormControl(),
    auteurId: new FormControl(),
  });

  formCover = new FormGroup({
    file: new FormControl(),
  })

  ngOnInit() {
    this.route.params.subscribe(params => {
      if (!params['id']) {
        return;
      }

      const id = +params['id'];

      this.catalogueService.getLivre(id).subscribe(livre => {
        this.livre.set(livre);
        this.form.patchValue(livre);
      });
    });

    this.auteurService.refresh();
    this.categorieService.refresh();
  }

  /**
   * Sauvegarde du formulaire
   */
  save() {
    this.errorMessage.set('');

    const data: Partial<Livre> = this.form.value;
    const livre = this.livre();

    if (!livre) {
      this.catalogueService.create(data).subscribe({
        next: () => {
          this.router.navigate(['/librarian', 'books']);
        },
        error: () => {
          this.showError('Erreur lors de la création du livre');
        }
      });

      return;
    }

    this.catalogueService.update(livre, data).subscribe({
      next: () => {
        this.refreshLivreDate(livre);
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
        this.refreshLivreDate(livre);
      },
      error: () => {
        this.showError('Erreur lors de la modification de la couverture livre');
      }
    })
  }

  protected refreshLivreDate(livre: Livre) {
    this.catalogueService.getLivre(livre.id).subscribe(livre => {
      this.livre.set(livre);
      this.form.patchValue(livre);
    });
  }
}
