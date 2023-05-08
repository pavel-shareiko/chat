import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChatListComponent } from './list/chat-list.component';
import { ChatComponent } from './chat/chat.component';

@NgModule({
  imports: [CommonModule],
  declarations: [ChatComponent, ChatListComponent],
  exports: [ChatComponent, ChatListComponent],
})
export class ChatsModule {}
