import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChatService } from '../chat.service';
import { IChat } from '../chat.model';

@Component({
  selector: 'app-chat-list',
  templateUrl: './chat-list.component.html',
  styleUrls: ['./chat-list.component.scss'],
})
export class ChatListComponent implements OnInit {
  chats: IChat[] = [];
  chatsLoaded = false;

  constructor(private chatService: ChatService) {}

  ngOnInit(): void {
    this.chatService.getAllChats().subscribe(response => {
      this.chats = response ?? [];
      this.chatsLoaded = true;
    });
  }
}
