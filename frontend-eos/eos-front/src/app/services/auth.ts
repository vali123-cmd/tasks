import { Injectable, inject} from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class Auth  {
  isAdmin(): boolean {
  const token = localStorage.getItem('token');
  if (!token) return false;

  try {
    
    const payloadPart = token.split('.')[1];
    const decodedPayload = JSON.parse(atob(payloadPart));
    
    return decodedPayload.roleId === 1; 
  } catch (error) {
    
    console.error('Error decoding token', error);
    return false;
  }
}

  
}