import { Component, signal, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Auth } from '../services/auth';
import { Theme } from '../theme/theme';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-header',
  imports: [RouterLink, CommonModule],
  templateUrl: './header.html',
  styleUrl: './header.scss',
})
export class Header {

  auth = inject(Auth);
  theme = inject(Theme);

  title = signal('Library');

  onLogout(): void {
    this.auth.logout();
  }
}
