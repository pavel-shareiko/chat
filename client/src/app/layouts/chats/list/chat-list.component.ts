import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChatListService } from './chat-list.service';
import { IChat } from '../chat.model';


@Component({
  selector: 'app-chat-list',
  templateUrl: './chat-list.component.html',
  styleUrls: ['./chat-list.component.scss']
})
export class ChatListComponent implements OnInit {
  chats: IChat[] = [];

  constructor(private chatListService: ChatListService) {

  }
  
  ngOnInit(): void { 
      this.chatListService.getAllChats().subscribe(res => {
        this.chats = res;
      });
  }

  trackById(index: number, chat: IChat): number {
    return chat.chatId;
  }
}
