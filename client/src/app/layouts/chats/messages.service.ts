import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'src/app/config/application-config.service';
import { IMessage } from './chat.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class MessagesService {
  resourceUrl: string = this.configService.getEndpointFor('/api/v1/messages');

  constructor(private configService: ApplicationConfigService, private httpClient: HttpClient) {}

  getChatMessages(chatId: number): Observable<IMessage[]> {
    return this.httpClient.get<IMessage[]>(`${this.resourceUrl}/chat/${chatId}`);
  }
}
