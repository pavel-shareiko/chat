import { AfterContentInit, Component, Input, OnInit } from '@angular/core';
import { ChatType, IChat } from '../chat.model';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements AfterContentInit {
  @Input() chat!: IChat;
  displayName: string = "";
  
  ngAfterContentInit(): void {
    console.log(this.chat);
    if (this.chat.chatType === ChatType.PERSONAL_CHAT) {
      const receiver = this.chat.participants[1]
      this.displayName = receiver.firstName + receiver.lastName;

      return;
    }

    if (this.chat.chatType === ChatType.SELF_CHAT) {
      this.displayName = "Self Chat";
      return;
    }

    if (this.chat.chatType === ChatType.GROUP_CHAT) {
      this.displayName = "Group Chat";
      return;
    }  
  }
}
