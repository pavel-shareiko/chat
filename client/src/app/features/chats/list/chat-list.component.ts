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
import { MessagesService } from '../services/messages.service';

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
    private messagesService: MessagesService,
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
    this.stompService.watch(`/user/queue/messages/delete`).subscribe((message: Message) => {
      this.onMessageDeleted(message);
    });
    this.stompService.watch(`/user/queue/messages/edit`).subscribe((message: Message) => {
      this.onMessageEdited(message);
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

  onMessageDeleted(message: Message) {
    const messageIds = JSON.parse(message.body) as number[];
    const affectedChatIndexes = this.chats
      .filter(chat => chat.lastMessage && messageIds.includes(chat.lastMessage.id))
      .map((chat, index) => index);

    affectedChatIndexes.forEach(chatIndex => {
      this.chatService.getChat(this.chats[chatIndex].chatId).subscribe(response => {
        this.chats[chatIndex] = response;
        setTimeout(() => this.reorderChats(), 0);
      });
    });
  }

  onMessageEdited(message: Message) {
    const updatedMessage = JSON.parse(message.body) as IMessage;
    const affectedChats = this.chats.filter(
      chat => chat.lastMessage && chat.lastMessage.id === updatedMessage.id
    );

    affectedChats.forEach(chat => {
      chat.lastMessage = updatedMessage;
    });
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

  /**
   * Sorts the `chats` array in descending order based on the `createdAt` property of the last message
   * of each chat. If a chat has no last message or no `createdAt` property, it is considered to have
   * a creation time of 0. This function mutates the `chats` array in place.
   */
  private reorderChats(): void {
    this.chats.sort((a, b) => {
      return (
        (b.lastMessage?.createdAt?.getTime() ?? 0) - (a.lastMessage?.createdAt?.getTime() ?? 0)
      );
    });
  }
}
