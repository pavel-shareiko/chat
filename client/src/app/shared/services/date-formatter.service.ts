import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class DateFormatterService {
  constructor() {}

  getDateFormat(date: Date | string | undefined): string {
    if (date === undefined) {
      return '';
    }
    const inputDate = new Date(date);
    const today = new Date();
    if (inputDate.toDateString() === today.toDateString()) {
      return 'HH:mm';
    }
    if (inputDate.getFullYear() === today.getFullYear()) {
      return 'd MMM HH:mm';
    }
    return 'YYYY, dd MMM HH:mm';
  }
}
