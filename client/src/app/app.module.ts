import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { MainComponent } from './core/components/main/main.component';
import { LoginComponent } from './features/login/login.component';
import { AuthInterceptor } from './core/interceptors/auth.interceptor';
import { RegistrationComponent } from './features/registration/registration.component';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NotFoundComponent } from './core/components/not-found/not-found.component';
import { ChatComponent } from './features/chats/chat/chat.component';
import { HeaderComponent } from './core/components/header/header.component';
import { AuthExpiredInterceptor } from './core/interceptors/auth-expired.interceptor';
import { ChatDialogueComponent } from './features/chats/dialogue/chat-dialogue.component';
import { MessageGroupingPipe } from './shared/pipes/message-grouping.pipe';
import { MessageHtmlPipe } from './shared/pipes/message-html.pipe';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { TextFieldModule } from '@angular/cdk/text-field';
import { rxStompServiceFactory } from './shared/stomp/rx-stomp-service-factory';
import { RxStompService } from './shared/stomp/rx-stomp.service';
import { FormValidationComponent } from './shared/forms/form-validation.component';
import { ChatListComponent } from './features/chats/list/chat-list.component';

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
    BrowserAnimationsModule,
    TextFieldModule,
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: AuthExpiredInterceptor, multi: true },
    { provide: RxStompService, useFactory: rxStompServiceFactory },
  ],
  bootstrap: [MainComponent],
})
export class AppModule {}
