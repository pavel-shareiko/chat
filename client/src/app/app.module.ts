import { NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";
import { ReactiveFormsModule } from "@angular/forms";
import { HttpClientModule, HTTP_INTERCEPTORS } from "@angular/common/http";

import { AppRoutingModule } from "./app-routing.module";
import { MainComponent } from "./layouts/main/main.component";
import { LoginComponent } from "./layouts/login/login.component";
import { ChatsComponent } from './layouts/chats/chats.component';
import { AuthInterceptor } from "./auth/auth.interceptor";
import { RegistrationComponent } from './layouts/registration/registration.component';
import { NotFoundComponent } from './not-found/not-found.component';

@NgModule({
  declarations: [
    MainComponent, 
    LoginComponent, 
    ChatsComponent, RegistrationComponent, NotFoundComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [MainComponent],
})
export class AppModule {}
