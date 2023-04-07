import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { ApplicationConfigService } from "src/app/config/application-config.service";
import { IChat } from "../chat.model";
import { Observable } from "rxjs";

@Injectable({
    providedIn: 'root'
}
)
export class ChatListService {
    
    resourceUrl: string = this.configService.getEndpointFor('/api/v1/chats')

    constructor(
        private configService: ApplicationConfigService,
        private httpClient: HttpClient) {}

    getAllChats(): Observable<IChat[]> {
        return this.httpClient.get<IChat[]>("http://localhost:8080/api/v1/chats");
    }
}