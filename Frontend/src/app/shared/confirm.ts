import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class Confirm {
 private confirmSubject = new Subject<{
    title: string;
    message: string;
    resolve: (value: boolean) => void;
  }>();

  confirm$ = this.confirmSubject.asObservable();

  open(title: string, message: string): Promise<boolean> {
    return new Promise<boolean>((resolve) => {
      this.confirmSubject.next({ title, message, resolve });
    });
  }
}
