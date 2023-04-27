import { DateFormatterService } from '../../../common/date-formatter.service';
import { Component, Input } from '@angular/core';
import { ChatType, IChat } from '../chat.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-chat[chat]',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss'],
})
export class ChatComponent {
  @Input() chat!: IChat;

  constructor(private router: Router, public dateFormatter: DateFormatterService) {}

  getLabel(): string {
    if (this.chat.chatType !== ChatType.PERSONAL_CHAT) {
      return '';
    }

    return this.chat.participants[0].username;
  }

  openDialogue(): void {
    this.router.navigate(['/chats/', this.chat.chatId]);
  }
}
