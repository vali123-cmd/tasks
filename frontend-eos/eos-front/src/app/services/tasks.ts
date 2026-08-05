import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { TaskPostDTO} from '../interfaces/taskPostDTO';
import { Task } from '../interfaces/task';
import { PageResponse } from '../interfaces/pageResponse';
@Injectable({
  providedIn: 'root',
})
export class Tasks {
  private http = inject(HttpClient)  
  apiUrl = 'http://localhost:8080/tasks';
  public getTasks() {
    return this.http.get<PageResponse<Task>>(this.apiUrl);
  }

  public createTask(Task: TaskPostDTO) {
    return this.http.post(this.apiUrl, Task);
  }

  public getTaskById(id: number) {
    return this.http.get<Task>(`${this.apiUrl}/${id}`); 
  }

  public updateTask(id: number, Task: TaskPostDTO) {
    return this.http.put(`${this.apiUrl}/${id}`, Task);
  } 

  public deleteTask(id: number) {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  public searchTasks(filters: any) {
    let params = new HttpParams();

    
    if (filters.name) params = params.append('name', filters.name);
    if (filters.status) params = params.append('status', filters.status);
    if (filters.assignedTo) params = params.append('assignedTo', filters.assignedTo);
    if (filters.startDate) params = params.append('startDate', filters.startDate);
    if (filters.endDate) params = params.append('endDate', filters.endDate);
    if (filters.page !== undefined) params = params.append('page', filters.page);
    if (filters.size !== undefined) params = params.append('size', filters.size);
    if (filters.sortBy) params = params.append('sortBy', filters.sortBy);
    if (filters.sortDir) params = params.append('sortDir', filters.sortDir);

    return this.http.get<PageResponse<Task>>(`${this.apiUrl}/search`, { params });
    
  }

}
