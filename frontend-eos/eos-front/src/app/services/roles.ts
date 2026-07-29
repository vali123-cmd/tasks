import { inject, Injectable } from '@angular/core';
import {HttpClient } from '@angular/common/http';

export interface RoleDTO {
  roleId: number;
  rolename: string;
}

@Injectable({
  providedIn: 'root',
})
export class Roles {

  private http = inject(HttpClient);

  getAllRoles()
  {
    return this.http.get<RoleDTO[]>(`http://localhost:8080/roles`)
  }

}
