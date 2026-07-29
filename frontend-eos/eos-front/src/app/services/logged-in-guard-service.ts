import {inject, Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';
import localStorageUtils from '../utils/localStorageUtils';
@Injectable({
  providedIn: 'root',
})
export class LoggedInGuardService {
  private readonly router = inject(Router);
  
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const token = localStorageUtils.getItem(localStorageUtils.tokenKey);
    if (token) {
      return true;
    } else {
      this.router.navigate(['/login']);
      return false;
    }
  }


}
