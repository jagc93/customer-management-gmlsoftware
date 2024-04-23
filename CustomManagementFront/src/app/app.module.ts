import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MainComponent } from './component/main/main.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { ClientComponent } from './component/client/client.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { ReactiveFormsModule } from '@angular/forms';
import { ClientService } from './service/client/client.service';
import { HttpClientModule } from '@angular/common/http';
import { ProgressBarService } from './service/progress-bar/progress-bar.service';
import { AlertService } from './service/aler/alert.service';
import { OffcanvasService } from './service/offcanvas/offcanvas.service';
import { ExportService } from './service/export/export.service';

@NgModule({
  declarations: [
    AppComponent,
    MainComponent,
    ClientComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    FontAwesomeModule,
    NgxPaginationModule
  ],
  providers: [
    ClientService,
    ProgressBarService,
    AlertService,
    OffcanvasService,
    ExportService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
