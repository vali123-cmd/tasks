import { Routes } from '@angular/router';
import { Homepage } from './homepage/homepage';
import { MyTasks} from './my-tasks/my-tasks';
import { NewTask } from './new-task/new-task';
import {Search} from './search/search';
import {EditTask} from './edit-task/edit-task';
export const routes: Routes = [
    {path:'home', component: Homepage},
    {path:'my-tasks', component: MyTasks},
    {path:'new-task', component: NewTask},
    {path:'search', component: Search},
    {path:'edit-task/:id', component: EditTask},
];
