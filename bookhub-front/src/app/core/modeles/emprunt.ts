export interface EmpruntDTO {
  idEmprunt: number;
  dateDebut: Date;
  dateRetourPrevue: Date;
  dateRetourEffective?: Date;
  statut: string;
  enRetard: boolean;
  utilisateurId: number;
  exemplaireId: number;
  livreId: number;
  titreLivre: string;
  auteurNom: string;
  auteurPrenom: string;
}

export interface MesEmpruntsResponse {
  enCours: EmpruntDTO[];
  historique: EmpruntDTO[];
}

export interface EmpruntCreationResponse {
  message: string;
  emprunt: EmpruntDTO;
}

