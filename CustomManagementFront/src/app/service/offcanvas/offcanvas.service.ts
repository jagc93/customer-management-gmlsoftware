import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OffcanvasService {

  private subject = new Subject<boolean>();
  public offcanvas$ = this.subject.asObservable();

  public show() {
    this.subject.next(true);
  }

  public hide() {
    this.subject.next(false);
  }
}
