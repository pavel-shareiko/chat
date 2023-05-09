import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { IUser } from 'src/app/core/models/user.model';
import { ApplicationConfigService } from 'src/app/core/config/application-config.service';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private resourceUrl = this.configService.getEndpointFor('/api/v1/users');
  constructor(private httpClient: HttpClient, private configService: ApplicationConfigService) {}

  public findUsers(username: string): Observable<IUser[]> {
    return this.httpClient.get<IUser[]>(`${this.resourceUrl}/search`, {
      params: {
        username,
      },
    });
  }
}
