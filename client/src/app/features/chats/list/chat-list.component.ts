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
import { faPlus } from '@fortawesome/free-solid-svg-icons';

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

  // icons
  faPlus = faPlus;

  constructor(
    private chatService: ChatService,
    private stompService: RxStompService,
    private accountService: AccountService,
    private modalService: NgbModal,
    private router: Router,
    private notificationService: NotificationService
  ) {}

  /**
   * Initializes the component, getting all chats, setting the current user,
   * and subscribing to watch for new messages.
   *
   * @return {void}
   */
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

  /**
   * Handles a received message by updating the relevant chat and, if the chat is not currently open,
   * adding it to the list of chats. Also plays a notification sound if the message is from another user.
   *
   * @param {Message} message - The received message.
   */
  onMessageReceived(message: Message) {
    const newMessage = JSON.parse(message.body) as IMessage;
    const chatIndex = this.chats.findIndex(chat => chat.chatId === newMessage.chatId);
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
    if (this.currentUser?.username !== newMessage.sender.username) {
      this.notificationService.playSound(notificationSounds.NEW_MESSAGE);
    }
  }

  /**
   * Opens the find chats modal.
   *
   * @return {void}
   */
  openFindChatsModal() {
    this.modalService.open(this.findChatsModal);
  }

  /**`
   * Starts a chat with the given username.
   *
   * @param {string} username - The username to start the chat with.
   */
  startChat(username: string) {
    this.chatService.startChat(username).subscribe({
      next: res => {
        this.router.navigate(['/chats', res]);
      },
    });
  }

  /**
   * Closes the Find Chats Modal by dismissing all modals.
   */
  closeFindChatsModal() {
    this.modalService.dismissAll();
  }

  /**
   * Moves the chat at the given index to the top of the list of chats.
   *
   * @param {number} chatIndex - The index of the chat to raise.
   */
  private raiseChat(chatIndex: number) {
    if (chatIndex === 0) {
      return;
    }

    const chatToRaise = this.chats[chatIndex];
    this.chats.splice(chatIndex, 1);
    this.chats.unshift(chatToRaise);
  }
}
