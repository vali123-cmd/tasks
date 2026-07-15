import { Component, OnInit, signal } from '@angular/core';
import { inject, Injectable } from '@angular/core';
import { Tasks } from '../services/tasks';
import { Task } from '../interfaces/task';

@Component({
  selector: 'app-my-tasks',
  imports: [],
  templateUrl: './my-tasks.html',
  styleUrl: './my-tasks.css',
})


export class MyTasks implements OnInit {
  private taskService = inject(Tasks);
  
  //cu observable
  // sortedTasks: Task[] = [];
  sortedTasks = signal<Task[]>([]);
  ngOnInit() {
    // this.taskService.getTasks().subscribe((tasks: Task[]) => {
    //   this.sortedTasks = tasks.sort((a: Task, b: Task) => new Date(a.dueDate).getTime() - new Date(b.dueDate).getTime());
    // })
    //array classic, am avea nevoie de change detector ref, ca sa se actualizeze la refresh



    // CU signal
    this.taskService.getTasks().subscribe((tasks: Task[]) => {
      this.sortedTasks.set(tasks.sort((a: Task, b: Task) => new Date(a.dueDate).getTime() - new Date(b.dueDate).getTime()));
    })
    
    //detecteaza automat schimbari

  }
}
