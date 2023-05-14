import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ApplicationConfigService {
  private serverUrl = `${environment.apiUrl}`;

    /**
   * Returns the full endpoint URL for the given API route by appending it to the server URL.
   *
   * @param {string} api - The API route to get the endpoint for.
   * @return {string} The full endpoint URL.
   */
  getEndpointFor(api: string): string {
    if (api.startsWith('/') && this.serverUrl.endsWith('/')) {
      api = api.substring(1);
    }
    return `${this.serverUrl}${api}`;
  }
}
