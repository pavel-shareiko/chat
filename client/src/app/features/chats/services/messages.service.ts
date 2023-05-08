import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'src/app/core/config/application-config.service';
import { IMessage } from '../../../core/models/chat.model';
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
        destination: `/app/messages/send`,
        body: JSON.stringify({ chatId: chatId, content: message }),
      })
    );
  }

  editMessage(messageId: number, content: string): Observable<void> {
    return of(
      this.stompService.publish({
        destination: `/app/messages/edit`,
        body: JSON.stringify({ messageId: messageId, newContent: content }),
      })
    );
  }

  deleteMessage(message: IMessage | number): Observable<void> {
    const messageId: number = typeof message === 'number' ? message : message.id;
    return of(
      this.stompService.publish({
        destination: `/app/messages/delete`,
        body: JSON.stringify(messageId),
      })
    );
  }

  deleteMessages(messages: IMessage[] | number[]): Observable<void> {
    const messageIds: number[] =
      typeof messages === 'number' ? messages : (messages as IMessage[]).map(m => m.id);
    return of(
      this.stompService.publish({
        destination: `/app/messages/delete-all`,
        body: JSON.stringify(messageIds),
      })
    );
  }
}
