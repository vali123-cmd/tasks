import { Component, OnInit, signal, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Tasks } from '../services/tasks';
import { Task } from '../interfaces/task';
import { StatusNames } from '../services/status-names';
import { TaskPostDTO } from '../interfaces/taskPostDTO';

@Component({
  selector: 'app-my-tasks',
  imports: [FormsModule],
  templateUrl: './my-tasks.html',
  styleUrl: './my-tasks.css',
})


export class MyTasks implements OnInit {
  private taskService = inject(Tasks);
  private statusService = inject(StatusNames);
  
  sortedTasks = signal<Task[]>([]);
  statusNames = signal<string[]>([]);
  activeModal = signal<'new' | 'edit' | null>(null);
  selectedTask: Task | null = null;
  newTaskForm: TaskPostDTO = this.createEmptyTaskForm();
  editTaskForm: TaskPostDTO = this.createEmptyTaskForm();

  ngOnInit() {
    this.refreshTasks();
    this.statusService.getStatusNames().subscribe((statusNames) => {
      this.statusNames.set(statusNames);
    });
  }

  openNewModal() {
    this.newTaskForm = this.createEmptyTaskForm();
    this.activeModal.set('new');
  }

  openEditModal(task: Task) {
    this.selectedTask = task;
    this.editTaskForm = {
      content: task.content,
      dueDate: task.dueDate.split('T')[0],
      statusName: task.statusName,
    };
    this.activeModal.set('edit');
  }

  closeModal() {
    this.activeModal.set(null);
    this.selectedTask = null;
  }

  submitNewTask() {
    if (!this.newTaskForm.content || !this.newTaskForm.dueDate || !this.newTaskForm.statusName) {
      return;
    }

    const payload: TaskPostDTO = {
      ...this.newTaskForm,
      dueDate: `${this.newTaskForm.dueDate}T00:00:00`,
    };

    this.taskService.createTask(payload).subscribe({
      next: () => {
        this.newTaskForm = this.createEmptyTaskForm();
        this.closeModal();
        this.refreshTasks();
      },
      error: (err) => {
        console.error('eroare', err);
      },
    });
  }

  submitEditTask() {
    if (!this.selectedTask || !this.editTaskForm.content || !this.editTaskForm.dueDate || !this.editTaskForm.statusName) {
      return;
    }

    const payload: TaskPostDTO = {
      ...this.editTaskForm,
      dueDate: `${this.editTaskForm.dueDate}T00:00:00`,
    };

    this.taskService.updateTask(this.selectedTask.id, payload).subscribe({
      next: () => {
        this.editTaskForm = this.createEmptyTaskForm();
        this.closeModal();
        this.refreshTasks();
      },
      error: (err) => {
        console.error('eroare', err);
      },
    });
  }

  private refreshTasks() {
    this.taskService.getTasks().subscribe((tasks: Task[]) => {
      this.sortedTasks.set(tasks.sort((a: Task, b: Task) => new Date(a.dueDate).getTime() - new Date(b.dueDate).getTime()));
    });
  }

  private createEmptyTaskForm(): TaskPostDTO {
    return {
      content: '',
      dueDate: '',
      statusName: '',
    };

  }
}
