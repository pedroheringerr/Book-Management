import { Component, inject } from '@angular/core';
import { Confirm } from '../confirm';

@Component({
  selector: 'app-confirm-modal',
  imports: [],
  templateUrl: './confirm-modal.html',
  styleUrl: './confirm-modal.scss',
})
export class ConfirmModal {
  title = '';
  message = '';

  private resolveFn!: (value: boolean) => void;

  private confirm = inject(Confirm);


  ngOnInit() {
    this.confirm.confirm$.subscribe(({ title, message, resolve }) => {
      this.title = title;
      this.message = message;
      this.resolveFn = resolve;

      const modal = new (window as any).bootstrap.Modal(
        document.getElementById('confirmModal')
      );
      modal.show();
    });
  }

  confirmAction() {
    this.resolveFn(true);
    this.close();
  }

  cancel() {
    this.resolveFn(false);
    this.close();
  }

  private close() {
    const modalEl = document.getElementById('confirmModal')!;
    const modal = (window as any).bootstrap.Modal.getInstance(modalEl);
    modal.hide();
  }
}
