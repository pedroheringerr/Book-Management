export interface UserModel {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  roles: Set<string>;
}
