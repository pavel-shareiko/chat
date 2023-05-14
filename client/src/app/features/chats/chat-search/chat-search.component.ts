import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, Output } from '@angular/core';
import { faFaceSadTear } from '@fortawesome/free-regular-svg-icons';
import { faSearch, faSpinner } from '@fortawesome/free-solid-svg-icons';
import { IUser } from 'src/app/core/models/user.model';
import { UserService } from 'src/app/shared/services/user.service';

@Component({
  selector: 'app-chat-search',
  templateUrl: './chat-search.component.html',
  styleUrls: ['./chat-search.component.scss'],
})
export class ChatSearchComponent {
  @Output() startChatEvent = new EventEmitter<string>();
  @Output() closeModalEvent = new EventEmitter<void>();
  error: string = '';
  username: string = '';
  usersLoading: boolean = false;
  usersLoaded: boolean = false;
  searchResults: IUser[] = [];

  faSearch = faSearch;
  faFaceSadTear = faFaceSadTear;
  faSpinner = faSpinner;

  constructor(private userService: UserService) {}

    /**
   * Finds users based on the entered username.
   * If the username is empty or starts with '#' and is not followed by a number, an error message is displayed.
   * Upon success, the results are displayed in the searchResults array.
   *
   * @return void
   */
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

    /**
   * Emits a startChatEvent with the given username and closes the modal.
   *
   * @param {string} username - The username of the user starting the chat.
   */
  startChat(username: string) {
    this.startChatEvent.emit(username);
    this.closeModal();
  }

  closeModal() {
    this.closeModalEvent.emit();
  }

    /**
   * Sets the error message to display and clears it after 5 seconds if no error is currently displayed
   *
   * @param {string} error - The error message to display
   */
  private displayError(error: string) {
    if (!this.error) {
      this.error = error;
      setTimeout(() => {
        this.error = '';
      }, 5000);
    }
  }
}
