import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { ProgressBarService } from '../../service/progress-bar/progress-bar.service';
import { AlertService } from '../../service/aler/alert.service';
import { OffcanvasService } from '../../service/offcanvas/offcanvas.service';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrl: './main.component.css'
})
export class MainComponent implements OnInit, OnDestroy {

  public isOffcanvasOpen: boolean = false;
  public showProgressBar = false;
  public loadProgressBar = 0;
  public alert: { isSuccess: boolean; isError: boolean; message: string; } = {
    isSuccess: false,
    isError: false,
    message: ''
  };

  private timeout: any;
  private intervalo: any;
  private subscriptionProgressBar!: Subscription;
  private subscriptionAlert!: Subscription;

  constructor(
    private progressBarService: ProgressBarService,
    private alertService: AlertService,
    private offcanvasService: OffcanvasService
  ) { }

  private resetProgressBar(): void {
    this.showProgressBar = false;
    this.loadProgressBar = 0;
    clearInterval(this.intervalo);
    clearTimeout(this.timeout);
    this.intervalo = undefined;
  }

  private startProgressBar() {
    if (!this.intervalo || this.subscriptionProgressBar) {
      this.resetProgressBar();
    }

    this.subscriptionProgressBar = this.progressBarService.progressBar$
      .subscribe(isShow => {
        setTimeout(() => {
          this.showProgressBar = isShow;
        }, 300);
        this.loadProgressBar = Math.floor(Math.random() * (50 - 25)) + 25;
        this.intervalo = setInterval(() => {
          if (!isShow) {
            setTimeout(() => {
              this.loadProgressBar = 100;
            }, 300);
            setTimeout(() => {
              this.resetProgressBar();
            }, 1000);
          } else {
            this.loadProgressBar += 0.9;
          }
        }, 800);
      });
  }

  private initAlert(): void {
    this.subscriptionAlert = this.alertService.alert$
      .subscribe(alert => this.alert = alert);
  }

  public openOffcanvas(): void {
    const offcanvas = document.getElementById('offcanvasExample');
    offcanvas?.classList.add('show');
    document.body.classList.add('offcanvas-open');
    this.isOffcanvasOpen = true;
    this.offcanvasService.show();
  }

  public closeOffcanvas(): void {
    const offcanvas = document.getElementById('offcanvasExample');
    offcanvas?.classList.remove('show');
    document.body.classList.remove('offcanvas-open');
    this.isOffcanvasOpen = false;
    this.offcanvasService.hide();
  }

  public ngOnInit(): void {
    this.startProgressBar();
    this.initAlert();
  }

  public ngOnDestroy(): void {
    this.subscriptionAlert?.unsubscribe();
  }
}
