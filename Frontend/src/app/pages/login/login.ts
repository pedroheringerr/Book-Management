import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Auth } from '../../services/auth';
import { Router } from '@angular/router';
import { Toast } from '../../shared/toast';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {

  formBuilder = inject(FormBuilder);
  authService = inject(Auth);
  router = inject(Router);
  toast = inject(Toast);

  loginForm: FormGroup = this.formBuilder.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required]
  });

  submit() {
    if (this.loginForm.valid) {
      const loginData = this.loginForm.value

      this.authService.login(loginData).subscribe({
        next: (res: any) => {
          localStorage.setItem('token', res.token);
          this.toast.show('Login successful', 'success');
          this.authService['loadUserFromToken']();
          this.router.navigate(['/'])
        },
        error: (err) => {
          this.toast.show('Invalid credentials', 'danger');
          console.error('API Error: ', err);
        }
      });
    } else {
      this.toast.show('Form is invalid. Please check fields.', 'warning');
    }
  }
}
