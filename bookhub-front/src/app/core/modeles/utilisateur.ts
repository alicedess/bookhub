export interface Utilisateur {
    id : number,
    email : string,
    password: string,
    nom: string,
    prenom: string,
    telephone: number,
    dateNaissance: Date,
    dateSuppression: Date,
    role : 'USER' | 'ADMIN' | 'LIBRAIRIAN'
}
