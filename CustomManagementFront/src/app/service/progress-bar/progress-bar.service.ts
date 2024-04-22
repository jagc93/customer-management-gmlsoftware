import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProgressBarService {

  private subject = new Subject<boolean>();
  public progressBar$ = this.subject.asObservable();

  public show() {
    this.subject.next(true);
  }

  public hide() {
    this.subject.next(false);
  }
}
