import { Injectable, inject} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CredentialsDTO } from '../interfaces/credentialsDTO';
import { RegisterCredentialsDTO } from '../interfaces/registerCredentialsDTO';
import { UserDTO } from '../interfaces/userDTO';
@Injectable({
  providedIn: 'root',
})
export class Users {
  private http = inject(HttpClient);

  login(credentials: CredentialsDTO) {
    return this.http.post('http://localhost:8080/auth/login', credentials, { responseType: 'text' });
  }

  createUser(credentials: RegisterCredentialsDTO) {
    return this.http.post('http://localhost:8080/auth/register', credentials, { responseType: 'text' });
  }

  getUsers() {
    return this.http.get<UserDTO[]>('http://localhost:8080/users');
  }

  updateUser(userId: number, user: UserDTO) {
    return this.http.put<UserDTO>(`http://localhost:8080/users/${userId}`, user);
  }

  deleteUser(userId: number) {
    return this.http.delete<void>(`http://localhost:8080/users/${userId}`);
  }
  
}
