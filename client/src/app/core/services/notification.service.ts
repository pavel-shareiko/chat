import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  constructor() {}

  public playSound(src: string): void {
    const audio = new Audio();
    audio.src = src;
    audio.load();
    audio.play().catch(err => {
      if (err.name === 'NotAllowedError') {
        console.log(`Sound cannot be played, because user didn't interact with the page first`);
      } else {
        throw err;
      }
    });
  }
}
