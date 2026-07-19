import { Injectable, inject} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CredentialsDTO } from '../interfaces/credentialsDTO';
import { RegisterCredentialsDTO } from '../interfaces/registerCredentialsDTO';
@Injectable({
  providedIn: 'root',
})
export class Users {
  private http = inject(HttpClient);

  login(credentials: CredentialsDTO) {
    return this.http.post('http://localhost:8080/users/login', credentials);
  }

  createUser(credentials: RegisterCredentialsDTO) {
    return this.http.post('http://localhost:8080/users', credentials);
  }
  
}
