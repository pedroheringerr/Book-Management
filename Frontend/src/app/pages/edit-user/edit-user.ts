import { Component, inject, OnInit, signal } from '@angular/core';
import {
  FormArray,
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { UserModel } from '../../models/user.interface';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user-service';
import { UpdatedUser } from '../../models/updated-user.interface';
import { Toast } from '../../shared/toast';

@Component({
  selector: 'app-edit-user',
  imports: [ReactiveFormsModule],
  templateUrl: './edit-user.html',
  styleUrl: './edit-user.scss',
})
export class EditUser implements OnInit {
  router: Router = inject(Router);
  formBuilder: FormBuilder = inject(FormBuilder);
  route: ActivatedRoute = inject(ActivatedRoute);
  userService: UserService = inject(UserService);
  toast: Toast = inject(Toast);

  updatedUser = signal<UserModel | null>(null);

  readonly allRoles: readonly string[] = ['READER', 'USER', 'ADMIN'];

  userForm: FormGroup = this.formBuilder.group({
    email: ['', [Validators.email, Validators.required]],
    firstName: ['', [Validators.required]],
    lastName: ['', [Validators.required]],
    roles: this.formBuilder.array<FormControl<boolean>>([]),
  });

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
      this.loadUser(Number(id));
    }
  }

  loadUser(id: number) {
    this.userService.getUserById(id).subscribe((user) => {
      const rolesSet = new Set<string>(user.roles);

      this.updatedUser.set({
        ...user,
        roles: rolesSet,
      });

      this.userForm.patchValue({
        email: user.email,
        firstName: user.firstName,
        lastName: user.lastName,
      });

      const rolesArray = this.userForm.get('roles') as FormArray;
      rolesArray.clear();

      this.allRoles.forEach((role) => {
        rolesArray.push(new FormControl(rolesSet.has(role)));
      });
    });
  }

  get rolesArray(): FormArray<FormControl<boolean>> {
    return this.userForm.get('roles') as FormArray<FormControl<boolean>>;
  }

  get rolesReady(): boolean {
    return this.rolesArray.length === this.allRoles.length;
  }

  submit() {
    if (this.userForm.valid) {
      const rolesArray: string[] = [];

      const rolesIndex = this.userForm.get('roles') as FormArray;

      rolesIndex.controls.forEach((control, index) => {
        if (control.value) {
          rolesArray.push(this.allRoles[index]);
        }
      });

      const user = {
        ...this.updatedUser()!,
        ...this.userForm.value,
      };

      const updatedUser: UpdatedUser = {
        email: user.email,
        firstName: user.firstName,
        lastName: user.lastName,
        roles: rolesArray,
      };

      this.userService.updateUser(user.id, updatedUser).subscribe({
        next: (res: any) => {
          this.toast.show('User updated successfully', 'success');
          console.log(res);
          this.router.navigate(['/users']);
        },
        error: (err) => {
          this.toast.show(`Failed to update user (ID: ${user.id})`, 'danger');
          console.error('Upadate failed: ', err);
        },
      });
    } else {
      this.toast.show('Form is invalid. Please check fields.', 'warning');
    }
  }
}
