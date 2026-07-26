import { Component, signal } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import localStorageUtils from './utils/localStorageUtils';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('eos-front');
  
  isLoggedIn(): boolean {
    if(localStorageUtils.getItem(localStorageUtils.tokenKey)) {
      return true;
    }
    return false;
  }

  logout(): void {
    localStorageUtils.deleteItem(localStorageUtils.tokenKey);
    window.location.reload();
  }

  

  



}
