import { Component, OnInit, signal, inject } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Tasks } from '../services/tasks';
import { Task } from '../interfaces/task';
import { StatusNames } from '../services/status-names';
import { TaskPostDTO } from '../interfaces/taskPostDTO';
import { StatusTypeDTO } from '../interfaces/statusTypeDTO';
import {debounceTime, distinctUntilChanged} from 'rxjs/operators';

@Component({
  selector: 'app-my-tasks',
  imports: [FormsModule, ReactiveFormsModule],
  templateUrl: './my-tasks.html',
  styleUrl: './my-tasks.css',
})


export class MyTasks implements OnInit {
  private taskService = inject(Tasks);
  private statusService = inject(StatusNames);
  private fb = inject(FormBuilder);
  
  sortedTasks = signal<Task[]>([]);
  


  statusNames = signal<StatusTypeDTO[]>([]);
  activeModal = signal<'new' | 'edit' | null>(null);
  selectedTask: Task | null = null;
  newTaskForm: TaskPostDTO = this.createEmptyTaskForm();
  editTaskForm: TaskPostDTO = this.createEmptyTaskForm();

  searchForm!: FormGroup;


  ngOnInit() {
    this.initSearchForm();
    this.refreshTasks();
    this.statusService.getStatusNames().subscribe((statusNames) => {
      this.statusNames.set(statusNames);
    });
  }
  initSearchForm() {
    this.searchForm = this.fb.group({
      name: [''],
      status: [''],
      assignedTo: [''],
      startDate: [''],
      endDate: ['']
    });

    this.searchForm.valueChanges.pipe(
      debounceTime(500),
      distinctUntilChanged()
    ).subscribe(filters => {
      this.applyFilters(filters);
    });
  }

  applyFilters(filters: any) {
    console.log('Filtrele trimise sunt:', filters);
    this.taskService.searchTasks(filters).subscribe({
      next: (tasks: Task[]) => {
        this.sortedTasks.set(tasks.sort((a, b) => new Date(a.dueDate).getTime() - new Date(b.dueDate).getTime()));
      },
      error: (err) => console.error('Eroare la filtrare', err)
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
