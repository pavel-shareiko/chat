import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { MainComponent } from './core/components/main/main.component';
import { CoreModule } from './core/core.module';
import { FormValidationModule } from './shared/forms/form-validation.module';
import { LoginModule } from './features/login/login.module';
import { RegistrationModule } from './features/registration/registration.module';
import { DialogueModule } from './features/chats/dialogue/dialogue.module';
import { ChatsModule } from './features/chats/chats.module';
import { NotFoundModule } from './core/components/not-found/not-found.module';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

@NgModule({
  declarations: [MainComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NotFoundModule,
    CoreModule,
    DialogueModule,
    FormValidationModule,
    ChatsModule,
    LoginModule,
    RegistrationModule,
    FontAwesomeModule,
  ],
  providers: [],
  bootstrap: [MainComponent],
})
export class AppModule {}
