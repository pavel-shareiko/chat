import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ChatComponent } from './layouts/chats/chat/chat.component';
import { ChatListComponent } from './layouts/chats/list/chat-list.component';
import { LoginComponent } from './layouts/login/login.component';
import { NotFoundComponent } from './layouts/not-found/not-found.component';
import { RegistrationComponent } from './layouts/registration/registration.component';
import { AuthGuard } from './auth/auth.guard';
import { LoginGuard } from './auth/login.guard';

const routes: Routes = [
  { path: '', redirectTo: 'chats', pathMatch: 'full' },
  { path: 'login', component: LoginComponent, canActivate: [LoginGuard] },
  { path: 'register', component: RegistrationComponent, canActivate: [LoginGuard] },
  { path: 'chats', component: ChatListComponent, canActivate: [AuthGuard] },
  { path: '404', component: NotFoundComponent },
  { path: '**', redirectTo: '404' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
