export interface IChat {
  chatId: number;
  displayName: string;
  lastMessage: IMessage | undefined;
  chatType: ChatType;
  participants: IParticipant[];
}

export interface IMessage {
  id: number;
  chatId: number;
  content: string;
  sender: IParticipant;
  createdAt: Date;
  modifiedAt: Date;
}

export interface IParticipant {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  active: boolean;
}

export enum ChatType {
  PERSONAL_CHAT = 'PERSONAL_CHAT',
  GROUP_CHAT = 'GROUP_CHAT',
  SELF_CHAT = 'SELF_CHAT',
}
