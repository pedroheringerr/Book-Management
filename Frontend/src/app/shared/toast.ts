import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class Toast {
  show(message: string, type: 'success' | 'danger' | 'warning' | 'info' = 'info') {
     const toast = document.createElement('div');
     toast.className = `toast align-items-center text-bg-${type} border-0 show`;
     toast.setAttribute('role', 'alert');

     toast.innerHTML = `
       <div class="d-flex">
         <div class="toast-body">${message}</div>
         <button type="button" class="btn-close btn-close-white me-2 m-auto"></button>
       </div>
     `;

     document.getElementById('toast-container')!.appendChild(toast);

     toast.querySelector('button')?.addEventListener('click', () => toast.remove());

     setTimeout(() => toast.remove(), 3500);
   }
}
