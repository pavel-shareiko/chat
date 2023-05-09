import { Component, EventEmitter, Input, Output } from '@angular/core';
import { IUser } from 'src/app/core/models/user.model';

@Component({
  selector: 'app-user[user]',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss'],
})
export class UserComponent {
  @Input() user!: IUser;
  @Output() startChatEvent = new EventEmitter<string>();

  startChat() {
    this.startChatEvent.emit(this.user.username);
  }
}
