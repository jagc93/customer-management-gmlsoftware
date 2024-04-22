import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AlertService {

  private subject = new Subject<{ isSuccess: boolean; isError: boolean; message: string; }>();
  public alert$ = this.subject.asObservable();

  public showError(e: any): void {
    this.subject.next({
      isError: true,
      isSuccess: false,
      message: typeof e.error === 'object' ? e.error.message || e.message : e.error
    });
  }

  public showSuccess(message: string): void {
    this.subject.next({
      isError: false,
      isSuccess: true,
      message
    });
  }
}
