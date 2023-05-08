import { Message } from '@stomp/stompjs';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { IChat, IMessage } from '../chat.model';
import { RxStompService } from 'src/app/shared/stomp/rx-stomp.service';
import { AccountService } from 'src/app/shared/services/account.service';
import { IUser } from 'src/app/core/models/user.model';
import { ChatService } from '../chat.service';

@Component({
  selector: 'app-chat-list',
  templateUrl: './chat-list.component.html',
  styleUrls: ['./chat-list.component.scss'],
})
export class ChatListComponent implements OnInit {
  chats: IChat[] = [];
  chatsLoaded = false;
  currentUser: IUser | null = null;

  constructor(
    private chatService: ChatService,
    private stompService: RxStompService,
    private accountService: AccountService
  ) {}

  ngOnInit(): void {
    this.chatService.getAllChats().subscribe(response => {
      this.chats = response ?? [];
      this.chatsLoaded = true;
    });

    this.accountService.getAuthenticationState().subscribe(user => {
      this.currentUser = user;
    });

    this.stompService.watch(`/user/queue/messages/new`).subscribe((message: Message) => {
      this.onMessageReceived(message);
    });
  }
  onMessageReceived(message: Message) {
    const newMessage = JSON.parse(message.body) as IMessage;
    const chatIndex = this.chats.findIndex(chat => chat.chatId === newMessage.chatId);
    if (chatIndex !== -1) {
      this.chats[chatIndex].lastMessage = newMessage;
    } else if (this.chatService.doesChatExist(newMessage.chatId)) {
      this.chatService.getChat(newMessage.chatId).subscribe(response => {
        this.chats.push(response);
      });
    }

    if (newMessage.sender.username !== this.currentUser?.username) {
      const audio = new Audio();
      audio.src = 'assets/audio/notifications/new-message.mp3';
      audio.load();
      audio.play();
    }
  }
}
