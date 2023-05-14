import { AccountService } from './../../../shared/services/account.service';
import { Component } from '@angular/core';
import { IUser } from '../../models/user.model';
import { AuthService } from 'src/app/shared/services/auth.service';
import { faPlus, faSignOut } from '@fortawesome/free-solid-svg-icons';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent {
  public readonly applicationName = environment.applicationName;
  public currentUser: IUser | null = null;

  faSignOut = faSignOut;
  faPlus = faPlus;

  constructor(
    private accountService: AccountService,
    private authService: AuthService,
    private router: Router
  ) {
    this.accountService.getAuthenticationState().subscribe(user => {
      this.currentUser = user;
    });
  }

  logout() {
    this.authService.logout();
    setTimeout(() => {
      this.router.navigate(['/login']);
    });
  }

  newChat() {}
}
