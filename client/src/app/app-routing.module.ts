import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ChatListComponent } from './features/chats/list/chat-list.component';
import { LoginComponent } from './features/login/login.component';
import { NotFoundComponent } from './core/components/not-found/not-found.component';
import { RegistrationComponent } from './features/registration/registration.component';
import { AuthGuard } from './core/guards/auth.guard';
import { NoAuthGuard } from './core/guards/no-auth.guard';
import { ChatDialogueComponent } from './features/chats/dialogue/chat-dialogue.component';

const routes: Routes = [
  { path: '', redirectTo: 'chats', pathMatch: 'full' },
  { path: 'login', component: LoginComponent, canActivate: [NoAuthGuard] },
  { path: 'register', component: RegistrationComponent, canActivate: [NoAuthGuard] },
  { path: 'chats', component: ChatListComponent, canActivate: [AuthGuard] },
  { path: 'chats/:id', component: ChatDialogueComponent, canActivate: [AuthGuard] },
  { path: '404', component: NotFoundComponent, canActivate: [AuthGuard] },
  { path: '**', redirectTo: '404' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
