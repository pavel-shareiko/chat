import { Injectable } from '@angular/core';
import { IUser } from '../models/user.model';
import { Observable, ReplaySubject, catchError, of, shareReplay, tap } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { ApplicationConfigService } from '../config/application-config.service';

@Injectable({
  providedIn: 'root',
})
export class AccountService {
  private userIdentity: IUser | null = null;
  private authenticationState = new ReplaySubject<IUser | null>(1);
  private currentUserCache$?: Observable<IUser> | null;

  constructor(private http: HttpClient, private config: ApplicationConfigService) {}

  /**
   * Authenticates a user identity and updates the authentication state.
   * If the identity is null, it clears the current user cache.
   * @param identity The user identity to authenticate, or null to clear the current user cache.
   */
  authenticate(identity: IUser | null): void {
    this.userIdentity = identity;
    this.authenticationState.next(this.userIdentity);
    if (!identity) {
      this.currentUserCache$ = null;
    }
  }

  /**
   * Returns an Observable that emits the current authentication state, represented
   * by an IUser object or null, and continues to emit new states when the authentication
   * state changes.
   *
   * @returns An Observable of IUser or null objects representing the authentication state.
   */
  getAuthenticationState(): Observable<IUser | null> {
    return this.authenticationState.asObservable();
  }

  /**
   * Checks if the user is logged in.
   *
   * @returns {boolean} Whether the user is logged in or not.
   */
  isLoggedIn(): boolean {
    return this.userIdentity !== null;
  }

  /**
   * Returns an Observable that emits the current user identity or null if not authenticated.
   * If `force` is true or there is no cached identity, fetches the current user identity from the server.
   * @param force - Whether to fetch the current user identity from the server, even if there is a cached identity.
   * @returns An Observable that emits the current user identity or null if not authenticated.
   */
  identity(force?: boolean): Observable<IUser | null> {
    if (!this.currentUserCache$ || force) {
      console.log(`making request to backend to fetch the user data...`);
      this.currentUserCache$ = this.fetchCurrentUser().pipe(
        tap((account: IUser) => {
          this.authenticate(account);
        }),
        shareReplay()
      );
    }
    return this.currentUserCache$.pipe(catchError(() => of(null)));
  }

  /**
   * Fetches the current user from the API endpoint using an HTTP GET request.
   * @returns An Observable emitting the fetched IUser object.
   */
  private fetchCurrentUser(): Observable<IUser> {
    return this.http.get<IUser>(this.config.getEndpointFor('api/v1/auth/me')).pipe(shareReplay());
  }
}
