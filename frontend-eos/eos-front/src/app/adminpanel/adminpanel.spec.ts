import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { Adminpanel } from './adminpanel';

describe('Adminpanel', () => {
  let component: Adminpanel;
  let fixture: ComponentFixture<Adminpanel>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Adminpanel],
      providers: [provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();

    fixture = TestBed.createComponent(Adminpanel);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
