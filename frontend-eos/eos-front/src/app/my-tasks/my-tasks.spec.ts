import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { MyTasks } from './my-tasks';
import { Tasks } from '../services/tasks';
import { StatusNames } from '../services/status-names';
import { Users } from '../services/users';

describe('MyTasks', () => {
  let component: MyTasks;
  let fixture: ComponentFixture<MyTasks>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyTasks]
    }).compileComponents();

    fixture = TestBed.createComponent(MyTasks);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
