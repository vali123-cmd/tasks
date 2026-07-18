import { Component, inject, OnInit } from '@angular/core';
import {FormsModule} from '@angular/forms';
import { Tasks } from '../services/tasks';
import {TaskPostDTO} from '../interfaces/taskPostDTO';
import { ActivatedRoute, Router } from '@angular/router';
import { StatusNames } from '../services/status-names';



@Component({
  selector: 'app-edit-task',
  imports: [FormsModule],
  templateUrl: './edit-task.html',
  styleUrl: './edit-task.css',
})
export class EditTask {
  private taskService = inject(Tasks);
  private statusService = inject(StatusNames);
  protected statusNames: string[] = [];
  task: TaskPostDTO = {
    content: '',
    dueDate: '',
    statusName: ''
  };
  taskId: number = 0;
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  ngOnInit() {
    this.statusService.getStatusNames().subscribe((statusNames) => {
      this.statusNames = statusNames;
    });

    this.taskId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.taskId) {
      this.taskService.getTaskById(this.taskId).subscribe({
        next: (response) => {
          console.log('Task retrieved ', response);
          this.task = {
            content: response.content,
            dueDate: response.dueDate.split('T')[0],
            statusName: response.statusName
          };
        },
        error: (err) => {
          console.error('eroare', err);
        }
      });
    }
  }


  onSubmit() {
    if (!this.task.content || !this.task.dueDate || !this.task.statusName) {
      console.error("Formular incomplet")
      return;
    }
    //adaugare ora pt parsare pt java localdatetime
    const payload: TaskPostDTO = {
      ...this.task,
      dueDate: `${this.task.dueDate}T00:00:00`
    };

    this.taskService.updateTask(this.taskId, payload).subscribe({
      next: (response) => {
        console.log('Task editat ', response);
        this.router.navigate(['/my-tasks']);
      },
      error: (err) => {
        console.error('eroare', err);
      }

    });
  }

}


