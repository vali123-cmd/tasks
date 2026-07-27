import { Injectable , Inject, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import localStorageUtils from '../utils/localStorageUtils';

@Injectable({
  providedIn: 'root',
})
export class GuestService {

  private readonly router = inject(Router);

    canActivate(): boolean {
      const token = localStorageUtils.getItem(localStorageUtils.tokenKey);
      if(token){
        this.router.navigate(['/my-tasks']);
        return false;
      }
      return true;
      
    }
}
