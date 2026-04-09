import { Routes } from '@angular/router';
import { InscriptionComponent } from './composants/authentification/inscription/inscription';
import { ConnexionComponent } from './composants/authentification/connexion/connexion';

export const routes: Routes = [
    { path : 'auth/register', component: InscriptionComponent},
    { path: 'auth/login', component: ConnexionComponent},
    // { path: 'profile', component: ProfileComponent},
];
