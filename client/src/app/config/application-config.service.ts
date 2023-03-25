import {Injectable} from "@angular/core";

@Injectable({
    providedIn: "root",
})
export class ApplicationConfigService {
    private serverUrl = "http://localhost:8080/"

    setServerUrl(endpointPrefix: string): void {
        if (!endpointPrefix.endsWith("/")) {
            endpointPrefix += "/";
        }
        this.serverUrl = endpointPrefix;
    }

    getEndpointFor(api: string): string {
        if (api.startsWith("/")) {
            api = api.substring(1);
        }
        return `${this.serverUrl}${api}`;
    }
}
