import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChatListComponent } from './list/chat-list.component';
import { ChatComponent } from './chat/chat.component';
import { FormsModule } from '@angular/forms';
import { FindChatsComponent } from './find-chats/find-chats.component';
import { UserComponent } from './user/user.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

@NgModule({
  imports: [CommonModule, FormsModule, FontAwesomeModule],
  declarations: [ChatComponent, ChatListComponent, FindChatsComponent, UserComponent],
  exports: [ChatComponent, ChatListComponent],
})
export class ChatsModule {}
