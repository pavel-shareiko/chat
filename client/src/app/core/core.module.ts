import { NgModule } from '@angular/core';
import { HeaderComponent } from './components/header/header.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { rxStompServiceFactory } from '../shared/stomp/rx-stomp-service-factory';
import { RxStompService } from '../shared/stomp/rx-stomp.service';
import { AuthExpiredInterceptor } from './interceptors/auth-expired.interceptor';
import { AuthInterceptor } from './interceptors/auth.interceptor';

@NgModule({
  declarations: [HeaderComponent, NotFoundComponent],
  exports: [HeaderComponent, NotFoundComponent],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: AuthExpiredInterceptor, multi: true },
    { provide: RxStompService, useFactory: rxStompServiceFactory },
  ],
})
export class CoreModule {}
