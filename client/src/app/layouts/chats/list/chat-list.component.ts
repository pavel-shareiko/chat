import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChatListService } from './chat-list.service';
import { ChatType, IChat } from '../chat.model';

@Component({
  selector: 'app-chat-list',
  templateUrl: './chat-list.component.html',
  styleUrls: ['./chat-list.component.scss'],
})
export class ChatListComponent implements OnInit {
  chats: IChat[] = [];
  chatsLoaded = false;

  constructor(private chatListService: ChatListService) {}

  ngOnInit(): void {
    this.chatListService.getAllChats().subscribe(response => {
      this.chats = response ?? [];
      this.chatsLoaded = true;
    });
  }
}
