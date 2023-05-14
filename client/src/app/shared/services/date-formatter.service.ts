import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class DateFormatterService {
  constructor() {}

  /**
   * Returns a string representing the format of the given date.
   *
   * @param {Date | string | undefined} date - the date to format
   * @return {string} the format of the given date as a string
   */
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
