import { Routes } from '@angular/router';
import { Homepage } from './homepage/homepage';
import { MyTasks} from './my-tasks/my-tasks';
import { NewTask } from './new-task/new-task';
import {Search} from './search/search';
import {EditTask} from './edit-task/edit-task';
import { LoginComponent } from './login-component/login-component';
import { LoggedInGuardService } from './services/logged-in-guard-service';
export const routes: Routes = [
    {path:'home', component: Homepage },
    {path:'my-tasks', component: MyTasks, canActivate: [LoggedInGuardService]},
    {path:'new-task', component: NewTask, canActivate: [LoggedInGuardService]},
    {path:'search', component: Search, canActivate: [LoggedInGuardService]},
    {path:'edit-task/:id', component: EditTask, canActivate: [LoggedInGuardService]},
    {path:'login', component: LoginComponent},
];

