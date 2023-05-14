import { Component, EventEmitter, Input, Output } from '@angular/core';
import { faMessage, faUser } from '@fortawesome/free-regular-svg-icons';
import { IUser } from 'src/app/core/models/user.model';
import { AccountService } from 'src/app/shared/services/account.service';

@Component({
  selector: 'app-user[user]',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss'],
})
export class UserComponent {
  @Input() user!: IUser;
  @Output() startChatEvent = new EventEmitter<string>();
  public currentUser!: IUser | null;

  faMessage = faMessage;
  faUser = faUser;

  constructor(private accountService: AccountService) {
    this.accountService.getAuthenticationState().subscribe(user => (this.currentUser = user));
  }

  startChat() {
    this.startChatEvent.emit(this.user.username);
  }
}
