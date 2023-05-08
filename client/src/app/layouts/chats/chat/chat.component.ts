import { DateFormatterService } from '../../../common/date-formatter.service';
import { Component, Input } from '@angular/core';
import { ChatType, IChat } from '../chat.model';
import { Router } from '@angular/router';
import { AccountService } from 'src/app/auth/account.service';
import { IUser } from 'src/app/models/user.model';

@Component({
  selector: 'app-chat[chat]',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss'],
})
export class ChatComponent {
  @Input() chat!: IChat;
  currentUser: IUser | null = null;

  constructor(
    private router: Router,
    public dateFormatter: DateFormatterService,
    private accountService: AccountService
  ) {
    accountService.getAuthenticationState().subscribe(user => {
      this.currentUser = user;
    });
  }

  getLabel(): string {
    if (this.chat.chatType !== ChatType.PERSONAL_CHAT) {
      return '';
    }

    return this.chat.participants[0].username;
  }

  openDialogue(): void {
    this.router.navigate(['/chats/', this.chat.chatId]);
  }

  getLastMessageSenderDisplayName(): string {
    if (!this.chat.lastMessage) {
      return '';
    }

    if (this.chat.lastMessage.sender.username === this.currentUser?.username) {
      return `You`;
    }

    return `${this.chat.lastMessage.sender.firstName} ${this.chat.lastMessage.sender.lastName}`;
  }
}
