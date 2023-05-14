import { NgModule } from '@angular/core';
import { TextFieldModule } from '@angular/cdk/text-field';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { RegistrationComponent } from './registration.component';
import { FormValidationModule } from 'src/app/shared/forms/form-validation.module';
import { CommonModule } from '@angular/common';
import { ChatsModule } from '../chats/chats.module';

@NgModule({
  imports: [
    CommonModule,
    ChatsModule,
    FormValidationModule,
    FormsModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbModule,
    BrowserAnimationsModule,
    TextFieldModule,
  ],
  declarations: [RegistrationComponent],
})
export class RegistrationModule {}
