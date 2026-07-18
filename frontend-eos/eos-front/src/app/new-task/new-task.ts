import { Component, inject, OnInit, signal } from '@angular/core';
import {TaskPostDTO} from '../interfaces/taskPostDTO';
import {FormsModule} from '@angular/forms';
import { Tasks } from '../services/tasks';
import { StatusNames } from '../services/status-names';
@Component({
  selector: 'app-new-task',
  imports: [FormsModule],
  templateUrl: './new-task.html',
  styleUrl: './new-task.css',
})
export class NewTask implements OnInit {
//create a task object with the same structure as the TaskPostDTO interface

  private taskService = inject(Tasks);
  private statusService = inject(StatusNames);

  protected readonly statusNames = signal<string[]>([]);

  
  newTask: TaskPostDTO = {
    content: '',
    dueDate: '',
    statusName: ''
  };

  ngOnInit() {
    this.statusService.getStatusNames().subscribe((statusNames) => {
      this.statusNames.set(statusNames);
    });
  }

  onSubmit() {
    if (!this.newTask.content || !this.newTask.dueDate || !this.newTask.statusName) {
      console.error("Formular incomplet")
      return;
    }
    //adaugare ora pt parsare pt java localdatetime
    this.newTask.dueDate = `${this.newTask.dueDate}T00:00:00`;

    this.taskService.createTask(this.newTask).subscribe({
      next: (response) => {
        console.log('Task creat ', response);
        //Resetez
        this.newTask = { content: '', dueDate: '', statusName: '' };
      },
      error: (err) => {
        console.error('eroare', err);
      }
    });
  }


}
