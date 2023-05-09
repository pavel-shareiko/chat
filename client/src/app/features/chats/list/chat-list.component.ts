import { NotificationService } from './../../../core/services/notification.service';
import { Message } from '@stomp/stompjs';
import { Component, ViewChild } from '@angular/core';
import { OnInit } from '@angular/core';
import { IChat, IMessage } from '../../../core/models/chat.model';
import { RxStompService } from 'src/app/shared/stomp/rx-stomp.service';
import { AccountService } from 'src/app/shared/services/account.service';
import { IUser } from 'src/app/core/models/user.model';
import { ChatService } from '../services/chat.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { notificationSounds } from 'src/app/core/constants/assets.constants';

@Component({
  selector: 'app-chat-list',
  templateUrl: './chat-list.component.html',
  styleUrls: ['./chat-list.component.scss'],
})
export class ChatListComponent implements OnInit {
  @ViewChild('findChatsModal') findChatsModal: any;
  chats: IChat[] = [];
  chatsLoaded = false;
  currentUser: IUser | null = null;

  constructor(
    private chatService: ChatService,
    private stompService: RxStompService,
    private accountService: AccountService,
    private modalService: NgbModal,
    private router: Router,
    private notificationService: NotificationService
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
    console.log(`Current chats`, this.chats);
    const newMessage = JSON.parse(message.body) as IMessage;
    const chatIndex = this.chats.findIndex(chat => chat.chatId === newMessage.chatId);
    console.log(`Message received for chat with index`, chatIndex);
    if (chatIndex !== -1) {
      this.chats[chatIndex].lastMessage = newMessage;
      this.raiseChat(chatIndex);
    } else {
      setTimeout(() => {
        this.chatService.getChat(newMessage.chatId).subscribe(response => {
          this.chats.unshift(response);
        });
      }, 0);
    }
    this.notificationService.playSound(notificationSounds.NEW_MESSAGE);
  }

  openFindChatsModal() {
    this.modalService.open(this.findChatsModal);
  }

  startChat(username: string) {
    this.chatService.startChat(username).subscribe({
      next: res => {
        this.router.navigate(['/chats', res]);
      },
    });
  }

  closeFindChatsModal() {
    this.modalService.dismissAll();
  }

  private raiseChat(chatIndex: number) {
    if (chatIndex === 0) {
      return;
    }

    const chatToRaise = this.chats[chatIndex];
    this.chats.splice(chatIndex, 1);
    this.chats.unshift(chatToRaise);
  }
}
