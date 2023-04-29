import { AfterViewChecked, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ChatService } from '../chat.service';
import { IMessage } from '../chat.model';
import { MessagesService } from '../messages.service';
import { DateFormatterService } from '../../../common/date-formatter.service';
import { AccountService } from 'src/app/auth/account.service';
import { IUser } from 'src/app/models/user.model';
@Component({
  selector: 'app-chat-dialogue',
  templateUrl: './chat-dialogue.component.html',
  styleUrls: ['./chat-dialogue.component.scss'],
})
export class ChatDialogueComponent implements OnInit, AfterViewChecked {
  @ViewChild('messageContainer') private messageContainer!: ElementRef;
  @ViewChild('myTextarea') myTextarea!: ElementRef;
  public chatName: string = '';
  public messages: IMessage[] = [];
  public currentUser: IUser | null = null;
  public loadingMessages = true;
  public newMessage: string = '';
  public rows: number = 1;
  public textareaWidth: number = 0;
  public maxHeight: string = '100px';

  private dialogueId!: number;

  constructor(
    public dateFormatter: DateFormatterService,
    private accountService: AccountService,
    private route: ActivatedRoute,
    private router: Router,
    private chatService: ChatService,
    private messagesService: MessagesService
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
  }

  ngAfterViewChecked(): void {
    this.messageContainer.nativeElement.scrollTop =
      this.messageContainer.nativeElement.scrollHeight;
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
      },
      error: err => {
        console.error(err);
        this.loadingMessages = false;
      },
    });
  }
  onInput() {
    const textarea = this.myTextarea.nativeElement;
    const { scrollHeight, clientWidth } = textarea;
    const maxHeight = parseInt(this.maxHeight);
    const newMessageLength = this.newMessage.length;
    const rowHeight = 20;
    const prevHeight = parseInt(textarea.style.height);

    if (clientWidth !== this.textareaWidth) {
      textarea.style.height = 'auto';
      this.textareaWidth = clientWidth;
    }

    if (scrollHeight > maxHeight) {
      textarea.style.overflow = 'auto';
    } else {
      textarea.style.overflow = 'hidden';
    }

    textarea.style.height = 'auto';
    const height = Math.min(scrollHeight, maxHeight);
    textarea.style.height = height + 'px';

    this.rows = Math.ceil(scrollHeight / rowHeight);

    if (
      (textarea.style.overflow === 'hidden' && parseInt(textarea.style.height) < prevHeight) ||
      textarea.value.length < newMessageLength
    ) {
      textarea.style.height = 'auto';
      this.rows = Math.ceil(textarea.scrollHeight / rowHeight);
    }
  }

  sendMessage(): void {
    if (!this.newMessage) {
      return;
    }
    this.newMessage = '';
    // this.messagesService.sendMessage(this.dialogueId, this.newMessage).subscribe({
    //   next: () => {
    //     this.newMessage = '';
    //     this.loadMessages();
    //   },
    //   error: err => {
    //     console.error(err);
    //   },
    // });
  }
}
