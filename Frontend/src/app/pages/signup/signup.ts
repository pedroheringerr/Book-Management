import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Auth } from '../../services/auth';
import { Router } from '@angular/router';
import { Toast } from '../../shared/toast';

@Component({
  selector: 'app-signup',
  imports: [ReactiveFormsModule],
  templateUrl: './signup.html',
  styleUrl: './signup.scss',
})
export class Signup {

  formBuilder: FormBuilder = inject(FormBuilder);
  auth: Auth = inject(Auth);
  router: Router = inject(Router);
  toast: Toast = inject(Toast);



  createAccountForm: FormGroup = this.formBuilder.group({
    email: ['', [Validators.email, Validators.required]],
    firstName: ['', [Validators.required]],
    lastName: ['', [Validators.required]],
    password: ['', [Validators.required]]
  })

  submit() {
    const accountData = this.createAccountForm.value;

    this.auth.signUp(accountData).subscribe({
      next: (res: any) => {
        console.log(res);
        this.toast.show('User created successfully', 'success');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.toast.show('Failed to create user', 'danger');
        console.error("Failed to create user", err);
      }
    })
  }

}
