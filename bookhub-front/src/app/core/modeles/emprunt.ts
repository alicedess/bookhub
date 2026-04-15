export interface EmpruntDTO {
  id: number;
  dateDebut: Date;
  dateRetourPrevue: Date;
  dateRetourEffective?: Date;
  statut: string;
  enRetard: boolean;
  utilisateurId: number;
  exemplaireId: number;
  livreId: number;
  titreLivre: string;
  auteur: string;
}

export interface MesEmpruntsResponse {
  enCours: EmpruntDTO[];
  historique: EmpruntDTO[];
}

export interface EmpruntCreationResponse {
  message: string;
  emprunt: EmpruntDTO;
}

