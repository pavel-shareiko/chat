import { Component, ElementRef, OnChanges, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ChatService } from '../chat.service';
import { IMessage } from '../chat.model';
import { MessagesService } from '../messages.service';
import { DateFormatterService } from '../../../common/date-formatter.service';
import { AccountService } from 'src/app/auth/account.service';
import { IUser } from 'src/app/models/user.model';
import { Message } from '@stomp/stompjs';
import { RxStompService } from 'src/app/shared/stomp/rx-stomp.service';

@Component({
  selector: 'app-chat-dialogue',
  templateUrl: './chat-dialogue.component.html',
  styleUrls: ['./chat-dialogue.component.scss'],
})
export class ChatDialogueComponent implements OnInit, OnChanges {
  @ViewChild('messageContainer') private messageContainer!: ElementRef;
  @ViewChild('chatInput') chatInput!: ElementRef;
  public chatName: string = '';
  public messages: IMessage[] = [];
  public selectedMessages: IMessage[] = [];
  public currentUser: IUser | null = null;
  public loadingMessages = true;
  public newMessage: string = '';
  public editMode: boolean = false;
  private dialogueId!: number;

  constructor(
    public dateFormatter: DateFormatterService,
    private accountService: AccountService,
    private route: ActivatedRoute,
    private router: Router,
    private chatService: ChatService,
    private messagesService: MessagesService,
    private stompService: RxStompService
  ) {}

  ngOnInit(): void {
    const chatId = this.extractChatIdFromRoute();
    if (!chatId) {
      this.router.navigate(['/']);
      return;
    }

    this.accountService.getAuthenticationState().subscribe(user => {
      this.currentUser = user;
    });

    this.dialogueId = chatId;
    this.chatService.doesChatExist(this.dialogueId).subscribe(exists => {
      if (!exists) {
        this.router.navigate(['/']);
      } else {
        this.getChatDisplayName();
        this.loadMessages();
      }
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

  ngOnChanges(): void {
    this.scrollToBottom();
  }

  scrollToBottom(): void {
    setTimeout(() => {
      this.messageContainer.nativeElement.scrollTop =
        this.messageContainer.nativeElement.scrollHeight;
    }, 0);
  }

  async onMessageReceived(message: Message) {
    const newMessage = JSON.parse(message.body) as IMessage;
    this.messages = [newMessage, ...this.messages];

    if (newMessage.sender.username !== this.currentUser?.username) {
      const audio = new Audio();
      audio.src = 'assets/audio/notifications/new-message.mp3';
      audio.load();
      audio.play();
    }
    this.scrollToBottom();
  }

  async onMessageDeleted(message: Message) {
    const messageIds = JSON.parse(message.body) as number[];
    this.messages = this.messages.filter(m => !messageIds.includes(m.id));
  }

  async onMessageEdited(message: Message) {
    const updatedMessage = JSON.parse(message.body) as IMessage;
    const messageIndex = this.messages.findIndex(m => m.id === updatedMessage.id);
    if (messageIndex !== -1) {
      const newMessages = [...this.messages]; // create a new array with all the old messages
      newMessages[messageIndex] = updatedMessage; // replace the modified message with the updated one
      this.messages = newMessages; // replace the old array with the new one
    }
  }

  goBack() {
    this.router.navigate(['/']);
  }

  getChatDisplayName(): void {
    this.chatService.getChat(this.dialogueId).subscribe(chat => {
      this.chatName = chat.displayName;
    });
  }

  extractChatIdFromRoute(): number | undefined {
    const id = +this.route.snapshot.paramMap.get('id')!;
    return isNaN(id) ? undefined : id;
  }

  loadMessages(): void {
    this.messagesService.getChatMessages(this.dialogueId).subscribe({
      next: (messages: IMessage[]) => {
        this.messages = messages;
        this.loadingMessages = false;
        this.scrollToBottom();
      },
      error: err => {
        console.error(err);
        this.loadingMessages = false;
      },
    });
  }

  sendMessage(): void {
    if (!this.newMessage) {
      return;
    }

    const messageContent = this.newMessage.trim();

    if (this.editMode) {
      // Editing an existing message
      if (this.selectedMessages.length !== 1) {
        throw new Error('Cannot edit more than one message at a time');
      }

      const selectedMessageId = this.selectedMessages[0].id;
      this.messagesService.editMessage(selectedMessageId, messageContent).subscribe({
        next: () => {
          this.editMode = false;
          this.newMessage = '';
          this.unselectAll();
        },
        error: err => {
          console.error(err);
        },
      });
    } else {
      // Sending a new message
      this.messagesService.sendMessage(this.dialogueId, messageContent).subscribe({
        next: () => {
          this.newMessage = '';
        },
        error: err => {
          console.error(err);
        },
      });
    }
  }

  selectMessage(clickedMessage: IMessage, htmlElement: HTMLElement) {
    if (this.editMode) {
      return;
    }
    this.selectedMessages.push(clickedMessage);
    htmlElement.classList.add('message__selected');
  }

  unselectMessage(messageIndex: number, htmlElement: HTMLElement) {
    if (this.editMode) {
      return;
    }
    this.selectedMessages.splice(messageIndex, 1);
    htmlElement.classList.remove('message__selected');
  }

  unselectAll() {
    this.selectedMessages.forEach(message => {
      const element = document.getElementById(`message-${message.id}`);
      if (element) {
        element.classList.remove('message__selected');
      }
    });
    this.selectedMessages = [];
  }

  deleteSelected(): void {
    this.messagesService.deleteMessages(this.selectedMessages).subscribe({
      next: () => {
        this.unselectAll();
      },
      error: err => {
        console.error(err);
      },
    });
  }

  enterEditMode(): void {
    if (this.selectedMessages.length !== 1) {
      throw new Error('Cannot edit more than one message at a time');
    }

    this.editMode = true;
    this.newMessage = this.selectedMessages[0].content;
  }

  allSelectedMessagesAreFromCurrentUser(): boolean {
    return this.selectedMessages.every(
      message => message.sender.username === this.currentUser?.username
    );
  }

  /* Event listeners */
  onInput(textarea: HTMLTextAreaElement) {
    const maxRows = 5;
    const lineHeight = parseInt(getComputedStyle(textarea).lineHeight);
    const rows = Math.floor(textarea.scrollHeight / lineHeight);
    if (rows > maxRows) {
      textarea.style.height = `${maxRows * lineHeight}px`;
    } else {
      textarea.style.height = 'auto';
      textarea.style.height = `${textarea.scrollHeight}px`;
    }
  }

  onKeydown(event: KeyboardEvent) {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      this.sendMessage();
    }
  }
  onMessageClick(clickedMessage: IMessage, event: MouseEvent) {
    const messageIndex = this.selectedMessages.indexOf(clickedMessage);
    const target = event.currentTarget as HTMLElement;
    if (messageIndex === -1) {
      this.selectMessage(clickedMessage, target);
    } else {
      this.unselectMessage(messageIndex, target);
    }
  }
}
