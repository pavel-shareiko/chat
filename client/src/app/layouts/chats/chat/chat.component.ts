import { AfterContentInit, Component, Input, OnInit } from '@angular/core';
import { ChatType, IChat, IMessage } from '../chat.model';

@Component({
  selector: 'app-chat[chat]',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss'],
})
export class ChatComponent {
  @Input() chat!: IChat;

  getDisplayName(): string {
    if (this.chat.chatType === ChatType.PERSONAL_CHAT) {
      const receiver = this.chat.participants[0];
      return `${receiver.firstName} ${receiver.lastName}`;
    }

    if (this.chat.chatType === ChatType.SELF_CHAT) {
      return 'Self Chat';
    }

    if (this.chat.chatType === ChatType.GROUP_CHAT) {
      return 'Group Chat';
    }

    return 'Unknown chat';
  }
}
