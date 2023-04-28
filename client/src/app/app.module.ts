import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { MainComponent } from './layouts/main/main.component';
import { LoginComponent } from './layouts/login/login.component';
import { AuthInterceptor } from './auth/auth.interceptor';
import { RegistrationComponent } from './layouts/registration/registration.component';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormValidationComponent } from './shared/form-validation.component';
import { NotFoundComponent } from './layouts/not-found/not-found.component';
import { ChatListComponent } from './layouts/chats/list/chat-list.component';
import { ChatComponent } from './layouts/chats/chat/chat.component';
import { HeaderComponent } from './header/header.component';
import { AuthExpiredInterceptor } from './auth/auth-expired.interceptor';
import { ChatDialogueComponent } from './layouts/chats/dialogue/chat-dialogue.component';
import { MessageGroupingPipe } from './common/pipes/message-grouping.pipe';
import { MessageHtmlPipe } from './common/pipes/message-html.pipe';

@NgModule({
  declarations: [
    MainComponent,
    LoginComponent,
    RegistrationComponent,
    NotFoundComponent,
    FormValidationComponent,
    ChatComponent,
    ChatListComponent,
    HeaderComponent,
    ChatDialogueComponent,
    MessageGroupingPipe,
    MessageHtmlPipe,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbModule,
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: AuthExpiredInterceptor, multi: true },
  ],
  bootstrap: [MainComponent],
})
export class AppModule {}
