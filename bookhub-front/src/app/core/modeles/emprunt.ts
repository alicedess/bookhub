export interface EmpruntDTO {
  idEmprunt: number;
  dateDebut: Date;
  dateRetourPrevue: Date;
  dateRetourEffective?: Date;
  statut: string;
  enRetard: boolean;
  utilisateurId: number;
  nomUtilisateur: string;
  exemplaireId: number;
  livreId: number;
  titreLivre: string;
}

export interface MesEmpruntsResponse {
  enCours: EmpruntDTO[];
  historique: EmpruntDTO[];
}

export interface EmpruntResponse {
  message: string;
  emprunt: EmpruntDTO;
}
