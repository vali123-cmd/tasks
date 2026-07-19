import { Component, inject } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Users } from '../services/users';

import { Router } from '@angular/router';

@Component({
  selector: 'app-login-component',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login-component.html',
  styleUrl: './login-component.css',
})
export class LoginComponent {
  isLoginMode: boolean = true;
  usersService = inject(Users);
  private fb = inject(FormBuilder);
  router = inject(Router);

  loginForm: FormGroup = this.fb.group({
    email: '',
    password: ''
  });
  
  registerForm: FormGroup = this.fb.group({    
    username: '',
    password: '',
    email: '',
    birthDate: ''
  });
  
  
  

  toggleMode() {
    this.isLoginMode = !this.isLoginMode;
    this.loginForm.reset();
    this.registerForm.reset();
  }
  
  registerCredentials = {
    username: '',
    password: '',
    email: '',
    birthDate: ''
  };

  updateRegisterCredentials() {
    this.registerCredentials.username = this.registerForm.get('username')?.value;
    this.registerCredentials.password = this.registerForm.get('password')?.value;
    this.registerCredentials.email = this.registerForm.get('email')?.value;
    this.registerCredentials.birthDate = this.registerForm.get('birthDate')?.value + 'T00:00:00'; // Append time to match the expected format
  }
  onSubmit() {
    if (this.isLoginMode) {
      
      console.log('Login credentials:', this.loginForm.value);
      this.usersService.login(this.loginForm.value).subscribe({
        next: (response) => {
          console.log('Login successful', response);
          //redirect to my-tasks page
          localStorage.setItem('user', JSON.stringify(response));
          this.router.navigate(['/my-tasks']);


        }
      });
    
      
    } else {
      console.log('Register credentials:', this.registerForm.value);
      this.updateRegisterCredentials();
      this.usersService.createUser(this.registerCredentials).subscribe({
        next: (response) => {
          console.log('User created successfully', response);
          this.router.navigate(['/my-tasks']);
          localStorage.setItem('user', JSON.stringify(response));
        },
        error: (err) => {
          console.error('Error creating user', err);
        }
      });
    
      
    }
  }
}
