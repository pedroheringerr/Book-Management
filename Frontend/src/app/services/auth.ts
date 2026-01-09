import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import { BehaviorSubject } from 'rxjs';
import { environment } from '../../environments/environment';

export interface AuthUser {
  email: string;
  roles: string[];
}

@Injectable({
  providedIn: 'root',
})
export class Auth {

  router = inject(Router);
  http = inject(HttpClient);
  jwtValidator = inject(JwtHelperService);

  private readonly url = `${environment.apiUrl}/api/auth`;

  private userSubject = new BehaviorSubject<AuthUser | null>(null);
  user$ = this.userSubject.asObservable();

  constructor() {
    this.loadUserFromToken();
  }

  login(loginData: any) {
    return this.http.post(`${this.url}/signin`, loginData);
  }

  logout(): void {
    localStorage.removeItem('token');
    this.userSubject.next(null);
    this.router.navigate(['/login']);
  }

  signUp(accountData: any) {
    return this.http.post(`${this.url}/signup`, accountData);
  }

  isAuthenticated(): boolean {
    const token = localStorage.getItem('token');

    return !!token && !this.jwtValidator.isTokenExpired(token);
  }

  private loadUserFromToken(): void {
    const token = localStorage.getItem('token');

    if (!token || this.jwtValidator.isTokenExpired(token)) {
      this.userSubject.next(null);
      return;
    }

    const decoded: any = this.jwtValidator.decodeToken(token);

    const roles =
      decoded.roles ??
      decoded.authorities?.map((r: string) => r.replace('ROLE_', '')) ??
      [];

    this.userSubject.next({
      email: decoded.sub,
      roles,
    });
  }

  get user(): AuthUser | null {
    return this.userSubject.value;
  }

  hasRole(role: string): boolean {
    return this.user?.roles.includes(role) ?? false;
  }

  isAdmin(): boolean {
    return this.hasRole('ADMIN');
  }

  isUser(): boolean {
    return this.hasRole('USER');
  }
}
