// find-chats.component.ts
import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, Output } from '@angular/core';
import { IUser } from 'src/app/core/models/user.model';
import { UserService } from 'src/app/shared/services/user.service';

@Component({
  selector: 'app-find-chats',
  templateUrl: './find-chats.component.html',
  styleUrls: ['./find-chats.component.scss'],
})
export class FindChatsComponent {
  @Output() startChatEvent = new EventEmitter<string>();
  @Output() closeModalEvent = new EventEmitter<void>();
  error: string = '';
  username: string = '';
  usersLoading: boolean = false;
  usersLoaded: boolean = false;
  searchResults: IUser[] = [];

  constructor(private userService: UserService) {}

  findUsers() {
    if (this.username.length === 0) {
      return;
    }

    if (this.username.length === 1 && this.username.charAt(0) === '#') {
      return;
    }

    if (this.username.charAt(0) === '#' && isNaN(Number(this.username.slice(1)))) {
      this.displayError('User id must be a number');
      return;
    }

    this.usersLoading = true;
    this.usersLoaded = false;

    this.userService.findUsers(this.username).subscribe({
      next: res => {
        this.usersLoading = false;
        this.usersLoaded = true;
        return (this.searchResults = res);
      },
      error: (err: HttpErrorResponse) => {
        this.displayError(err.error.message);
        this.usersLoading = false;
        this.usersLoaded = false;
      },
    });
  }

  startChat(username: string) {
    this.startChatEvent.emit(username);
  }

  closeModal() {
    this.closeModalEvent.emit();
  }

  private displayError(error: string) {
    if (!this.error) {
      this.error = error;
      setTimeout(() => {
        this.error = '';
      }, 5000);
    }
  }
}
