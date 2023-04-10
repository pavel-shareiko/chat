import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ChatComponent } from './layouts/chats/chat/chat.component';
import { ChatListComponent } from './layouts/chats/list/chat-list.component';
import { LoginComponent } from './layouts/login/login.component';
import { NotFoundComponent } from './layouts/not-found/not-found.component';
import { RegistrationComponent } from './layouts/registration/registration.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegistrationComponent },
  { path: 'chats', component: ChatListComponent },
  { path: '404', component: NotFoundComponent },
  { path: '**', redirectTo: '404' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
