import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ApplicationConfigService {
  private serverUrl = `${environment.apiUrl}`;

  getEndpointFor(api: string): string {
    if (api.startsWith('/')) {
      api = api.substring(1);
    }
    return `${this.serverUrl}${api}`;
  }
}
