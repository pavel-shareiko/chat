export interface LoginUser {
  username: string;
  password: string;
}

export interface RegistrationUser {
  firstName: string;
  lastName: string;
  username: string;
  password: string;
}

export interface IUser {
  id: number;
  firstName: string;
  lastName: string;
  username: string;
  roles: IRole[];
}

export interface IRole {
  name: string;
}
