import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'src/app/config/application-config.service';
import { IChat, IMessage } from './chat.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ChatService {
  resourceUrl: string = this.configService.getEndpointFor('/api/v1/chats');

  constructor(private configService: ApplicationConfigService, private httpClient: HttpClient) {}

  getAllChats(): Observable<IChat[]> {
    return this.httpClient.get<IChat[]>(this.resourceUrl);
  }

  doesChatExist(chatId: number): Observable<boolean> {
    return this.httpClient.get<boolean>(`${this.resourceUrl}/${chatId}/exists`);
  }
}
