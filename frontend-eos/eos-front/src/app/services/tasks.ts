import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { TaskPostDTO} from '../interfaces/taskPostDTO';
import { Task } from '../interfaces/task';
@Injectable({
  providedIn: 'root',
})
export class Tasks {
  private http = inject(HttpClient)  

  public getTasks() {
    return this.http.get<any[]>('http://localhost:8080/tasks');
  }

  public createTask(Task: TaskPostDTO) {
    return this.http.post('http://localhost:8080/tasks', Task);
  }

  public getTaskById(id: number) {
    return this.http.get<Task>(`http://localhost:8080/tasks/${id}`); 
  }

  public updateTask(id: number, Task: TaskPostDTO) {
    return this.http.put(`http://localhost:8080/tasks/${id}`, Task);
  } 

}
