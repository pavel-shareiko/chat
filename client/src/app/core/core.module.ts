import { NgModule } from '@angular/core';
import { HeaderComponent } from './components/header/header.component';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { rxStompServiceFactory } from '../shared/stomp/rx-stomp-service-factory';
import { RxStompService } from '../shared/stomp/rx-stomp.service';
import { AuthExpiredInterceptor } from './interceptors/auth-expired.interceptor';
import { AuthInterceptor } from './interceptors/auth.interceptor';
import { CommonModule } from '@angular/common';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { MatTooltipModule } from '@angular/material/tooltip';

@NgModule({
  imports: [CommonModule, FontAwesomeModule, MatTooltipModule],
  declarations: [HeaderComponent],
  exports: [HeaderComponent],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: AuthExpiredInterceptor, multi: true },
    { provide: RxStompService, useFactory: rxStompServiceFactory },
  ],
})
export class CoreModule {}
