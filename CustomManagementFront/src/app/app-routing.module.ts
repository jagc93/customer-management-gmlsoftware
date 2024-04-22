import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainComponent } from './component/main/main.component';
import { AppComponent } from './app.component';
import { ClientComponent } from './component/client/client.component';

const routes: Routes = [
  {
    path: '',
    component: AppComponent,
    children: [
      {
        path: '',
        component: MainComponent,
        children: [
          { path: 'clients', component: ClientComponent },
          { path: '**', pathMatch: 'full', redirectTo: 'clients' }
        ]
      }
    ]
  },
  { path: '**', pathMatch: 'full', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: false })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
