import { Component, inject, OnInit, signal } from '@angular/core';
import { UserModel } from '../../models/user.interface';
import { UserService } from '../../services/user-service';
import { Router } from '@angular/router';
import { Toast } from '../../shared/toast';
import { Confirm } from '../../shared/confirm';

@Component({
  selector: 'app-user',
  imports: [],
  templateUrl: './user.html',
  styleUrl: './user.scss',
})
export class User implements OnInit {

  userService: UserService = inject(UserService);
  router: Router = inject(Router);
  toast: Toast = inject(Toast);
  confirm: Confirm = inject(Confirm);

  users = signal<UserModel[]>([]);

  ngOnInit(): void {
      this.loadUsers();
  }

  loadUsers(): void {
    this.userService.getUsers().subscribe({
      next: (req: any) => {
        this.users.set(req.content);
      },
      error: (err) => {
        this.toast.show('Failed to load users', 'danger');
        console.error('Failed to get Users: ', err);
      }
    });
  }

  async delete(id: number) {
    const ok = await this.confirm.open(
      'Delete user',
      `Are you sure you want to delete user ID ${id}?`
    );

    if (!ok) return;

    this.userService.deleteUser(id).subscribe({
      next: () => {
        this.toast.show(`User ${id} deleted`, 'success');
        this.users.set(this.users().filter(u => u.id !== id));
      },
      error: (err) => {
        this.toast.show(`Failed to delete user ${id}`, 'danger');
        console.error(err);
      }
    });
  }

  edit(id: number) {
    this.router.navigate(['/users/edit', id]);
  }

}
