export interface IChat {
  chatId: number;
  lastMessage: IMessage;
  chatType: ChatType;
  participants: IParticipant[];
}

export interface IMessage {
  id: number;
  content: string;
  sender: IParticipant;
  createdAt: Date;
  modifiedAt: Date;
}

export enum ChatType {
  PERSONAL_CHAT,
  GROUP_CHAT,
  SELF_CHAT
}

export interface IParticipant{
    id: number,
    username: string,
    firstName: string,
    lastName: string,
    active: boolean 
}
