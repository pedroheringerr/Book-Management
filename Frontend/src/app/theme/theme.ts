import { Injectable, signal } from '@angular/core';

export type ThemeOption = 'light' | 'dark';

@Injectable({
  providedIn: 'root',
})
export class Theme {

  theme = signal<ThemeOption>('dark');

  constructor() {
    const saved = localStorage.getItem('theme') as ThemeOption | null;
    this.setTheme(saved ?? this.getSystemTheme());
  }

  private getSystemTheme(): ThemeOption {
    return window.matchMedia('(prefers-color-scheme: dark)').matches
      ? 'dark'
      : 'light';
  }

  setTheme(theme: ThemeOption) {
    this.theme.set(theme);
    document.documentElement.setAttribute('data-bs-theme', theme);
    localStorage.setItem('theme', theme);
  }

  toggleTheme() {
    this.setTheme(this.theme() === 'dark' ? 'light' : 'dark');
  }
}
