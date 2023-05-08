import { Pipe, PipeTransform } from '@angular/core';
import { IMessage } from 'src/app/features/chats/chat.model';

@Pipe({
  name: 'messageGrouping',
})
export class MessageGroupingPipe implements PipeTransform {
  /**
   * Groups messages by timestamp with a maximum time gap of 10 minutes for messages sent today
   * and a maximum time gap of one day for messages sent in the past
   *
   * @param messages An array of IMessage objects sorted in descending order by timestamp
   * @returns An array of arrays, where each subarray contains IMessage objects
   */
  transform(messages: IMessage[]): IMessage[][] {
    const groupedMessages: IMessage[][] = [];

    let lastGroupTime: Date | null = null;
    let currentGroup: IMessage[] = [];

    for (let i = messages.length - 1; i >= 0; i--) {
      const message: IMessage = messages[i];
      const messageTime: Date = new Date(message.createdAt);
      const today: Date = new Date();

      if (
        messageTime.getFullYear() < today.getFullYear() ||
        messageTime.getMonth() < today.getMonth() ||
        messageTime.getDate() < today.getDate()
      ) {
        // Group messages from a day before or earlier
        const groupDate: Date = new Date(
          messageTime.getFullYear(),
          messageTime.getMonth(),
          messageTime.getDate()
        );
        if (lastGroupTime === null || groupDate.getTime() !== lastGroupTime.getTime()) {
          // Start a new group for this day
          if (currentGroup.length > 0) {
            groupedMessages.push(currentGroup);
          }
          currentGroup = [];
          lastGroupTime = groupDate;
        }
      } else {
        // Group messages sent today
        const messageMinutes: number = messageTime.getMinutes();
        const groupMinutes: number = Math.floor(messageMinutes / 10) * 10;
        const groupTime: Date = new Date(
          today.getFullYear(),
          today.getMonth(),
          today.getDate(),
          messageTime.getHours(),
          groupMinutes
        );
        if (lastGroupTime === null || groupTime.getTime() !== lastGroupTime.getTime()) {
          // Start a new group for this interval
          if (currentGroup.length > 0) {
            groupedMessages.push(currentGroup);
          }
          currentGroup = [];
          lastGroupTime = groupTime;
        }
      }

      currentGroup.push(message);
    }

    if (currentGroup.length > 0) {
      groupedMessages.push(currentGroup);
    }

    return groupedMessages;
  }
}
