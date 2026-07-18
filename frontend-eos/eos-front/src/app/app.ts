import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('eos-front');
  
  getUser(){
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  }


  isLoggedIn() {
    const user = localStorage.getItem('user');
    return user !== null;
  }
  
  logout() {
    localStorage.removeItem('user');
    window.location.href = '/login'; 
  }
}
