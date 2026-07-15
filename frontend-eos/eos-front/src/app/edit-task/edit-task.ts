import { Component, inject, OnInit,  signal } from '@angular/core';
import {FormsModule} from '@angular/forms';
import { Tasks } from '../services/tasks';
import {TaskPostDTO} from '../interfaces/taskPostDTO';
import { ActivatedRoute, Router } from '@angular/router';



@Component({
  selector: 'app-edit-task',
  imports: [FormsModule],
  templateUrl: './edit-task.html',
  styleUrl: './edit-task.css',
})
export class EditTask {
  private taskService = inject(Tasks);
  protected readonly task = signal<any>({
    id: null,
    content: '',
    dueDate: '',
    statusName: ''
  });
  taskId: number = 0;
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  ngOnInit() {
    this.taskId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.taskId) {
      this.taskService.getTaskById(this.taskId).subscribe({
        next: (response) => {
          console.log('Task retrieved ', response);
          this.task.set({
            id: this.taskId,
            content: response.content,
            dueDate: response.dueDate.split('T')[0], // extragem doar data fara formatul java
            statusName: response.statusName
          });
        },
        error: (err) => {
          console.error('eroare', err);
        }
      });
    }
  }


  onSubmit() {
    if (!this.task().content || !this.task().dueDate || !this.task().statusName) {
      console.error("Formular incomplet")
      return;
    }
    //adaugare ora pt parsare pt java localdatetime
    this.task.set({
      ...this.task(),
      dueDate: `${this.task().dueDate}T00:00:00`
    });

    this.taskService.updateTask(this.taskId, this.task()).subscribe({
      next: (response) => {
        console.log('Task editat ', response);
        
        this.task.set({ id: this.taskId, content: this.task().content, dueDate: this.task().dueDate, statusName: this.task().statusName });
      },
      error: (err) => {
        console.error('eroare', err);
      }

    });
  }

}


