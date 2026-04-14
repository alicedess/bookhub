import { type Auteur } from './auteur';
import { type Categorie } from './categorie';

export type Livre = {
  id: number,
  isbn: string,
  titre: string,
  resume: string,
  imageCouverture: string,
  nbPage: number,
  auteurId: number,
  auteurNom: string,
  auteurPrenom: string,
  categorieId: number,
  categorieLibelle: string,
  nbExemplaire: number,
  nbExemplaireDispo: number,
  note: number,
  dateParution: string,
}
