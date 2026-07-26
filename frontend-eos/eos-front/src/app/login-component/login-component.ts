import { Component, inject } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Users } from '../services/users';

import { Router } from '@angular/router';
import { CredentialsDTO } from '../interfaces/credentialsDTO';
import localStorageUtils from '../utils/localStorageUtils';

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
    this.registerCredentials.username = btoa(this.registerForm.get('username')?.value);
    this.registerCredentials.password = btoa(this.registerForm.get('password')?.value);
    this.registerCredentials.email = btoa(this.registerForm.get('email')?.value);
    this.registerCredentials.birthDate = this.registerForm.get('birthDate')?.value + 'T00:00:00'; // Append time to match the expected format
  }

  login(): void {
    const encodedUserDTO: CredentialsDTO = {
      email: btoa(this.loginForm.get('email')?.value),
      password: btoa(this.loginForm.get('password')?.value)
    };

    this.usersService.login(encodedUserDTO).subscribe({
      next: (response: any) => {
        if(response.startsWith('403'))
        {
          console.error('Login failed: Invalid credentials');
          return;
        }
        localStorageUtils.setItem(localStorageUtils.tokenKey, response as string);

        this.router.navigate(['/my-tasks']);
      },
      error: (err) => {
        console.error('Error during login', err);
      }
      });

  }
  onSubmit() {
    if (this.isLoginMode) {
      
       this.login();
    
      
    } else {
      console.log('Register credentials:', this.registerForm.value);
      this.updateRegisterCredentials();
      this.usersService.createUser(this.registerCredentials).subscribe({
        next: (response) => {
          console.log('User created successfully', response);
          this.router.navigate(['/my-tasks']);
          localStorageUtils.setItem(localStorageUtils.tokenKey, response as string);
        },
        error: (err) => {
          console.error('Error creating user', err);
        }
      });
    
      
    }
  }
}
