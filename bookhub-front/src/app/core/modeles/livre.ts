import { type Auteur } from './auteur';
import { type Categorie } from './categorie';

export type Livre = {
  id: number,
  isbn: string,
  titre: string,
  resume: string,
  imageCouverture: string,
  auteur: Auteur,
  categorie: Categorie,
  nbPage: number,
}
