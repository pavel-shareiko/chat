import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'src/app/config/application-config.service';
import { IMessage } from './chat.model';
import { Observable, of } from 'rxjs';
import { RxStompService } from 'src/app/shared/stomp/rx-stomp.service';

@Injectable({
  providedIn: 'root',
})
export class MessagesService {
  resourceUrl: string = this.configService.getEndpointFor('/api/v1/messages');

  constructor(
    private configService: ApplicationConfigService,
    private httpClient: HttpClient,
    private stompService: RxStompService
  ) {}

  getChatMessages(chatId: number): Observable<IMessage[]> {
    return this.httpClient.get<IMessage[]>(`${this.resourceUrl}/chat/${chatId}`);
  }

  sendMessage(chatId: number, message: string): Observable<void> {
    return of(
      this.stompService.publish({
        destination: `/app/chats/messages`,
        body: JSON.stringify({ chatId: chatId, content: message }),
      })
    );
  }
}
