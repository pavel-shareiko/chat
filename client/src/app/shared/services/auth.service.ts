import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, ReplaySubject, catchError, mergeMap, of, shareReplay, tap } from 'rxjs';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { IUser, LoginUser, RegistrationUser } from '../../core/models/user.model';
import { AccountService } from './account.service';

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(
    private http: HttpClient,
    private config: ApplicationConfigService,
    private accountService: AccountService
  ) {}

  /**
   * Registers a new user by sending a POST request to the registration endpoint.
   * @param user The user object containing the registration data.
   * @returns An observable that emits the registered user object and replays the last emitted value to new subscribers.
   */
  register(user: RegistrationUser): Observable<RegistrationUser> {
    return this.http
      .post<RegistrationUser>(this.config.getEndpointFor('api/v1/auth/register'), {
        ...user,
      })
      .pipe(shareReplay());
  }

  /**
   * Logs in the given user and returns an observable that emits the logged-in user or null if the login failed.
   *
   * @param user The user to log in.
   * @returns An observable that emits the logged-in user or null if the login failed.
   */
  login(user: LoginUser): Observable<IUser | null> {
    return this.http
      .post<string>(this.config.getEndpointFor('api/v1/auth/login'), {
        ...user,
      })
      .pipe(
        shareReplay(),
        tap(res => this.saveSession(res)),
        mergeMap(() => this.accountService.identity(true))
      );
  }

  /**
   * Clears the localStorage and sessionStorage and logs out the user by authenticating them as null.
   */
  logout(): void {
    localStorage.clear();
    sessionStorage.clear();
    this.accountService.authenticate(null);
  }

  /**
   * Saves the provided session token in the local storage.
   *
   * @param token - The session token to save.
   */
  private saveSession(token: any): void {
    localStorage.setItem('id_token', token.token);
  }
}
