import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { StatusTypeDTO } from '../interfaces/statusTypeDTO';

@Injectable({
  providedIn: 'root',
})
export class StatusNames {
   
  private http = inject(HttpClient);

  getStatusNames() {
    return this.http.get<StatusTypeDTO[]>('http://localhost:8080/statuses');
  }




}
