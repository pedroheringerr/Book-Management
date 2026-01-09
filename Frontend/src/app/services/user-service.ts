import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { UserModel } from '../models/user.interface';
import { UpdatedUser } from '../models/updated-user.interface';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UserService {

  http: HttpClient = inject(HttpClient);

  url = `${environment.apiUrl}/api/user`;

  getUsers() {
    return this.http.get<UserModel>(this.url);
  }

  getUserById(id: number) {
    return this.http.get<UserModel>(`${this.url}/${id}`);
  }

  deleteUser(id: number) {
    return this.http.delete(`${this.url}/${id}`);
  }

  updateUser(id: number, user: UpdatedUser) {
    return this.http.put(`${this.url}/${id}`, user);
  }

}
