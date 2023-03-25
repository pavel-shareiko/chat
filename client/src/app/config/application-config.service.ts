import {Injectable} from "@angular/core";

@Injectable({
    providedIn: "root",
})
export class ApplicationConfigService {
    private endpointPrefix = "http://localhost:8080/";

    setEndpointPrefix(endpointPrefix: string): void {
        if (!endpointPrefix.endsWith("/")) {
            endpointPrefix += "/";
        }
        this.endpointPrefix = endpointPrefix;
    }

    getEndpointFor(api: string): string {
        if (api.startsWith("/")) {
            api = api.substring(1);
        }
        return `${this.endpointPrefix}${api}`;
    }
}
