import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Users } from '../services/users';
import { Roles, RoleDTO } from '../services/roles';
import { UserDTO } from '../interfaces/userDTO';

@Component({
  selector: 'app-adminpanel',
  imports: [CommonModule, FormsModule],
  templateUrl: './adminpanel.html',
  styleUrl: './adminpanel.css',
})
export class Adminpanel implements OnInit {

  private userService = inject(Users);
  private rolesService = inject(Roles);

  users = signal<UserDTO[]>([]);
  roles = signal<RoleDTO[]>([]);
  loading = signal(true);
  savingUserId = signal<number | null>(null);
  error = signal<string | null>(null);

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.loading.set(true);
    this.error.set(null);

    this.userService.getUsers().subscribe({
      next: (users) => this.users.set(users),
      error: (err) => {
        console.error('Error getting users:', err);
        this.error.set('Unable to load users.');
        this.loading.set(false);
      },
      complete: () => this.loadRoles(),
    });
  }

  loadRoles() {
    this.rolesService.getAllRoles().subscribe({
      next: (roles) => {
        this.roles.set(roles);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Error getting roles:', err);
        this.error.set('Unable to load roles.');
        this.loading.set(false);
      },
    });
  }

  saveUser(user: UserDTO) {
    this.savingUserId.set(user.userId);
    this.userService.updateUser(user.userId, user).subscribe({
      next: (updatedUser) => {
        this.users.update((currentUsers) =>
          currentUsers.map((currentUser) =>
            currentUser.userId === updatedUser.userId ? updatedUser : currentUser,
          ),
        );
        this.savingUserId.set(null);
      },
      error: (err) => {
        console.error('Error updating user:', err);
        this.error.set('Unable to update user.');
        this.savingUserId.set(null);
      },
    });
  }

  deleteUser(userId: number) {
    if (!confirm('Delete this user?')) {
      return;
    }

    this.userService.deleteUser(userId).subscribe({
      next: () => {
        this.users.update((currentUsers) => currentUsers.filter((user) => user.userId !== userId));
      },
      error: (err) => {
        console.error('Error deleting user:', err);
        this.error.set('Unable to delete user.');
      },
    });
    
    
  }

}
