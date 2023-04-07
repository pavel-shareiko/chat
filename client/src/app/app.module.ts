import {NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";

import {AppRoutingModule} from "./app-routing.module";
import {MainComponent} from "./layouts/main/main.component";
import {LoginComponent} from "./layouts/login/login.component";
import {AuthInterceptor} from "./auth/auth.interceptor";
import {RegistrationComponent} from "./layouts/registration/registration.component";

import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {FormValidationComponent} from "./shared/form-validation.component";
import { NotFoundComponent } from "./layouts/not-found/not-found.component";
import { ChatListComponent } from "./layouts/chats/list/chat-list.component";
import { ChatComponent } from "./layouts/chats/chat/chat.component";
import { HeaderComponent } from './header/header.component';


@NgModule({
    declarations: [
        MainComponent,
        LoginComponent,
        RegistrationComponent,
        NotFoundComponent,
        FormValidationComponent,
        ChatComponent,
        ChatListComponent,
        HeaderComponent
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
        {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
    ],
    bootstrap: [MainComponent],
})
export class AppModule {
}
