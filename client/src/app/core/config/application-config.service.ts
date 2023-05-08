import { Injectable } from '@angular/core';
import { environment } from '../environment/environment';

@Injectable({
  providedIn: 'root',
})
export class ApplicationConfigService {
  private serverUrl = `${environment.protocol}://${environment.host}:${environment.port}/`;

  getEndpointFor(api: string): string {
    if (api.startsWith('/')) {
      api = api.substring(1);
    }
    return `${this.serverUrl}${api}`;
  }
}
