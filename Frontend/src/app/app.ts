import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Header } from './header/header';
import { ConfirmModal } from './shared/confirm-modal/confirm-modal';
import { Theme } from './theme/theme';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Header, ConfirmModal],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {

  themeService = inject(Theme);

  constructor () {}
}
