import {HttpInterceptorFn} from '@angular/common/http';
import LocalStorageUtils from '../utils/localStorageUtils';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { inject } from '@angular/core';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  
  if (req.url.includes('/login') || req.url.includes('/register')) {
    return next(req);
  }
  
  const token = LocalStorageUtils.getItem(LocalStorageUtils.tokenKey);
  let processedRequest;
  if(token){
    processedRequest = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`),
    });
  } else {
    processedRequest = req;
  }
  
  return next(processedRequest).pipe(
    catchError((error) =>{
      if (error.status === 401 || error.status === 403) {
        LocalStorageUtils.deleteItem(LocalStorageUtils.tokenKey);
        router.navigate(['/login']);
      }
      throw error;
    })
  )
};