import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { TaskPostDTO} from '../interfaces/taskPostDTO';
import { Task } from '../interfaces/task';
@Injectable({
  providedIn: 'root',
})
export class Tasks {
  private http = inject(HttpClient)  
  apiUrl = 'http://localhost:8080/tasks';
  public getTasks() {
    return this.http.get<any[]>(this.apiUrl);
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

  public searchTasks(filters: any) {
    let params = new HttpParams();

    
    if (filters.name) params = params.append('name', filters.name);
    if (filters.status) params = params.append('status', filters.status);
    if (filters.assignedTo) params = params.append('assignedTo', filters.assignedTo);
    if (filters.startDate) params = params.append('startDate', filters.startDate);
    if (filters.endDate) params = params.append('endDate', filters.endDate);

    return this.http.get<Task[]>(`${this.apiUrl}/search`, { params });
  
  }

}
