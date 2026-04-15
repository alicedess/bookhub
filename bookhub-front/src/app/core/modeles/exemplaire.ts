export type Exemplaire = {
  id: number;
  codeBarre: string;
  etat: Etat;
  estDisponible: boolean;
  idLivre: number;
}

export type Etat = 'BON'|'NEUF'|'ABIME';
