import { inject, Injectable } from '@angular/core';
import {Auth} from './auth';
import { Router } from '@angular/router';
@Injectable({
  providedIn: 'root',
})
export class Adminguard {
  private readonly router = inject(Router);
  private readonly authService = inject(Auth);
  
  canActivate(): boolean {
    if (!this.authService.isAdmin()) {
      this.router.navigate(['/my-tasks']);
      return false;
    }
    return true;
  }
}
