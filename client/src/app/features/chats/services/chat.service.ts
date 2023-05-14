import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'src/app/core/config/application-config.service';
import { IChat } from '../../../core/models/chat.model';
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

  getChat(chatId: number): Observable<IChat> {
    return this.httpClient.get<IChat>(`${this.resourceUrl}/${chatId}`);
  }

  doesChatExist(chatId: number): Observable<boolean> {
    return this.httpClient.get<boolean>(`${this.resourceUrl}/${chatId}/exists`);
  }

  startChat(username: string) {
    return this.httpClient.post<number>(`${this.resourceUrl}/${username}`, {});
  }
}
